import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class LetItFlow extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
  int numParticles = 1000;
  Particle[] particles = new Particle[numParticles];
  float mult = 150;

  public LetItFlow(Tunnel t, int w, int h, String p) {
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
    background(0);
    frameRate(10000);
    smooth();
    colorMode(HSB);
    for (int i = 0; i < particles.length; i++) {
      particles[i] = new Particle();
    }
    fill(0, 10);
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
      //     track();
      //    beat.detect(audio.mix);
      //   if (beat.isOnset())
      //    generateColors();

      rect(-1, -1, width+1, height+1);
      for (Particle p : particles) {
        p.update();
        p.show();
      }
    }
  }


  // Additional Classes
  class Particle {
    PVector loc, vel;
    float hu;
    float xOff;
    float yOff;
    float xInc = (float)0.1;
    float yInc = (float)0.03;
    float lmult;

    Particle() {
      loc = new PVector(random(width), random(height));
      vel = new PVector();
      hu = 0;
      xOff = 0;
      yOff = 0;
    }

    void update() {
      vel.x = noise((float)(xOff/10+loc.y/150))-(float)0.5;
      vel.y = noise((float)(yOff/10+loc.x/150))-(float)0.5;
      xInc = map(noise(xInc), 0, 1, 0, (float)0.15);
      yInc = map(noise(yInc), 0, 1, 0,(float)0.03);
      mult = 3*map(noise(mult), 0, 1, 0,(float)90);


      vel.mult(mult);
      loc.add(vel);

      hu +=10*xInc;

      if (lmult == mult)mult++;
      if (hu > 255)hu = 0;
      if (loc.x<0)loc.x+=width;
      if (loc.x>width)loc.x-=width;
      if (loc.y<0)loc.y+=height;
      if (loc.y>height)loc.y-=height;

      lmult = mult;

      xOff +=1.25*xInc;
      yOff +=2.7*yInc;
    }

    void show() {
      strokeWeight((float)0.4);
      stroke(hu, 255, 255, 40);
      line(loc.x, loc.y, loc.x-vel.x, loc.y-vel.y);
    }
  }
}