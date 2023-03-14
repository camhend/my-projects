// Name: Dictionary
// Author: Cameron Henderson
// Created: 3/10/2023

import java.util.*;

// This program is a dictionary that uses a Binary Search Tree
// to store and manage the the identification and contact information 
// of all employees. The user can add, delete, modify, or display 
// entered employee info.
public class Dictionary {
   private EmployeeNode overallRoot;
   
   // User interface for use of this Dictionary program.
   // Creates a main menu and takes user selections for 
   // which operation to run.
   public void userInterface () {
      Scanner input = new Scanner(System.in);
      String select;
      boolean valid;
      do {
         System.out.println("Welcome to the Employee Contact List");
         System.out.println("[ALL]: View all employees");
         System.out.println("[NUM]: Display total number of records");
         System.out.println("[ID]: View employee(s) by Employee ID");
         System.out.println("[NAME]: View employee(s) by name");
         System.out.println("[ADD]: Add a new employee's information.");
         System.out.println("[MOD]: Modify an existing employee");
         System.out.println("[DEL]: Delete an existing employee. ");
         System.out.println("[EXIT]: Exit the program. ");
         System.out.print("Please type a command: ");
         valid = true;
         select = input.nextLine().toUpperCase();
         System.out.println();
         switch (select) {
            case "ALL":
               if (overallRoot == null) {
                  System.out.println("No entries to display");
                  System.out.println("Please add an entry first");
               } else {
                  lookUpAllInterface();
               }
               break;
            case "NUM":
               System.out.print("Total number of records: ");
               System.out.println(numRecords());
               break;
            case "ID":
               if (overallRoot == null) {
                  System.out.println("No entries to display");
                  System.out.println("Please add an entry first");
               } else {
                  lookUpID(enterID());
               }
               break;
            case "NAME":
               if (overallRoot == null) {
                  System.out.println("No entries to display");
                  System.out.println("Please add an entry first");
               } else {
                  lookUpNameInterface();
               }
               break;
            case "ADD":
               EmployeeInfo employeeInfo = enterEmployeeInfo();
               add(employeeInfo);
               break;
            case "MOD":
               if (overallRoot == null) {
                  System.out.println("No entries to modify");
                  System.out.println("Please add an entry first");
               } else {
                  modify(enterID());
               }
               break;
            case "DEL":
               if (overallRoot == null) {
                  System.out.println("No entries to delete");
                  System.out.println("Please add an entry first");
               } else {
                  deleteInterface(enterID());
               }               
               break;
            case "EXIT":
               break; 
            default:
               valid = false;
               System.out.println("Not a valid entry");
               break;
         } // end of switch/case
         if (!select.equals("EXIT")) {
            System.out.println("Press Enter to return to main menu");
            input.nextLine(); 
         }
      } while (!valid || !select.equals("EXIT")); // end of do/while loop
   } // end of userInterface method
   
   // Create a user interface to get an Employee ID number
   // from the user. Returns the entered integer.
   private int enterID() {
      Scanner input = new Scanner(System.in);
      boolean valid;
      int id = 0;
      do {
         System.out.print("Enter the Employee ID: ");
         valid = true;
         if (input.hasNextInt()) {
            id = input.nextInt();
         } else {
            System.out.println("Not a valid ID entry");
            valid = false;
         }
         input.nextLine();
      } while (!valid); 
      return id;
   }
   
