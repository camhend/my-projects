// Name: Regicide Card Game
// Author: Cameron Henderson
// Created: 1/24/2023

// This class defines a Boss for use in the Regicide class.
// Based on the Face of the Card given on initialization, 
// the Boss object stores an initial Attack and Health value.
// Methods can get information about the boss, and reduce 
// it's values.

public class Boss {
   private int attack;
   private int health;
   private Card card;
   
   public Boss (Card card) {
      this.card = card;
      switch (card.getFace()) {
         case JACK:
            attack = 10;
            health = 20;
            break;
         case QUEEN:
            attack = 15;
            health = 30;
            break;
         case KING:
            attack = 20;
            health = 40;
            break;
         default:
            break;
      }
   }
   
   // return the current Health
   public int getHealth() {    
      return health;
   }
   
   // return the current Attack
   public int getAttack() {
      return attack;
   }
   
   // reduce the current health by the given int
   public void reduceHealth (int reduction) {
      health = health - reduction;
   }
   
   // reduce the current attack by the given int
   public void reduceAttack (int reduction) {
      attack = attack - reduction;
      if (attack < 0) {
         attack = 0;
      }
   }
}