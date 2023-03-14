// Name: Dictionary
// Author: Cameron Henderson
// Created: 3/10/2023

// This class encapsulates identification and contact information
// about an individual employee. This object will be the data stored
// in an EmployeeNode as part of a Binary Search Tree within Dictionary.
public class EmployeeInfo {
   public int ID;
   public String firstName;
   public String lastName;
   public String streetAddress;
   public String city;
   public String state;
   public String zip;
   public String email;
   public String phone;
      
   public EmployeeInfo (int ID,
                    String firstName,
                    String lastName,
                    String streetAddress,
                    String city,
                    String state,
                    String zip,
                    String email,
                    String phone) {
      this.ID = ID;
      this.firstName = firstName;
      this.lastName = lastName;
      this.streetAddress = streetAddress;
      this.city = city;
      this.state = state;
      this.zip = zip;
      this.email = email;
      this.phone = phone;
   }
   
   // Return a String representation of 
   // the Employee's information
   public String toString() {
      String address = streetAddress + ", " 
         + city + ", " + state + " " + zip;
      return "Employee ID: " + ID + "\n"
         + "Name: " + getName() + "\n" 
         + "Address: " + address + "\n"
         + "Email: " + email + "\n"
         + "Phone: " + phone;
   }
   
   // Return a String as a combination of 
   // the Employee's first name and last name.
   public String getName() {
      return firstName + " " + lastName;
   }

}