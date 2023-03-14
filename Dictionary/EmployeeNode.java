// Name: Dictionary
// Author: Cameron Henderson
// Created: 3/10/2023

// This class is a node in a Binary Search Tree in Dictionary.
// Upon construction, this node takes a Employee object to store
// in the Dictionary Binary Search Tree.
public class EmployeeNode {
   public EmployeeInfo employeeInfo;
   public EmployeeNode left;
   public EmployeeNode right;
   
   // construct a leaf node with Employee's information
   public EmployeeNode (EmployeeInfo employee) {
      this(employee, null, null);
   }
   
   // construct a branch node with Employee's information and links
   public EmployeeNode (EmployeeInfo employeeInfo,
                        EmployeeNode left,
                        EmployeeNode right) {
      this.employeeInfo = employeeInfo;
      this.left = left;
      this.right = right;
   }
      
}