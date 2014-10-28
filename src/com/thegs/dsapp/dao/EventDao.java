package com.thegs.dsapp.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.thegs.dsapp.model.Event;
import com.thegs.dsapp.model.helper.IncorrectTimeException;


public enum EventDao {
    instance;

    private Map<String, Event> contentStore = new HashMap<String, Event>();

    private EventDao() {
       
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