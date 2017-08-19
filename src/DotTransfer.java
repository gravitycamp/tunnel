import processing.core.*;
import java.util.*;

class DotTransfer extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  float a = 30;
  
  public DotTransfer(Tunnel t, int w, int h, String p) {
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
    noStroke();
    fill(0, 255, 150);
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
      track();

      background(0);
      translate(width/2-25, height/2);
      for (int i = 0; i < 360; i+=a) {
        for (int q = -36; q < 36; q++) {
          float x = i/(a/6)+tan(radians(dist(i/(a/2), i/(a/2), 0, 0)+q*50+frameCount))*a;
          ellipse(x, q*10, 5, 5);
        }
      }
    }
  }
}