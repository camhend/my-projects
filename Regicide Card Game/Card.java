// Name: Regicide Card Game
// Author: Cameron Henderson
// Created: 1/24/2023

// This is a class that represents a playing card.
   // Each object in insantiated with a Suit and a Face.
   // Each object can be called to give information
   // about it.

public class Card {
   private final Face face;
   private final Suit suit;
   
   public Card (Face face, Suit suit) {
      this.face = face;
      this.suit = suit;
   }
   
   public Suit getSuit() { return suit; }
   
   public Face getFace() { return face; }
   
   // return the value associated with the given Face
   // inside the Face enum
   public int getFaceValue() { return face.getFaceValue(); }
   
   public String toString() {
      return "[" + face + " of " + suit + "]";
   }
}  