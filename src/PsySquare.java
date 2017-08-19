import processing.core.*;
import java.util.*;

class PsySquare extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  public PsySquare(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
  }

  public void settings()
  {
    size(width, height);
  }

  public void setup() {
    frameRate(70);
    noFill();
    stroke(0, 255, 150);
    rectMode(CENTER);
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
      //      track();
      //   background(0);

      background(20, 0, 50);
      translate(width/2, height/2);
      for (int i = 0; i < 360; i+=30) {
        float angle = sin(radians(i/2-frameCount*5))*15;
        if (angle > 1) {
          strokeWeight(angle);
        } else {
          strokeWeight(1);
        }
        pushMatrix();
        rotate(radians(i*angle/200));
        rect(0, 0, i*(float)1.0, i*(float)1.0);
        popMatrix();
      }
    }
  }
}