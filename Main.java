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
  //placeholder until my image is made
  JTextArea mapDisplay;

  Font basicFont;

  //initialize the main Variables
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
    mapDisplay = new JTextArea();

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
    mapDisplay.setEnabled(false);

    //create an action listener on the input field
    inputField.addActionListener(this);

    //set the monospaced font 
    inputField.setFont(basicFont);
    outputField.setFont(basicFont);
    inventoryDisplay.setFont(basicFont);
    descriptionDisplay.setFont(basicFont);
    mapDisplay.setFont(basicFont);

    //add components to the main panel
    mainPanel.add(inputField);
    mainPanel.add(outputField);
    mainPanel.add(inventoryDisplay);
    mainPanel.add(descriptionDisplay);
    mainPanel.add(mapDisplay);

    //add main panel to the frame
    frame.add(mainPanel);

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
    //adds one item to the room description (this just looks cleaner than adding this every time)
    descriptionDisplay.setText(descriptionDisplay.getText() + "\n" + "- " + item);
  }

  //location methods:
  // --- MENU --- //
  public void locationMenu(){
    switch(input[0]){
      case "start":
        outputField.setText("You wake up...\nand you're missing...\nyour left sock!\n\nafter panicking for a few minutes, you decide to investigate");
        location = "room";
        break;
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
          if(!inventoryContains("coin") && roomDrawerOpen){
            roomItemCoin();
          }else{
            outputField.setText("you cannot do this");
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
      case "take":
          outputField.setText("You take the gold coin from the open drawer");
          inventory.add("coin");
          hasDrawerCoin = true;
        break;
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
        outputField.setText("there is a bucket lowered in the well with a crank to raise it. \nyou see your reflection in the water...\ngood lookin\' fella.");
        break;
      case "use":
        if(!hasWellCoin){
          outputField.setText("you raise the bucket out of the well water. inside you find a gold coin.\nYou take it because it's free money!");
          inventory.add("coin");
          hasWellCoin = true;
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
        if(!hasBarrelCoin){
          outputField.setText("it's a barrel full of apples, inside you notice a gold coin.\nyou take it because it's free money!");
          inventory.add("coin");
          hasBarrelCoin = true;
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
        if(inventoryContains(input[2])){
          if(input[2].equals("coin")){
            if(!inventoryContains("goat")){
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
          if(!inventoryContains("map")){
            shopItemMap();
            break;
          }else{
            outputField.setText("you already have the map");
          }
          break;
        case "shovel":
          if(!inventoryContains("shovel")){
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
          if(shopBalance >= 1){
            outputField.setText("since you were a good customer and actually paid for it, you can take the map."); 
            inventory.add("map");
            shopBalance--; 
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the map, he then kicks you out of his shop.");
            location = "town";
          }
          break;
        case "take":
          //if you have enough money 
          if(shopBalance >= 1){
            outputField.setText("since you were a good customer and actually paid for it, you can take the map."); 
            inventory.add("map");
            shopBalance--; 
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the map, he then kicks you out of his shop.");
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
        case "get":
          //if you have enough money 
          if(shopBalance >= 1){
            outputField.setText("since you were a good customer and actually paid for it, you can take the shovel."); 
            inventory.add("shovel");
            shopBalance--; 
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the shovel, he then kicks you out of his shop.");
            location = "town";
          }
          break;
        case "take":
          //if you have enough money 
          if(shopBalance >= 1){
            outputField.setText("since you were a good customer and actually paid for it, you can take the shovel."); 
            inventory.add("shovel");
            shopBalance--; 
          }else{
            outputField.setText("the shopkeeper yells at you for trying to steal the shovel, he then kicks you out of his shop.");
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
        outputField.setText("the shopkeeper says: \"Oho! welcome traveller! to buy things from THIS shop, you give me gold coins and take anything you want!");
        break;
      case "give":
        if(inventoryContains(input[2])){
          if(input[2].equals("coin")){
            if(coinsGiven < 2){
              outputField.setText("the shopkepper takes your gold coin and says: \"Ok! now ya can take one item from the shop.\"");
              inventory.remove("coin");
              shopBalance += 1;
              coinsGiven += 1;
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
            if(!shrubguyEntered && !shrubguyExited){  
              forestItemShrub();
            }else{
              outputField.setText("it turns out the shrub was actually a guy in disguise... remember?");
            }
            break;
          case "rock":
            forestItemRock();
            break;
          case "shrubguy":
            if(shrubguyEntered && !shrubguyExited){
              forestItemShrubGuy();
            }else if(!shrubguyEntered){
              outputField.setText("there is no " + input[1] + " at this location.");
            }else{
              outputField.setText("shrub guy ran away... remember?");
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
        case "examine":
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
          if(!hasRockCoin){
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
            if(shrubguyEntered && !shrubguyExited){
              outputField.setText("the SHRUBGUY yells to you \"HA HA, YOU FOOL! YOU FELL VICTIM TO ONE OF THE CLASSIC BLUNDERS! \nNEVER GO IN AGAINST A SHRUBGUY WHEN DEATH IS ON THE LINE!!!!\" \nat this point, you are tired of the SHRUBGUY's yelling (although you appreciate the movie quote) and you release your goat, \nit begins to chew on SHRUBGUY's leaves which causes the SHRUBGUY to run away.\n\nthe path is now clear, you can continue through the forest.");
              shrubguyExited = true;
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
          if(shrubguyEntered && !shrubguyExited){
              outputField.setText("You cannot pass, as SHRUBGUY blocks your path\n(be careful, he has a knife)");
            }else if(!shrubguyEntered){
              outputField.setText("you begin to travel to the north, when you hear someone walking behind you. \nwhen suddenly a shrub jumps in front of you and blocks the path. \nyou realize, that it is not a shrub at all! it is a guy dressed like a shrub. \n\"GREETINGS TRAVELLER!!!!\" the shrub man cries \"IT IS I! THE AMAZING SHRUB-GUY!!!! \nDO NOT EVEN THINK ABOUT GOING NORTH!!! AS I WILL STOP YOU!!! A-HAHAHAHAHAH!!!\"");
              shrubguyEntered = true;
            }else{
              outputField.setText("now that SHRUBGUY is gone, you can safely exit the forest.");
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
    if(input.length > 1){
      switch(input[1]){
        case "oldman":
          beachItemOldman();
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
          beachDirectionWest
          break;
        default:
          outputField.setText("there is no " + input[1] + " at this location.");
          break;
      }
    }else{
      outputField.setText("unknown command, try typing \"help\" for a list of commands");
    }
  }
  //beach items
  //OLD MAN//
  public void beachItemOldman(){

  }
  //beach inventory items
  //SHOVEL//
  public void beachUseShovel(){

  }
  //beach directions
  //NORTH//
  public void beachDirectionNorth(){
    switch(input[0]){
        case "go":
            outputField.setText("");
            location = "";
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
            outputField.setText("");
            location = "";
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
            outputField.setText("");
            location = "";
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

    //store the nput in a Variable
    input = inputField.getText().split(" ");

    //clear the input and output fields
    inputField.setText("");

    //check if the user wants a list of commands
    if(input[0].equals("help")){
      outputField.setText("COMMANDS:\n-examine: get a further description of an object\n-use: interact with a given object or inventory item\n-get/take: puts the given object in your inventory\n-go: sends you to the given location\n-open/close: opens or closes the given object\n-talkto: talks to the given person\n-give (target) (item): gives the specified person the specified object\n-eat: eats the specified target");
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
      }
    }

    //update the inventory display
    inventoryDisplay.setText("inventory:");
    for(int i = 0; i < inventory.size(); i++){
      inventoryDisplay.setText(inventoryDisplay.getText() + "\n" + inventory.get(i));
    }

    //update the room description
    descriptionDisplay.setText(location + " description:");
    switch(location){
      case "menu":
        descriptionAdd("type \"start\" to begin");
        break;
      case "room":
        descriptionAdd("a bed");
        descriptionAdd("a chest of drawers");
        descriptionAdd("a front door");
        descriptionAdd("a town to the north");
        //if the coin is available
        if(roomDrawerOpen && !hasDrawerCoin){
          descriptionAdd("a coin");
        }
        break;
      case "town":
        descriptionAdd("an old well");
        descriptionAdd("some barrels of fruit");
        descriptionAdd("a goat merchant");
        descriptionAdd("a store to the east");
        descriptionAdd("woods to the north");
        descriptionAdd("your home to the south");
        break;
      case "shop":
        descriptionAdd("a shopkeeper");
        if(!inventoryContains("map")){
          descriptionAdd("a map");
        }
        if(!inventoryContains("shovel")){
          descriptionAdd("a shovel");
        }
        descriptionAdd("a western path leading \nto the town");
        break;
      case "forest":
        descriptionAdd("a rock");
        if(shrubguyEntered && !shrubguyExited){
            descriptionAdd("SHRUBGUY!!!");
          }else if(!shrubguyEntered){
            descriptionAdd("a shrub");
          }
        descriptionAdd("a north path that leads \nout of the forest");
        descriptionAdd("a south path that leads to \nthe town");
        break;
      case "beach":
        descriptionAdd("an old man");
        descriptionAdd("a pirate ship to the north");
        descriptionAdd("a cave to the east");
        descriptionAdd("a forest to the west");
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