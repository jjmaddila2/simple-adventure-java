package com.example;

import java.util.ArrayList;

public class Player {
    private double attack;
    private double defense;
    private int level;
    private double health;
    private String name;
    private ArrayList<Item> items;
    private double currentHealth;


    public String getName() {
        return name;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }

    public int getLevel() {
        return level;
    }

    public double getHealth() {
        return health;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
