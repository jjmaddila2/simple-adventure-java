package com.example;

public class Monster {
    private String name;
    private double attack;
    private double defense;
    private double health;
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

    public double getHealth() {
        return health;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }
}
