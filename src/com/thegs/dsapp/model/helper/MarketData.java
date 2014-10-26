package com.thegs.dsapp.model.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MarketData {
	private int index;
	private String sec;
	private String date;
	private String time;
	private String gmtOffset;
	private String type;
	private String price;
	private String volume;
	private String bidPrice;
	private String bidSize;
	private String askPrice;
	private String askSize;
	
	private Calendar startTime;
	private Calendar endTime;
	private String csvString;
	private long fileSize;
	
	private List<MarketData> md = new ArrayList<MarketData>();
	//TODO: change this resourcesFolder - it is calling data from elsewhere now
	private String resourcesFolder = System.getProperty("catalina.home") + "/webapps/ROOT/cs9322ass1/";


	public MarketData(String eventSetId) throws FileNotFoundException, ParseException {
		readCSV(eventSetId);
	}
	
	private MarketData() {
		
	}
	
	public MarketData(String sec2, Calendar startDate, Calendar endDate,
			String dataSourceURL) throws IOException, IncorrectTimeException, ParseException {
		this.sec = sec2;
		this.startTime = startDate;
		this.endTime = endDate;
		URLtoMD(dataSourceURL);
	}
	

	public List<MarketData> getMd() {
		return md;
	}

	private void URLtoMD(String dataSourceURL) throws IOException, IncorrectTimeException, ParseException {
    	URL dataURL = new URL(dataSourceURL);
        
        InputStream is = dataURL.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String theLine;
        String result = "";

        if (endTime.before(startTime)) {
            throw new IncorrectTimeException();
        }
        
        // Read in the lines
        while ((theLine = br.readLine()) != null) {
            // Don't process the header line, just add it to the result
            if (!theLine.contains("#RIC,Date[G],Time[G],GMT Offset,Type,Price,Volume,Bid Price,Bid Size,Ask Price,Ask Size")) {
                String[] lineArray = theLine.split(",");
                Calendar lineDate = getDateFromArray(lineArray);
                // Add the line to the result if it is between the startTime
                // and endTime and the security code is the same
                if ((lineDate.after(startTime) || lineDate.equals(startTime)) && (lineDate.before(endTime) || lineDate.equals(endTime))
                        && sec.equals(lineArray[0])) {
                    result += theLine + "\n";
                }

            } else {
            	result += theLine + "\n";
            }
        }
        
        csvString = result;
	}

	private Calendar getDateFromArray(String[] lineArray) throws ParseException {
		Calendar c = convertDate(lineArray[1]);
		convertTime(lineArray[2], c);
		return c;
	}

	private void convertTime(String time, Calendar c) {
		String[] times = time.split(":");
		String[] seconds = times[2].split("\\.");
		c.set(Calendar.HOUR, Integer.parseInt(times[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(times[1]));
		c.set(Calendar.SECOND, Integer.parseInt(seconds[0]));
		c.set(Calendar.MILLISECOND, Integer.parseInt(seconds[1]));
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGmtOffset() {
		return gmtOffset;
	}

	public void setGmtOffset(String gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getBidSize() {
		return bidSize;
	}

	public void setBidSize(String bidSize) {
		this.bidSize = bidSize;
	}

	public String getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	public String getAskSize() {
		return askSize;
	}

	public void setAskSize(String askSize) {
		this.askSize = askSize;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public Calendar getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public Calendar getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}


	private void readCSV(String eventSetId) throws FileNotFoundException, ParseException, NoSuchElementException {
		//Get scanner instance
		File f = new File(resourcesFolder+eventSetId+".csv");
		this.fileSize = f.length();
        Scanner lineScanner = new Scanner(f);
         lineScanner.nextLine();
         boolean setStart  = false;
        //Get all tokens and store them in some data structure
        
        while (lineScanner.hasNextLine()) 
        {
        	String line = lineScanner.nextLine();
        	Scanner scanner = new Scanner(line);
        	scanner.useDelimiter(",");
        	
        	MarketData marketData = new MarketData();
        	if(scanner.hasNext())
        		marketData.setSec(scanner.next());
        	if(scanner.hasNext())
        		marketData.setDate(scanner.next());
        	if(scanner.hasNext())
        		marketData.setTime(scanner.next());
        	if (!setStart) {
        		Calendar c = convertDate(marketData.getDate());
        		convertTime(marketData.getTime(), c);
        		this.setStartTime(c);
        		setStart = true;
        	}
    	
    		if(scanner.hasNext())
        		marketData.setGmtOffset(scanner.next());
        	if(scanner.hasNext())
        		marketData.setType(scanner.next());
        	if(scanner.hasNext())
        		marketData.setPrice(scanner.next());
        	if(scanner.hasNext())
        		marketData.setVolume(scanner.next());
        	if(scanner.hasNext())
        		marketData.setBidPrice(scanner.next());
        	if(scanner.hasNext())
        		marketData.setBidSize(scanner.next());
        	if(scanner.hasNext())
        		marketData.setAskPrice(scanner.next());
        	if(scanner.hasNext())
        		marketData.setAskSize(scanner.next());
        	else
        		marketData.setAskSize("");
        	md.add(marketData);
        	scanner.close();
        }
        lineScanner.close();

        if (md.size() > 0) {
        	String tempDate = md.get(md.size() - 1).getDate();
        	String tempTime = md.get(md.size() - 1).getTime();
        	Calendar c = convertDate(tempDate);
    		convertTime(tempTime, c);
    		this.setEndTime(c);
        }
        
	}
	
	private Calendar convertDate(String date) throws ParseException {
		Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        
        return c;
	}

	public String stringify() {
		if (csvString.isEmpty()) {
			csvString += "#RIC,Date[G],Time[G],GMT Offset,Type,Price,Volume,Bid Price,Bid Size,Ask Price,Ask Size\n";
			for (MarketData m : md) {
	        	csvString += m.getSec() + ",";
	        	csvString += m.getDate() + ",";
	        	csvString += m.getTime() + ",";
	        	csvString += m.getGmtOffset() + ",";
	        	csvString += m.getType() + ",";
	        	if (this.getCurrencyCode().equals("AUD")) {
	        		csvString += "AUD";
	        	}
	        	csvString += m.getPrice() + ",";
	        	csvString += m.getVolume() + ",";
	        	csvString += m.getBidPrice() + ",";
	        	csvString += m.getBidSize() + ",";
	        	csvString += m.getAskPrice() + ",";
	        	csvString += m.getAskSize() + "\n";
	        	csvString += "\n";
			}
		}
		
		return csvString;
	}

	public int size() {
		return md.size();
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public String getCurrencyCode() {
		String ans = md.get(0).getPrice().replaceAll("[^A-Za-z]+", "");
		if (ans.isEmpty()) {
			return "AUD";
		} else return ans;
	}
}
