// This enum represents the Face of a playing card 
// and is used in creating a Card object.
// It can provide information about the value of 
// each Face.
public enum Face {
   ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(15), KING(20);
   
   private int faceValue;
   
   private Face(int faceValue) {
      this.faceValue = faceValue;
   }
   
   public int getFaceValue() { return faceValue; }
}