/* 
This class represents a circle or ellipse; which also has movement via velocity.
Has functions/methods on how to collide with: (1) Paddle and (2) Box objects.
Feel free to add any helper methods/functions necessary.
*/

class Ball {
  //Declare class variables/members like position, velocity, diameter, speed, etc.
  float xPos, yPos, diameter;
  PVector velocity;
  float speed;
  boolean outOfBounds;
  boolean isStar, isShroom;
  int starCounter, shroomCounter;

  //Constructor
  Ball(float x, float y, float d, PVector v, float s) {
    //Initialize variables needed to create a ball
    xPos = x;
    yPos = y;
    diameter = d;
    velocity = v;
    speed = s;
  }
  
  //Perform actions necessary to update the current frame of the Ball
  void update() {
    velocity.normalize();
    velocity.mult(speed);
    xPos += velocity.x;
    yPos += velocity.y;
    
    //println(specialBallCounter);
    if (starCounter == 0)
      isStar = false;
      
    //println(specialPaddleCounter);
    if (shroomCounter == 0)
      isShroom = false;
  }
  
  //Helper function to process Ball-Paddle collisions
  void collide_paddle(Paddle thePaddle) {
    //First, check collision by finding nearest point on top of paddle to compare to ball
    float deltaX = xPos - max(thePaddle.xPos - (thePaddle.w / 2), min(xPos, thePaddle.xPos + (thePaddle.w / 2)));
    float deltaY = yPos - max(thePaddle.yPos - (thePaddle.h / 2), min(yPos, thePaddle.yPos + (thePaddle.h / 2)));
    
    if ((deltaX * deltaX + deltaY * deltaY) < ((diameter / 2) * (diameter / 2))) {
      float leftMost = thePaddle.xPos - (thePaddle.w / 2);
      float firstDivide = thePaddle.xPos - (thePaddle.w / 6); 
      float secondDivide = thePaddle.xPos + (thePaddle.w / 6); 
      float rightMost = thePaddle.xPos + (thePaddle.w / 2);
      
      if (isStar) 
        starCounter--;
        
      if (isShroom) {
        shroomCounter--;
        thePaddle.w = 150 * 2;
      }
      else {
        thePaddle.w = 150;
      }
      
      //Next, check where the collision along the paddle occured (0 to 1/3, 1/3 to 2/3, 2/3 to 1)
      if (leftMost <= xPos && xPos <= firstDivide) {
        //println("Up-Left");
        velocity.x = -1;
        velocity.y = -1;
      }
      else if (firstDivide <= xPos && xPos <= secondDivide) {
        //println("Straight up");
        velocity.x = 0;
        velocity.y = -1;
      }
      else if (secondDivide <= xPos && xPos <= rightMost) {
        //println("Up-Right");
        velocity.x = 1;
        velocity.y = -1;
      }
    }
  }

  //Helper function to process Ball-Box collisions
  void collide_box(Box theBox) {
    if (theBox.active == true) {
      //First, check collision by finding nearest point on top of paddle to compare to ball
      float deltaX = xPos - max(theBox.xPos - (theBox.side / 2), min(xPos, theBox.xPos + (theBox.side / 2)));
      float deltaY = yPos - max(theBox.yPos - (theBox.side / 2), min(yPos, theBox.yPos + (theBox.side / 2)));
      
      if ((deltaX * deltaX + deltaY * deltaY) < ((diameter / 2) * (diameter / 2))) {
        if (theBox.isStarPower == true) {
          isStar = true;
          starCounter = 3;
        }
        
        if (theBox.isShroomPower == true) {
          isShroom = true;
          shroomCounter = 3;
        }
        
        //Hit below the box
        if (yPos <= theBox.yPos - (theBox.side / 2)) {
          //println("Below");
          if (!isStar) {
            velocity.y *= -1;
          }
        }
        
        //Hit above the box
        if (yPos >= theBox.yPos + (theBox.side / 2)) {
          //println("Adove");
          if (!isStar) {
            velocity.y *= -1;
          }
        }
        
        //Hit left of box
        if (xPos < theBox.xPos) {
          //println("Left");
          if (!isStar) {
            velocity.x *= -1;
          }
        }
        
        //Hit right of box
        if (xPos > theBox.xPos) {
          //println("Right");
          if (!isStar) {
            velocity.x *= -1;
          }
        }
        
        //If reached this part, collision was detected, and box is no longer active
        theBox.active = false;
      }
      
      //Otherwise, no collision was detected
    }
  }
  
  //Helper function to process Ball-Wall collisions (left, top, & right wall boundaries)
  void collide_wall() {
    //Left and right walls
    if (xPos <= (diameter / 2) || xPos >= width - (diameter / 2))
      velocity.x *= -1;
    //Top wall
    else if (yPos <= (diameter / 2))
      velocity.y *= -1;
    //Bottom wall
    else if (yPos >= height - (diameter / 2))
      outOfBounds = true;
  }
  
  //Draws the current location of the Ball (circle or ellipse) after update() is processed 
  void draw() {
    strokeWeight(1);
    if (isStar == true)
      fill(255, 255, 255, 125);
    else
      fill(255);
    circle(xPos, yPos, diameter);
  }
}
