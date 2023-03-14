// Name: Phonebook Manager
// Author: Cameron Henderson
// Created: 2/10/2023

import java.util.*;

// This class is a user interface for usage of the PhonebookManager object.
// The user is provided instructions on how to use the PhonebookManager.
// If the user provides incorrect inputs, the user is notified and reprompted.

public class Interface {
   private static PhonebookManager phonebook = new PhonebookManager();
   
   public static void main(String[] args) {   
      System.out.println("Welcome to the Phonebook Manager");
      
      boolean validEntry = true;
      Scanner input = new Scanner(System.in);
      String choice;
      do {
         // Phonebook main menu
         if ( validEntry ) {
            System.out.println();
            System.out.println("ADD - Add an entry.");
            System.out.println("INSERT - Insert an entry at a selected index");
            System.out.println("EDIT - Edit an entry.");
            System.out.println("DEL - Delete an entry.");
            System.out.println("VIEW - View an entry.");
            System.out.println("ALL - View the entire phonebook");
            System.out.println("QUIT - Exit Phonebook Manager");
            System.out.println();
         }
         validEntry = true;
         
         System.out.print("Please enter a command: ");
         choice = input.nextLine().toLowerCase();
         switch (choice) {
            case ("add"):
               System.out.print("Enter the first name: ");
               String first = input.nextLine();

               System.out.print("Enter the last name: ");
               String last = input.nextLine();
               
               System.out.print("Enter the street address: ");
               String address = input.nextLine();
               
               System.out.print("Enter the city: ");
               String city = input.nextLine();
               
               System.out.print("Enter the phone number: ");
               String phone = input.nextLine();
               
               phonebook.add(first, last, address, city, phone);
               break;
            case ("insert"):
               int index = -1;
               // Prompt the user to select an index only if the Phonebook
               // contains contacts. Otherwise, just add the new contact
               if (phonebook.size() > 0) {
                  System.out.println();
                  System.out.println("___ALL CONTACTS___");
                  System.out.println(phonebook);
                  System.out.println();
                  System.out.println("Enter an index to insert the contact.");
                  System.out.println("New contact will be instered above.");
                  System.out.println("Subsequent contacts will be shifted down.");
                  System.out.println();
                  
                  // Ensure that user enters an integer that correctly
                  // corresponds to an index in the allInventories list
                  String range = "1 - " + (phonebook.size() + 1);
                  boolean accept;
                  do {
                     accept = false;
                     index = -1;
                     System.out.print("Please enter a number ");
                     System.out.print("(Range: " + range + "): ");
                     if ( input.hasNextInt() ) {
                        index = input.nextInt() - 1;
                        if (index >= 0 && index <= phonebook.size() ) {
                           accept = true;
                        } else {
                           System.out.println("INVALID: Index is out of range.");
                           input.nextLine();
                        }
                     } else {
                        System.out.println("INVALID: Entry must be an integer");
                        input.nextLine();
                     }
                  } while ( !accept );
                  input.nextLine();
               }
               
               System.out.print("Enter the first name: ");
               first = input.nextLine();
               
               System.out.print("Enter the last name: ");
               last = input.nextLine();
               
               System.out.print("Enter the street address: ");
               address = input.nextLine();
               
               System.out.print("Enter the city: ");
               city = input.nextLine();
               
               System.out.print("Enter the phone number: ");
               phone = input.nextLine();
               
               if ( phonebook.size() == 0 ) {
                  phonebook.add(first, last, address, city, phone);
               } else if ( index == phonebook.size() ) {
                  phonebook.add(first, last, address, city, phone);
               } else {
                  phonebook.add(index, first, last, address, city, phone);
               }
               break;
            case ("edit"):
               edit();                   
               break;
            case ("del"):
               delete();
               break;   
            case ("view"):
               view();
               break;   
            case ("all"):
               System.out.println();
               System.out.println("___ALL CONTACTS___");
               System.out.println(phonebook);
               break;
            case ("quit"):
               break;
            default:
               validEntry = false;
               System.out.println("INVALID COMMAND");
               break;
         }      
      } while ( !choice.equals("quit") );
   } // end of main method
   
