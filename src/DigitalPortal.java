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

  float verm = 150;
  float verms = 2;

  float azul = 150;
  float azuls = (float)0.5;

  float verd = 150;
  float verds = (float)1.5;

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
      //     generateColors();
      noStroke();
      noCursor();
      translate(width/2, height/2);
      fill(verm, verd, azul);

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

      verm += verms;
      if (verm >= 255 || verm <= 0) {
        verms = verms*-1;
      }
      if (verm <= 0) {
        verms = random(1, 5);
      }

      verd += verds;
      if (verd >= 255 || verd <= 0) {
        verds = verds*-1;
      }
      if (verd <= 0) {
        verds = random(1, 5);
      }

      azul += azuls;
      if (azul >= 255 || azul <= 0) {
        azuls = azuls*-1;
      }
      if (azul <= 0) {
        azuls = random(1, 5);
      }
    }
  }

  public void mouseClicked() {
    dir = dir*-1;
  }



  // Additional Classes
}