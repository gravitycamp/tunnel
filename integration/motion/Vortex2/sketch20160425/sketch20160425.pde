/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/348641*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/**
* psychedelic stamps
*
* @author aa_debdeb
* @date 2016/04/25
*/

void setup(){
  size(640, 640);
  background(255);
  colorMode(HSB, 360, 100, 100);
  noStroke();
}

void draw(){
  if(mousePressed){
    float diameter = sqrt(sq(pmouseX - mouseX) - sq(mouseY - mouseY)) * 3;
    float startHue = (frameCount * 10) % 360;
    for(float v = 1.0; v >= 0.0; v -= 0.01){
      float h = startHue - 360 * v;
      if(h < TWO_PI){h += 360;}
      fill(h, 100, 100);
      float d = diameter * v;
      ellipse(mouseX, mouseY, d, d);
    }
  } 
}

