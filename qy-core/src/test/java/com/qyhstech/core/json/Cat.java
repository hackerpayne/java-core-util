package com.qyhstech.core.json;


public class Cat extends Animal {
    private String color;

    public Cat() {
    }

    public Cat(String name, String color) {
        super(name);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}