// Name: Phonebook Manager
// Author: Cameron Henderson
// Created: 2/10/2023

// This class defines a ListNode for use in the 
// Phonebook Manager LinkedList. A single ListNode 
// instance stores the information for a single contact.
public class ListNode {
   String first;     // first name
   String last;      // last name
   String address;   // street address
   String city;      // city of residence
   String phone;     // contact phone number
   ListNode next;
   
   // Construct a new ListNode if reference 
   // to the next ListNode is NOT passed.
   public ListNode (String first, String last, 
                    String address, String city, 
                    String phone) 
   {
      this.first = first;
      this.last = last;
      this.address = address;
      this.city = city;
      this.phone = phone;
      this.next = null;
   }
   
   // Construct a new ListNode if a reference 
   // to the next ListNode IS passed.
   public ListNode (String first, String last, 
                    String address, String city, 
                    String phone, ListNode next) 
   {
      this.first = first;
      this.last = last;
      this.address = address;
      this.city = city;
      this.phone = phone;
      this.next = next;
   }
}