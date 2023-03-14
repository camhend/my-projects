// Name: Regicide Card Game
// Author: Cameron Henderson
// Created: 1/24/2023

import java.util.*;

// This is a program that simulates the card game Regicide. 
// It is a game played with 52 playing cards where the objective 
// is to play cards to try to defeat all 12 Royal Face cards.
// The game continues until all 12, or the player, are defeated.
// The game creates virtual decks of cards to simulate the game.
public class Regicide {
   private static final int MAX_HAND_SIZE = 8;
   private static int JOKERS = 2;
   private static final Face[] ONLY_NUMS = 
                        {Face.ACE, Face.TWO, Face.THREE, 
                        Face.FOUR, Face.FIVE, Face.SIX, 
                        Face.SEVEN, Face.EIGHT, Face.NINE, 
                        Face.TEN};  
   
   private static final Face[] ALL_ROYALS = 
                        // order matters to castleDeck
                        {Face.KING, Face.QUEEN, Face.JACK}; 
                        
   private static final Suit[] SUITS = Suit.values();
             
   private static Stack<Card> tavernDeck = new Stack<Card>();
   private static Stack<Card> castleDeck = new Stack<Card>();  
   private static Stack<Card> discardDeck = new Stack<Card>();          
    
   public static void main(String[] args) {
      // create a new Tavern Deck (where the player draws new cards from)
      // fills the deck with NON-Royal face cards
      newDeck(tavernDeck, ONLY_NUMS);
      
      // create the player's initial hand
      ArrayList<Card> hand = new ArrayList<Card>();
      while (hand.size() < MAX_HAND_SIZE) {
         hand.add(tavernDeck.pop());
      }
      
      // create a new "Castle Deck" 
      // (where the 12 bosses (Royal Face cards) are kept)
      newCastleDeck(castleDeck);
      
      Scanner input = new Scanner(System.in);
      boolean lose = false;
      
      // play another round until the castle deck is empty 
      // (i.e. all bosses are defeated) OR the player loses the game
      while ( !castleDeck.isEmpty() && !lose ) {
         // create a Boss object to track Attack and Health of boss
         Boss boss = new Boss(castleDeck.peek()); 
            
         ArrayList<Card> cardsPicked = new ArrayList<Card>();
         Stack<Card> cardsInPlay = new Stack<Card>();
         
         // play another round if the boss has health remaining 
         // AND the player hasn't lost
         while (boss.getHealth() > 0 && !lose) {   
            
            // display the game board and prompt
            displayBoard(hand, cardsInPlay, boss);
            System.out.print("Select which card(s) you would ");
            System.out.println("like to play by entering the number above it.");
            System.out.print("To play multiple cards, ");
            System.out.println("separate card selections by a space.");
            System.out.println();
            
            // FIRST PHASE the player gets a turn to play card(s)
            playerPhase(hand, cardsInPlay, boss);
            
            // if boss is still alive, then go to the boss's turn
            if (boss.getHealth() > 0) {
               System.out.print("Press Enter to proceed to enemy's turn. ");
               input.nextLine();
               
               // SECOND PHASE: The enemy boss gets a turn to attack the player
               // and check if the player survived 
               lose = enemyTurn(hand, cardsInPlay, boss);    
            }
         }
         
         // if the player hasn't lost the game, 
         // then check if the boss was defeated.
         if (!lose) {
         
            // if exact damage is done, then the boss's card 
            // goes to the top of the Tavern Deck to be drawn next
            if (boss.getHealth() == 0) {
               System.out.print("Exact damage! ");
               System.out.println("Defeated Royal goes to top of tavern deck.");
               tavernDeck.push(castleDeck.pop());
            
            // if the boss's health goes negative, 
            // then their card goes to the discard pile
            } else {
               System.out.print("Overkill damage. ");
               System.out.println("Defeated Royal goes to discard deck.");
               discardDeck.push(castleDeck.pop());
            }
            
            // end of round all cards PLAYED during
            // the round then go to the discard pile
            System.out.println("Cards in play are moved to the discard pile");
            System.out.print("Press Enter to face the next Royal ");
            input.nextLine();
            while ( !cardsInPlay.isEmpty() ) {
               discardDeck.push(cardsInPlay.pop());
            }
         } // end of effects if player ended the round without losing
         
      } // end of while loop castle deck is not empty
         
         // if the castle deck is empty: prompt WIN message
         if (castleDeck.isEmpty()) {
            System.out.println();
            System.out.println("No more Royals! YOU WIN!!");
         }
   } // end of main method
   