   // Create a user interface for adding a 
   // new employee to the Dictionary.
   // Prompts the user to enter a new EmployeeInfo, 
   // then returns the created EmployeeInfo object. 
   public EmployeeInfo enterEmployeeInfo() {
      Scanner input = new Scanner(System.in);
      System.out.println("Enter the new Employee's information: ");
      int ID = enterID();
      System.out.print("First Name: ");
      String firstName = input.nextLine();
      System.out.print("Last Name: ");
      String lastName = input.nextLine();
      System.out.print("Street Address: ");
      String streetAddress = input.nextLine();
      System.out.print("City: ");
      String city = input.nextLine();
      System.out.print("State: ");
      String state = input.nextLine();
      System.out.print("Zip Code: ");
      String zip = input.nextLine();
      System.out.print("Email: ");
      String email= input.nextLine();
      System.out.print("Phone number: ");
      String phone = input.nextLine();
      System.out.println();
      
      EmployeeInfo employeeInfo = new EmployeeInfo(ID,
                                                   firstName,
                                                   lastName,
                                                   streetAddress,
                                                   city,
                                                   state,
                                                   zip,
                                                   email,
                                                   phone);
                  
      return employeeInfo;
   }
   
   // Takes an EmployeeInfo object and adds it to this 
   // Dictionary's Binary Search Tree. 
   public void add(EmployeeInfo employeeInfo) {
      overallRoot = add(overallRoot, employeeInfo);
   }
   
   private EmployeeNode add(EmployeeNode root, EmployeeInfo employeeInfo) {
      if (root == null) {
         root = new EmployeeNode(employeeInfo, null, null);
         System.out.println("The following Employee was added: ");
         System.out.println(employeeInfo);
         System.out.println();
      } else if (root.employeeInfo.ID > employeeInfo.ID) {
         root.left = add(root.left, employeeInfo);
      } else if (root.employeeInfo.ID < employeeInfo.ID) {
         root.right = add(root.right, employeeInfo);
      } else {
         System.out.println("That employee ID already exists.");
         System.out.println("New employee NOT added.");
      }
      return root;
   }  
   
