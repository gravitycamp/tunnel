import processing.core.*; //<>//
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Burn extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
  
  //config constants
  int fumes = 40; //more fumes incrament radius of largest fumes
  int numFlames = 2;
  int FinalnumFlames = 40;
  int maxCounter = 20;  //the counter that increaments to this number each frame before incramenting framerate
  int rate = 5;  //initial framerate
  int finalFrameRate = 20;

  int counter = 0;  
  int[] xpos = new int[fumes];
  int[] ypos = new int[fumes];
  flame theFlames[];


  public Burn(Tunnel t, int w, int h, String p) {
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
    background(0);
    smooth();
    frameRate(rate);
    for (int i = 0; i < fumes; i++) {
      xpos[i] = 0;
      ypos[i] = 0;
    }
    theFlames = new flame[FinalnumFlames];
    for (int i = 0; i < FinalnumFlames; ++i) {
      theFlames[i] = new flame();
    }
  }
  
  public void draw() {
    synchronized (Tunnel.class) {
      background(0);
      for (int i = 0; i < numFlames; ++i) {
        theFlames[i].update();
      }
      counter++;
      if (counter == maxCounter)
      {
        counter = 0;
        numFlames++;
        if (numFlames == FinalnumFlames)
          numFlames--;
        frameRate(++rate);
        if (rate == finalFrameRate)
          rate--;
      }
    }
  }
  // Additional Classes
  class flame {
    flame() {
    };
    void update() {
      for (int i = 0; i < fumes-1; i++) {
        xpos[i] = xpos[i+1];
        ypos[i] = ypos[i+1];
        ypos[i] -= 5;
        xpos[i] += random (-10, 10);
      }
      xpos[xpos.length-1] = (int)random(width);
      ypos[ypos.length-1] =height- (int)random((float).5*height);

      for (int i = 0; i < fumes; i++) {
        noStroke();
        fill(230, 200-3*i, 20, 200); //-i*350
        ellipse(xpos[i], ypos[i], i, i);
      }
    }
  }
}