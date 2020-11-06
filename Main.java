import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main implements Runnable, ActionListener{

  // Class Variables 
  JPanel mainPanel;

  JTextField inputField;
  JTextArea outputField;

  JTextArea descriptionDisplay;
  JTextArea inventoryDisplay;
  JLabel mapDisplay;
  ImageIcon mapSmall;
  ImageIcon mapBig;

  Font basicFont;

  //initialize the main Variables
  String pureInput; //the input but it's not sperated
  String[] input; //the input split up into it's seperate words (0 is the action, 1 is the target)
  String location = "menu"; //the player's current location
  ArrayList<String> inventory; //the array list to hold the inventory items


  //location specific Variables
  //room
  boolean roomDoorOpen = false;
  boolean roomDrawerOpen = false;
  boolean hasDrawerCoin = false;

  //town
  boolean hasWellCoin = false;
  boolean hasBarrelCoin = false;
  
  //shop
  int shopBalance = 0;//the current in store credit you have given the shopkeeper
  int coinsGiven = 0; //the total amount of coins you've given to the shopkeeper, will not go over 2. (this is to prevent softlocking)

  //forest
  boolean shrubguyEntered = false;
  boolean shrubguyExited = false;
  boolean hasRockCoin = false;

  //beach
  boolean boxRevealed = false;
  int oldManConvo = 0;

  //ship
  boolean chestUnlocked = false;


  // Method to assemble our GUI
  public void run(){
    // Creats a JFrame that is 800 pixels by 600 pixels, and closes when you click on the X
    JFrame frame = new JFrame("Sock Quest");
    // Makes the X button close the program
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // makes the windows 800 pixel wide by 600 pixels tall
    frame.setSize(800,600);
    // shows the window
    frame.setVisible(true);
    
    //set up the main panel

    mainPanel = new JPanel();
    mainPanel.setLayout(null); 

    //set up the components
    inputField = new JTextField();
    //the menu description (sorry for the cheesy ASCII art)
    outputField = new JTextArea("welcome to:\n\n #####  #######  #####  #    #     #####  #     # #######  #####  #######\n#     # #     # #     # #   #     #     # #     # #       #     #    #    \n#       #     # #       #  #      #     # #     # #       #          #    \n #####  #     # #       ###       #     # #     # #####    #####     #    \n      # #     # #       #  #      #   # # #     # #             #    #    \n#     # #     # #     # #   #     #    #  #     # #       #     #    #    \n #####  #######  #####  #    #     #### #  #####  #######  #####     #    \n\ntype \"start\" to begin...");

    descriptionDisplay = new JTextArea("menu description:\n- type \"start\" to begin");
    inventoryDisplay = new JTextArea("inventory:");
    mapDisplay = new JLabel();

    mapSmall = new ImageIcon("map1.png");
    mapBig = new ImageIcon("map2.png");

    basicFont = new Font("MONOSPACED", Font.PLAIN, 12);

    //set up component locations
    inputField.setBounds(10,305,385,20);
    outputField.setBounds(10,10,780,285);
    inventoryDisplay.setBounds(10,335,187,225);
    descriptionDisplay.setBounds(208,335,187,225);
    mapDisplay.setBounds(405,305,385,255);

    //disable the text areas
    outputField.setEnabled(false);
    inventoryDisplay.setEnabled(false);
    descriptionDisplay.setEnabled(false);

    //create an action listener on the input field
    inputField.addActionListener(this);

    //set the monospaced font 
    inputField.setFont(basicFont);
    outputField.setFont(basicFont);
    inventoryDisplay.setFont(basicFont);
    descriptionDisplay.setFont(basicFont);

    //add components to the main panel
    mainPanel.add(inputField);
    mainPanel.add(outputField);
    mainPanel.add(inventoryDisplay);
    mainPanel.add(descriptionDisplay);
    mainPanel.add(mapDisplay);

    //add main panel to the frame
    frame.add(mainPanel);

    //add the map image to the map display
    mapDisplay.setIcon(mapSmall);

    //initialize the array list 
    inventory = new ArrayList<>();
  }


  //functional methods
  public boolean inventoryContains(String item){
    //check every item in the inventory array and see if the requested item is there
    for(int i = 0; i < inventory.size(); i++){
      if(inventory.get(i).equals(item)){
        return true;
      }
    }
    //if it has check all the contents of the inventory, and nothing matches your parameter, retruns false
    return false;
  } 

  public void descriptionAdd(String item){
    //adds one item to the room description (this just looks cleaner than typing this every time)
    descriptionDisplay.setText(descriptionDisplay.getText() + "\n" + "- " + item);
  }

  //location methods:
  // --- MENU --- //
  public void locationMenu(){
    if(pureInput.equals("start")){
      outputField.setText("You wake up...\nand you're missing...\nyour left sock!\n\nafter panicking for a few minutes, you decide to investigate");
      location = "room";
    }
  }
  // --- ROOM --- //
  public void locationRoom(){
    if(input.length > 1){
      switch(input[1]){
        case "drawer":
          roomItemDrawer();
          break;
        case "door":
          roomItemDoor();
          break;
        case "bed":
          roomItemBed();
          break;
        case "coin":
          if(!hasDrawerCoin && roomDrawerOpen){ //if the drawer is open and you have not gotten the coin yet
            roomItemCoin();
          }else{
            outputField.setText("there is no " + input[1] + " at this location.");
          }
          break;
        case "north":
          roomDirectionNorth();
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
      }
    }else{
      outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  }
  //room items
  //DRAWER//
  public void roomItemDrawer(){
    switch(input[0]){
      case "open":
      //check if the drawer is closed
        if(!roomDrawerOpen){
          outputField.setText("You open the drawer.");
          roomDrawerOpen = true;
        }else{
          outputField.setText("You cannot do this, the drawer is already open.");
        }
        break;
      case "close":
      //check if the drawer is open
        if(roomDrawerOpen){
          outputField.setText("You close the drawer.");
          roomDrawerOpen = false;
        }else{
          outputField.setText("You cannot do this, the drawer is already closed.");
        }
        break;
      case "examine":
        if(roomDrawerOpen){
          if(hasDrawerCoin){
            outputField.setText("it is a chest of drawers in your room. \nit is empty.");
          }else{
            outputField.setText("it is a chest of drawers in your room. \nit is currently open, and you see a shiny gold coin inside.");
          }
        }else{
          outputField.setText("it is a chest of drawers in your room. \nit is currently closed.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //DOOR//
  public void roomItemDoor(){
    switch(input[0]){
      case "open":
      //check if the drawer is closed
        if(!roomDoorOpen){
          outputField.setText("You open the door.");
          roomDoorOpen = true;
        }else{
          outputField.setText("You cannot do this, the door is already open.");
        }
        break;
      case "close":
      //check if the drawer is open
        if(roomDoorOpen){
          outputField.setText("You close the door.");
          roomDoorOpen = false;
        }else{
          outputField.setText("You cannot do this, the door is already closed.");
        }
        break;
      case "examine":
        if(roomDoorOpen){
          outputField.setText("it is the front door to your humble home. \nit is currently open, you see a path that goes north.");
        }else{
          outputField.setText("it is the front door to your humble home. \nit is currently closed.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //BED//
  public void roomItemBed(){
    switch(input[0]){
      case "use":
        outputField.setText("now is not the time to be sleeping!");
        break;
      case "examine":
        outputField.setText("It is your wooden bed. \nit is not very comfortable.");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //COIN//
  public void roomItemCoin(){
    switch(input[0]){
      case "get":
        outputField.setText("You take the gold coin from the open drawer");
        inventory.add("coin");
        hasDrawerCoin = true;
        break;
      case "examine":
        outputField.setText("It is a gold coin that can be used to buy things at shops. \nit is of small value.");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //room directions
  //NORTH//
  public void roomDirectionNorth(){
    switch(input[0]){
      case "go":
        if(roomDoorOpen){
          outputField.setText("You travel out the front door and follow the path until you reach the town.");
          location = "town";
        }else{
          outputField.setText("You cannot go north, the front door is closed");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " " + input[1]);
        break;
    }
  }

  // --- TOWN --- //
  public void locationTown(){
    if(input.length > 1){
      switch(input[1]){
        case "well":
          townItemWell();
          break;
        case "barrel":
          townItemBarrel();
          break;
        case "merchant":
          townItemMerchant();
          break;
        case "north":
          townDirectionNorth();
          break;
        case "east":
          townDirectionEast();
          break;
        case "south":
          townDirectionSouth();
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
      }
    }else{
      outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  }
  //town items
  //WELL//
  public void townItemWell(){
    switch(input[0]){
      case "examine":
        outputField.setText("there is a bucket lowered in the well with a crank to raise it.\n(type \"use well\" to raise the crank) \nyou see your reflection in the water...\ngood lookin\' fella.");
        break;
      case "use":
        if(!hasWellCoin){ //if you haven't found the well coin
          outputField.setText("you raise the bucket out of the well water. inside you find a gold coin.\nYou take it because it's free money!");
          inventory.add("coin");
          hasWellCoin = true; //you found the well coin
        }else{
          outputField.setText("you raise the bucket out of the well water, but there is nothing inside.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //BARREL//
  public void townItemBarrel(){
    switch(input[0]){
      case "examine":
        if(!hasBarrelCoin){ //if you don't have the barrel coin yet
          outputField.setText("it's a barrel full of apples, inside you notice a gold coin.\nyou take it because it's free money!");
          inventory.add("coin");
          hasBarrelCoin = true; //you found the barrel coin
        }else{
          outputField.setText("it's a barrel full of apples.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //GOAT MERCHANT//
  public void townItemMerchant(){
    switch(input[0]){
      case "examine":
        outputField.setText("the goat merchant is a tall heavy man, and he smells of goats\nI guess that's not much of a surprise.\n\nyou also notice that he is only wearing one sock. It seems you are not the only one missing a left sock today.");
        break;
      case "talkto":
        outputField.setText("you greet the goat seller, he replies \"Hello there! could I interest you in a goat? only one gold coin each!\"");
        break;
      case "give":
        if(inventoryContains(input[2])){ //if you have the item you're giving
          if(input[2].equals("coin")){ //if it is a coin
            if(!inventoryContains("goat")){ //if you haven't bought a goat yet
              outputField.setText("the goat merchant takes your gold coin and says: \"Thanks a bunch pal. I need these to buy another sock. \nmine was stolen last night\"\nhe then gives you the goat you purchased.");
              inventory.remove("coin");
              inventory.add("goat");
            }else{
              outputField.setText("Why would you do that? you only need one goat.");
            }
          }else{
            outputField.setText("the merchant tells you: \"I told you already, I want gold coins, not " + input[2] + "!\"");
          }
        }else{
          outputField.setText("you can't give something you don't have!");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //town directions
  //NORTH//
  public void townDirectionNorth(){
    switch(input[0]){
      case "go":
      //cannot enter forest without the map
        if(inventoryContains("map")){
            outputField.setText("now that you have your handy map you can safely enter the forest!");
            location = "forest";
        }else{
          outputField.setText("If you go into the forest without a map you'll surely get lost! \nInstead, you head back to the town.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " " + input[1]);
        break;
    }
  }
  //EAST//
  public void townDirectionEast(){
    switch(input[0]){
      case "go":
          outputField.setText("you follow a winding road outside the town. it takes you to a local shop outside town.");
          location = "shop";
        break;
      default:
        outputField.setText("You cannot " + input[0] + " " + input[1]);
        break;
    }
  }
  //SOUTH//
  public void townDirectionSouth(){
    switch(input[0]){
      case "go":
          outputField.setText("You travel down the path to the south until you end up back at your home");
          location = "room";
        break;
      default:
        outputField.setText("You cannot " + input[0] + " " + input[1]);
        break;
    }
  }


  // --- SHOP --- //
 public void locationShop(){
   if(input.length > 1){
      switch(input[1]){
        case "map":
          if(!inventoryContains("map")){ //if you don't have the map yet
            shopItemMap();
            break;
          }else{
            outputField.setText("you already have the map");
          }
          break;
        case "shovel":
          if(!inventoryContains("shovel")){ //if you don't have the shovel yet
            shopItemShovel();
            break;
          }else{
            outputField.setText("you already have the shovel");
          }
          break;
        case "shopkeeper":
          shopItemShopKeeper();
          break;
        case "west":
          shopDirectionWest();
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
      }
    }else{
      outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  } 
  //shop items
  //MAP//
  public void shopItemMap(){
    switch(input[0]){
        case "examine":
          outputField.setText("it is a map of the whole island!\n that might be useful for getting through the forest!");
          break;
        case "get":
          //if you have enough money 
          if(shopBalance >= 1){ //if you have at least one coin given to the shopkeeper
            outputField.setText("since you were a good customer and actually paid for it, you can take the map.\n(also, your minimap has been updated)"); 
            inventory.add("map");
            shopBalance--; //remove one from the shop balance
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the map, he then kicks you out of his shop.\n\nYou are now in the town.");
            location = "town";
          }
          break;
        default:
          outputField.setText("You cannot " + input[0] + " the " + input[1]);
          break;
      }
  }
  //SHOVEL//
  public void shopItemShovel(){
    switch(input[0]){
        case "examine":
          outputField.setText("it is a shovel.\n\nhow much description does it need?");
          break;
        case "get": //same as "get" and "take" above, now with a shovel
          //if you have enough money 
          if(shopBalance >= 1){
            outputField.setText("since you were a good customer and actually paid for it, you can take the shovel."); 
            inventory.add("shovel");
            shopBalance--; 
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the shovel, he then kicks you out of his shop.\n\nYou are now in the town.");
            location = "town";
          }
          break;
        default:
          outputField.setText("You cannot " + input[0] + " the " + input[1]);
          break;
      }
  }
  //SHOPKEEPER//
  public void shopItemShopKeeper(){
  switch(input[0]){
      case "examine":
        outputField.setText("The shopkeeper is a burly man. there is a sign beside him saying: \"pay first! take later!\"");
        break;
      case "talkto":
        outputField.setText("the shopkeeper says: \"Oho! welcome traveller! to buy things from THIS shop, you give me gold coins and take \nanything you want!");
        break;
      case "give":
        if(inventoryContains(input[2])){//if you have what you are trying to give
          if(input[2].equals("coin")){ //if it is a coin
            if(coinsGiven < 2){ //if you haven't given him a total of 2 coins yet (to make sure they can buy the goat)
              outputField.setText("the shopkepper takes your gold coin and says: \"Ok! now ya can take one item from the shop.\"");
              inventory.remove("coin");
              shopBalance += 1; //add one to how much the player can take
              coinsGiven += 1; //add one to total money given to the shopkeeper
            }else{
              outputField.setText("you've given the shopkeeper enough money by now. You don't want to spend it all in one place, do you?");
            }
          }else{
            outputField.setText("the shopkeeper tells you: \"I told you already, I want gold coins, not a " + input[2] + "!\"");
          }
        }else{
          outputField.setText("you can't give something you don't have!");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //shop directions
  //WEST//
  public void shopDirectionWest(){
    switch(input[0]){
        case "go":
            outputField.setText("You travel back down the winding road until you have returned to the town.");
            location = "town";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }

  // --- FOREST --- //
  public void locationForest(){
    if(input.length > 1){
        switch(input[1]){
          case "shrub":
            if(!shrubguyEntered && !shrubguyExited){  //if you have not yet met the SHRUBGUY
              forestItemShrub();
            }else{ //if SHRUBGUY has been revealed
              outputField.setText("there is no " + input[1] + " at this location.");
            }
            break;
          case "rock":
            forestItemRock();
            break;
          case "shrubguy": 
            if(shrubguyEntered && !shrubguyExited){ //if shrubguy exists
              forestItemShrubGuy();
            }else{ //if shrub guy does not exist
              outputField.setText("there is no " + input[1] + " at this location.");
            }
            break;
          case "goat":
            forestUseGoat();
            break;
          case "north":
            forestDirectionNorth();
            break;
          case "south":
            forestDirectionSouth();
            break;
          default:
            outputField.setText("there is no " + input[1] + " at this location.");
            break;
        }
    }else{
        outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  } 
  //forest items
  //SHRUB//
  public void forestItemShrub(){
    switch(input[0]){
        case "examine": //monty python reference #1
          outputField.setText("it is a shrub (it is one that looks nice, and it's not too expensive).\n\nyou reach in to see if there is anything inside... and you hear someone say: \"ow\".");
          break;
        default:
          outputField.setText("You cannot " + input[0] + " the " + input[1]);
          break;
    }
  }
  //ROCK//
  public void forestItemRock(){
    switch(input[0]){
      case "examine":
        if(!hasRockCoin){ //if the player hasn't taken a rock coin yet
          outputField.setText("it is a rock, you look underneath and find... \n\na gold coin!\nyou take it because it's free money!");
          inventory.add("coin");
          hasRockCoin = true;
        }else{
          outputField.setText("it is a rock, you look underneath and find... \n\nnothing... \n\nwhat? did you expect more money to be under there?");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //SHRUBGUY//
  public void forestItemShrubGuy(){
    switch(input[0]){
        case "examine":
          outputField.setText("it is not a shrub at all, just some creep who dresses up like a shrub to stop any passing travellers.\nonly if there was something i could use to get him to leave.");
          break;
        case "talkto":
          outputField.setText("you ask the SHRUBGUY to let you pass. \nshrubguy yells to you \"OH, SO YOU WANT TO LEAVE??? OK THEN!\" the SHRUBGUY briefly moved out of your way\nbut before you can pass, he returns to his original position and yells to you:\n\"HA HA, YOU THINK I WOULD ACTUALLY LET YOU PASS THAT EASILY!?!?!?\"");
          break;
        default:
          outputField.setText("You cannot " + input[0] + " the " + input[1]);
          break;
      }
  }
  //inventory items
  public void forestUseGoat(){
    switch(input[0]){
      case "use":
        if(inventoryContains("goat")){
            if(shrubguyEntered && !shrubguyExited){ //if shrub guy exists
              outputField.setText("the SHRUBGUY yells to you \"HA HA, YOU FOOL! YOU FELL VICTIM TO ONE OF THE CLASSIC BLUNDERS! \nNEVER GO IN AGAINST A SHRUBGUY WHEN DEATH IS ON THE LINE!!!!\" \nat this point, you are tired of the SHRUBGUY's yelling (although you appreciate the movie quote) and you \nrelease your goat, it begins to chew on SHRUBGUY's leaves which causes the SHRUBGUY to run away.\n\nthe path is now clear, you can continue through the forest.");
              shrubguyExited = true; //make shrubguy leave
            }else if(!shrubguyEntered){
              outputField.setText("why do you need to release the goat here?"); 
            }else{
              outputField.setText("now that you have gotten rid of the SHRUBGUY, your goat is kinda useless.");
            }
        }else{
          outputField.setText("what goat do you mean?");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }

  //forest directions
  //NORTH//
  public void forestDirectionNorth(){
    switch(input[0]){
        case "go":
          if(shrubguyEntered && !shrubguyExited){ //if shrubguy is blocking you
              outputField.setText("You cannot pass, as SHRUBGUY blocks your path\n(be careful, he has a knife)");
            }else if(!shrubguyEntered){ //if you haven't seen shrub guy yet
              outputField.setText("you begin to travel to the north, when you hear someone walking behind you. \nwhen suddenly a shrub jumps in front of you and blocks the path. \nyou realize, that it is not a shrub at all! it is a guy dressed like a shrub. \n\"GREETINGS TRAVELLER!!!!\" the shrub man cries \"IT IS I! THE AMAZING SHRUB-GUY!!!! \nDO NOT EVEN THINK ABOUT GOING NORTH!!! AS I WILL STOP YOU!!! A-HAHAHAHAHAH!!!\"");
              shrubguyEntered = true; //make shrubguy come to stop you
            }else{ //otherwise, you can pass
              outputField.setText("now that SHRUBGUY is gone, you can safely exit the forest. \nyou follow the long path until you arrive on the beach");
              location = "beach";
            }
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }
  //SOUTH//
  public void forestDirectionSouth(){
    switch(input[0]){
        case "go":
            outputField.setText("The forest was too spooky for you, so you retreat to the town.");
            location = "town";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }

  // --- BEACH --- //
  public void locationBeach(){
    if(oldManConvo > 0){ //if the player is in the old man conversation (this is first increased when talking to the old man) this is so that their inputs only effect the old man conversation and not anything else
      switch(oldManConvo){ //monty python reference #2
        case 1:
          outputField.setText("You tell the old man that your name is " + pureInput + ".\n\nthe old man says: \"What... is your Quest?\"");
          oldManConvo++; //go to the next question
          break;
        case 2:
          outputField.setText("You tell the old man that your quest is " + pureInput + ".\n\nthe old man says:  \"What... is your Favourite color?\"");
          oldManConvo++; //go to the next question
          break;
        case 3:
          outputField.setText("You tell the old man that your favourite color is " + pureInput + ".\n\nthe old man says:  \"Good Job...\"\n...\nYou realize that talking to this man was completely useless.");
          oldManConvo = 0; //reset the conversation
          break;
      }
    }else{ //check for actions
      if(input.length > 1){
        switch(input[1]){
          case "oldman":
            beachItemOldman();
            break;
          case "box":
            if(boxRevealed){ //if you have dug up the box
              beachItemBox();
            }else{
              outputField.setText("there is no " + input[1] + " at this location.");
            }
            break;
          case "shovel":
            beachUseShovel();
            break;
          case "north":
            beachDirectionNorth();
            break;
          case "east":
            beachDirectionEast();
            break;
          case "west":
            beachDirectionWest();
            break;
          default:
            outputField.setText("there is no " + input[1] + " at this location.");
            break;
        }
      }else{
        outputField.setText("unknown command, try typing \"help\" for a list of commands");
      }
    }
  }
  //beach items
  //OLD MAN//
  public void beachItemOldman(){
    switch(input[0]){
      case "examine":
        outputField.setText("it is an old man sitting in a small tent on the beach, he is murmuring things to himself.");
        break;
      case "talkto":
        outputField.setText("\"Stop! Who would cross this Beach must answer me these questions three, ere the other side he see.\"\nyou ask the old man what the first question is,\nthe man mumbles: \"What... is your name?\"");
        oldManConvo++; //add 1 to question counter (starts the conversation with the old man *before action check)
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //BOX//
  public void beachItemBox(){
    switch(input[0]){
      case "examine":
        if(!inventoryContains("key")){ //if you don't have the key yet
          outputField.setText("it is a small cardboard box that you dug up on the beach. inside there is a small key. \nyou decide to take it");
          inventory.add("key"); //give the user the key
        }else{
          outputField.setText("it is a small cardboard box that you dug up on the beach.\nit is empty.");
        }
        break;
      case "open": //this is the same as examine, just different wording
        if(!inventoryContains("key")){ 
          outputField.setText("inside the box there is a small key. \nyou decide to take it");
          inventory.add("key");
        }else{
          outputField.setText("the box is empty.");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //beach inventory items
  //SHOVEL//
  public void beachUseShovel(){
    switch(input[0]){
      case "use":
        if(inventoryContains("shovel")){ //if you have the shovel
          if(!boxRevealed){ //if you haven't dug up the beach yet
            outputField.setText("You start digging around the beach until you find a box buried underground.");
            boxRevealed = true; //make the box exist
          }else{
            outputField.setText("You've already dug holes through the entire beach, you probably shouldn't dig anymore.");
          }
        }else{
          outputField.setText("what shovel do you mean?");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //beach directions
  //NORTH//
  public void beachDirectionNorth(){
    switch(input[0]){
        case "go":
            outputField.setText("you notice a ship docked at the beach, you decide to board it.");
            location = "pirateShip";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }
  //EAST//
  public void beachDirectionEast(){
    switch(input[0]){
        case "go":
            outputField.setText("you continue along the beach until you arrive at a cave, it smells real bad inside.");
            location = "cave";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }
  //WEST//
  public void beachDirectionWest(){
    switch(input[0]){
        case "go":
            outputField.setText("You travel to the west until you reach the forest.");
            location = "forest";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }
  // --- PIRATE SHIP --- //
  public void locationPirateShip(){
    if(input.length > 1){
      switch(input[1]){
        case "pirate":
          shipItemPirate();
          break;
        case "chest":
          shipItemChest();
          break;
        case "key":
          shipUseKey();
          break;
        case "south":
          shipDirectionSouth();
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
        }
    }else{
        outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  }
  
  //pirate ship items
  //PIRATE//
  public void shipItemPirate(){
    switch(input[0]){
      case "examine":
        outputField.setText("it is a sad-looking pirate, he doesn't look hostile.\nmaybe you should talk to him."); 
        break;
      case "talkto":
        outputField.setText("\"Ahoy Matey, I 'ave gotten meself in a bit of a pickle. \nI 'ave mistakenly buried the key to me treasure chest under the beach to the south of 'ere.\nwould you do a favour fer me lad, and 'elp me find it?\"");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //CHEST//
  public void shipItemChest(){
    switch(input[0]){
      case "examine":
        if(!chestUnlocked){ //if the chest is locked
          outputField.setText("it is a chest belonging to the pirate, it is locked");
        }else{
          if(!inventoryContains("segway")){ //if you haven't gotten the segway yet
            outputField.setText("you open the now-unlocked chest.\ninside you find....\na segway?\n\nthe pirate is just as surprised as you are.\n\n\"That ain't me treasure!?! well I 'ave no use fer a segway! you can keep it young lad.\"\n\nyou take the segway.");
            inventory.add("segway"); //give the user the segway
          }else{
            outputField.setText("the chest is empty.");
          }
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //inventory items
  //KEY//
  public void shipUseKey(){
    switch(input[0]){
      case "use":
        if(inventoryContains("key")){ //if you have the key
          if(!chestUnlocked){ //if you have not unlocked the chest
            outputField.setText("You unlock the chest with the small key, now examine it to see what's inside!.");
            chestUnlocked = true; //unlock the chest
          }else{
            outputField.setText("You've already unlocked the chest, there is no need to unlock it again.");
          }
        }else{
          outputField.setText("what key do you mean?");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //pirate ship directions
  //SOUTH//
  public void shipDirectionSouth(){
    switch(input[0]){
        case "go":
            outputField.setText("You leave the pirate ship and return to the beach");
            location = "beach";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }

  /// --- Cave --- ///
  public void locationCave(){
    if(input.length > 1){
      switch(input[1]){
        case "peglegman":
          caveItemPeglegman();
          break;
        case "socks":
          caveItemSocks();
          break;
        case "diary":
          caveItemDiary();
          break;
        case "west":
          caveDirectionWest();
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
      }
    }else{
      outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  }
  //cave items
  //PEG LEG MAN//
  public void caveItemPeglegman(){
    switch(input[0]){
      case "examine":
        outputField.setText("The peg-legged man is very large, he sits alone in the cave looking very menacing.");
        break;
      case "talkto":
        outputField.setText("you ask the man if you could have your sock back, \"NO!\" the man cries \"If I can't wear two socks NO ONE CAN! \nthat's why I stole everyone's left sock. \nNOW SCRAM!!!!\"\nyou decide to stop talking to the man now.");
        break;
      case "give":
        if(inventoryContains(input[2])){ //if you have the item you are trying to give
          if(input[2].equals("segway")){ //if you are giveng the segway
            outputField.setText("\"what is this? a segway? finally, my one true dream of riding a segway \nhas been fulfilled. Thank you! in return I will give you your left sock back!\"\n...\n...\n\nfinally, you have obtained your left sock! you return home with two socks... and relax.");
            inventory.clear();
            inventory.add("sock"); //give the player their final reward
            location = "end"; //end the game
          }else{
            outputField.setText("the man tells you: \"WHAT IS THIS?! are you trying to bribe me with " + input[2] + "!?! \nNO! I will not accept it!\"");
          }
        }else{
          outputField.setText("you can't give something you don't have!");
        }
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //SOCKS//
  public void caveItemSocks(){
    switch(input[0]){
      case "examine":
        outputField.setText("it is a pile of everyone's left socks, maybe the peg-legged man took them!");
        break;
      case "get":
        outputField.setText("you try to grab a sock from the pile, but the peg-legged man stops you\n and says: \"NO! IF I CAN'T WEAR TWO SOCKS, NO ONE CAN!!\"");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }

  //DIARY//
  public void caveItemDiary(){
    switch(input[0]){
        case "examine":
          outputField.setText("you read an entry in the peg-legged man's diary:\n\n\"I hate my peg-leg, only if I had some sort of motorized vehicle so that I could get around easier.\""); //hint: IT'S THE SEGWAY!!!
          break;
        default:
          outputField.setText("You cannot " + input[0] + " the " + input[1]);
          break;
    }
  }

  //cave directions
  //WEST//
  public void caveDirectionWest(){
    switch(input[0]){
        case "go":
            outputField.setText("You leave the cave, and return to the beach");
            location = "beach";
          break;
        default:
          outputField.setText("You cannot " + input[0] + " " + input[1]);
          break;
      }
  }

  // method called when a button is pressed
  public void actionPerformed(ActionEvent e){
    // get the command from the action
    String command = e.getActionCommand();

    //store the input in a Variables
    pureInput = inputField.getText();
    input = inputField.getText().split(" ");

    //clear the input and output fields
    inputField.setText("");

    //check if the user wants a list of commands
    if(pureInput.equals("help")){
      outputField.setText("COMMANDS:\n-examine: get a further description of an object\n-use: interact with a given object or inventory item\n-get: puts the given object in your inventory\n-go: sends you to the given location\n-open/close: opens or closes the given object\n-talkto: talks to the given person\n-give (target) (item): gives the specified person the specified object\n-eat: eats the specified target");
    }else{
      //go to the method based on the player's location
      switch(location){
        case "menu":
          locationMenu();
          break;
        case "room":
          locationRoom();
          break;
        case "town":
          locationTown();
          break;
        case "shop":
          locationShop();
          break;
        case "forest":
          locationForest();
          break;
        case "beach":
          locationBeach();
          break;
        case "pirateShip":
          locationPirateShip();
          break;
        case "cave":
          locationCave();
          break;
        case "end":
          outputField.setText("####### #     # #######    ####### #     # ######  ###\n   #    #     # #          #       ##    # #     # ###\n   #    #     # #          #       # #   # #     # ###\n   #    ####### #####      #####   #  #  # #     #  # \n   #    #     # #          #       #   # # #     #    \n   #    #     # #          #       #    ## #     # ###\n   #    #     # #######    ####### #     # ######  ### ");
          break;
        }
    }

    //update the inventory display
    inventoryDisplay.setText("inventory:");
    for(int i = 0; i < inventory.size(); i++){ //repeat for every item in the inventory
      inventoryDisplay.setText(inventoryDisplay.getText() + "\n" + inventory.get(i)); //add the next part of the inventory to the display 
    }
    //update the map display
    if(inventoryContains("map")){ //if they bought the better map
      mapDisplay.setIcon(mapBig); //show the full map
    }else{ //otherwise
      mapDisplay.setIcon(mapSmall); //show the small map
    }
    //update the room description (show the items that can be interacted with)
    descriptionDisplay.setText(location + " description:");
    switch(location){
      case "menu":
        descriptionAdd("type \"start\" to begin");
        break;
      case "room":
        descriptionAdd("bed");
        descriptionAdd("drawer");
        descriptionAdd("door");
        descriptionAdd("north");
        //if the coin is available
        if(roomDrawerOpen && !hasDrawerCoin){
          descriptionAdd("coin");
        }
        break;
      case "town":
        descriptionAdd("well");
        descriptionAdd("barrel");
        descriptionAdd("merchant");
        descriptionAdd("east");
        descriptionAdd("north");
        descriptionAdd("south");
        break;
      case "shop":
        descriptionAdd("shopkeeper");
        if(!inventoryContains("map")){ //if you haven't bought the map
          descriptionAdd("map");
        }
        if(!inventoryContains("shovel")){ //if you haven't bought the shovel
          descriptionAdd("shovel");
        }
        descriptionAdd("west");
        break;
      case "forest":
        descriptionAdd("rock");
        if(shrubguyEntered && !shrubguyExited){ //if shrubguy is there
            descriptionAdd("shrubguy");
          }else if(!shrubguyEntered){ //if he is still a shrub
            descriptionAdd("shrub");
          }
        descriptionAdd("north");
        descriptionAdd("south");
        break;
      case "beach":
        descriptionAdd("oldman");
        if(boxRevealed){ //if you dug up the box
          descriptionAdd("box");
        }
        descriptionAdd("north");
        descriptionAdd("east");
        descriptionAdd("west");
        break;
      case "pirateShip":
        descriptionAdd("pirate");
        descriptionAdd("chest");
        descriptionAdd("south");
        break;
      case "cave":
        descriptionAdd("peglegman");
        descriptionAdd("socks");
        descriptionAdd("diary");
        descriptionAdd("west");
        break;
    }
  }

  // Main method to start our program
  public static void main(String[] args){
    // Creates an instance of our program
    Main gui = new Main();
    // Lets the computer know to start it in the event thread
    SwingUtilities.invokeLater(gui);
  }
}
