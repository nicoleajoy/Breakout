/* 
This class represents a rectangle (also a square).
The Box disappears when the Ball collides into it .
Feel free to add any helper methods/functions necessary.
*/

class Box {
  //Declare class variables/members necessary for a Box
  float xPos, yPos, side;
  color colour;
  int value;
  boolean active;
  boolean isStarPower, isShroomPower;
  
  //Constructor
  Box(float x, float y, float s, color c, int v, boolean a) {
    xPos = x;
    yPos = y;
    side = s;
    colour = c;
    value = v;
    active = a;
  }
 
  //Perform actions necessary to update the current frame of the Box
  void update() {
    //Nothing to update!
  }
  
  //Draws the current location of the Box (rectangle/square) [or hides it] after update() is processed
  void draw() {
    if (active == true && isStarPower == false && isShroomPower == false) {
      strokeWeight(1);
      fill(colour);
      rectMode(CENTER);
      square(xPos, yPos, side);
      rectMode(CORNER);
    }
    else if (active == true && isStarPower == true) {
      strokeWeight(1);
      fill(colour);
      rectMode(CENTER);
      square(xPos, yPos, side);
      rectMode(CORNER);
      imageMode(CENTER);
      image(star, xPos, yPos);
    }
    else if (active == true && isShroomPower == true) {
      strokeWeight(1);
      fill(colour);
      rectMode(CENTER);
      square(xPos, yPos, side);
      rectMode(CORNER);
      imageMode(CENTER);
      image(shroom, xPos, yPos);
    }
  }
}
