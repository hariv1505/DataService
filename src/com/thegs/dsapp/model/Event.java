package com.thegs.dsapp.model;

import java.io.FileNotFoundException;
import java.text.ParseException;

import javax.xml.bind.annotation.XmlRootElement;

import com.thegs.dsapp.model.helper.MarketData;

@XmlRootElement
public class Event {
    private String id;
    private MarketData md;

    public Event(){

    }
    public Event (String id) throws FileNotFoundException, ParseException{
        this.id = id;
        this.md = new MarketData(id);
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
	
	public String giveXML() {
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
	
	public void createXMLFile() {
		String toEnter = this.giveXML();
		//TODO: where to create XML? Maybe just create temp files that we don't keep, but use to present answer?
	}
    
}
