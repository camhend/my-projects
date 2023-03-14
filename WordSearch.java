// Program name: Word Search Generator
// Author: Cameron Henderson
// Created 2/28/2023

import java.util.*;

// This program allows a user to create a word search
// puzzle from a series of entered words. Entered words
// will alterate between being placed randomly or 
// being placed in a way where it overlaps with another word.
// The user can then view their word search, or see the solution.
public class WordSearch {
   public static void main (String[] args) {
   
      Scanner input = new Scanner(System.in);
      String choice;
      boolean wasMade = false;
      char[][] xy = {};
      char[][] xySolution = {};
      
      do {
         printIntro();
         choice = input.nextLine().substring(0,1).toLowerCase();
         switch (choice) {
            case "g":
               xy = generate();
               xySolution = new char[xy.length][xy[0].length];
               for (int x = 0; x < xy.length; x++) {
                  xySolution[x] = Arrays.copyOf(xy[x], xy.length);
               }
               fillLetters(xy);
               fillDashes(xySolution);
               wasMade = true;
               break;
            case "p":
               if (wasMade) {
                  print(xy); 
               } else {
                  System.out.println("Please generate a word search");
                  System.out.println("before attemping to print");
                  System.out.println("Press Enter to return");
                  input.nextLine();
               }
               break;
            case "s":
               if (wasMade) {
                  print(xySolution); 
               } else {
                  System.out.println("Please generate a word search");
                  System.out.println("before attemping to");
                  System.out.println("view the solution");
                  System.out.println("Press Enter to return");
                  input.nextLine();
               }
               break;
            case "q":
               break;
            default:
               System.out.println("Please enter a valid input.");
               System.out.println("Press Enter to return");
               input.nextLine();
               break;
         }
      } while ( !choice.equals("q") );      
   }
   
   public static void printIntro () {
      System.out.println("Welcome to the word search generator");
      System.out.println("This program will allow you to generate");
      System.out.println("a custom word search puzzle.");
      System.out.println("---------------------------------------");
      System.out.println("Please select an option:");
      System.out.println("Generate a new word search (g)");
      System.out.println("Print out your word search (p)");
      System.out.println("Show the solution (s)");
      System.out.println("Quit the program (q)");
      
   }
   
   public static char[][] generate() {
      ArrayList<String> words = new ArrayList<String>();
      
      // Keep track of the starting X/Y coordinates 
      // and orientation of all entered words
      Map<String, int[]> wordCoords = new HashMap<String, int[]>();
      
      Scanner input = new Scanner(System.in);
      System.out.println("Enter the words to enter into the word search");
      System.out.println("Enter \"q\" when finished");
      String nextWord;
      int maxLength = 0;
      int totalLetters = 0;
      do {
         nextWord = input.nextLine().toUpperCase();
         if (!nextWord.equals("Q") && validWordInput(nextWord)) { 
            nextWord = nextWord.replace(" ", "");
            words.add(nextWord);
            maxLength = Math.max(maxLength, nextWord.length());
            totalLetters += nextWord.length();
         }
      } while ( !nextWord.equals("Q") );
      
      // This is a design choice to determine the 
      // size of the word search either by:
      // 1. side lengths are 2 + the length of the longest word OR 
      // 2. the area of the word search is 2 * the number of letters entered
      // (whichever is larger)
      int side = Math.max(
         maxLength + 2, (int) Math.ceil(Math.sqrt(2 * totalLetters)));
      char[][] xy = new char[side][side];
      
      // alternate between searching for a word to overlap with
      // or placing the word randomly.
      boolean flip = true;
      for (String word : words) {
         if ( flip ) {
            boolean overlapWorked = placeOverlap(word, wordCoords, xy);
            if ( !overlapWorked ) {
               placeRandom(word, wordCoords, xy);
            } else {
               flip = !flip;
            }
         } else {
            placeRandom(word, wordCoords, xy);
            flip = !flip;
         }
      }
      return xy;
   }
   
   // Return a boolean that checks if the given String 
   // contains only a single word of capitalized alphabetic letters.
   // If it fails, then print an appropriate error message.
   public static boolean validWordInput (String word) {
      Scanner wordScan = new Scanner(word);
      word = wordScan.next();
      // check if user inputs multiple words
      if ( !wordScan.hasNext() ) { 
         // check if user input any non-letters
         for (int i = 0; i < word.length(); i++) { 
            if ((int) word.charAt(i) < 65 || word.charAt(i) > 90) { 
               System.out.println("Please enter a word with only letters");
               return false;
            }
         } 
         // check if user entered a single character string
         if ( word.length() < 2 ) {
           System.out.println("Please enter a word with more than 1 letter");
           return false;
         }   
      // check if user entered multiple words       
      } else {
         System.out.println("Please enter one word at a time.");
         return false;
      }
      return true;
   }
     
   // Try to place the given word into the word search
   // at random locations and orientations. 
   public static void placeRandom(String word, 
                                  Map<String, int[]> wordCoords, 
                                  char[][] xy) {
      Random rand = new Random();
      for (int i = 0; i < 100; i++) {
         
         // X and Y coordinates of first letter of the word
         int xCoord = rand.nextInt(xy.length);
         int yCoord = rand.nextInt(xy[0].length);
         
         // Random integer -1, 0, or 1
         // Allows for randomly incrementing, decrementing or not changing
         // the X and Y coordinates of the next letter in the word
         int xDir = rand.nextInt(3) - 1;
         int yDir = rand.nextInt(3) - 1;
         
         // If the word can fit at the given coordinates and orientation
         // then place it there in the word search.
         // Also, store the word's coordinates and orientation.
         boolean valid = canFit(word, xCoord, yCoord, xDir, yDir, xy);
         if (valid) {
            int[] coords = {xCoord, yCoord, xDir, yDir};
            wordCoords.put(word, coords);
            for (int k = 0; k < word.length(); k++) {
               xy[xCoord][yCoord] = word.charAt(k);
               xCoord += xDir;
               yCoord += yDir;
            }
            return;
         }
      }
   }
   
