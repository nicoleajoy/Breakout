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
    
    color c = color(0);
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
  void update() {
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
  void draw() {
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
  void keyPressed() {
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
  void keyReleased() {
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
