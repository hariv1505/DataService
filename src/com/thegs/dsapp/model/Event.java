package com.thegs.dsapp.model;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
	private String xmlString;
	
	private String resourcesFolder = System.getProperty("catalina.home") + "/webapps/ROOT/cs9322ass2/";
	private String xmlURI;
	private String xslPath = resourcesFolder;

    public Event(){

    }
    public Event (String id) throws ParseException, IncorrectTimeException, IOException{
        this.id = id;
        this.md = new MarketData(this.id, true);
        this.xmlURI = ""; 
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
		
		return ans;
		
	}
	
	public boolean createXMLFile() throws IOException {
		File outputFile = new File(resourcesFolder + id + ".xml");
		if (!xmlURI.equals(resourcesFolder + id + ".xml") || 
				!outputFile.exists()) {
			xmlString = this.makeXML();
			
			FileUtils.writeStringToFile(outputFile, xmlString);
			
			xmlURI = resourcesFolder + id + ".xml";
			return true;
		} else {
			return false;
		}
	}
	
	public String getXML() {
		return xmlString;
	}
	
	public String convert(String xsl) throws IOException {
    	
		this.createXMLFile();
		File f = new File(resourcesFolder + this.id + ".xml"); 
    	String filexsl = xslPath  + xsl + ".xsl";
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	
    	StringWriter writer = new StringWriter();

        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        try {
            File stylesheet = new File(filexsl);
            File datafile = new File(xmlURI);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(datafile);
            
            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = tFactory.newTransformer(stylesource);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        }
        catch (TransformerConfigurationException e) 
        {
            e.printStackTrace();
        }
        catch (TransformerFactoryConfigurationError e) 
        {
            e.printStackTrace();
        }
        catch (TransformerException e) 
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	return writer.toString();
    }
    
}