   // Search all words that have been placed in the word search so far
   // and then try to find a valid position that would allow the given
   // word to intersect with a word in the word search at a matching letter.
   // Takes in a map of String mapped to an int array representing its
   // starting XY coordinate and orientation.
   public static boolean placeOverlap (String word, 
                                    Map<String, int[]> wordCoords, 
                                    char[][] xy) {
      List<String> words = new ArrayList<>(wordCoords.keySet());
      Collections.shuffle(words);
      ArrayList<Integer> wordCharAt = shuffle(word.length());
      
      for (String next : words) { // all words already in the word search
         ArrayList<Integer> nextCharAt = shuffle(next.length());
         for (int w : wordCharAt) { // word.charAt(w) in random order
            for (int n : nextCharAt) { // next.charAt(n) in random order
               if (word.charAt(w) == next.charAt(n)) {

                  // Iterate through all 8 cardinal directions
                  // in a random order. Used to change the
                  // orientation of the word.
                  ArrayList<Integer> xDir = new ArrayList<>(
                     Arrays.asList(-1, 0, 1));
                  Collections.shuffle(xDir);
                  ArrayList<Integer> yDir = new ArrayList<>(
                     Arrays.asList(-1, 0, 1));
                  Collections.shuffle(yDir);                 
                  for (int randXDir : xDir) {
                     for (int randYDir : yDir) {
                        
                        // Find the x/y coordinate where
                        // "word" should begin in order to 
                        // get it to intersect with 
                        // "next" at the matching letter
                        int xCoord = wordCoords.get(next)[0] 
                           + (n * wordCoords.get(next)[2]) 
                           - (w * randXDir);
                        int yCoord = wordCoords.get(next)[1] 
                           + (n * wordCoords.get(next)[3]) 
                           - (w * randYDir);
                        
                        // If the word can fit at the given 
                        // coordinates and orientation then 
                        // place it there in the word search.
                        // Also, store the word's 
                        // coordinates and orientation
                        boolean valid = canFit(word, 
                                               xCoord, yCoord, 
                                               randXDir, randYDir, 
                                               xy);
                        if (valid) {
                           int[] coords = {xCoord, yCoord, 
                                           randXDir, randYDir};
                           wordCoords.put(word, coords);
                           for (int k = 0; k < word.length(); k++) {
                              xy[xCoord][yCoord] = word.charAt(k);
                              xCoord += randXDir;
                              yCoord += randYDir;
                           }
                           return true;
                        }
                     } // end for (int randYDir : yDir)
                  } //end for (int randXDir : xDir)
               } // end if (word.charAt(w) == next.charAt(n))
            } // end for (int n : nextCharAt)
         } // end for (int w : wordCharAt)
      } // end for (String next : words)
      return false; 
   }
      
   // Make a shuffled list of numbers that helps with
   // interating in a random order without changing the order of the
   // original object. Takes an integer that determines the
   // size of the list, and returns a shuffled list of 
   // numbers 0 to size.
   public static ArrayList<Integer> shuffle(int size) {
      ArrayList<Integer> random = new ArrayList<Integer>();
      for (int i = 0; i < size; i++) {
         random.add(i);
      }
      Collections.shuffle(random);
      return random;
   }
   
   // Takes a X, Y Coordinate of the first letter and an orientation. 
   // Return a boolean for wether it can be placed there.
   public static boolean canFit(String word, 
                                int xCoord, int yCoord, 
                                int xDir, int yDir, 
                                char[][] xy) 
   {
      // check if the word will go out of bounds.
      // Reject a 0,0 direction since this would 
      // cause the word to print all in one spot
      if (   !(xDir == 0 && yDir == 0)
          && xCoord >= 0 
          && xCoord < xy.length
          && yCoord >= 0
          && yCoord < xy[0].length
          
          && xCoord + xDir * (word.length() - 1) >= 0 
          && xCoord + xDir * (word.length() - 1) < xy.length
          && yCoord + yDir * (word.length() - 1) >= 0
          && yCoord + yDir * (word.length() - 1) < xy[0].length) 
      {
         int testX = xCoord;
         int testY = yCoord;
         
         // Reject a placement if the next spot is not empty
         // BUT if the letters match then allow the placement
         for (int j = 0; j < word.length(); j++) {
            if (xy[testX][testY] != 0
                && xy[testX][testY] != word.charAt(j)) {
               return false;
            }
            testX += xDir;
            testY += yDir;
         }
      } else {
         return false;
      }
      return true;
   } // end of canFit method
   
   // Fill all empty spaces in the word search with dashes
   public static void fillDashes (char[][] xy) {
      for (char[] y : xy) {
         for (int i = 0; i < y.length; i++) {
            if (y[i] == 0) {
               y[i] = '-';
            }
         }
      }
   }
   
   // Fill all empty spaces in the word search with random letters
   public static void fillLetters (char[][] xy) {
      Random rand = new Random();
      
      for (char[] y : xy) {
         for (int i = 0; i < y.length; i++) {
            if (y[i] == 0) {
               y[i] = (char) (rand.nextInt(26) + 65);
            }
         }
      }
   }
   
   // Print the word search to the console
   public static void print(char[][] xy) {
      for (int i = 0; i < xy[0].length; i++) {
         for (char[] y : xy) {
            System.out.print(y[i] + " ");
         }
         System.out.println();
      }
      System.out.println();
   }
   
}