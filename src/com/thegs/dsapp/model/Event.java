package com.thegs.dsapp.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.thegs.dsapp.model.helper.IncorrectTimeException;
import com.thegs.dsapp.model.helper.MarketData;

@XmlRootElement
public class Event {
    private String id;
    private MarketData md;
	
	private String resourcesFolder = System.getProperty("catalina.home") + "/webapps/ROOT/cs9322ass2/";
	private String xmlURI;
	private String xslPath = resourcesFolder;

    public Event(){

    }
    public Event (String id) throws ParseException, IncorrectTimeException, IOException{
        this.id = id;
        this.md = new MarketData(this.id, true);
        this.xmlURI = resourcesFolder + this.id + ".xml";
        this.createXMLFile();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	public MarketData getMD() {
		return md;
	}
	public void setMD(MarketData md) {
		this.md = md;
	}
	
	private String makeXML() {
		System.out.println("makeXML");
		String ans = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response>";
		for (MarketData mdata: md.getMd()) {
			ans += "<Record>";
			
			ans += "<RIC>";
			ans += mdata.getSec();
			ans += "</RIC>";
			
			ans += "<Date>";
			ans += mdata.getDate();
			ans += "</Date>";
			
			ans += "<Time>";
			ans += mdata.getTime();
			ans += "</Time>";
			
			ans += "<GMT>";
			ans += mdata.getGmtOffset();
			ans += "</GMT>";
			
			ans += "<Type>";
			ans += mdata.getType();
			ans += "</Type>";
			
			ans += "<Price>";
			ans += mdata.getPrice();
			ans += "</Price>";
			
			ans += "<Volume>";
			ans += mdata.getVolume();
			ans += "</Volume>";
			
			ans += "<BidPrice>";
			ans += mdata.getBidPrice();
			ans += "</BidPrice>";
			
			ans += "<BidSize>";
			ans += mdata.getBidSize();
			ans += "</BidSize>";
			
			ans += "<AskPrice>";
			ans += mdata.getAskPrice();
			ans += "</AskPrice>";
			
			ans += "<AskSize>";
			ans += mdata.getAskSize();
			ans += "</AskSize>";
			
			ans += "</Record>";
			
		}
		ans += "</Response>";
		System.out.println("/makeXML");
		return ans;
		
	}
	
	public boolean createXMLFile() throws IOException {
		xmlURI = resourcesFolder + id + ".xml";
		File outputFile = new File(xmlURI);
		if (!outputFile.exists()) {
			String xmlString = this.makeXML();
			System.out.println("createXML");
			FileUtils.writeStringToFile(outputFile, xmlString, "UTF-8");
			System.out.println("/createXML");
			return true;
		} else {
			return false;
		}
	}
	
	public String getXML() throws IOException {
		File f = new File(xmlURI);
		String ans = "";
		if (f.exists()) {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(xmlURI));
			String line = null;
			while ((line = br.readLine()) != null) {
				ans += line;
			}
			return ans;
		} else {
			this.createXMLFile();
			return this.getXML();
		}
	}
	
	public String convert(String xsl) throws IOException, ParserConfigurationException, SAXException, TransformerException {
    	
		this.createXMLFile();
    	String filexsl = xslPath  + xsl + ".xsl";
    	
    	Writer writer = new StringWriter();

        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        File stylesheet = new File(filexsl).getAbsoluteFile();
        File datafile = new File(xmlURI).getAbsoluteFile();

        Source dataSource = new StreamSource(datafile);
        Source styleSource = new StreamSource(stylesheet);
        
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(styleSource);

        Result result = new StreamResult(writer);
        transformer.transform(dataSource, result);
    

    	return writer.toString();
    }
    
}