   // Fills a given stack with shuffled playing cards. 
   // Requires a list of allowed faces. 
   public static void newDeck(Stack<Card> deck, Face[] faces) {
      Card[] tempDeck = new Card[faces.length * 4];
      for (int i = 0; i < tempDeck.length; i++) {
         tempDeck[i] = (new Card(faces[i % faces.length], 
                       Suit.values()[i / faces.length]));
      }
      shuffle(tempDeck);

      for (int i = 0; i < faces.length * 4; i++) {
         deck.push(tempDeck[i]);
      }
   }
   
   // Fills a given stack with a shuffled castle deck with all 
   // Royal face cards in same order of ALL_ROYALS
   public static void newCastleDeck(Stack<Card> castleDeck) {      
      // loop for each unique Royal face card
      for (int royal = 0; royal < ALL_ROYALS.length; royal++) {
         Card[] group = new Card[4];
         
         // Fill the Card[] with all Royal face cards (Jack, Queen, King)
         for (int count = 0; count < 4; count++) {
            group[count] = new Card(ALL_ROYALS[royal], SUITS[count]); 
         }
         shuffle(group);
         // put the resulting Card arrays into the Castle Deck
         for (int count = 0; count < 4; count++) {
            castleDeck.push(group[count]); 
         } 
      }   
   }
   
   // takes a card array and shuffles it into a new order
   public static void shuffle(Card[] deck) {
        Random rand = new Random();
        
        for ( int first = 0; first < deck.length; first++) {
            int second = rand.nextInt(deck.length); 
            
            // swap current Card with randomly selected Card
            Card temp = deck[first]; 
            deck[first] = deck[second];
            deck[second] = temp;  
        } 
    } 
   
   // Player selects cards to play against the enemy Royal boss
   public static void playerPhase(ArrayList<Card> hand, 
                                  Stack<Card> cardsInPlay, Boss boss) {
                                 
      ArrayList<Card> cardsPicked = new ArrayList<Card>();
      // get cards picked by the player from their hand
      System.out.print("Enter [y] to \"Yield\" ");
      System.out.println("if you would like to play zero cards." );
      cardsPicked = pickCards(hand, cardsInPlay, boss, false);
      
      // if player choses "y" to Yield in pickCards, 
      // then an empty ArrayList is returned
      if ( cardsPicked.isEmpty() ) {
         System.out.println("You choose to yield");
      // otherwise, discard the cards picked from hand
      // and resolve the effects of the cards picked
      } else {
         System.out.println("Card(s) played: " + cardsPicked);
         hand = discardFromHand (cardsPicked, hand);
         resolveCardsPlayed(cardsPicked, hand, boss);
      }
      
      // put the Cards Picked into the Cards In Play
      for (int i = 0; i < cardsPicked.size(); i++) {
         cardsInPlay.push(cardsPicked.get(i));
      }
   }
   