   // Allow the user to select a contact and edit its contact information.
   // If multiple contacts match the name entered, then prompt the user 
   // to select one. 
   public static void edit() {
      System.out.println("Enter the name of the contact to edit.");
      
      // get all ListNodes indeces that match the entered name
      ArrayList<Integer> indexList = getNameIndeces(); 
      // allow user to pick which contact if there are multiple
      // ListNodes with the entered name
      int index = pickIndex(indexList);
      
      boolean validEntry = true; 
      String choice;
      Scanner input = new Scanner(System.in);
      if ( index != -1 ) {
         do {
            // Prevent reprinting this prompt if this loop repeats
            // because the user has entered an invalid value
            if ( validEntry ) { 
               System.out.println();
               String nodeInfo = phonebook.nodeString(index);
               System.out.println(nodeInfo);
               System.out.println("----------------");
               System.out.println("Which detail would you like to edit?");
               System.out.println("FIRST - edit the first name");
               System.out.println("LAST - edit the last name");
               System.out.println("ADDRESS - edit the street address");
               System.out.println("CITY - edit the city");
               System.out.println("PHONE - edit the phone number");
               System.out.println("BACK - return to previous menu");
               System.out.println();
            }
            validEntry = true;
            
            System.out.print("Please enter a command: ");
            choice = input.nextLine().toLowerCase();
            String entry;
            switch (choice) {
               case ("first"): 
                  System.out.print("New first name: ");
                  entry = input.nextLine();
                  phonebook.setFirst(index, entry);
                  break;
               case ("last"): 
                  System.out.print("New last name: ");
                  entry = input.nextLine();
                  phonebook.setLast(index, entry);
                  break;
               case ("address"): 
                  System.out.print("New address: ");
                  entry = input.nextLine();
                  phonebook.setAddress(index, entry);
                  break;
               case ("city"): 
                  System.out.print("New city: ");
                  entry = input.nextLine();
                  phonebook.setCity(index, entry);
                  break; 
               case ("phone"): 
                  System.out.print("New phone number: ");
                  entry = input.nextLine();
                  phonebook.setPhone(index, entry);
                  break; 
               case ("back"):
                  break;  
               default:
                  validEntry = false;
                  System.out.println("INVALID COMMAND");
                  break;
            } // end of switch/case
         } while ( !choice.equals("back") );
      } 
   } // end of edit method
   
   // Allow the user to select a contact and view its contact information.
   // If multiple contacts match the name entered, then view all matches.
   public static void view() {
      Scanner input = new Scanner(System.in);
      String choice;
      do {
         System.out.println("Enter the name of the contact ");
         System.out.println("you would like to view: ");
         // get all ListNodes indices that match the entered name
         ArrayList<Integer> indexList = getNameIndeces(); 
         // print all ListNodes found that match the entered name
         System.out.println();
         for (int i = 0; i < indexList.size(); i++) {
            int nextIndex = indexList.get(i);
            System.out.println(phonebook.nodeString(nextIndex));
         }
         if (indexList.size() > 1) {
            System.out.println();
            System.out.println("MULTIPLE MATCHING ENTRIES");
         }
         do {
            System.out.print("View another entry? (y/n) ");
            choice = input.nextLine();
            if ( !choice.substring(0,1).toLowerCase().equals("y") 
                 && !choice.substring(0,1).toLowerCase().equals("n")) 
            {
               System.out.println("INVALID ENTRY");
            }
         } while ( !choice.substring(0,1).toLowerCase().equals("y") 
                 && !choice.substring(0,1).toLowerCase().equals("n") ); 
         
      } while ( choice.substring(0,1).toLowerCase().equals("y") );
   } // end of view method
   
