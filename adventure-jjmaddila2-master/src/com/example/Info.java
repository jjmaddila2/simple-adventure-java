package com.example;

import java.util.ArrayList;

public class Info {
    private String startingRoom;
    private String endingRoom;
    private Rooms[] rooms;
    private Player player;
    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public Rooms[] getRooms() {
        return rooms;
    }

    public Player getPlayerOne() {
        return player;
    }
}
