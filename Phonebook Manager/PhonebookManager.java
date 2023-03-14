// Name: Phonebook Manager
// Author: Cameron Henderson
// Created: 2/10/2023

import java.util.*;

// This class defines an object called PhonebookManager that allows 
// for storage and modification of contact information. 
public class PhonebookManager {
   private ListNode front;
   
   public PhonebookManager() {
      front = null;
   }
   
   // Create a contact in the as a new node at the end of the 
   // PhoneBook LinkedList. This method requires values for 
   // First Name, Last Name, address, city, and phone number
   // to create the new contact.
   public void add(String first, String last, 
                   String address, String city, String phone) {
      phone = phoneReformat(phone);
      if (front == null) {
         front = new ListNode(first, last, address, city, phone);
      } else {
         ListNode current = front;
         while (current.next != null) {
            current = current.next;
         }
         current.next = new ListNode(first, last, address, city, phone);
      }
   }
   
   // Create a contact in the as a new node at a given 
   // index in the PhoneBook LinkedList. This method requires 
   // values for First Name, Last Name, address, city, and phone number
   // to create the new contact.
   public void add(int index, String first, String last, 
                   String address, String city, String phone) {
      phone = phoneReformat(phone);
      if (index == 0) {
         front = new ListNode(first, last, address, city, phone, front);
      } else {
         ListNode current = front;
         for (int i = 0; i < index - 1; i++) {
            current = current.next;
         }
         current.next = new ListNode(first, last, address, 
                                     city, phone, current.next);
      }
   }
   
   // Remove the first ListNode. 
   // If the LinkedList is empty, then throw a NoSuchElement Exception.
   public void remove() {
      if (front == null) {
         throw new NoSuchElementException();
      } else {
         front = front.next;
      }
   }
   
   // Remove a ListNode at the given index
   // If the LinkedList is empty, then throw a NoSuchElement Exception.
   public void remove(int index) {
      if (front == null) {
         throw new NoSuchElementException();
      } else {
         if (index == 0) {
         front = front.next;
         } else {
            ListNode current = front;
            for (int i = 0; i < index - 1; i++) {
               current = current.next;
            }
            current.next = current.next.next;
         }
      }
   }
    
   // Return the number of ListNodes in the LinkedList
   public int size() {
      int count = 0;
      if (front == null) {
         return 0;
      } else {
         count++;
         ListNode current = front;
         while (current.next != null) {
            current = current.next;
            count++;
         }
      } 
      return count;
   }
   
   // Return an ArrayList of ListNode indeces that have the
   // given String in their "first" field. 
   // If no ListNodes match, then return an empty ArrayList.
   public ArrayList<Integer> getFirstIndex(String first) {
      ArrayList<Integer> indexList = new ArrayList<Integer>();
      int index = 0;
      if (front != null) {
         ListNode current = front;
         while (current != null) {
            if (current.first.equals(first)) {
               indexList.add(index);
            }
            index++;
            current = current.next;
         }
      }
      return indexList;
   }
   
   // Return an ArrayList of ListNode indeces that have the
   // given String in their "last" field. 
   // If no ListNodes match, then return an empty ArrayList.
   public ArrayList<Integer> getLastIndex(String last) {
      ArrayList<Integer> indexList = new ArrayList<Integer>();
      int index = 0;
      if (front != null) {
         ListNode current = front;
         while (current != null) {
            if (current.last.equals(last)) {
               indexList.add(index);
            }
            index++;
            current = current.next;
         }
      }
      return indexList;
   }
   
   // Return an ArrayList of ListNode indeces that have the
   // given String in their "first AND "last" fields. 
   // If no ListNodes match, then return an empty ArrayList.
   public ArrayList<Integer> getNameIndex(String first, String last) {
      ArrayList<Integer> indexList = new ArrayList<Integer>();
      int index = 0;
      if (front != null) {
         ListNode current = front;
         while (current != null) {
            if (current.first.equalsIgnoreCase(first) 
                && current.last.equalsIgnoreCase(last)) 
            {
               indexList.add(index);
            }
            index++;
            current = current.next;
         }
      }
      return indexList;
   }
   
   // Change the value of the "first" field of the ListNode
   // at the given index.
   public void setFirst(int index, String first) {
      ListNode current = front;
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      current.first = first;
   }
   
   // Change the value of the "last" field of the ListNode
   // at the given index.
   public void setLast(int index, String last) {
      ListNode current = front;
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      current.last = last;
   }
   
   // Change the value of the "address" field of the ListNode
   // at the given index.
   public void setAddress(int index, String address) {
      ListNode current = front;
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      current.address = address;
   }
   
   // Change the value of the "city" field of the ListNode
   // at the given index.
   public void setCity(int index, String city) {
      ListNode current = front;
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      current.city = city;
   }
   
   // Change the value of the "phone" field of the ListNode
   // at the given index.
   public void setPhone(int index, String phone) {
      phone = phoneReformat(phone);
      ListNode current = front;
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      current.phone = phone;
   }
   
   // Return a modified String that removes
   // any common symbols used in phone number formatting.
   private String phoneReformat(String phone) {
      phone = phone.replace("(", "");
      phone = phone.replace(")", "");
      phone = phone.replace("-", "");
      phone = phone.replace(" ", "");
      return phone;
   }
      
   // Return a String representation of the ListNode at given index
   public String nodeString (int index) {
      ListNode current = front;
      String nodeString = "";
      for (int i = 0; i < index; i++) {
         current = current.next;
      }
      nodeString += "Name: " + current.first + " " + current.last + "\n";
      nodeString += "\tAddress: " + current.address + ", " + current.city + "\n";
      nodeString += "\tPhone: " + current.phone;
      return nodeString;
   }
   
   // Return a String representation of the all ListNodes of PhoneBook LinkedList
   public String toString() {
      String listString = "";
      int count = 1;
      if (front != null) {
         ListNode current = front;
         while (current != null) {
            listString += count + "- Name: " + 
               current.first + " " + current.last + "\n";
               
            listString += "\tAddress: " + 
               current.address + ", " + current.city + "\n";
               
            listString += "\tPhone: " + current.phone;
            listString += "\n";
            current = current.next;
            count++;
         }
      } else {
         listString = "No entries found.";
      }
      return listString;
   }
   
}