   // Allow the user to select a contact and give the option to delete it.
   // If multiple contacts match the name entered, then view all matches
   // and prompt the user to select which one to delete.
   public static void delete() {
      Scanner input = new Scanner(System.in);
      String choice;
      do {
         System.out.println();
         System.out.println("Enter the name of the contact ");
         System.out.println("you would like to delete: ");
         // get all ListNodes indices that match the entered name
         ArrayList<Integer> indexList = getNameIndeces(); 
         int index;
         
         // show matching ListNodes and prompt the user to delete
         if ( !indexList.isEmpty() ) {
            // if multiple matches, then prompt the user to select one
            index = pickIndex(indexList); 
            do {
               System.out.println();
               System.out.println(phonebook.nodeString(index));
               System.out.println("----------------");
               System.out.print("Delete this entry? (y/n) ");
               choice = input.nextLine().toLowerCase();
               if ( !choice.substring(0,1).equals("y") 
                    && !choice.substring(0,1).equals("n")) 
               {
                  System.out.println("INVALID ENTRY");
               }
               if ( choice.substring(0,1).equals("y") ) {
                  phonebook.remove(index);
                  System.out.println("ENTRY DELETED");
               }
            } while ( !choice.substring(0,1).equals("y") 
                    && !choice.substring(0,1).equals("n") );
         }                
         do {
            System.out.print("Delete another entry? (y/n) ");
            choice = input.nextLine().toLowerCase();
            if ( !choice.substring(0,1).equals("y") 
                 && !choice.substring(0,1).equals("n")) 
            {
               System.out.println("INVALID ENTRY");
            }
         } while ( !choice.substring(0,1).equals("y") 
                 && !choice.substring(0,1).equals("n") ); 
         
      } while ( choice.substring(0,1).equals("y") );
   } // end of delete method
   
   
   // Prompt user for first and last name to look up, then return a list of
   // PhonebookManager indices that match that match that name. 
   // If no matches are found, then return empty ArrayList
   public static ArrayList<Integer> getNameIndeces () {
      
      Scanner input = new Scanner(System.in);
      ArrayList<Integer> indexList = new ArrayList<Integer>();

      // User attempts to enter a name to look up. If no matching entries,
      // then loop if user enters "y", and then exit only if user enters "n".
      String choice;
      boolean retry;
      do {
         retry = false;
         System.out.print("First name: ");
         String inputFirst = input.nextLine();
         System.out.print("Last name: ");
         String inputLast = input.nextLine();
         indexList = phonebook.getNameIndex(inputFirst, inputLast);
         
         // prompt user to retry if NO phonebook entries match
         if ( indexList.isEmpty() ) {
            System.out.println();
            System.out.println("NO MATCHING ENTRIES");
            do {
               System.out.print("Retry? (y/n) ");
               choice = input.nextLine().toLowerCase();
               if ( !choice.substring(0,1).equals("y") 
                     && !choice.substring(0,1).equals("n") ) 
               {
                  System.out.println("INVALID ENTRY");
               } else if (choice.substring(0,1).equals("y")) {
                  retry = true;
               }
            } while ( !choice.substring(0,1).equals("y") 
                 && !choice.substring(0,1).equals("n") );
            
         } 
      } while ( retry );
      return indexList;
   } // end of getNameIndeces
      
   // Takes a list of indices, and displays the ListNodes at those indices. 
   // If multiple indices are passed, the user is prompted to select between
   // the ListNodes shown, and the chosen ListNode's index is returned.
   public static int pickIndex(ArrayList<Integer> indexList) {
      int index = -1; 
      Scanner input = new Scanner(System.in); 
      if ( indexList.size() > 1 ) {
         System.out.println();
         
         // display all ListNodes at the given indices
         for (int i = 0; i < indexList.size(); i++) {
            System.out.print(i + 1 + ": ");
            System.out.println(phonebook.nodeString(indexList.get(i)));
         }
         System.out.println("----------------");
         System.out.println("MULTIPLE MATCHING ENTRIES FOUND");
         System.out.println("To select an entry, ");
         System.out.print("enter the corresponding number:  ");
         
         // require a valid integer within range
         boolean accept;
         do {
            accept = false;
            int num = -1;
            if ( input.hasNextInt() ) {
               num = input.nextInt() - 1;
               if (num >= 0 && num < indexList.size() ) {
                  accept = true;
                  index = indexList.get(num);
               } else {
                  System.out.println("INVALID: Out of bounds.");
                  System.out.println("To select an entry, ");
                  System.out.println("enter the corresponding number: ");
                  input.nextLine();
               }
            } else {
               System.out.println("INVALID: Must enter an integer.");
               System.out.println("To select an entry, ");
               System.out.println("enter the corresponding number: ");
               input.nextLine();
            }
         } while ( !accept );
      // if only one index is passed, then return that index
      } else if (indexList.size() == 1) {
         index = indexList.get(0);
      }
      return index;
   }
}
