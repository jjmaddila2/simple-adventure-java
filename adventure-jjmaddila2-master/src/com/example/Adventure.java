package com.example;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.*;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.MalformedURLException;
import java.net.URL;


public class Adventure {

    public static int EXPERIENCE_POINTS = 0;
    private static double CURRENT_HEALTH = 0;

    /**
     * a simple main method which really only calls the play method which runs the game
     * @param args
     */
    public static void main(String[] args) {

        play();
    }

    /**
     *
     * @param url takes an string which will be the url
     * @return returns a string which is the new url that is converted to a json
     * @throws UnirestException throws an exception which lets it so an invalid
     */
    public static String turnUrlToJson(String url) throws UnirestException {
       return Unirest.get(url).asString().getBody();
    }


    /**
     *
     * @return returns a new info object which contains all of the given objects needed in order to run the game
     */
    public static Info infoObject() {
        //creation of a new gson object
        Gson adventure = new Gson();
        String jsonToString = Data.getFileContentsAsString("OfficeTemplate.json");
        Info jsonInfoObject = adventure.fromJson(jsonToString, Info.class);
        //after turning the json to a string the string is printed
        return jsonInfoObject;
    }

    /**
     * a method where the game is simply played
     */
    public static void play() {
        //defines scanner so it can take user input
        Scanner Keyboard = new Scanner(System.in);
        // an arraylist which will take all of the items that the player picks up
        ArrayList<Item> playerItems = new ArrayList<Item>();
        //creation of the info object that will be called a lot
        Info info = infoObject();
        String currentRoom = info.getStartingRoom();
        //A string that will be used if someone eneters an incorrect direction later in the code
        String ifRoomIsNull;
        String endingRoom = info.getEndingRoom();
        System.out.println("Your Journey has Started Here");
        setCurrentHealth(info.getPlayerOne().getHealth());
        double maxHealth = getCurrentHealth();
        int currentLevel = info.getPlayerOne().getLevel();
        //while loop that will end once the person has moved to the ending room
        while (!(currentRoom.equals(endingRoom))) {
            System.out.println("");
            System.out.println(printOutRoomDescription(info, currentRoom));
            System.out.println(printOutRoomItems(info, currentRoom));
            if (printOutMonstersInRoom(currentRoom, info) == null) {
                System.out.println(printOutRoomDirections(info, currentRoom));
            }
            else {
                System.out.println(printOutMonstersInRoom(currentRoom,info));
            }
            System.out.println("What would you like to do?");
            //user input so they can decide their next move
            String userDescsion = Keyboard.nextLine().toLowerCase();
            //substring userInput is a method that substrings the first word of the user input
            //the following ifs check if that substringed word equals  key word
            if (substringUserInput(userDescsion).equals("go")) {
                if (printOutMonstersInRoom(currentRoom, info) != null) {
                    System.out.println("You can go anywhere until you kill all monsters");
                }
                //if the user goes to a room using a direction that is not valid that isRoomisNull will turn null
                //and approate error message printed
                //otherwise currentRoom is set to the new room
                else {
                    ifRoomIsNull = goToAnotherRoom(substringSecondWordInUserInput(userDescsion), currentRoom, info);
                    if (ifRoomIsNull != null) {
                        currentRoom = ifRoomIsNull;
                    }
                }
            } else if (substringUserInput(userDescsion).equals("list")) {
                System.out.println(printOutListOfItems(playerItems));
            } else if (substringUserInput(userDescsion).equals("drop")) {
                dropItem(playerItems, substringSecondWordInUserInput(userDescsion), info, currentRoom);
            } else if (substringUserInput(userDescsion).equals("take")) {
                pickUpItem(playerItems, substringSecondWordInUserInput(userDescsion), info, currentRoom);
            }//if the substring hits exit or quit then the program will automatically exit
            else if (substringUserInput(userDescsion).equals("playerinfo")) {
                printPlayerInfo(info, maxHealth);
            }
            else if (substringUserInput(userDescsion).equals("duel")) {
                duelMonster(info, currentRoom, substringSecondWordInUserInput(userDescsion), maxHealth, currentLevel, playerItems);
            }
            else if (substringUserInput(userDescsion).equals("quit") || substringUserInput(userDescsion).equals("exit")) {
                System.exit(0);
            }//if the user inputs some message that is not able to be understood the following message is printed
            else {
                System.out.println("I don't understand " + userDescsion);
            }
        }
        //if the current roomn
        System.out.println("You have reached your final destination");
        System.out.println(printOutRoomDescription(info, currentRoom));

    }