   // Enemmy turn: the player "takes damage" and discards value 
   //              equal to the boss's attack.
   // Check if the player loses and return the result
   public static boolean enemyTurn (ArrayList<Card> hand, 
                                    Stack<Card> cardsInPlay, Boss boss) {
      displayBoard(hand, cardsInPlay, boss);
      Scanner input = new Scanner(System.in);
      boolean lose = false;
      ArrayList<Card> cardsPicked = new ArrayList<Card>();
      
      // resolve boss's attack only if the boss has a positive Attack value
      if (boss.getAttack() > 0) {
         // get the player's total hand value to check if 
         // they have enough to survive the attack
         int handValue = 0;
         for (int i = 0; i < hand.size(); i++) {
            handValue += hand.get(i).getFaceValue();
         }
         // if the player cannot survive the boss's attack, they lose
         if (handValue < boss.getAttack() && JOKERS == 0) {
            System.out.println("You cannot survive the attack. You lose!");  
            lose = true;
         // if player can survive, then resolve the boss's attack
         } else {
            
            // prompt player to pick cards to discard
            // loop until the player selects a combination
            // of cards with total value equal or greater
            // than the boss's attack value
            int total;
            do {  
               System.out.print("They attack! Pick cards to discard. ");
               System.out.print("The total must equal or ");
               System.out.println("exceed the Royal's Attack value.");               
               // if player HAS a joker, they can survive 
               // but they must use a joker
               if (handValue < boss.getAttack()) {
                  System.out.println("Hint: You must use a Joker [j] to survive.");
               }
               System.out.println();
               
               // cards the player chose to discard
               cardsPicked = pickCards(hand, cardsInPlay, boss, true);
               
               // total up the value of cards the player selected
               total = 0;
               for (int i = 0; i < cardsPicked.size(); i++) { 
                  total += cardsPicked.get(i).getFace().getFaceValue();
               } 
               
               // prompt if the total is not sufficient          
               if (total < boss.getAttack()) {
                  System.out.println("Card total is not enough!");
                  System.out.println();
               }
            // go back to prompt the player for a new card selection
            // if the total value is not sufficient
            } while (total < boss.getAttack()); 
            
            // discard cards selected from the player's hand
            // to the discard deck
            System.out.println("Card(s) discarded: " + cardsPicked);
            hand = discardFromHand (cardsPicked, hand);
            for (int i = 0; i < cardsPicked.size(); i++) {
               discardDeck.push(cardsPicked.get(i));
            }
         } // end of else (resolving boss's attack)
      
      // else: boss's DOES NOT have a positive value 
         // do NOT check if player will lose
         // and do NOT resolve boss's attack
      } else {
         System.out.println("Boss's attack is zero. No cards will be discarded");
      }
      
      // if player hasn't lost, go to next turn
      if (!lose) {
         System.out.print("Press Enter to proceed to your turn. ");
         input.nextLine();
      }   
      return lose;      
   }
   
   // prompt player to pick cards from their hand. 
   // Return an ArrayList with the cards selected
   public static ArrayList<Card> pickCards (ArrayList<Card> hand, 
                                            Stack<Card> cardsInPlay, Boss boss, 
                                            boolean discardPhase) {
      Scanner cardSelect = new Scanner(System.in);
      ArrayList<Card> cardsPicked = new ArrayList<Card>();
      String selection;
      boolean validSelect;
      boolean validPlay = true;;
      
      System.out.print("Enter [j] to play joker. ");
      System.out.print("A joker discards your current hand, ");
      System.out.print("then completely refills it. ");
      System.out.println("You have " + JOKERS + " left" );
      System.out.println("Enter [h] to print the rules" );
      System.out.println();
      
      System.out.print("Select cards: ");
      
      do {
         // take user card selection or input as a String
         selection = cardSelect.nextLine();
         // check if the input is a valid input
         // (String of integers less than handsize, "j", or "y")
         validSelect = isValidSelection(selection, hand);
         
         // if user selects "j"
         // then discard the hand and draw a new full hand.
         // Prompt if this action is not possible 
         // or if this action empties the Tavern (draw) deck
         if (selection.toLowerCase().equals("j")) { 
            if (JOKERS > 0 && !tavernDeck.isEmpty()) {
               hand = joker(hand);
               JOKERS--;
               displayBoard(hand, cardsInPlay, boss);
            } else {
               if (JOKERS == 0) {
                  System.out.println("No jokers remaining.");
               }
            }
            if (tavernDeck.isEmpty()) {
               System.out.println("The tavern deck is empty!");
            }   
         // if user selects "h", then print the rules
         } else if (selection.toLowerCase().equals("h")) {
            printRules();
            
         // invalid selection error message
         } else if ( !validSelect ) { 
            System.out.println("Invalid entry and/or card selection");
            
         // allow "y" as input, to return an empty ArrayList 
         // as cardsPicked
         } else if (selection.toLowerCase().equals("y")) {
         
         // if passed, and no alternate inputs
         // then turn the String of integers into 
         // the corresponding cards in hand
         // then check if it's a valid play by the game's rules
         } else {
            cardsPicked = selectionToCardsPlayed(selection, hand);  
            validPlay = isValidPlay(cardsPicked, discardPhase); 
            if ( !validPlay ) {
               System.out.println("Invalid card combination.");
            }
         }  
      } while ( !validSelect || !validPlay || selection.toLowerCase().equals("j"));       
      return cardsPicked;
   } // end of pickCards
   
