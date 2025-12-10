package com.example.foodapp.Domain;

public class Time{
    private int id;
    private String Value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public Time() {
    }

    @Override
    public String toString() {
        return Value ;
    }
}
