import processing.core.*;
import java.util.*;
import ddf.minim.*;

class Agentes extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Ball theBalls[];
  int numBalls = 50;

  public Agentes(Tunnel t, int w, int h, String p) {
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
    frameRate(25);

    theBalls = new Ball[numBalls];
    for (int i = 0; i < numBalls; ++i) {
      float ballSize = constrain(20 + (noise(randomGaussian()) * 4), 1, 100)*14*(map(tunnel.getAudioAverage(), 0, 20, (float).5, (float).6))-140;
      theBalls[i] = new Ball(random(width-80)+40, random(height-80)+40, ballSize);
      theBalls[i].randomiseDirection();
    }
    background(0);
  }

  float trackX = 0;
  float trackY = 0;
  float ptrackX = 0;
  float ptrackY = 0;
  float trackZ = 0;

  public void track() {
    if (Main.kinect != null) {
      Main.kinect.update();
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
      ptrackX = trackX;
      ptrackY = trackY;
    } else {
      ptrackX = pmouseX;
      ptrackY = pmouseY;
      trackX = mouseX;
      trackY = mouseY;
    }
  }

  public void draw() {
    synchronized (Tunnel.class) {
      //    track();
      colorMode(RGB, 255, 255, 255);
      fill(color(0, 0, 0), 20); 
      rect(0, 0, width, height);
      colorMode(HSB, 100, 100, 100);
      
      for (int i = 0; i < numBalls; ++i) {
        //float ballSize = constrain(20 + (noise(randomGaussian()) * 4), 1, 100)*14*(map(tunnel.getAudioAverage(), 0, 20, (float).5, (float).6))-145;
        float ballSize = (map(tunnel.getAudioAverage(), 0, 20, 5,30));
     //   print("audio average ");
     //   println(tunnel.getAudioAverage());
     //   print("ball size ");
    //    println(ballSize);
        theBalls[i].size=ballSize;
        theBalls[i].move();
        theBalls[i].display();
      }
    }
  }  

  // Additional Classes
  class Ball {
    float x;       
    float y;        
    float size;     
    float speed;    
    float xdirection;
    float ydirection;
    float omega;    

    Ball(float x_, float y_, float size_) {
      x = x_;
      y = y_;
      size = size_;

      colorMode(HSB, 360, 100, 100);

      speed = 0;
      xdirection = 0;
      ydirection = 0;
      omega = 0;
    }

    void randomiseDirection() {
      speed = random(4,8);
      xdirection = random(360);
      ydirection = random(360);
      omega = randomGaussian() * (float)0.3;
    }

    void move() {
      float dx, dy; 
      dx = cos(radians(xdirection)*(noise(1)*80)) * speed;
      dy = sin(radians(ydirection)*(noise(1)*40)) * speed;
      x += dx;
      y += dy;
      xdirection += omega;
      ydirection += omega;
      checkBounds();
    }
    
    void checkBounds() {
      if (x <= 0 + size*2 || x >= width - size*2) {
        xdirection += 180;
        xdirection = xdirection % 360;
      }
      if ( y <= 0 + size*2 || y >= height - size*2) {
        ydirection += 180;
        ydirection = ydirection % 360;
      }
    }

    void display() {
      noStroke();
      if (xdirection<180|| ydirection > 180) {
        fill(350, 100, 100);
        ellipse(x, y, size, size);
        fill(350, 60, 100);
        ellipse(x, y, size/2, size/2);
        fill(350, 0, 100);
        ellipse(x, y, size/4, size/4);
      } else {
        fill(10, 100, 100);
        ellipse(x, y, size, size);
        fill(10, 50, 100);
        ellipse(x, y, size/2, size/2);
        fill(10, 10, 100);
        ellipse(x, y, size/4, size/4);
      }
    }
  }
}