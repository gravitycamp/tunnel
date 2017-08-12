import processing.core.*; //<>// //<>// //<>//
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class PhaseFlow extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  ArrayList <Mover> particles;
  float time=0;
  int bgcolor;

  int baseColor = 0;
  int direction = 1;


  public PhaseFlow(Tunnel t, int w, int h, String p) {
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
    colorMode(HSB);
    bgcolor=color(random(5), random(5), random(5), 10);

    // set the background color
    background(bgcolor);

    // smooth edges
    smooth();

    // limit the number of frames per second
    frameRate(30);

    // set the width of the line. 
    strokeWeight(12);

    particles = new ArrayList();

    for (int i = 0; i <300; i++)
    {
      Mover m = new Mover();
      particles.add (m);
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
      //   beat.detect(audio.mix);
      //   if (beat.isOnset())
      //     generateColors();

      //background(0);

      time=time+1;
      baseColor = baseColor+1;
      if (time>100) {
        bgcolor=color(random(2), random(2), random(2), 10);
        time=0;
        //direction=-direction;
      }
      fill(bgcolor);
      rect(0, 0, width, height);


      int i = 0;
      while (i < particles.size () )
      {
        Mover m = particles.get(i);
        if (random(500)<2) {
          m.setRandomValues();
        }
        m.move((float)0.03*direction);
        m.disp(); 
        i = i + 1;
      }
    }
  }

  public void mouseClicked() {
    bgcolor=color(random(30), random(30), random(30), 10);
  }


  // Additional Classes
  class Mover
  {
    PVector location; 
    float ellipseSize;
    int col;

    Mover () // Constructor
    {
      setRandomValues();
    }

    Mover (float x, float y, int c) // Constructor
    {
      setValues (x, y, c);
    }

    // SET ---------------------------

    void setRandomValues ()
    {
      location = new PVector (random (width), random (height));
      ellipseSize = 1; 
      colorMode(HSB);
      col=color((random(150, 180)+baseColor) % 255, random(100, 255), 255, 250);
    }
    void setValues (float x, float y, int c)
    {
      location = new PVector (x, y);
      ellipseSize = random (8, 20);   
      col=c;
    }


    // MOVE -----------------------------------------

    void move (float dt)
    {
      PVector newLoc = new PVector(location.x, location.y);
      PVector shift = new PVector();
      float x = location.x-width/2;
      float y = location.y-height/2;
      shift.x = y;
      shift.y = -sin(x*TWO_PI/(width))*height/8;
      shift.normalize();
      shift.mult(100*dt);
      location.add(shift);
      location.x=mod1(location.x);    


      //newLoc=flow(location,dt);

      //location.x=newLoc.x;
      //location.y=newLoc.y;
      colorMode(HSB);
      col=color((hue(col)+1) % 255, saturation(col), brightness(col), 150);
    }

    // DISPLAY ---------------------------------------------------------------

    void disp ()
    {
      noStroke();
      fill (col);
      ellipse (location.x, location.y, ellipseSize, ellipseSize);
    }
  }

  PVector flow(PVector loc, float dt) {
    PVector output = new PVector(loc.x, loc.y);
    PVector shift = new PVector();
    shift.x=loc.y;
    shift.y=-loc.x;
    shift.normalize();
    shift.mult(dt);
    output.add(shift);
    return(output);
  }


  float mod1(float x) {
    float x1=x;
    while (x1>width) {
      x1=x1-width;
    }
    while (x1<0) {
      x1=x1+width;
    }
    return(x1);
  }
}