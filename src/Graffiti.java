import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Graffiti extends PApplet {

  int width;
  int height;
    String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  BeatDetect beat;

  // Instance Variables
  int numBalls = 500;
  int maxBalls = numBalls;
  int fps;
  boolean clearBG, doSmooth;
  int shapeType;
  float maxVelocity = 4, minAccel = (float)0.1, maxAccel = (float)0.2;
  int dy=1;

  Seeker[] ball = new Seeker[numBalls];

  public Graffiti(Tunnel t, int w, int h, String p) {
      width = w;
      height = h;
      tunnel = t;
        position = p;
      audio = tunnel.in;
  }

  public void settings()
  {
      size(width, height);
  }

  public void setup() {
    beat = new BeatDetect();
    background(0);
    frameRate(70);
    if(!IsTracking)
    {
      trackX = width/2;
      trackY = height/2;
    }
      
    // Additional Setup
    colorMode(HSB, 255);
    noStroke();
    clearBG = true;
    doSmooth = true;
    shapeType = 1;

    for(int i=0; i<numBalls; i++){
      ball[i] = new Seeker(new PVector(random(width), random(height)));
    }

    // numBalls adjusted to a sane default for web distribution
    numBalls = 500;
  }

  float trackX = 0;
  float trackY = 0;
  float trackZ = 0;
  boolean IsTracking = false;

  public void track() {
   if(Main.kinect != null) {
       Main.kinect.update();
       IsTracking = Main.kinect.IsTracking;

       switch (position) {
         case "Tunnel":
         case "Wall":
         case "Ceil":
         case "RWall":
           trackX = (float)width * Main.kinect.RightHandDepthRatio;
           trackY = (float)height * Main.kinect.RightHandRaisedRatio;
           trackZ = 0;
           break;
         case "LWall":
           trackX = (float)width * Main.kinect.LeftHandDepthRatio;
           trackY = (float)height * Main.kinect.LeftHandRaisedRatio;
           trackZ = 0;
           break;
       }
   } else {
    //trackX = mouseX;
    //trackY = mouseY;
   }
  }

  public void draw() {
    synchronized (Tunnel.class) {
      track();
      beat.detect(audio.mix);
      if(beat.isOnset()) {
         clearBG = false;
      } else {
         clearBG = true;
      }
      if(clearBG){
        background(0);
      }
      if(!IsTracking)
      {
        trackX = width/2;
        if ((trackY <= 0) || (trackY >= height))  // if out of bounds
          dy = - dy; // swap direction  
        trackY +=dy;
      }
      rectMode(CENTER);
      for(int i=0; i<numBalls; i++) {
        ball[i].seek(new PVector(trackX, trackY));
        ball[i].render();
      }
      float ballSize = (map(tunnel.getAudioAverage(), 0, 20, 5,15));
      ellipse(trackX,trackY,ballSize,ballSize);
    }
  }

  // Additional Classes
  class Seeker{
  PVector position;
  float accelRate, radius;
  PVector velocity = new PVector(0, 0);
  int fillColor;
  float rnd;

  public Seeker(PVector pos){
    position = pos;
    rnd = random(1);
    fillColor = color((int) (rnd*255), 180, 255);
  }

  public void seek(PVector target){
    accelRate = map(rnd, 0, 1, minAccel, maxAccel);
    target.sub(position);
    target.normalize();
    target.mult(accelRate);
    velocity.add(target);
    velocity.limit(maxVelocity);

    position.add(velocity);
  }

  public void render(){
    fill(fillColor);
    radius = sq(map(velocity.mag(), 0, maxVelocity, 4, 1));
    radius = radius/2;
    if(shapeType == 0){
      rect(position.x, position.y, radius, radius);
    }
    else{
      float audioAverage = tunnel.getAudioAverage()/50;
      ellipse(position.x, position.y, radius+audioAverage, radius+audioAverage);
    }
  }
}
}