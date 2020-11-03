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
  String location = "room"; //the player's current location
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
    inventoryDisplay = new JTextArea();
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
  }

  //location methods:
  // --- ROOM --- //
  public void locationRoom(){
    switch(input[1]){
      case "drawer":
        roomItemDrawer();
        break;
      case "door":
        //roomItemDoor();
        break;
      case "bed":
        //roomItemBed();
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
          outputField.setText("it is a chest of drawers in your room. \nit is currently open.");
        }else{
          outputField.setText("it is a chest of drawers in your room. \nit is currently closed.");
        }
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

    //clear the input field
    inputField.setText("");

    //go to the method based on the player's location
    switch(location){
      case "room":
        locationRoom();
        break;
      case "town":
        locationTown();
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
