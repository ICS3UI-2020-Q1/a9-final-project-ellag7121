import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main implements Runnable, ActionListener{

  // Class Variables 
  JPanel mainPanel;

  JTextField inputField;
  JTextArea outputField;

  JTextArea statDisplay;
  JTextArea inventoryDisplay;
  //placeholder until my image is made
  JTextArea mapDisplay;

  //initialize the main Variables
  String[] input; //the input split up into it's seperate words (0 is the action, 1 is the target)
  String location = "menu"; //the player's current location
  ArrayList<String> inventory; //the array list to hold the inventory items


  //location specific Variables
  //room
  boolean roomDoorOpen = false;
  boolean roomDrawerOpen = false;

  //town

  


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
    outputField = new JTextArea();

    statDisplay = new JTextArea();
    inventoryDisplay = new JTextArea("inventory:");
    mapDisplay = new JTextArea();

    //set up component locations
    inputField.setBounds(10,305,385,20);
    outputField.setBounds(10,10,780,285);
    inventoryDisplay.setBounds(10,335,187,245);
    statDisplay.setBounds(208,335,187,245);
    mapDisplay.setBounds(405,305,385,275);

    //disable the text areas
    outputField.setEnabled(false);
    inventoryDisplay.setEnabled(false);
    statDisplay.setEnabled(false);
    mapDisplay.setEnabled(false);

    //create an action listener on the input field
    inputField.addActionListener(this);

    //add components to the main panel
    mainPanel.add(inputField);
    mainPanel.add(outputField);
    mainPanel.add(inventoryDisplay);
    mainPanel.add(statDisplay);
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

  //location methods:
  // --- MENU --- //
  public void locationMenu(){
    switch(input[1]){
      case "start":
        location = "room";
        break;
    }
  }
  // --- ROOM --- //
  public void locationRoom(){
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
        System.out.println("hi");
        if(!inventoryContains("coin") && roomDrawerOpen){
          roomItemCoin();
        }else{
          outputField.setText("you cannot do this");
        }
        break;
      case "north":
        roomItemNorth();
        break;
      default:
        outputField.setText("there is no " + input[1] + " at this location.");
        break;
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
          if(inventoryContains("coin")){
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
          outputField.setText("it is the front door to your humble home. \nit is currently open, you see a path that leads to the town.");
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
        break;
      case "get":
        outputField.setText("You take the gold coin from the open drawer");
        inventory.add("coin");
        break;
      case "examine":
        outputField.setText("It is a gold coin that can be used to buy things at shops. \nit is of small value.");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }
  //NORTH//
  public void roomItemNorth(){
    switch(input[0]){
      case "go":
          outputField.setText("You take the gold coin from the open drawer");
          inventory.add("coin");
        break;
      case "examine":
        outputField.setText("It is a gold coin that can be used to buy things at shops. \nit is of small value.");
        break;
      default:
        outputField.setText("You cannot " + input[0] + " the " + input[1]);
        break;
    }
  }

  // --- TOWN --- //
  public void locationTown(){

  }

  // method called when a button is pressed
  public void actionPerformed(ActionEvent e){
    // get the command from the action
    String command = e.getActionCommand();

    //store the nput in a Variable
    input = inputField.getText().split(" ");

    //clear the input and output fields
    inputField.setText("");
    outputField.setText("");

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
    }

    //update the inventory display
    inventoryDisplay.setText("inventory:");
    for(int i = 0; i < inventory.size(); i++){
      inventoryDisplay.setText(inventoryDisplay.getText() + "\n" + inventory.get(i));
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
