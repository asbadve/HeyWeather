package com.badve.ajinkya.heyweather.Models;

import java.math.BigInteger;

/**
 * Created by Ajinkya on 30-05-2015.
 */
public class City {
    private int id;
    private String name;
    private double lat,lon;
    int countryCode;

    public City() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public City(int id, String name, double lat, double lon, int countryCode) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", countryCode=" + countryCode +
                '}';
    }
}
