package com.thegs.dsapp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.thegs.dsapp.model.helper.MarketData;

@XmlRootElement
public class Event {
    private String id;
    private MarketData md;
	private String xmlString;
	
	private String resourcesFolder = System.getProperty("catalina.home") + "/webapps/ROOT/cs9322ass1/";
	private String xmlURI;

    public Event(){

    }
    public Event (String id) throws FileNotFoundException, ParseException{
        this.id = id;
        this.md = new MarketData(id);
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
		String ans = "<Response>";
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
		if (!xmlURI.equals(resourcesFolder + id + ".xml")) {
			xmlString = this.makeXML();
			
			File outputFile = new File(resourcesFolder + id + ".xml");
			FileUtils.writeStringToFile(outputFile, md.stringify());
			
			xmlURI = resourcesFolder + id + ".xml";
			return true;
		} else {
			return false;
		}
	}
	
	public String getXML() {
		return xmlString;
	}
    
}
