/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private Treasure treasure;
    private boolean cheat;
    private boolean easy;
    //set to 0 initially; 1 == win & game over; 2 == lose & game over
    private int winCondition;
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        cheat = (toughness == 0);
        easy = (toughness == 0.1);
        treasure = new Treasure();
        winCondition = 0;
    }

    public int getWinCondition()
    {
        return winCondition;
    }
    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }
        if(!cheat){
        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            if(terrain.getTerrainName().equals("Plains")){
                printMessage = "You want a regular fight, stranger?  You got it!\nOof! Umph! Ow!\n";}
            else if(terrain.getTerrainName().equals("Desert")){
                printMessage = "You want trouble, stranger? Why already so hot, Welp  You got it!\nOof! Umph! Ow!\n";}
            else if(terrain.getTerrainName().equals("Jungle")){
                printMessage = "You want trouble, stranger!  Can you even see? \nOof! Umph! Ow!\n";}
            else if(terrain.getTerrainName().equals("Ocean")){
                printMessage = "You want trouble, stranger!  You're surrounded by water\nBoom,bam,pow\n";}
            else{printMessage = "If its a fight you want, you got it, \nOof! Umph! Ow!\n"; }
            int goldDiff = (int)(Math.random() * 10) + 1;
            if(easy){goldDiff *= 2;}
            if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                if(hunter.getGold() - goldDiff < 0)
                {
                    printMessage += "What?! You don't have enough to pay up? Guess your time is up!";
                    winCondition = 2;
                }
                else
                {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                    hunter.changeGold(-1 * goldDiff);
                }
            }
        }
        }
        else {
            if (Math.random() > noTroubleChance) {
                printMessage = "You couldn't find any trouble";
            } else {
                if (terrain.getTerrainName().equals("Plains")) {
                    printMessage = "You want a regular fight, stranger?  You got it!\nOof! Umph! Ow!\n";
                } else if (terrain.getTerrainName().equals("Desert")) {
                    printMessage = "You want trouble, stranger? Why already so hot, Welp  You got it!\nOof! Umph! Ow!\n";
                } else if (terrain.getTerrainName().equals("Jungle")) {
                    printMessage = "You want trouble, stranger!  Can you even see? \nOof! Umph! Ow!\n";
                } else if (terrain.getTerrainName().equals("Ocean")) {
                    printMessage = "You want trouble, stranger!  You're surrounded by water\nBoom,bam,pow\n";
                } else {
                    printMessage = "If its a fight you want, you got it, \nOof! Umph! Ow!\n";
                }
                int goldDiff = 100;
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
        }
    }


    public void huntForTreasure(){
        String treasureStr = treasure.getType();
        printMessage = "You search in the town for treasure... and found" + treasureStr;
        if(treasureStr.equals(treasure.DUST)){
            printMessage += ("\nAhhhchoo! That dust is mighty dusty, and it's certainly no treasure!");
        }
        else
        {
            if(hunter.collectTreasure(treasure))
            {
                printMessage += ("\n That's a new one! You pick it up and add it to your treasure collection");
                if(Treasure.collectionHAsALLTreasures(hunter.getTreasureCollection()))
                {
                winCondition = 1;
                }
            }
            else
            {
                printMessage += ("You have one of those already, who needs two?! You put it back.");
            }
        }
    }


    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .16)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .33)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .5)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .66)
        {
            return new Terrain("Desert", "Water");
        }
        else if (rnd <.83)
        {
            return new Terrain("Cave", "Torch");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }
}