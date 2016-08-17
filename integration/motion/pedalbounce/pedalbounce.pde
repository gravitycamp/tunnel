/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/377180*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
void setup() {
  size(300, 300);
  noStroke();
}

void draw() {
  background(0);
  translate(width/2, height/2);
  for (float q=30; q>=0; q-=0.1) {
    float r = (6*q+frameCount)%180;
    float f = map(r, 0, 180, 255, 0);
    fill(int((255-f)*sin(radians(r*mouseY))), int(255*sin(radians(r* mouseY))), int(f/2*sin(radians(r*mouseY))));
    for (int i=0; i<360; i+=60) {
      float x = sin(radians(i))*r;
      float y = cos(radians(i))*r;
      float l = sin(radians(((mouseX) /30 )*r-11*frameCount))*r/2*sin(radians(r));
      float s = 3*sin(radians(r));
      
      pushMatrix();
      translate(x, y);
      rotate(radians(-i));
      if (s > 0) ellipse(l, 0, s, s);
      popMatrix();
    }
  }
}