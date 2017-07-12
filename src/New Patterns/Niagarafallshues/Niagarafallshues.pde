/*
Niagara falls hues

Uses tons of motion blur to get a falls effect.

Controls:
  - Move the mouse to interact with the particles.

Author:
  Jason Labbe

Site:
  jasonlabbe3d.com
*/

var allParticles = [];
var globalHue = 0;
var spawnPerFrame = 3;
var mouseSize = 100;


function Particle(x, y) {
  this.lastPos = new p5.Vector(x, y);
  this.pos = new p5.Vector(x, y);
  this.vel = new p5.Vector(0, 0);
  this.acc = new p5.Vector(0, 0);
  this.size = random(2, 20);
  this.h = globalHue;
}


function setup() {
  createCanvas(windowWidth, windowHeight);
  colorMode(HSB, 360);
  
  mouseX = width/2;
  mouseY = height/2;
}


function draw() {
  noStroke();
  fill(0, 5);
  rect(0, 0, width, height);
  
  for (var i = 0; i < spawnPerFrame; i++) {
  	allParticles.push(new Particle(random(width), 0));
  }
  
  for (var i = allParticles.length-1; i > -1; i--) {
    allParticles[i].acc.add(new p5.Vector(0, allParticles[i].size*0.01));
    
    // Quick check to avoid calculating distance if possible.
    if (abs(allParticles[i].pos.x-mouseX) < mouseSize) {
      d = dist(mouseX, mouseY, allParticles[i].pos.x, allParticles[i].pos.y);
      if (d < mouseSize) {
        var vec = new p5.Vector(mouseX, mouseY-mouseSize);
        vec.sub(new p5.Vector(allParticles[i].pos.x, allParticles[i].pos.y));
        vec.normalize();
        allParticles[i].vel.add(vec);

        allParticles[i].h += 1.5;
        if (allParticles[i].h > 360) {
          allParticles[i].h = 0;
        }
      }
    }
    
    allParticles[i].vel.add(allParticles[i].acc);
    allParticles[i].pos.add(allParticles[i].vel);
    allParticles[i].acc.mult(0);
    
    stroke(allParticles[i].h, 360, 360);
    strokeWeight(allParticles[i].size);
    line(allParticles[i].lastPos.x, allParticles[i].lastPos.y, 
         allParticles[i].pos.x, allParticles[i].pos.y);
    
    allParticles[i].lastPos.set(allParticles[i].pos.x, allParticles[i].pos.y);
    
    if (allParticles[i].pos.y > height || allParticles[i].pos.x < 0 || allParticles[i].pos.x > width) {
      allParticles.splice(i, 1);
    }
  }
  
  globalHue += 0.15;
  if (globalHue > 360) {
    globalHue = 0;
  }
}