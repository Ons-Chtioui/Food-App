package com.example.foodapp.Domain;

public class Price {
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

    public Price() {
    }

    @Override
    public String toString() {
        return Value ;
    }
}