   // take a String of integers and get the cards in the hand
   // that correspond to the numbers in the String (index 1 to hand size)
   // return the retreived cards as an ArrayList
   public static ArrayList<Card> selectionToCardsPlayed (String selection,
                                                         ArrayList<Card> hand) {
      Scanner selectionScan = new Scanner(selection);
      ArrayList<Card> cardsPicked = new ArrayList<Card>();
      
      // convert the String of integers into an Integer ArrayList
      ArrayList<Integer> nums = new ArrayList<>();
      while (selectionScan.hasNextInt()) {
         nums.add(selectionScan.nextInt());
      }
      
      // find the largest number in the Integer ArrayList
      int max = nums.get(0);; 
      while ( !nums.isEmpty() ) {
         if (nums.size() == 1) {
            max = nums.get(0);
         } else {
            for (int j = 0; j < nums.size() - 1; j++) {
               max = Math.max(nums.get(j), nums.get(j+1));
            }
         }
         
         // put cards into cardsPicked from 
         // highest index in "hand" to lowest index
         // this is so that when cards are then removed
         // from then hand, they can be removed sequentially
         // from highest index to lowest using .remove()
         // without the resulting shifting causing the wrong
         // card to be removed
         nums.remove(nums.indexOf(max));
         cardsPicked.add(hand.get(max - 1));
      } 
      return cardsPicked;      
   }
   
   // check if the input String is a valid selection 
   // (String of integers, "j", or "y")
   public static boolean isValidSelection(String selection, 
                                          ArrayList<Card> hand) {
      Scanner selectionScan = new Scanner(selection);
      // keep track of numbers selected
      ArrayList<Integer> nums = new ArrayList<Integer>();
      
      // accept input for selecting joker or yield
      if (selection.toLowerCase().equals("j") 
          || selection.toLowerCase().equals("y")) { 
         return true;
      // reject no input
      } else if (!selectionScan.hasNext()) { 
         return false;
      } else {
         // require all tokens to be integers
         while (selectionScan.hasNext()) { 
            if (!selectionScan.hasNextInt()) {
               return false;
            } else {
               int nextNum = selectionScan.nextInt();             
               // reject if number selected is greater than 
               // the hand size (card selected is not a valid option)
               if (nextNum > hand.size() || nextNum < 1) { 
                  return false;   
               // reject if number selected has already been selected   
               } else if (nums.contains(nextNum)) { 
                  return false;
               } else {
                  // if token is accepted, then keep 
                  // track of numbers selected
                  nums.add(nextNum); 
               }
            }
         }
         return true;
      }
   } // end of isValidSelection
   