    /**
     *
     * @param userInput  a parameter of the user input
     * @return will return the first word in the user input
     */
    public static String substringUserInput(String userInput) {
        if (userInput.length() > 3) {
            if (userInput.substring(0, 3).equals("go ")) {
                return "go";
            }
        }
        if (userInput.length() == 4) {
            if (userInput.substring(0,4).equals("list")) {
                return "list";
            }
            if (userInput.substring(0,4).equals("quit")) {
                return "quit";
            }
            if (userInput.substring(0,4).equals("exit")) {
                return "exit";
            }
        }
        if (userInput.length() > 5) {
            if (userInput.substring(0,4).equals("take")) {
                return "take";
            }
            if (userInput.substring(0,4).equals("drop")) {
                return "drop";
            }

            if (userInput.substring(0,4).equals("duel")) {
                return "duel";
            }
        }

        if (userInput.length() == 6) {
            if (userInput.substring(0, 6).equals("attack")) {
                return "attack";
            } else if (userInput.substring(0,6).equals("status")) {
                return "status";
            }
        }
        if (userInput.length() == 9) {
            if (userInput.substring(0,9).equals("disengage")) {
                return "disengage";
            }
        }
        if (userInput.length() == 10){
            if (userInput.substring(0, 10).equals("playerinfo")) {
                return "playerinfo";
            }
        }
        if (userInput.length() > 11) {
            if (userInput.substring(0,11).equals("attack with")) {
                return "attack with";
            }
        }
        //if the first substring word does not match the desired word then empty string returned
        return "";
    }

    /**
     *
     * @param userInput after the first part of the user input is substringed the second part of the word is inputed
     * @return a substringed version of the user input
     */
    public static String substringSecondWordInUserInput(String userInput) {
        //splits the input so every word is its own element in an array
        String[] words = userInput.split(" ");
        //adds all words to a single string
        String substringInput = "";
        for (int i = 1; i < words.length; i++) {
            if (words[i].equals("with")){
                continue;
            }
            substringInput += words[i] + " ";
        }
        //removes the final space and returns
        substringInput = substringInput.substring(0,substringInput.length()-1);
        return substringInput;
    }

