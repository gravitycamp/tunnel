/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/110684*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
void setup(){
  // Sets up the background and canvas size
  background(0);
  size(400, 400);
}

void draw(){
  // Sets the stroke colour
  stroke(random(255),random(255),random(255));
  // Draws the pen tip
  line((pmouseX + random(50)), (pmouseY + random(50)), (mouseX + random(80)), (mouseY + random(70)));
}