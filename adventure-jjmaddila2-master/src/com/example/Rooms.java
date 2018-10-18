package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Rooms {
    private String name;
    private String description;
    private ArrayList<Item> items;
    private Direction[] directions;
    private ArrayList<Monster> monsters;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Direction[] getDirection() {
        return directions;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
}