    /**
     *
     * @param info info object so i can access any element in the JSON
     * @param currentRoom a string that points to the current room someone is in
     * @return the description of the current room
     */
    public static String printOutRoomDescription(Info info, String currentRoom) {
        String roomDescription = "";
        //goes through all the rooms and looks for the current room and gets its description
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                roomDescription = info.getRooms()[i].getDescription();
            }
        }
        return roomDescription;
    }

    /**
     *
     * @param info info object so i can access any element in the JSON
     * @param currentRoom a string that points to the current room someone is in
     * @return all of the items in a given room
     */
    public static String printOutRoomItems(Info info, String currentRoom) {
        String allOfTheItemsInARoom = "";
        //goes through all of the rooms, finds the correct rooms nd adds the items to a list
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                for (int j = 0; j < info.getRooms()[i].getItems().size(); j++) {
                    allOfTheItemsInARoom += info.getRooms()[i].getItems().get(j).getName() + ", ";
                }
            }
        }
        //if no items in rooms prints error message
        if (allOfTheItemsInARoom.equals("")) {
            return "There are no items in this room";
        }
        //substrings out the final comma and space
        allOfTheItemsInARoom = allOfTheItemsInARoom.substring(0,allOfTheItemsInARoom.length()-2);
        //return the string with the items
        return "Items in this room: " + allOfTheItemsInARoom;
    }

    /**
     *
     * @param info info object so i can access any element in the JSON
     * @param currentRoom a string that points to the current room someone is in
     * @return returns all of the directions for a given room
     */
    public static String printOutRoomDirections(Info info, String currentRoom) {
        String allRoomDirections = "";
        //goes through all of the rooms and finds the correct one then adds
        //all of the directions for that room into a given string
        for (int i = 0; i < info.getRooms().length; i++) {
                if (currentRoom.equals(info.getRooms()[i].getName())) {
                    for (int k = 0; k < info.getRooms()[i].getDirection().length; k++) {
                        allRoomDirections += info.getRooms()[i].getDirection()[k].getDirectionName() + ", ";
                    }
                }
        }
        //if no direction in a given room
        if (allRoomDirections.equals("")) {
            return "There is no where to go";
        }
        //substrigs out the last comma and space
        allRoomDirections = allRoomDirections.substring(0,allRoomDirections.length()-2);
        //returns the directions
        return "No monsters in room, you can go: " + allRoomDirections;
    }


    /**
     *
     * @param direction the direction that was substringed earlier from user input
     * @param currentRoom a string that points to the current room someone is in
     * @param info info object so i can access any element in the JSON
     * @return the new current room
     */
    public static String goToAnotherRoom(String direction, String currentRoom, Info info) {
        //make a place that will hold the current room just in case i need to acess it later
        String placeHolderRoom = currentRoom;
        direction = direction.toLowerCase();
        // turns the first char and the 6th char to uppercase because north and south are both only 5 chars long
        if (direction.length() > 5) {
            direction = direction.substring(0,1).toUpperCase() +
                direction.substring(1,5) + direction.substring(5,6).toUpperCase() +
                    direction.substring(6);
        }
        //if not 5 char then just make the first letter uppercase
        else {
            direction = direction.substring(0,1).toUpperCase() + direction.substring(1);
        }
        //indexofRoom is a variable which is the index of the room when the current room is equal to the room of choice
        int indexOfRoom = 0;
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equalsIgnoreCase(info.getRooms()[i].getName())) {
                indexOfRoom = i;
            }
        }
        //for loop that goes through all the directions for a given room and sets placeholderRoom
        // to the room for the direction that you want to go
        for (int i  = 0; i < info.getRooms()[indexOfRoom].getDirection().length; i++) {
            if (direction.equals(info.getRooms()[indexOfRoom].getDirection()[i].getDirectionName())) {
                placeHolderRoom = info.getRooms()[indexOfRoom].getDirection()[i].getRoom();
            }
        }
        //the newroom equals the current rooms that means the directions did not exist
        if (placeHolderRoom.equals(currentRoom)) {
            System.out.println("Sorry you cannot go" + direction);
            return null;
        }
        return placeHolderRoom;
    }

    /**
     *
     * @param items an arraylist of your items
     * @return the arraylist of your items printed out
     */
    public static String printOutListOfItems(ArrayList<Item> items) {
        //if size is zero then you have no items to print because items is empty
        if (items.size() == 0) {
            return "You have no items to your name";
        }
        String listOfItems = "";
        //goes through the arraylist and adds all values to a string
        for (int i = 0; i < items.size(); i++) {
            listOfItems += items.get(i).getName() + ", ";
        }
        //susbtrings the string to remove the last comma and space
        listOfItems = listOfItems.substring(0, listOfItems.length()-2);
        //returns the list of items
        return "You have: " + listOfItems;
    }

    /**
     *
     * @param items arraylist of the items you own
     * @param itemToPickUp a string of the item you want to pick up
     * @param info info object so i can access any element in the JSON
     * @param currentRoom a string that points to the current room someone is in
     * @return an arraylist of your item with the new item
     */
    public static ArrayList<Item> pickUpItem(ArrayList<Item> items, String itemToPickUp, Info info, String currentRoom) {
        //.size of arraylist so i cn compare later to see if anything was added to arraylist
        int arraySize = items.size();
        //finds the current room and then finds the right item and adds it to your arraylist
        //also deletes it from the arraylist of the room
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                for (int j = 0; j < info.getRooms()[i].getItems().size(); j++) {
                    if (itemToPickUp.equals(info.getRooms()[i].getItems().get(j).getName())) {
                        items.add(info.getRooms()[i].getItems().get(j));
                        info.getRooms()[i].getItems().remove(j);
                    }
                }
            }
        }
        //if size stayed the same that means item ws not picked and arraylist stays the same
        //also error message is printed
        if (arraySize == items.size()) {
            System.out.println("Sorry you cannot pick up" + itemToPickUp);
            return null;
        }
        return items;
    }

    /**
     *
     * @param items arraylist of the items you own
     * @param itemToDrop item that you want to drop off
     * @param info info object so i can access any element in the JSON
     * @param currentRoom a string that points to the current room someone is in
     * @return an arraylist of your item without the item
     */
    public static ArrayList<Item> dropItem(ArrayList<Item> items, String  itemToDrop, Info info, String currentRoom) {
        //goes through all the rooms, finds the right room, finds the right item to drop
        //then adds the item to the room arraylist and drops the item in your arraylist
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                for (int j = 0; j < items.size(); j++) {
                    if (itemToDrop.equals(items.get(j).getName())) {
                        info.getRooms()[i].getItems().add(items.get(j));
                        items.remove(j);
                        //returns the new arraylist
                        return items;
                    }
                }
            }
        }
        //if goes through the loop then no item to drop then prints error message
        System.out.println("You do not have the item you want to drop");
        return items;
    }

    /**
     *
     * @param currentRoom represents the room the player is currently in
     * @param info the info object so I can access any element in the JSON
     * @return a string of all the monsters
     */
    public static String printOutMonstersInRoom(String currentRoom, Info info) {
        //simple string where I will add all the monsters
        String monstersInTheRoom = "";
        //loop that goes through all the rooms until i find the room that i am in
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                //in the correct room goes through all the monsters and adds the name to the string
                for (int j = 0; j < info.getRooms()[i].getMonsters().size(); j++) {
                    monstersInTheRoom += info.getRooms()[i].getMonsters().get(j).getName() + ", ";
                }
            }
        }
        //if no monsters are there then returns null
        if (monstersInTheRoom.equals("")) {
            return null;
        }
        //substrings the final comma and space to make it better to look at
        monstersInTheRoom = monstersInTheRoom.substring(0, monstersInTheRoom.length() - 2);
        //returns the string
        return "The monsters in the room are " + monstersInTheRoom;

    }

    /**
     *
     * @param info info object so i access any element in the json
     * @param maxHealth the maximum health so i can access another method with the largest possible health
     */
    public static void printPlayerInfo(Info info, double maxHealth) {
        //prints attack, defense and health
        System.out.println("Player attack: " + info.getPlayerOne().getAttack());
        System.out.println("Player defense: " + info.getPlayerOne().getDefense());
        //calls a different method which will print the health in the right format
        System.out.println("Player health: " + printPlayerCurrentHealth(info, maxHealth, getCurrentHealth()));
    }

    /**
     *
     * @param info info object so i access any element in the json
     * @param maxHealth the max health so I can create a ratio for how much health the player has left
     * @param currentHealth so i can create a ratio of max health and total health
     * @return a string of the current health
     */
    public static String printPlayerCurrentHealth(Info info, double maxHealth, double currentHealth) {
        //variable to store the ratio between the current and total health
        double ratioBetweenCurrentAndTotalHealth = currentHealth / maxHealth;
        //a string where the health will be added
        String healthOutput = "";
        //will add ascii values by .05 which means if ratio is .5 then 10 of ascii value will be added out
        //of a possible 20
        for (double i = .05; i <= 1; i+= .05) {
            //if the user has a ratio higher than i then a # will be added
            //this reprents having s health point
            if (ratioBetweenCurrentAndTotalHealth >= i) {
                healthOutput += "#";
            }
            //if the user has a ratio below i then a _ will be added
            //this represents not having that health point
            else {
                healthOutput += "_";
            }
        }
        //returns the health
        return "Player health: " + healthOutput;
    }

    /**
     *
     * @param info info object so i access any element in the json
     * @param indexOfRoom the room which the monster resides in
     * @param indexOfMonster the index of the actual monster itself
     * @return a string of the monsters health
     */
    public static String printMonsterCurrentHealth(Info info, int indexOfRoom, int indexOfMonster) {
        //acesses the max and current health of the monster
        double maxHealth = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getHealth();
        double currentHealth = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getCurrentHealth();
        //variable to store the ratio between the current and total health
        double ratio = currentHealth/maxHealth;
        //a string where the health will be added
        String healthOutput = "";
        //will add ascii values by .05 which means if ratio is .5 then 10 of ascii value will be added out
        //of a possible 20
        for (double i = .05; i <= 1; i += .05) {
            //if the user has a ratio higher than i then a # will be added
            //this reprents having s health point
            if (ratio >= i) {
                healthOutput += "#";
            }
            //if the user has a ratio below i then a _ will be added
            //this represents not having that health point
            else {
                healthOutput += "_";
            }
        }
        return "Monster health: " + healthOutput;
    }


    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param currentRoom the current room that the player is in
     * @param userInput the userinput done before that should equal the monster name
     * @param maxHealth the max health of the player
     * @param level the current level of the player
     * @param items an arraylist of the itesm that a player has
     */
    private static void duelMonster(Info info, String currentRoom, String userInput, double maxHealth, int level,
                                    ArrayList<Item> items) {
        //setting the index of room and monster before hand
        int indexOfRoom = 0;
        int indexOfMonsterInRoom = -1;
        //goes through loop of rooms and monsters and if correct monster and room is found then index variable made above
        //will change
        for (int i = 0; i < info.getRooms().length; i++) {
            if (currentRoom.equals(info.getRooms()[i].getName())) {
                indexOfRoom = i;
                for (int j = 0; j < info.getRooms()[i].getMonsters().size(); j++) {
                    if (userInput.equals(info.getRooms()[i].getMonsters().get(j).getName())) {
                        indexOfMonsterInRoom = j;
                    }
                }
            }
        }
        //if monster that user wants to duel is not there than error message with print
        if (indexOfMonsterInRoom == -1) {
            System.out.println("Sorry, " + userInput + " is not a monster you can fight");
            return;
        }
        //creates a scanner that will take user input
        Scanner Keyboard = new Scanner(System.in);
        System.out.println("");
        System.out.println("YOU ARE NOW IN THE DUEL INTERFACE");
        System.out.println("");
        //user input so they can decide their next move
        String userDescsion = "";
        //while loop that user can put in commands until they type disengage or the mosnter dies
        while (true) {
            //takes input from user
            System.out.println("What action would you like to do?");
            userDescsion = Keyboard.nextLine();
            //if user wants to attack will call the attack method to do the attack
            if (substringUserInput(userDescsion).equals("attack")) {
                //if the monster is killed than a specfic string will be returend and of then will call the method
                //ifMonsterKilled which will remove the monster and exit the duel interface
                String result = attackMonster(info, indexOfRoom, indexOfMonsterInRoom, getCurrentHealth(),
                        info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonsterInRoom).getCurrentHealth());
                if (ifMonsterKilled(info, indexOfRoom, indexOfMonsterInRoom, result)) {
                    return;
                }
                //same thing with attack function but with an item
            } else if (substringUserInput(userDescsion).equals("attack with")) {
                String differentString = attackMonsterWithItem(info, items, indexOfRoom, indexOfMonsterInRoom, getCurrentHealth(),
                        info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonsterInRoom).getCurrentHealth(),
                        substringSecondWordInUserInput(userDescsion));
                if (ifMonsterKilled(info, indexOfRoom, indexOfMonsterInRoom, differentString)){
                    return;
                }
            //if user puts in playerinfo will simply print out the method defined above
            } else if (substringUserInput(userDescsion).equals("playerinfo")) {
                printPlayerInfo(info, maxHealth);
            //displays the health of monster and player if staus put
            } else if (substringUserInput(userDescsion).equals("status")) {
                System.out.println(printPlayerCurrentHealth(info, maxHealth, getCurrentHealth()));
                System.out.println(printMonsterCurrentHealth(info, indexOfRoom, indexOfMonsterInRoom));
            }
            //will end the duel interface if user puts in disengage
            else if (substringUserInput(userDescsion).equals("disengage")) {
                return;
            //gives the list of items that the user has
            } else if (substringUserInput(userDescsion).equals("list")) {
                System.out.println(printOutListOfItems(items));
            //exits out of the program if user puts in quit or exit
            } else if (substringUserInput(userDescsion).equals("quit") || substringUserInput(userDescsion).equals("exit")) {
                System.exit(0);
            }
        }
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param indexOfRoom the index of the room that the monster is in
     * @param indexOfMonsterInRoom the index of monster within the room
     * @param result weather or not the correct string is given from the duel
     * @return a boolean weather or not the monster was killed
     */
    public static boolean ifMonsterKilled(Info info, int indexOfRoom, int indexOfMonsterInRoom, String result) {
        //if result equals the desired result than will call function to add experience points
        if (result.equals("you have killed the monster!!")) {
            System.out.println(result);
            increaseExperiecePoints(info, indexOfRoom, indexOfMonsterInRoom);
            //will remove the monster that is now dead
            info.getRooms()[indexOfRoom].getMonsters().remove(indexOfMonsterInRoom);
            double points = getCurrentLevelFromExp(info.getPlayerOne().getLevel() + 1);
            upGradeLevel(info, info.getPlayerOne().getLevel() + 1, points);
            return true;
        }
        return false;
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param indexOfRoom the index of the room that the monster is in
     * @param indexOfMonster the index of monster within the room
     * @param currentPlayerHealth the current health of the player
     * @param currentMonsterHealth the current health of the monster
     * @return returns a string wheather or not the monster was killed
     */
    public static String attackMonster(Info info, int indexOfRoom, int indexOfMonster, double currentPlayerHealth,
                                     double currentMonsterHealth) {

        //the damage that will be played on the monster
        double damageOnMonster = info.getPlayerOne().getAttack() - info.getRooms()[indexOfRoom].getMonsters()
                .get(indexOfMonster).getDefense();

        //if statement that puts damage on the monster
        if (damageOnMonster > 0) {
            currentMonsterHealth -= damageOnMonster;
            info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).setCurrentHealth(currentMonsterHealth);
        }
        //if the damage on the monster is under zero them no damage will be put on the monster
        else {
            System.out.println("Sorry your attack is to small to put damage on the monster");
        }
        //records the damage that will be played on the player
        double damageOnPlayer = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getAttack() - info.
                getPlayerOne().getDefense();
        //only puts the damage on the player if damageOnPlayer is above zero and sets the current health
        if (damageOnPlayer > 0) {
            currentPlayerHealth -= damageOnPlayer;
            setCurrentHealth(currentPlayerHealth);
        }
        //otherwise no damage will be played
        else {
            System.out.println("The monster could not hurt you");
        }
        //if the players health goes above zero then prints end statment and game ends
        if (currentPlayerHealth <= 0) {
            System.out.println("sorry you have died to the monster");
            System.exit(0);
        }
        //if monster health goes below zero than returns error message
        if (currentMonsterHealth <= 0) {
            return "you have killed the monster!!";
        }
        return "";
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param items an arraylist of items that the person has
     * @param indexOfRoom the room index that the monster is currently in
     * @param indexOfMonster the index of monster we are fighting
     * @param currentPlayerHealth the current health of the player
     * @param currentMonsterHealth the current health of the monster
     * @param itemToAttackWith the item (as a string) that the player wants to attack with
     * @return  a string which represents weather the monster has been killed
     */
    public static String attackMonsterWithItem(Info info, ArrayList<Item> items, int indexOfRoom,
                                        int indexOfMonster, double currentPlayerHealth,
                                        double currentMonsterHealth, String itemToAttackWith) {
        //represents the index of item starts with -1 so if item is not there then it stays at -1
        int indexOfItem = -1;
        //goes through all the items and looks for the item and the same as itemToAttackWith
        for (int i = 0; i < items.size(); i++) {
            if (itemToAttackWith.equals(items.get(i).getName())) {
                indexOfItem = i;
            }
        }
        //if item is not there than idexOfItem is -1 ad print error statement
        if (indexOfItem == -1) {
            System.out.println("Sorry you do not have that item");
            return "";
        }
        //claculates the damage on the monster with the item damage
        double damageOnMonster = info.getPlayerOne().getAttack() - info.getRooms()[indexOfRoom].getMonsters()
                .get(indexOfMonster).getDefense() + items.get(indexOfItem).getDamage();
        //prints statement that you lost item once used
        System.out.println("Sorry you do not have " + items.get(indexOfItem).getName() + " anymore");
        //removes the item from the list
        items.remove(indexOfItem);
        //if damage is greater than zero than removes the damage form the currentMonsterHealth
        //and sets it health
        if (damageOnMonster > 0) {
            currentMonsterHealth -= damageOnMonster;
            info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).setCurrentHealth(currentMonsterHealth);
        }
        //if damage is below zero then attack is not given
        else {
            System.out.println("Sorry your attack is to small to put damage on the monster");
        }
        //represents the damage on the player
        double damageOnPlayer = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getAttack() - info.
                getPlayerOne().getDefense();
        //only plays damage is monster attack is greater than player defense
        if (damageOnPlayer > 0) {
            currentPlayerHealth -= damageOnPlayer;
            setCurrentHealth(currentPlayerHealth);
        } else {
            System.out.println("The monster could not hurt you");
        }
        //if player is below zero than game ends
        if (currentPlayerHealth <= 0) {
            System.out.println("sorry you have died to the monster");
            System.exit(0);
        }
        //if monster health is below zero than eroor message prints and points add up
        else if (currentMonsterHealth <= 0) {
            return "you have killed the monster!!";
        }
        return "";
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param indexOfRoom index of the room the monster is in
     * @param indexOfMonster index if the monster in the room
     * @return the experience points you got in monster fight
     */
    public static double increaseExperiecePoints(Info info, int indexOfRoom, int indexOfMonster) {
        //putting a variable to set player attack, defense, and health
        double monsterAttack = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getAttack();
        double monsterDefense= info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getDefense();
        double monsterHealth = info.getRooms()[indexOfRoom].getMonsters().get(indexOfMonster).getHealth();
        //adds the experience points using the formula
        EXPERIENCE_POINTS += (((monsterAttack + monsterDefense) / 2 + monsterHealth) * 20);
        return EXPERIENCE_POINTS;
    }

    /**
     *
     * @param newLevel the new level (the level that the player wants to get)
     * @return the amounts of points needed to move up another level
     */
    public static double getCurrentLevelFromExp(int newLevel) {
        if (newLevel == 0) {
            return 0;
        }
        if (newLevel == 1) {
            return 25;
        }
        if (newLevel == 2) {
            return 50;
        }
        return (getCurrentLevelFromExp(newLevel-2)+getCurrentLevelFromExp(newLevel - 1))*1.1;
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     * @param newlevel the next level that the player wants to get
     * @param points the points needed to get to that next level
     */
    public static void upGradeLevel(Info info, int newlevel, double points) {
        //if you have the amount of points needed to upgrade than you move to next level
        if (EXPERIENCE_POINTS >= points) {
            System.out.println("You have upgraded to level " + newlevel);
            EXPERIENCE_POINTS -= points;
            info.getPlayerOne().setLevel(newlevel);
            //method to upgrade abilites
            upgradeAbilites(info);
        }
        //if no level up than says how much needed to upgrade
        else {
            System.out.println("You need " + (points - EXPERIENCE_POINTS) + " to level up to level to " + newlevel);
        }
    }

    /**
     *
     * @param info an info object so I can access any element in the JSON
     */
    public static void upgradeAbilites(Info info) {
        //simply sets a new variable to upgrade attack, defense, and health
        double newAttack = info.getPlayerOne().getAttack() * 1.5;
        double newDefense = info.getPlayerOne().getDefense() * 1.5;
        double newHealth  = info.getPlayerOne().getHealth() * 1.3;
        //then sets the attack, defense, and health and regens health
        info.getPlayerOne().setAttack(newAttack);
        info.getPlayerOne().setDefense(newDefense);
        info.getPlayerOne().setHealth(newHealth);
        setCurrentHealth(newHealth);
    }

    /**
     *
     * @return the current health
     */
    public static double getCurrentHealth() {
        return CURRENT_HEALTH;
    }

    /**
     *
     * @param currentHealth sets the current health
     */
    public static void setCurrentHealth(double currentHealth) {
        CURRENT_HEALTH = currentHealth;
    }


}