   // take an ArrayList of cards, and check if the cards played 
   // are a legal play in the game.
   // take a boolean that designates which phase of play it is.
   // return a boolean of the determination of whether it
   // is a valid play
   public static boolean isValidPlay (ArrayList<Card> cardsPicked, 
                                      boolean discardPhase) {
      Face nextFace; 
      int faceTotal = 0;
      
      // if during discard phase, 
      // legal play is not applicable
      if (discardPhase) {
         return true;
      }
      
      // if playing 2 cards and one is an Ace, then accept
      if (cardsPicked.size() > 1) {
         if (cardsPicked.size() == 2) {    
            for (int i = 0; i < cardsPicked.size(); i++) {
               if (cardsPicked.get(i).getFace() == Face.ACE) {
                  return true;
               }
            }
         }
          // if playing multiple cards, 
          // they must all be the same Face value, 
          // and they must total to 10 or less
         for (int i = 0; i < cardsPicked.size() - 1; i++) { 
            // look at face of next card in cardsPicked
            nextFace = cardsPicked.get(i).getFace(); 
            
            // all Faces should be the same
            if (i < cardsPicked.size() - 1 
            && nextFace != cardsPicked.get(i + 1).getFace()) { 
               return false;
            } 
            
            // each combo card played has to be 5 or less
            if (nextFace.getFaceValue() > 5) { 
               return false;
            // all combo cards played together cannot exceed 10
            } else if (faceTotal > 10) { 
              return false;
            } else {
              faceTotal += nextFace.getFaceValue();
            }
         }
      }
      return true;
   } // end of isValidPlay
   
   // take a list of cards, and discard those cards as they are
   // found in the hand
   public static ArrayList<Card> discardFromHand (ArrayList<Card> cardsPicked,
                                                  ArrayList<Card> hand) {
      Card nextCard;
      for (int i = 0; i < (cardsPicked.size()); i++) {
         nextCard = cardsPicked.get(i);
         hand.remove(nextCard);
      }
      return hand;
   }
   
   // take an ArrayList of cards, add their values together.
   // Resolve damage and Suit power based on total value of 
   // cards played, and Suits of all cards
   public static void resolveCardsPlayed (ArrayList<Card> cardsPicked, 
                                          ArrayList<Card> hand, Boss boss) {
      // total the value of all cards in the cardsPicked ArrayList
      int damageTotal = 0;
      for (int i = 0; i < cardsPicked.size(); i++) { 
         damageTotal += cardsPicked.get(i).getFace().getFaceValue();
      }
      
      boolean dblDamage = false;
      boolean restore = false;
      boolean draw = false;
      boolean block = false;
      
      
      for (int i = 0; i < cardsPicked.size(); i++) { 
         
         // check if a Suit Power is blocked
         // (if the Suit Power is the same suit as the boss)
         // otherwise, mark the Suit power as active
         switch (cardsPicked.get(i).getSuit()) {
            case SPADES: 
               if (castleDeck.peek().getSuit() != Suit.SPADES) {
                  block = true;   
               } else {
                  System.out.print("The Royal is a Spade; ");
                  System.out.println("they block your Spade's power.");
               }
               break;
            case HEARTS:
               if (castleDeck.peek().getSuit() != Suit.HEARTS) {
                  restore = true;
               } else {
                  System.out.print("The Royal is a Heart; ");
                  System.out.println("they block your Heart's power.");
               }
               break;
            case DIAMONDS:
               if (castleDeck.peek().getSuit() != Suit.DIAMONDS) {
                  draw = true;
               } else {
                  System.out.print("The Royal is a Diamond; ");
                  System.out.println("they block your Diamond's power.");
               }
               break;
            case CLUBS:
               if (castleDeck.peek().getSuit() != Suit.CLUBS) {
                  dblDamage = true;  
               } else {
                  System.out.print("The Royal is a Club; ");
                  System.out.println("they block your Club's power.");
               }
         }
      }
      
      // resolve Suit powers
      
      if (restore) {
         // put cards from the discard deck to the bottom of the
         // tavern deck. For total damageTotal, or until the 
         // discard deck is empty, whichever is first
         int amount;
         amount = Math.min(discardDeck.size(), damageTotal);
         System.out.print("Replaced " + amount + " ");
         System.out.print("cards from the discard deck to ");
         System.out.println("the bottom of the Tavern Deck");
         discardDecktoTavernBottom(amount);
      }
      if (draw) {
         // draw damageTotal cards, or until hand size limit is reached
         int drawAmount = 0;
         while (hand.size() < MAX_HAND_SIZE && drawAmount < damageTotal) {
            hand.add(tavernDeck.pop());
            drawAmount++;
         }
         System.out.println("Draw " + drawAmount + " cards");
      }
      if (block) {
         // reduce the boss's attack by damageTotal
         boss.reduceAttack(damageTotal);
         System.out.println("Boss's attack reduced by " + damageTotal + "");
      }
      if (dblDamage) {
         // double damage dealt to the boss
         System.out.println("Damage doubled");
         damageTotal *= 2;
      }
      
      System.out.println("Dealt " + damageTotal + " damage");
      boss.reduceHealth(damageTotal);
   }
   
