import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {



//Objects declared to create the UI (ControlP5) and GameState
ControlP5 cp5;
GameState gs;

PImage star, shroom;

//Initalize window settings, objects, etc. to start the game 
public void setup() {
  background(223, 185, 185);
  
  cp5 = new ControlP5(this);
  gs = new GameState();
  
  cp5.addButton("Reset")
     .setPosition(0, 0)
     .setSize(70, 70);
     
  star = loadImage("star.gif");
  star.resize(25, 25);
  shroom = loadImage("shroom.png");
  shroom.resize(25, 25);
}


//Updates the GameState every frame
public void draw() {
  background(223, 185, 185);
  gs.draw();
  
  String displayMessage = "";
  
  if (gs.start == false) {
    displayMessage = "Press [SPACE] to Start";
  }
  else if (gs.paused == false) {
    gs.update();
    displayMessage = "Score: " + gs.score;
  }
  else {
    displayMessage = "(Paused)";
  }
  
  if (gs.gameOver == true) {
    gs.paused = true;
    displayMessage = "You lose. :(";
  }
  else if (gs.gameWin == true) {
    gs.paused = true;
    displayMessage = "You Win! :)";
  }
  
  textAlign(CENTER);
  fill(75);
  textSize(35);
  text(displayMessage, 0, (height / 2) - 300, width - 1 , (height / 2) - 300); 
}


//Handles when keyboard input is pressed
public void keyPressed() {
  gs.keyPressed();
}


//Handles when keyboard input is released 
public void keyReleased() {
  gs.keyReleased();
}


// Reset score and game elements
public void Reset() {
  //println("Clicked reset");
  gs = new GameState();
}
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
  public void update() {
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
  public void collide_paddle(Paddle thePaddle) {
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
  public void collide_box(Box theBox) {
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
  public void collide_wall() {
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
  public void draw() {
    strokeWeight(1);
    if (isStar == true)
      fill(255, 255, 255, 125);
    else
      fill(255);
    circle(xPos, yPos, diameter);
  }
}
/* 
This class represents a rectangle (also a square).
The Box disappears when the Ball collides into it .
Feel free to add any helper methods/functions necessary.
*/

class Box {
  //Declare class variables/members necessary for a Box
  float xPos, yPos, side;
  int colour;
  int value;
  boolean active;
  boolean isStarPower, isShroomPower;
  
  //Constructor
  Box(float x, float y, float s, int c, int v, boolean a) {
    xPos = x;
    yPos = y;
    side = s;
    colour = c;
    value = v;
    active = a;
  }
 
  //Perform actions necessary to update the current frame of the Box
  public void update() {
    //Nothing to update!
  }
  
  //Draws the current location of the Box (rectangle/square) [or hides it] after update() is processed
  public void draw() {
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
/* 
This class is the brains of the game.
Handles how the game is run especially interactions between the Ball, Paddle, and Box objects.
Feel free to add any helper methods/functions necessary.
*/

class GameState {
  //Declare class variables/members necessary to help with running the game
  boolean start, paused;
  boolean gameOver, gameWin;
  int score, collected;
  
  //Declare class objects for the game: a Ball, Paddle, collection of Boxes, etc.
  PVector zeroVector = new PVector(0, 0);
  Ball ball = new Ball(width / 2, height - 40, 20, zeroVector, 8);
  Paddle paddle = new Paddle();
  Box[][] boxes = new Box[10][6];

  //Constructor
  GameState(/* possible parameters needed to create a GameState */) {
    start = false;
    paused = false;
    gameOver = false;
    collected = 0;
    
    int c = color(0);
    int counter = 6;
    
    for (int i = 0; i < boxes.length; i++) {
      for (int j = 0; j < boxes[0].length; j++) {
        if (j == 0)
          c = color(255, 118, 143);
        else if (j == 1)
          c = color(255, 173, 111);
        else if (j == 2)
          c = color(254, 255, 127);
        else if (j == 3)
          c = color(123, 255, 119);
        else if (j == 4)
          c = color(126, 185, 255);
        else if (j == 5)
          c = color(196, 134, 255);
        boxes[i][j] = new Box((i * 50) + 175, (j * 50) + 280, 25, c, counter, true);
        counter -= 1;
      }
      //Restart counter before next iteration
      counter = 6;
    }
    
    for (int i = 0; i < boxes.length; i++) {
      int j = (int)random(0, boxes[0].length);
      int p = (int)random(0, 2);
      
      if (p == 0) {
        //boxes[i][j].colour = color(0);
        boxes[i][j].isStarPower = true;
        boxes[i][j].isShroomPower = false;
      }
      else {
        //boxes[i][j].colour = color(255);
        boxes[i][j].isStarPower = false;
        boxes[i][j].isShroomPower = true;
      }
    }
  }

  //Call update() on respective game objects: Ball, Box(es), and/or Paddle
  //Handle interactions of current state of game at every frame
  public void update() {
    ball.update();
    ball.collide_wall();
    ball.collide_paddle(paddle);
    
    int tempScore = 0;
    collected = 0;
    
    for (int i = 0; i < boxes.length; i++) {
      for (int j = 0; j < boxes[0].length; j++) {
        if (boxes[i][j].active == true) {
          ball.collide_box(boxes[i][j]);
        }
        else {
          collected += 1;
          tempScore += boxes[i][j].value;
        }
      }
    }
    score = tempScore;
    
    if (ball.outOfBounds == true)
      gameOver = true;
      
    if (collected == 60)
      gameWin = true;
  }
  
  //Draws the current state of the Game
  public void draw() {
    ball.draw();
    paddle.draw();
    
    for (int i = 0; i < boxes.length; i++) {
      for (int j = 0; j < boxes[0].length; j++) {
        if (boxes[i][j].active == true) {
          boxes[i][j].draw();
        }
      }
    }
  }
  
  //Handles what happens when specific keys on the keyboard is pressed
  public void keyPressed() {
    //Press [SPACE] to begin game
    if (key == ' ' && start == false) {
      start = true;
      ball.velocity = new PVector(0, -1);
    }
    
    //Move paddle using [a] and [d]
    if (key == 'a' && start == true && paused == false) {
      //println("Pressed [a]");
      paddle.moveLeft = true;
      paddle.update();
    }
    else if (key == 'd' && start == true && paused == false) {
      //println("Pressed [d]");
      paddle.moveLeft = false;
      paddle.update();
    }
    
    //Pause game with [p]
    if (key == 'p') {
      //println("Pressed [p]");
      if (paused == false)
        paused = true;
      else
        paused = false;
    }
  }
  
  
  //Handles what happens when specific keys on the keyboard is released
  public void keyReleased() {
    if (key == 'a') {
      //println("Released [a]");
      //TODO: something
    }
    else if (key == 'd') {
      //println("Released [d]");
      //TODO: something
    }
    else if (key == 'p') {
      //println("Released [p]");
      //TODO: something
    }
  }
}
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
  public void update() {
    if (moveLeft == true)
      xPos -= 25;
    else
      xPos += 25;
    
    //Don't paddle go off screen
    //xPos = constrain(xPos, 0, width);
    xPos = constrain(xPos, w / 2, width - (w / 2));
  }
  
  
  //Draws the current location of the Paddle (rectangle) after update() is processed
  public void draw() {
    strokeWeight(1);
    fill(100);
    rectMode(CENTER);
    rect(xPos, yPos, w, h);
    rectMode(CORNER);
  }
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
