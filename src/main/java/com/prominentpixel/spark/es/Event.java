package com.prominentpixel.spark.es;

import java.io.Serializable;

public class Event implements Serializable {

    private int id;
    private String venue;
    private String location;
    private String address;

    public Event(int id, String address,String location,String venue){
        this.id = id;
        this.address=address;
        this.location=location;
        this.venue=venue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