   // take the player's hand, discard the hand to the discard deck
   // then repopulate from the tavern deck until max hand size
   // or until no more cards can be drawn
   public static ArrayList<Card> joker(ArrayList<Card> hand) {
      while ( !hand.isEmpty() ) {
         discardDeck.push(hand.get(0));
         hand.remove(0);
      }
      while (hand.size() < MAX_HAND_SIZE && !tavernDeck.isEmpty()) {
         hand.add(tavernDeck.pop());  
      }
      return hand;
   }
   
   // called when player plays a Heart. Takes an integer, and places
   // that many cards on the bottom of the tavern deck
   public static void discardDecktoTavernBottom (int amount) {
      // create array equal to discard deck size
      Card[] tempArray = new Card[discardDeck.size()];
      
      // turn discard deck into a "discard array" (empty discardDeck)
      for (int i = 0; i < tempArray.length; i++) {
         tempArray[i] = discardDeck.pop();
      } 
      
      // shuffle the "discard array"
      shuffle(tempArray);
      
      // create a new deck to be the new tavern deck
      Stack<Card> tempList = new Stack<Card>();
      
      // put AMOUNT cards into the new tavern deck FIRST
      for (int i = 0; i < amount; i++) {
         tempList.push(tempArray[i]);
      } 
      
      // then empty the rest of the temp array into the discard deck
      for (int i = amount; i < tempArray.length; i++) {
         discardDeck.push(tempArray[i]);
      } 
      
      // second temp list to use for "picking up" the tavern deck. 
      Stack<Card> secondTempList = new Stack<Card>();
      while ( !tavernDeck.isEmpty() ) {
         secondTempList.push(tavernDeck.pop()); 
      }
      
      // put the tavern deck on top of the first temp list
      while ( !secondTempList.isEmpty() ) {
         tempList.push(secondTempList.pop());
      }
      
      tavernDeck = tempList;
            
   }
   
   // display the current state of the board to the player. 
   // Provides stats,  draws a picture of the current enemy, and draws the hand
   public static void displayBoard (ArrayList<Card> hand, 
                                    Stack<Card> cardsInPlay, Boss boss) {      
      int cardSize = 14;   
      int leftAlignNum = 50;
      
      System.out.println();
      System.out.println();
      statsDisplay(cardsInPlay, leftAlignNum, cardSize, boss);
      printCardPicture(leftAlignNum, cardSize, castleDeck);
      System.out.println();
       
      // create String for cards in hand
      String printHand = "";
      for (int i = 0; i < hand.size(); i++) {
         printHand += hand.get(i) + " ";
      }  
      
      // print numbers above cards in hand
      String tempString = printHand;
      System.out.print(" ");
      for (int i = 0; i < hand.size(); i++) {
          for (int j = 0; j < tempString.indexOf("of"); j++) {
            System.out.print(" ");
          }
          tempString = tempString.substring(tempString.indexOf("of") + 1);
          System.out.print(i + 1);
      }  
      System.out.println();
                  
      // print hand
      System.out.println(printHand);
      System.out.println();
      
   }   
   
