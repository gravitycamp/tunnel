import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class SinusWave extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  PVector xx, yy, fcx, fcy, sx, sy;
  int seed = 0, cell = 6;
  Boolean randomized = true;

  public SinusWave(Tunnel t, int w, int h, String p) {
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

    strokeWeight(3);
    initialize();
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
      ///      track();
      //    beat.detect(audio.mix);
      //   if (beat.isOnset())
      //    generateColors();

//      background(0);
      noStroke();
      fill(45, 23);
      rect(0, 0, width, height);

      randomSeed(seed);

      float fc = (float)(frameCount+1);
      stroke(255, 79, 0);
      float x, y, maxx, minx, maxy, miny;
      for (float i = 0; i < width; i += cell)
      {
        for (float j = 0; j < height; j += cell)
        {
          x = randomized ? random(width) : i;
          y = randomized ? random(height) : j;
          maxx =  xx.x * (1 + sin(y/sx.x + fc/fcx.x)) + width;
          minx = -xx.y * (1 + sin(y/sx.y + fc/fcx.y));
          x = map(x, 0, width, minx, maxx);
          if (x > 0 && x < width) {
            maxy =  yy.x * (1 + sin(x/sy.x + fc/fcy.x)) + height;
            miny = -yy.y * (1 + sin(x/sy.y + fc/fcy.y));
            y = map(y, 0, height, miny, maxy);
            if (y > 0 && y < height)
              point(x, y);
          }
        }
      }
    }
    }

    void initialize() {
      seed += frameCount;
      randomSeed((int)random(1000) + frameCount);
      xx = new PVector(random(10, 110), random(10, 110));
      yy = new PVector(random(10, 110), random(10, 110));
      sx = new PVector(random(30, 70), random(30, 70));
      sy = new PVector(random(30, 70), random(30, 70));
      fcx = new PVector(random(20, 70), random(20, 70));
      fcy = new PVector(random(20, 70), random(20, 70));
    }

    public void mousePressed() {
      initialize();
    }

    public void keyPressed() {
      randomized = !randomized;
    }


    // Additional Classes
  }