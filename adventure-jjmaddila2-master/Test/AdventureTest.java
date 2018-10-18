import com.example.Adventure;
import com.example.Info;
import com.example.Item;
import com.google.gson.*;
import org.junit.Rule;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
public class AdventureTest {
    Gson gson = new Gson();
    Adventure test = new Adventure();
    Adventure newTest = new Adventure();
    Info info  = new Info();

    @Test
    public void goToAnotherRoomTest() {
        assertEquals("JimDesk", test.goToAnotherRoom("north", "PamOffice", test.infoObject()));
        assertEquals("CreedDesk", test.goToAnotherRoom("northeast", "PamOffice", test.infoObject()));
    }

    @Test
    public void testPrintingRoomDescriptions() {
        assertEquals("You are at Pam's office you can see Jim's and Creed's office", test.printOutRoomDescription(test.infoObject()
                , "PamDesk"));
    }

    @Test
    public void testPrintingRoomItems() {
        assertEquals("Items in this room: pen", test.printOutRoomItems(test.infoObject()
                , "PamDesk"));
    }

    @Test
    public void testPrintingRoomDirections() {
        assertEquals("No monsters in room, you can go: North, NorthEast", test.printOutRoomDirections(test.infoObject()
                , "PamDesk"));
    }

    @Test
    public void testSubstringFirstWord() {
        assertEquals("go", test.substringUserInput("go north"));
        assertEquals("take", test.substringUserInput("take me out to the ballgame"));
        assertEquals("", test.substringUserInput("hey north"));
    }

    @Test
    public void testSubstringSecondWord() {
        assertEquals("north", test.substringSecondWordInUserInput("go north"));
        assertEquals("me out to the ballgame", test.substringSecondWordInUserInput("take me out to the ballgame"));
        assertEquals("north", test.substringSecondWordInUserInput("hey north"));
    }

    @Test
    public void testPrintListOfItem() {
        ArrayList<Item> sample = new ArrayList<Item>(Arrays.asList(new Item("pen", .1),
                new Item("hammer", .1)));
        assertEquals("You have: pen, hammer", test.printOutListOfItems(sample));
    }


    @Test
    public void testPickingUpItems() {
        ArrayList<Item> sample = new ArrayList<Item>(Arrays.asList(new Item("pen", .1)));
        ArrayList<Item> sample1 = new ArrayList<Item>(Arrays.asList(new Item("pen", .1),
                new Item("hammer", .1)));
        ArrayList<Item> temp = new ArrayList<Item>();
        assertEquals(sample, test.pickUpItem(temp, "pen", test.infoObject(), "PamDesk"));
        assertEquals(sample1, test.pickUpItem(temp, "wrapping paper", test.infoObject(), "JimDesk"));
        assertEquals(null, test.pickUpItem(temp, "wrapping ", test.infoObject(), "JimDesk"));
    }

    @Test
    public void testDropingItems() {
        ArrayList<Item> sample = new ArrayList<Item>(Arrays.asList(new Item("pen", .1)));
        ArrayList<Item> sample1 = new ArrayList<Item>(Arrays.asList(new Item("pen", .1),
                new Item("wrapping paper", .15)));
        assertEquals(sample, test.dropItem(sample1, "wrapping paper", test.infoObject(), "PamDesk"));
        assertEquals(sample1, test.dropItem(sample1, "wrapping ", test.infoObject(), "PamDesk"));
    }

    @Test
    public void testGetStartingRoom() {
        assertEquals("PamDesk", test.infoObject().getStartingRoom());
    }

    @Test
    public void testGetEndingRoom() {
        assertEquals("MichealOffice", test.infoObject().getEndingRoom());
    }

    @Test
    public void testGetName() {
        assertEquals("PamDesk", test.infoObject().getRooms()[0].getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("You are at Pam's office you can see Jim's and Creed's office",
                test.infoObject().getRooms()[0].getDescription());
    }


      @Test
    public void testGetItems() {
        ArrayList<String>items = new ArrayList<String>(Arrays.asList("pen"));
        assertEquals(items, test.infoObject().getRooms()[0].getItems());
    }

    @Test
    public void testGetDirectionName() {
        assertEquals("North", test.infoObject().getRooms()[0].getDirection()[0].getDirectionName());
    }

    @Test
    public void testGetRoom() {
        assertEquals("JimDesk", test.infoObject().getRooms()[0].getDirection()[0].getRoom());
    }

    @Test
    public void testMonsterName() {
        assertEquals("snowman", test.infoObject().getRooms()[1].getMonsters().get(0).getName());
    }

    @Test
    public void testMonsterAttack() {
        assertEquals(0.3, test.infoObject().getRooms()[1].getMonsters().get(0).getAttack(), .01);
    }

    @Test
    public void testMonsterDefense() {
        assertEquals(0.1, test.infoObject().getRooms()[1].getMonsters().get(0).getDefense(), .01);
    }

    @Test
    public void testMonsterHealth() {
        assertEquals(0.625, test.infoObject().getRooms()[1].getMonsters().get(1).getHealth(), .01);
    }

    @Test
    public void testPlayerAttack() {
        assertEquals(1, test.infoObject().getPlayerOne().getAttack(), .01);
    }

    @Test
    public void testPlayerDefense() {
        assertEquals(1, test.infoObject().getPlayerOne().getDefense(), .01);
    }

    @Test
    public void testPlayerHealth() {
        assertEquals(1.5, test.infoObject().getPlayerOne().getHealth(), .01);
    }

    @Test
    public void testPlayerLevel() {
        assertEquals(0, test.infoObject().getPlayerOne().getLevel());
    }

    @Test
    public void testPlayerName() {
        assertEquals("Player 1", test.infoObject().getPlayerOne().getName());
    }

    @Test
    public void testPlayerItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        assertEquals(items, test.infoObject().getPlayerOne().getItems());
    }

