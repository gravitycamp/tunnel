/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/377846*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/*
Heart beat
  
Controls:
- Left-click explode heart.
- Right-click reset heart.
- Up & down arrow keys to change amount of points (hold down if it's too slow)
  
Inspired by Ilyas Shafigin's Heart Wings sketch (http://www.openprocessing.org/sketch/377004)
Author: Jason Labbe
Site: jasonlabbe3d.com
*/


// Global variables
ArrayList<Particle> allParticles = new ArrayList<Particle>();
float pulsateMult = 0;
PVector explosionPos;
PVector gravity = new PVector(0, 0.1, 0);
float angleSteps = 2.5;


class Particle {
  PVector pos = new PVector(0, 0, 0);
  PVector vel = new PVector(0, 0, 0);
  int zDepth = 0;
  float rotation = 45;
  float spinRate = 0;
  color pixelColor;
  float explosionMult = 0;
  boolean active = false;

  Particle(float x, float y, float z) {
    this.pos.set(x, y, z);
    this.explosionMult = random(0.5, 8.0);
    this.spinRate = random(-0.25, 0.25);
  }

  void draw() {
    strokeWeight(2);
    stroke(this.pixelColor);
    
    pushMatrix();
    translate(this.pos.x, this.pos.y, this.pos.z);
    rotateZ(this.rotation);
    if (this.active) {
      rotateX(frameCount*this.spinRate);
    }
    rect(0, 0, 3, 3);
    popMatrix();
    
    if (this.active) {
      this.rotation += this.spinRate;
    }
  }
}


// Code for getting heart shape by Ilyas Shafigin
void createHeart(int heartSize, int depthAmount, int spacing) {
  frameCount = 0;
  allParticles.clear();

  for (int z = 0; z < depthAmount; z++) {
    for (float angle = -90; angle < 90; angle += angleSteps) {
      float t = angle*2.0;
      float x = 16*pow(sin(radians(t)), 3);
      float y = -13*cos(radians(t)) + 5*cos(radians(2*t)) + 2*cos(radians(3*t)) + cos(radians(4*t));

      Particle particle = new Particle(x*heartSize+width/2, y*heartSize+height/2, z*spacing);
      particle.pixelColor = color(30+z*20, 0, z*5);
      particle.zDepth = z+1;
      allParticles.add(particle);
    }
  }
}


void setup() {
  size(640, 640, P3D);
  noFill();
  rectMode(CENTER);

  explosionPos = new PVector(0, 0, 0);

  createHeart(6, 15, 10);
}


void draw() {
  background(30);

  for (Particle particle : allParticles) {
    if (particle.active) {
      // Particle is dynamic
      particle.vel.add(gravity);

      if (particle.pos.x == width/2) { 
        // This is just so there aren't any particles going straight down the middle
        particle.vel.x += random(-0.15, 0.15);
      }

      particle.pos.add(particle.vel);
    } else {
      // Particle is passive and should keep pulsating
      PVector dir = new PVector(-width/2, -height/2, 0);
      dir.add(particle.pos);
      dir.normalize();
      dir.mult(pulsateMult*particle.zDepth*0.05+cos(frameCount*0.05)*1.5);
      particle.pos.add(dir);
    }

    particle.draw();
  }

  pulsateMult = sin(frameCount*0.05)*1.5;

  // Display text
  stroke(255);
  textAlign(CENTER);
  textSize(10);
  text("Left-click to break my heart </3 then right-click to mend it <3", width/2, height-30, -10);
}


void mousePressed() {
  // Explode heart
  if (mouseButton == LEFT) {
    explosionPos.x = -mouseX;
    explosionPos.y = -mouseY;

    for (Particle particle : allParticles) {
      if (particle.active) {
        continue;
      }

      PVector dir = new PVector(particle.pos.x, particle.pos.y, particle.pos.z);
      dir.add(explosionPos);
      dir.normalize();
      dir.mult(particle.explosionMult);
      particle.vel.add(dir);
      particle.active = true;
    }
  } else if (mouseButton == RIGHT) {
    createHeart(6, 15, 10);
  }
}


void keyPressed() {
  if (keyCode == 38) {
    // Up arrow to increate points
    angleSteps = max(1.0, angleSteps-0.5);
    createHeart(6, 15, 10);
  } else if (keyCode == 40) {
    // Down arrow to decrease points
    angleSteps = min(5.0, angleSteps+0.5);
    createHeart(6, 15, 10);
  }
}