   // Find the minimum value in a valid Binary Search Tree.
   public EmployeeInfo getMin() {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         return getMin(overallRoot);
      }
   }
   
   private EmployeeInfo getMin(EmployeeNode root) {
      if (root.left == null) {
         return root.employeeInfo;
      }
      else {
         return getMin(root.left);
      }
   } 
   
   // Takes an integer representing the an Employee's 
   // Employee ID number. Search for the EmployeeNode
   // with that ID number and delete it from the 
   // Binary Search Tree.
   // If the Binary Search Tree is empty, then
   // throws a NoSuchElementException.
   public void delete(int ID) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {
         overallRoot = delete(overallRoot, ID);
      }
   } 
   
   private EmployeeNode delete(EmployeeNode root, int ID) {
      if (root == null) {
         System.out.println("Employee ID not found");
         return null;
      } else if (root.employeeInfo.ID > ID) {
         root.left = delete(root.left, ID);
      } else if (root.employeeInfo.ID < ID) {
         root.right = delete(root.right, ID);
      } else { // root.employeeInfo.ID == ID
         if (root.right == null) {
            root = root.left;
         } else if (root.left == null) {
            root = root.right;
         } else {
            root.employeeInfo = getMin(root.right);
            root.right = delete(root.right, root.employeeInfo.ID);
         } 
      }
      return root;
   }
   
   // Create a user interface for use with the delete method.
   // Takes a integer representing an Employee ID of the Employee
   // to be deleted from the Binary Search Tree. 
   // Prompts for confirmation before deleting.
   public void deleteInterface(int ID) {
      Scanner input = new Scanner(System.in);
      System.out.println("The following Employee Info will be deleted: ");
      lookUpID(ID);
      System.out.print("Confirm delete? (y/n): ");
      boolean validEntry;
      String entry;
      do {
         entry = input.nextLine().toLowerCase();
         validEntry = true;
         if (entry.equals("y")) {
            delete(ID);
            System.out.println("Employee Info deleted.");
         } else if (entry.equals("n")) {
            System.out.println("Delete operation cancelled.");
         } else {
            System.out.println("Invalid entry.");
            validEntry = false;
         }
      } while (!validEntry);
   }
   
   // Takes an integer representing an Employee ID number
   // and modifies the matching Employee's information.
   // The user is prompted to enter the new information.
   // If the Binary Search Tree is empty, then
   // throws a NoSuchElementException.
   public void modify(int ID) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {
         overallRoot = modify(overallRoot, ID);
      }
   } 
   
   private EmployeeNode modify(EmployeeNode root, 
                               int ID) {
      if (root == null) {
         System.out.println("Employee ID not found");
         return null;
      } else if (root.employeeInfo.ID > ID) {
         root.left = modify(root.left, ID);
      } else if (root.employeeInfo.ID < ID) {
         root.right = modify(root.right, ID);
      } else { // root.employeeInfo.ID == ID
         root.employeeInfo = userModifyInterface(root.employeeInfo);
      }
      return root; 
   }
   
   // Called by modify method to prompt a user to edit 
   // an EmployeeInfo object with new information.
   private EmployeeInfo userModifyInterface(EmployeeInfo employeeInfo) {
      Scanner input = new Scanner(System.in);
      int select = 0;
      // do/while loop allows user to change multiple of 
      // the EmployeeInfo until they enter "9" to exit
      do {
         System.out.println();   
         System.out.println(employeeInfo);
         System.out.println();   
         System.out.println("[1]: First Name");
         System.out.println("[2]: Last Name");
         System.out.println("[3]: Street Address");
         System.out.println("[4]: City");
         System.out.println("[5]: State");
         System.out.println("[6]: Zip");
         System.out.println("[7]: Email");
         System.out.println("[8]: Phone");
         System.out.println("[9]: EXIT MODIFY");
         System.out.println();
         select = 0;  
         boolean valid;
         do { // ensure the user enters a valid selection 1 - 9
            System.out.print(
               "Enter a number to select which detail to modify: ");
            valid = true;
            if (input.hasNextInt()) {
               select = input.nextInt();
               if (select < 1 || select > 9) {
                  System.out.println("Not a valid selection");
                  valid = false;
               }
            } else {
               System.out.println("Not a valid selection");
               valid = false;
            }
            input.nextLine();
         } while (!valid); // end of do/while loop
         
         // get user input for a String,
         // then update the EmployeeInfo field that 
         // the user selected with that String.
         String update = "";
         if (select != 9) {
            System.out.print("Enter the updated information: ");
            update += input.nextLine();
         }
         switch (select) {
            case 1:
               System.out.print("First Name: ");
               if (confirmModify(employeeInfo.firstName, update)) {
                  employeeInfo.firstName = update;
               }
               break;
            case 2:
               System.out.print("Last Name: ");
               if (confirmModify(employeeInfo.lastName, update)) {
                  employeeInfo.lastName = update;
               }
               break;
            case 3:
               System.out.print("Street Address: ");
               if (confirmModify(employeeInfo.streetAddress, update)) {
                  employeeInfo.streetAddress = update;
               }
               break;
            case 4:
               System.out.print("City: ");
               if (confirmModify(employeeInfo.city, update)) {
                  employeeInfo.city = update;
               }
               break;
            case 5:
               System.out.print("State: ");
               if (confirmModify(employeeInfo.state, update)) {
                  employeeInfo.state = update;
               }
               break;
            case 6:
               System.out.print("Zip Code: ");
               if (confirmModify(employeeInfo.zip, update)) {
                  employeeInfo.zip = update;
               }
               break;
            case 7:
               System.out.print("Email: ");
               if (confirmModify(employeeInfo.email, update)) {
                  employeeInfo.email = update;
               }
               break;
            case 8:
               System.out.print("Phone: ");
               if (confirmModify(employeeInfo.phone, update)) {
                  employeeInfo.phone = update;
               }
               break;
            case 9: 
               break;
            default:
               System.out.println("Not a valid selection");
               break;
            }    
      } while (select != 9); // end of primary do/while loop
      return employeeInfo;
   } // end of userModifyInterface method
   
   // Prompt user to confirm a modification by displaying 
   // the String before, and the String after modification.
   // Returns the users choice as a boolean.
   private boolean confirmModify(String before, String after) {
      Scanner input = new Scanner(System.in);
      boolean choice = false;
      boolean validEntry;
      String entry;
      System.out.println(before + " -> " + after);
      do {
         System.out.print("Confirm changes? (y/n): ");
         entry = input.nextLine().toLowerCase();
         validEntry = true;
         if (entry.equals("y")) {
            choice = true;
         } else if (entry.equals("n")) {
            System.out.println("Changes not made.");
            choice = false;
         } else {
            System.out.println("Invalid entry.");
            validEntry = false;
         }
      } while (!validEntry);
      return choice;
   } // end of confirmModify method
   
   // Takes a integer representing an Employee's
   // Employee ID number and displays the matching
   // Employee from the Binary Search Tree as a String.
   public void lookUpID(int ID) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {
         lookUpID(overallRoot, ID);
      }
   } 
   
   private void lookUpID(EmployeeNode root, int ID) {
      if (root == null) {
         System.out.println("Employee ID not found");
      } else if (root.employeeInfo.ID > ID) {
         lookUpID(root.left, ID);
      } else if (root.employeeInfo.ID < ID) {
         lookUpID(root.right, ID);
      } else { // root.employeeInfo.ID == ID
         System.out.println(root.employeeInfo);
         System.out.println();
      }
   }
   
   // Returns the number of Employees entered
   // into this Dictionary's Binary Search Tree.
   public int numRecords() {
      int total = 0;
      total = numRecords(overallRoot, total);
      return total;
   }  
   
   private int numRecords(EmployeeNode root, int total) {
      if (root != null) {
         total++;
         total = numRecords(root.left, total);
         total = numRecords(root.right, total);
      } 
      return total;
   }
   
   // Create a user interface for use with the lookUpAll methods.
   // Prints instructions and runs one of the lookUpAll methods 
   // (pre-order, in-order, or post-order) depending on user selection.
   private void lookUpAllInterface() {
      System.out.println("Select the traversal method:");
      System.out.println("[PRE]: pre-order");
      System.out.println("[IN]: in-order");
      System.out.println("[POST]: post-order");
      System.out.println();
      System.out.print("Please type a command: ");
      
      Scanner input = new Scanner(System.in);
      String select;
      boolean valid;
      do {
         select = input.nextLine().toUpperCase();
         valid = true;
         if (select.equals("PRE")) {
            lookUpAllPreOrder();
         } else if (select.equals("IN")) {
            lookUpAllInOrder();
         } else if (select.equals("POST")){
            lookUpAllPostOrder();
         } else {
            valid = false;
            System.out.println("Not a valid entry");
         }
      } while (!valid);
   }
   
   // Print all employees in the Binary Search Tree (BST).
   // Performs pre-order traversal of the BST.
   public void lookUpAllPreOrder() {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         lookUpAllPreOrder(overallRoot);
      } 
   }  
   
   private void lookUpAllPreOrder(EmployeeNode root) {
      if (root != null) {
         System.out.println(root.employeeInfo.toString());
         System.out.println();
         lookUpAllPreOrder(root.left);
         lookUpAllPreOrder(root.right);
      } 
   }
   
   // Print all employees in the Binary Search Tree (BST).
   // Performs in-order traversal of the BST.
   public void lookUpAllInOrder() {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         lookUpAllInOrder(overallRoot);
      } 
   }  
   
   private void lookUpAllInOrder(EmployeeNode root) {
      if (root != null) {
         lookUpAllInOrder(root.left);
         System.out.println(root.employeeInfo.toString());
         System.out.println();
         lookUpAllInOrder(root.right);
      } 
   }
   
   // Print all employees in the Binary Search Tree (BST).
   // Performs post-order traversal of the BST.
   public void lookUpAllPostOrder() {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         lookUpAllPostOrder(overallRoot);
      } 
   }  
   
   private void lookUpAllPostOrder(EmployeeNode root) {
      if (root != null) {
         lookUpAllPostOrder(root.left);
         lookUpAllPostOrder(root.right);
         System.out.println(root.employeeInfo.toString());
         System.out.println();
      } 
   }
   
   // Create a user interface for use with the lookUpName methods.
   // Prints instructions and runs one of the lookUpName methods 
   // (pre-order, in-order, or post-order) depending on user selection.
   private void lookUpNameInterface() {
      Scanner input = new Scanner(System.in);
      System.out.print("Enter the name to look up: ");
      String name = input.nextLine();
      
      System.out.println("Select the traversal method:");
      System.out.println("[PRE]: pre-order");
      System.out.println("[IN]: in-order");
      System.out.println("[POST]: post-order");
      System.out.println();
      System.out.print("Please type a command: ");
      
      String select;
      boolean valid;
      do {
         select = input.nextLine().toUpperCase();
         valid = true;
         if (select.equals("PRE")) {
            lookUpNamePreOrder(name);
         } else if (select.equals("IN")) {
            lookUpNameInOrder(name);
         } else if (select.equals("POST")){
            lookUpNamePostOrder(name);
         } else {
            valid = false;
            System.out.println("Not a valid entry");
         }
      } while (!valid);
   } // end of lookUpNameInterface
   
   // Prints any employees that have a name that contains 
   // the given String. Oeration is done by pre-order traversal 
   // of the BST.
   public void lookUpNamePreOrder(String name) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         name = name.toLowerCase();
         boolean match = false;
         if (!lookUpNamePreOrder(overallRoot, name, match)) {
            System.out.println("No matching entries found");
         }
      } 
   }  
   
   private boolean lookUpNamePreOrder(EmployeeNode root, 
                                 String name, boolean match) {
      if (root != null) {
         if (root.employeeInfo.getName().toLowerCase().contains(name)) {
               System.out.println(root.employeeInfo.toString());
               System.out.println();
               match = true;
         }
         match = lookUpNamePreOrder(root.left, name, match);
         match = lookUpNamePreOrder(root.right, name, match);
      } 
      return match;
   } 
   
   // Prints any employees that have a name that contains 
   // the given String. Oeration is done by in-order traversal 
   // of the BST.
   public void lookUpNameInOrder(String name) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {
         name = name.toLowerCase();
         boolean match = false;  
         if (!lookUpNameInOrder(overallRoot, name, match)) {
            System.out.println("No matching entries found");
         }
      } 
   }  
   
   private boolean lookUpNameInOrder(EmployeeNode root, 
                                     String name, boolean match) {
      if (root != null) {
         match = lookUpNameInOrder(root.left, name, match);
         if (root.employeeInfo.getName().toLowerCase().contains(name)) {
            System.out.println(root.employeeInfo.toString());
            System.out.println();
            match = true;
         }
         match = lookUpNameInOrder(root.right, name, match);
      } 
      return match;
   }
   
   // Prints any employees that have a name that contains 
   // the given String. Oeration is done by post-order traversal 
   // of the BST.
   public void lookUpNamePostOrder(String name) {
      if (overallRoot == null) {
         throw new NoSuchElementException();
      } else {  
         name = name.toLowerCase();
         boolean match = false;
         if (!lookUpNamePostOrder(overallRoot, name, match)) {
            System.out.println("No matching entries found");
         }
      } 
   }  
   
   private boolean lookUpNamePostOrder(EmployeeNode root, 
                                    String name, boolean match) {
      if (root != null) {
         match = lookUpNamePostOrder(root.left, name, match);
         match = lookUpNamePostOrder(root.right, name, match);
         if (root.employeeInfo.getName().toLowerCase().contains(name)) {
            System.out.println(root.employeeInfo.toString());
            System.out.println();
            match = true;
         }
      } 
      return match;
   }                
                            
} // end of Dictionary class