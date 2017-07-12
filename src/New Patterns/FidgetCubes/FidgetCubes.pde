/*
Fidget cubes

Uses the mouse's current and previous positions to get a vector that spins the cubes.

Seems to perform better in processing.js than p5.js.

Controls:
	- Move the mouse to spin the cubes.

Author:
  Jason Labbe

Site:
  jasonlabbe3d.com
*/


float boxSize = 25;
ArrayList<Cube> allCubes = new ArrayList<Cube>();


class Cube {
  
  PVector pos;
  PVector rot;
  float spinX = 0;
  float spinY = 0;
  float hueValue = 0;
  
  Cube(float x, float y) {
    this.pos = new PVector(x, y);
    this.rot = new PVector(0, 0);
  }
}


void setup() {
  size(800, 600, P3D);
  
  colorMode(HSB, 255);
  
  for (float y = 0; y < height; y += boxSize*1.5) {
    for (float x = 0; x < width; x += boxSize*1.5) {
      allCubes.add(new Cube(x, y));
    }
  }
}


void draw() {
  background(255);
  
  for (int i = 0; i < allCubes.size(); i++) {
    Cube c = allCubes.get(i);
    
    float d = dist(mouseX, mouseY, c.pos.x, c.pos.y);
    
    if (d < boxSize*4) {
      PVector motion = new PVector(pmouseX, pmouseY);
      motion.sub(mouseX, mouseY);
      c.spinX += motion.x*0.1;
      c.spinY += motion.y*0.1;
    }
    
    c.rot.x += c.spinX;
    c.spinX *= 0.97;
    
    c.rot.y += c.spinY;
    c.spinY *= 0.97;
    
    c.hueValue += (abs(c.spinX)+abs(c.spinY))*0.05;
    if (c.hueValue > 255) {
      c.hueValue = 0;
    }
    
    pushMatrix();
    translate(c.pos.x, c.pos.y, 0);
    rotateY(radians(-c.rot.x));
    rotateX(radians(c.rot.y));
    
    fill(c.hueValue, 255, 255);
    box(boxSize);
    popMatrix();
  }
}