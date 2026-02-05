package com.qyhstech.core.json;

public class Fish extends Animal {
    private String species;

    public Fish() {
    }

    public Fish(String name, String species) {
        super(name);
        this.species = species;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
}
