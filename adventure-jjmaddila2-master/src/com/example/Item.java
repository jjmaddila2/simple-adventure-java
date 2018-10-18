package com.example;

public class Item {
    private String name;
    private double damage;

    public Item (String givenName, double givenDamage) {
        this.name = givenName;
        this.damage = givenDamage;
    }

    public String getName() {
        return name;
    }

    public double getDamage() {
        return damage;
    }
}
