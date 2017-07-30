import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class DigitalPortal extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
  float rota;
  float dir;

  float R= 100;
  float Rs = 2;

  float B= 30;
  float Bs = (float)0.5;

  float G= 20;
  float Gs = (float)1.5;

  public DigitalPortal(Tunnel t, int w, int h, String p) {
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
    minim = new Minim(this);
    beat = new BeatDetect();

    background(255); 
    dir = 1;
    frameRate(60);
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
        trackY = (float)height * Main.kinect.RightHandSideRatio;
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
      //track();
      //     beat.detect(audio.mix);
      //    if (beat.isOnset())
      noStroke();
      noCursor();
      translate(width/2, height/2);
      fill(constrain(R,0,150), constrain(G,0,150), constrain(B,0,150));

      //for(int i=0;;){}
      pushMatrix();
      rotate(rota);
      rect(0, 0, 20, 20, 5);
      popMatrix();

      for (int i=1; i<=60; i++) {
        if (i%2==0) {
          pushMatrix();
          //rotate(rota+i*2);
          rotate((rota*i*4)*dir);
          translate(i*25-i*6, 0);
          rect(0, 0, 20, 20, 5);
          popMatrix();
        } else {
          pushMatrix();
          //rotate(-rota-i*2);
          rotate((rota+i*4)*-dir);
          translate(i*25-i*6, 0);
          rect(0, 0, 20, 20, 5);
          popMatrix();
        }
      }
      rota += 0.05;
      R+= Rs;
      if (R>= 255 || R<= 0) {
        Rs = Rs*-1;
      }
      if (R<= 0) {
        Rs = random(1, 5);
      }
      G+= Gs;
      if (G>= 255 || G<= 0) {
        Gs = Gs*-1;
      }
      if (G<= 0) {
        Gs = random(1, 5);
      }

      B+= Bs;
      if (B>= 255 || B<= 0) {
        Bs = Bs*-1;
      }
      if (B<= 0) {
        Bs = random(1, 5);
      }
    }
  }

  public void mouseClicked() {
    dir = dir*-1;
  }



  // Additional Classes
}