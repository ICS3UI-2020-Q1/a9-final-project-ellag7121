import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements Runnable, ActionListener{

  // Class Variables 
  JPanel mainPanel;

  JTextField inputField;
  JTextArea outputField;

  JTextArea statDisplay;
  JTextArea inventoryDisplay;
  //placeholder until my image is made
  JTextArea mapDisplay;
  


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
    outputField.setBounds(10,10,780,285);
    inputField.setBounds(10,305,385,20);
    inventoryDisplay.setBounds(10,335,187,255);
    statDisplay.setBounds(208,335,187,255);
    mapDisplay.setBounds(405,305,385,285);

    //add components to the main panel
    mainPanel.add(outputField);
    mainPanel.add(inputField);
    mainPanel.add(inventoryDisplay);
    mainPanel.add(statDisplay);
    mainPanel.add(mapDisplay);

    //add main panel to the frame
    frame.add(mainPanel);
  }

  // method called when a button is pressed
  public void actionPerformed(ActionEvent e){
    // get the command from the action
    String command = e.getActionCommand();

  }

  // Main method to start our program
  public static void main(String[] args){
    // Creates an instance of our program
    Main gui = new Main();
    // Lets the computer know to start it in the event thread
    SwingUtilities.invokeLater(gui);
  }
}
