import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class SketchName extends PApplet {

  int width;
  int height;
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  // Instance Variables

  public SketchName(Tunnel t, int w, int h) {
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
    }
  }
  
  // Additional Classes
}