   // called by displayBoard to give stats about the game at the top of the board
   // including the tavern deck size, the discard deck size, 
   // the number of cards in play, and boss's current health and attack values
   public static void statsDisplay (Stack<Card> cardsInPlay, 
                                    int leftAlignNum, int cardSize, Boss boss) {
      int attack = boss.getAttack();
      int health = boss.getHealth();
      
      // generate Strings for the stats displays
      String tavDeckDisplay = "Tavern Deck:   " + tavernDeck.size() + " ";
      String discardDisplay = "Discard Total: " + discardDeck.size();
      String cardsInPlayDisplay = "Cards In Play: " + cardsInPlay.size();
      // add spaces to place the joker display on the right
      String jokersDisplay  = "";
      for (int i = 0; i < leftAlignNum * 2 + cardSize
           - tavDeckDisplay.length(); i++) {
         jokersDisplay += " ";
      }
      jokersDisplay += "Jokers: " + JOKERS;
      String healthDisplay = "HEALTH: " + health;
      String attackDisplay = "ATTACK: " + attack;
      
      // print all stats displays generated above
      System.out.print(tavDeckDisplay);
      System.out.print(jokersDisplay);
      System.out.println();
      System.out.println(discardDisplay);
      System.out.println(cardsInPlayDisplay);
      
      // display royal's current HEALTH total
      for (int i = 0; i < leftAlignNum - healthDisplay.length()  - 2; i++) {
         System.out.print(" ");
      }
      System.out.print(healthDisplay);
      
      // display the royal's current ATTACK total
      for (int i = 0; i < cardSize + 4; i++) {
               System.out.print(" ");
      }  
      System.out.print(attackDisplay);
      System.out.println();  
   }
   
   // called by displayBoatd to draw a picture of the current boss's card
   public static void printCardPicture(int leftAlignNum, 
                                       int cardSize, Stack<Card> castleDeck) {
      String[] drawClub = {"  (__)  ",
                           "(__)(__)", 
                           "   /\\   "};
                           
      String[] drawDiamond = {"   /\\   ",
                              "  /  \\  ", 
                              "  \\__/  ",};
                              
      String[] drawHeart = {"  /\\/\\  ",
                            "  \\  /  ", 
                            "   \\/   "};
                            
      String[] drawSpade = {"   /\\   ",
                            "  /__\\  ",
                            "   /\\   "};
      // number of characters in each above Strings
      int drawingWidth = 8; 
      
      // create String used to align card picture from left
      String leftAlign = "";
      for (int i = 0; i < leftAlignNum; i++) {
         leftAlign += " ";
      }
      
      // create Strign for top of card picture: e.g. "_____"
      String top = leftAlign + "";
      for (int i = 0; i < cardSize; i++) {
               top += "_";
               } 
      top += "\n";
      
      // create String for row with the face on the left: e.g. "|JACK    |" 
      String faceOnLeft = leftAlign + "|" + castleDeck.peek().getFace().name(); 
      for (int i = 0; i < cardSize - 
           castleDeck.peek().getFace().name().length() - 2; i++) {
               faceOnLeft += " ";
      }
      faceOnLeft += "|\n";
      
      // create String for an empty row: e.g. "|       |"
      String emptyRow = leftAlign + "|";
      for (int i = 0; i < cardSize - 2; i++) {
      emptyRow += " ";
      }
      emptyRow += "|\n";
      
      // create String used to print ALL of 
      // the rows of one the above draw"Suit" drawings
      String suitDrawing = "";
      for (int i = 0; i < 3; i++) {
         suitDrawing += leftAlign + "|";
         for (int j = 0; j < (cardSize - 2 - drawingWidth) / 2; j++) {
            suitDrawing += " ";  
         }
         // pick one of the draw"Suit" drawings based on the card 
         switch (castleDeck.peek().getSuit()) {
            case CLUBS:
               suitDrawing += drawClub[i];  
               break;  
            case HEARTS:
               suitDrawing += drawHeart[i];  
               break;  
            case DIAMONDS:
               suitDrawing += drawDiamond[i];  
               break;    
            case SPADES:
               suitDrawing += drawSpade[i];;  
               break;    
            default:
               break;
         }
         for (int j = 0; j < (cardSize - 2 - drawingWidth) / 2; j++) {
            suitDrawing += " ";  
         }
         suitDrawing += "|\n";
      }
      
      // create String for row with Face on the right: e.g. "|____JACK|"
      String faceOnRight = leftAlign + "|";
      for (int i = 0; i < cardSize 
           - castleDeck.peek().getFace().name().length() - 2; i++) {
         faceOnRight += "_";
      }
      faceOnRight += castleDeck.peek().getFace().name();
      faceOnRight += "|\n";
      
      // print the above Strings to create the card drawing
      System.out.print(top);
      System.out.print(faceOnLeft);
      System.out.print(emptyRow);
      System.out.print(suitDrawing);
      System.out.print(emptyRow);
      System.out.print(faceOnRight);
      System.out.println();
      
      // print card toString under picture
      String bossCardName = leftAlign + castleDeck.peek();
      System.out.println(bossCardName);
   }
   
