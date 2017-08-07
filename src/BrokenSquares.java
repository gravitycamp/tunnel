import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class BrokenSquares extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  Minim minim;
  BeatDetect beat;
  int rotAngle = 0;
  float offset = 0;
  int H = 40;
  int dH =1;

  public BrokenSquares(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
  }

  public void settings()
  {
    int scale = 1;
    size(width*scale, height*scale);
  }

  public void setup() {
    minim = new Minim(this);
    beat = new BeatDetect();
    frameRate(60);
    colorMode(HSB);
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
      //   track();
      //    beat.detect(audio.mix);
      //   if (beat.isOnset())
      //     generateColors();

    //  frameRate(offset+10);
      background(0);
      rotate(radians(10));
      H+=dH;
      if ((H <= 2) || (H >= 250))  // if out of bounds
        dH = - dH; // swap direction  
    
      starField(offset);
      rotAngle += 1;
      offset += 1;
      if (offset == 56) {
        offset = 0;
      }
    }
  }
  void starField(float offSet) {
    int w = (int)(width/30);
    int l = (int)(height/30);
    
    for (int i=0; i<w; i++) {
      for (int j = 0; j<l; j++) {
        if ((i+j)%2==0) {
          star1((float)(40*1.4*i-offSet),(float)( 40*1.4*j-offSet-l*6));
        } else {
          star2((float)(40*1.4*i-offSet), (float)(40*1.4*j-offSet-l*6));
        }
      }
    }
  }

  void star1(float x, float y) {
    pushMatrix();
    translate(x, y);
    rotate(radians(rotAngle));
    rectMode(CENTER);
    //rect(0,0,40,40);
    pushMatrix();
    for (int k=0; k<4; k++) {
      if (k%2==0) {
        fill(250-H, 150, 150);
      } else {
        fill(150,H, 50);
      }
      stroke(255);
      triangle(0, 0, -40, -30, 0, -30);
      rotate(radians(90));
    }
    popMatrix();
    popMatrix();
  }

  void star2(float x, float y) {
    pushMatrix();
    translate(x, y);
    rotate(radians(rotAngle));
    rectMode(CENTER);
    //rect(0,0,40,40);
    pushMatrix();
    for (int k=0; k<4; k++) {
      if (k%2==1) {
        fill(250-H, 150, 150);
      } else {
        fill(150,H, 50);
      }
      stroke(255);
      triangle(0, 0, -40, -30, 0, -30);
      rotate(radians(90));
    }
    popMatrix();
    popMatrix();
  }
    
}