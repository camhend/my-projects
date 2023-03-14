// Name: Guessing Game
// Author: Cameron Henderson
// Created: 1/8/2023

import java.util.*;

// This program is a guessing game which generates a random number, and the 
// user must guess the number created. The user can play multiple games, 
// and get game statistics after play is finished.
public class CHGuess {
   public static void main (String[] args) {
      
      // range of numbers to guess from; 1 to range
      int range = 100;
      
      // variables for overall game statistics report
      int gamesPlayed = 0;
      int guesses = 0;
      int totalGuesses = 0;
      int best = 0;
      String choice;
      Scanner console = new Scanner(System.in);
      
      introduce(range);
      System.out.println();
      
      // prompt user to option to play a new game and update 
      // overall game statistics after each game is played
      do {
         guesses = playGame(range);
         gamesPlayed++;
         totalGuesses += guesses;
         if (best == 0 || best > guesses) {
            best = guesses;
         } 
         
         // require answer to user prompt start with "y" or "n"
         do {
            System.out.print("Do you want to play again? ");
            choice = console.next().toLowerCase().substring(0,1);
            if (!choice.equals("y") && !choice.equals("n")) {
               System.out.println("Please enter a valid input");
            }
         } while (!choice.equals("y") && !choice.equals("n"));
         System.out.println();    
         
      } while (choice.equals("y"));
      
      report(gamesPlayed, totalGuesses, best);
   }
   
   // prints an introductor paragraph that explain the game to the user,
   // including a predesignated range of numbers to guess from.
   public static void introduce(int range) {
      System.out.println("This program allows you to play a guessing game.");
      System.out.println("I will think of a number between 1 and");
      System.out.println(range + " and will allow you to guess until");
      System.out.println("you get it. For each guess, I will tell you");
      System.out.println("whether the right answer is higher or lower");
      System.out.println("than your guess.");
   }
   
   // play a single game of the guessing game, 
   // then return the number of guesses 
   public static int playGame(int range) {
      Random rand = new Random();
      Scanner console = new Scanner(System.in);
      int goal = rand.nextInt(range) + 1;
      int userGuess = 0;
      int guesses = 0;
      
      // ask user for their guess and loop until the user guesses the answer
      System.out.printf("I'm thinking of a number between 1 and %d...%n", range);
      while (userGuess != goal) {
         System.out.print("Your guess? ");
         
         // handle if user enters a non-integer input
         while (!console.hasNextInt()) {
            System.out.print("Invalid; input must be an integer ");
            console.next();
         }
         userGuess = console.nextInt();
         
         if (userGuess < goal) {
            System.out.println("It's higher.");
         } else if (userGuess > goal) {
            System.out.println("It's lower.");
         }
         guesses++; 
      }
      
      System.out.printf("You got it right in %d guess", guesses);
      if (guesses > 1) {
         System.out.println("es");
      } else {
         System.out.println();
      }
      return guesses;
   }
   
   // print a report of overall game statistics
   public static void report(int gamesPlayed, int totalGuesses, int best) {
      System.out.println("Overall results:");
      System.out.printf("\ttotal games   = %d%n", gamesPlayed);
      System.out.printf("\ttotal guesses = %d%n", totalGuesses);
      System.out.printf(
         "\tguesses/game  = %.1f%n", (double) totalGuesses / gamesPlayed
      );
      System.out.printf("\tbest game     = %d%n", best);
   }
    
}