   // print a set of rules for the game when the player enters "h"
   public static void printRules () {
      System.out.println();
      System.out.println("AIM OF THE GAME:");
      System.out.print("Try to defeat 12 powerful Royals ");
      System.out.println("in order of Jacks, Queens, and then Kings.");
      System.out.print("Jacks are the weakest, ");
      System.out.println("Queens are a bit more dangerous, ");
      System.out.println("and Kings are the most powerful. ");
      System.out.println();
      System.out.println();
      
      System.out.println("HOW TO PLAY");
      System.out.println("The game is played in 4 phases:");
      System.out.println();
      System.out.println("1. PLAY A CARD OR YIELD:");
      System.out.println("   Play a card to attack the enemy.");
      System.out.println("   The number on the card determines the ");
      System.out.println("   attack value dealt to the enemy.");
      System.out.println("   Yield [y] to play no cards.");
      System.out.print("   Joker [j] to discard your hand ");
      System.out.println("and draw to Max Hand Size (" + MAX_HAND_SIZE + ")");
      System.out.println();
      System.out.println("2. ACTIVATE THE SUIT POWER:");
      System.out.println("   Each suit has a different ability (see below).");
      System.out.print("   The enemy Royal has a Suit ");
      System.out.println("and will block abiltiies of the same Suit.");
      System.out.println();
      System.out.println("3. DEAL DAMAGE TO THE ENEMY:");
      System.out.println("   Deal your card(s) total attack power to the enemy.");
      System.out.print("   Bring the enemy below zero ");
      System.out.println("Health to discard them to discard pile.");
      System.out.print("   Deal enough damage to bring ");
      System.out.println("them to exactly zero Health ");
      System.out.println("   to put their card on the TOP of the Tavern Deck");
      System.out.println();
      System.out.println("4. SUFFER DAMAGE FROM THE ENEMY:");
      System.out.print("   The enemy attacks you, ");
      System.out.println("and you must defend yourself ");
      System.out.println("   by discarding a total value equal to ");
      System.out.println("   or greater than the Attack of the enemy Royal ");
      System.out.println();
      System.out.println();
      
      System.out.println("SUIT POWER: Cards played have additional effects ");
      System.out.println("            (X = total attack value of cards played)");
      System.out.print("DIAMONDS: Draw X cards from the Tavern Deck ");
      System.out.println("(Up to max hand size: (" + MAX_HAND_SIZE +") )");
      System.out.print("HEARTS: Restore X cards from the ");
      System.out.println("Discard pile to the bottom of the Tavern Deck");
      System.out.println("SPADES: Reduce enemy Attack by X");
      System.out.println("CLUBS: Double Damage");
      System.out.println();
      System.out.println();

      System.out.print("COMBO CARDS: ");
      System.out.println("You can normally only play one card at a time.");
      System.out.println("             You can play multiple cards in two ways:");
      System.out.print("1. Pairs, triples, or quadrules of the same face value, ");
      System.out.println("but their total value is less than 10. ");
      System.out.println("   (Up to four 2's, three 3's, two 4's, or two 5's.) ");
      System.out.print("2. Two cards are played together, ");
      System.out.println("and at least one is an Ace");
      System.out.println();
      System.out.println();
      
      System.out.println("PLAYING A ROYAL FROM HAND: ");
      System.out.println("JACK in hand counts as " + Face.JACK.getFaceValue());
      System.out.println("QUEEN in hand counts as " + Face.QUEEN.getFaceValue());
      System.out.println("KING in hand counts as " + Face.KING.getFaceValue());
      System.out.println();
      System.out.println();
      
      System.out.print("Select cards: ");

   }
   
} // end of Regicide class



         