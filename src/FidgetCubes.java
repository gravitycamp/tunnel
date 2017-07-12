import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class FidgetCubes extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  float boxSize = 25;
  ArrayList<Cube> allCubes = new ArrayList<Cube>();

  public FidgetCubes(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
    audio = tunnel.in;
  }

  public void settings()
  {
    size(width, height); //need size(250, 250, P3D); or size(250, 250, OPENGL), but this causes an error;
  }

  public void setup() {
    minim = new Minim(this);
    beat = new BeatDetect();

    colorMode(HSB, 255);

    for (float y = 0; y < height; y += boxSize*1.5) {
      for (float x = 0; x < width; x += boxSize*1.5) {
        allCubes.add(new Cube(x, y));
      }
    }
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
      track();
//beat.detect(audio.mix);
  //    if (beat.isOnset())
 //       generateColors();

      background(255);

      for (int i = 0; i < allCubes.size(); i++) {
        Cube c = allCubes.get(i);

        float d = dist(trackX, trackY, c.pos.x, c.pos.y);

        if (d < boxSize*4) {
          PVector motion = new PVector(ptrackX, ptrackY);
          motion.sub(trackX, trackY);
          c.spinX += motion.x*0.1;
          c.spinY += motion.y*0.1;
        }

        c.rot.x += c.spinX;
        c.spinX *= 0.97;

        c.rot.y += c.spinY;
        c.spinY *= 0.97;

        c.hueValue += (abs(c.spinX)+abs(c.spinY))*0.05;
        if (c.hueValue > 255) {
          c.hueValue = 0;
        }

        pushMatrix();
        translate(c.pos.x, c.pos.y, 0);
        rotateY(radians(-c.rot.x));
        rotateX(radians(c.rot.y));

        fill(c.hueValue, 255, 255);
        box(boxSize);
        popMatrix();
      }
    }
  }

  // Additional Classes
  class Cube {

    PVector pos;
    PVector rot;
    float spinX = 0;
    float spinY = 0;
    float hueValue = 0;

    Cube(float x, float y) {
      this.pos = new PVector(x, y);
      this.rot = new PVector(0, 0);
    }
  }
}