import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Spotlight extends PApplet {

  int width;
  int height;
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  // Instance Variables

  public Spotlight(Tunnel t, int w, int h) {
      width = w;
      height = h;
      tunnel = t;
      audio = tunnel.in;
  }

  public void settings()
  {
      size(width, height);
  }

  public void setup() {
    minim = new Minim(this);
    beat = new BeatDetect();

    // Additional Setup
    smooth();
  }

  float trackX = 0;
  float trackY = 0;
  float trackZ = 0;

  public void track() {
   if(Main.kinect != null) {
       Main.kinect.update();
       trackX = (float)width * Main.kinect.RightHandDepthRatio;
       trackY = (float)height * Main.kinect.RightHandRaisedRatio;
       trackZ = 0;
   } else {
       trackX = mouseX;
       trackY = mouseY;
   }
  }

  public void draw() {
    synchronized (Tunnel.class) {
      track();
      beat.detect(audio.mix);
      if(beat.isOnset()) {
      } else {
      }


        int c1 = (int)blendColor(color(0,0,255), color(random(255),random(255),random(255)), ADD);//random(0,255);
        int c2 = (int)blendColor(color(0,0,255), color(random(255),random(255),random(255)), ADD);//random(0,255);
        int c3 = (int)blendColor(color(0,0,255), color(random(255),random(255),random(255)), ADD);//random(0,255);
        int s = (int)random(0,tunnel.getAudioAverage() * 15);
        int t = (int)random(0,255);

        fill(0,0,0,50);
        noStroke();
        rect(0,0,600,600);
        fill(c1,c2,c3,t);
        ellipse(trackX,trackY,s,s);

    }
  }

  // Additional Classes
}