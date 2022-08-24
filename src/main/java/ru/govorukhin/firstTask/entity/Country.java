package ru.govorukhin.firstTask.entity;

public class Country {
    private int country;
    private String country_text;

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getCountry_text() {
        return country_text;
    }

    public void setCountry_text(String country_text) {
        this.country_text = country_text;
    }

    @Override
    public String toString() {
        return country + ": " + country_text;
    }
}
