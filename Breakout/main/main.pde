import controlP5.*;

//Objects declared to create the UI (ControlP5) and GameState
ControlP5 cp5;
GameState gs;

PImage star, shroom;

//Initalize window settings, objects, etc. to start the game 
void setup() {
  background(223, 185, 185);
  size(800, 800);
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
void draw() {
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
void keyPressed() {
  gs.keyPressed();
}


//Handles when keyboard input is released 
void keyReleased() {
  gs.keyReleased();
}


// Reset score and game elements
void Reset() {
  //println("Clicked reset");
  gs = new GameState();
}
