package com.thegs.dsapp.dao;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.thegs.dsapp.model.Event;


public enum EventDao {
    instance;

    private Map<String, Event> contentStore = new HashMap<String, Event>();

    private EventDao() {

        Event e;
		try {
			e = new Event("1");
			contentStore.put("1", e);
		} catch (FileNotFoundException e1) {
			System.err.println("Do not add event set ID 1");
			e1.printStackTrace();
		} catch (ParseException e1) {
			System.err.println("Do not add event set ID 1");
			e1.printStackTrace();
		}
		try {
	        e = new Event("2");
	        contentStore.put("2", e);
		} catch (FileNotFoundException e1) {
			System.err.println("Do not add event set ID 2");
			e1.printStackTrace();
		} catch (ParseException e1) {
			System.err.println("Do not add event set ID 2");
			e1.printStackTrace();
		}
    }
    public Map<String, Event> getStore(){
        return contentStore;
    }
	public boolean addEvent(Event e) {
		if (!contentStore.keySet().contains(e.getId())) {
			contentStore.put(e.getId(), e);
			return true;
		} else {
			return false;
		}
	}
	
	public Event getEventById(String id) {
		return contentStore.get(id);
	}
	public void updateEvent(Event newEvent) {
		contentStore.remove(newEvent.getId());
		contentStore.put(newEvent.getId(), newEvent);
		
	}
	public void deleteEvent(Event e) {
		contentStore.remove(e.getId());
		
	}

}