    @Test
    public void testItemDamage() {
        assertEquals(0.1, test.infoObject().getRooms()[0].getItems().get(0).getDamage(), .01);
    }
    @Test
    public void testItemName() {
        assertEquals("pen", test.infoObject().getRooms()[0].getItems().get(0).getName());
    }

    @Test
    public void testPrintPlayerCurrentHealth() {
        assertEquals("Player health: ##########_________", test.printPlayerCurrentHealth(info, 1.0, 0.5));
        assertEquals("Player health: ########___________", test.printPlayerCurrentHealth(info, 1.0, 0.43));
    }

    @Test
    public void testPrintMonsterCurrentHealth() {
        assertEquals("Monster health: ###################", test.printMonsterCurrentHealth(test.infoObject(), 3, 0));
        assertEquals("Monster health: ###################", test.printMonsterCurrentHealth(test.infoObject(), 2, 1));
    }

    @Test
    public void testGetCurrentLevelFromExp() {
        assertEquals(25, test.getCurrentLevelFromExp(1),.1);
        assertEquals(50, test.getCurrentLevelFromExp(2),.1);
        assertEquals(82.5, test.getCurrentLevelFromExp(3),.1);
        assertEquals(145.75, test.getCurrentLevelFromExp(4),.1);
    }

    @Test
    public void testIfMonsterKilled() {
        String result1 = newTest.attackMonster(newTest.infoObject(), 4, 0, newTest.infoObject().getPlayerOne().getHealth(),
                newTest.infoObject().getRooms()[4].getMonsters().get(0).getHealth());
        String result = test.attackMonster(test.infoObject(), 1, 0, test.infoObject().getPlayerOne().getHealth(),
                test.infoObject().getRooms()[1].getMonsters().get(0).getHealth());

        assertEquals(true, test.ifMonsterKilled(test.infoObject(),1,0, result));
        assertEquals(false, newTest.ifMonsterKilled(newTest.infoObject(),4,0, result1));
    }

    @Test
    public void testAttackMonster() {
        assertEquals("you have killed the monster!!", newTest.attackMonster(newTest.infoObject(), 1,
                0, newTest.infoObject().getPlayerOne().getHealth(),
                test.infoObject().getRooms()[1].getMonsters().get(0).getHealth()));
        assertEquals("", test.attackMonster(test.infoObject(), 4, 0, test.infoObject().getPlayerOne().getHealth(),
                test.infoObject().getRooms()[4].getMonsters().get(0).getHealth()));
    }

    @Test
    public void testAttackMonsterWithItem() {
        ArrayList<Item> sample = new ArrayList<Item>(Arrays.asList(new Item("pen", .1)));
        assertEquals("you have killed the monster!!", newTest.attackMonsterWithItem(newTest.infoObject(), sample,
                1, 0, newTest.infoObject().getPlayerOne().getHealth(),
                test.infoObject().getRooms()[1].getMonsters().get(0).getHealth(), "pen"));
        assertEquals("", test.attackMonsterWithItem(test.infoObject(),sample, 4, 0,
                test.infoObject().getPlayerOne().getHealth(), test.infoObject().getRooms()[4].getMonsters().
                        get(0).getHealth(), "pen"));
    }

    @Test
    public void testExperiencePoints() {
        assertEquals(14.0, newTest.increaseExperiecePoints(newTest.infoObject(), 1,0),.1);
        assertEquals(47.0, newTest.increaseExperiecePoints(newTest.infoObject(), 3,0),.1);
    }

    @Test
    public void testUpgrades() {
        newTest.increaseExperiecePoints(newTest.infoObject(), 3,0);
        newTest.upGradeLevel(newTest.infoObject(), 1, 25);
        assertEquals(0, newTest.infoObject().getPlayerOne().getLevel());
        assertEquals(1, newTest.infoObject().getPlayerOne().getAttack(),.1);
        assertEquals(1, newTest.infoObject().getPlayerOne().getDefense(),.1);
        assertEquals(1.5, newTest.infoObject().getPlayerOne().getHealth(),.1);
    }




}
