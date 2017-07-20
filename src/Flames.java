import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Flames extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
  float x =0;
  float y = 0;

  public Flames(Tunnel t, int w, int h, String p) {
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
    blendMode(ADD);
    stroke(200, 100, 50, 10);
    noFill();
  }


  public void draw() {
    synchronized (Tunnel.class) {

      for (int j=0; j<100; j++) {
        y=x/100;
        beginShape();
        for (int i=0; i<width; i++) {
          vertex(i, y);
          y=y+(float)((noise(y/100, i/100)-0.5)*4);
        }
        endShape();
        x=x+1;
      }
    }
  }
}