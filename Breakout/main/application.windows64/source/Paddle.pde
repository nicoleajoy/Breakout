/* 
This class represents a rectangle.
It can also handle movement via keyboard input.
Feel free to add any helper methods/functions necessary.
*/

class Paddle {
  //Declare class variables/members like position, velocity, size, speed, etc.
  float xPos, yPos, w, h;
  //PVector velocity;
  //float speed;
  boolean moveLeft;
  
  //Constructor
  Paddle(/* possible parameters needed to create a Paddle */) {
    xPos = width / 2;
    yPos = height - 20;
    w = 150;
    h = 20;
    //velocity = new PVector(0, 0);
    //speed = 5;
  }
  
  
  //Perform actions necessary to update the current frame of the Paddle
  void update() {
    if (moveLeft == true)
      xPos -= 25;
    else
      xPos += 25;
    
    //Don't paddle go off screen
    //xPos = constrain(xPos, 0, width);
    xPos = constrain(xPos, w / 2, width - (w / 2));
  }
  
  
  //Draws the current location of the Paddle (rectangle) after update() is processed
  void draw() {
    strokeWeight(1);
    fill(100);
    rectMode(CENTER);
    rect(xPos, yPos, w, h);
    rectMode(CORNER);
  }
}
