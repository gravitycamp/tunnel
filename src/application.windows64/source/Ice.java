import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Ice extends PApplet {

  int width;
  int height;
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;

  // Instance Variables
  float[][] z1, v1, z2, v2;

  public Ice(Tunnel t, int w, int h) {
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
    colorMode(RGB, 10);
    background(0);
    z1 = new float[width][height];
    v1 = new float[width][height];
    z2 = new float[width][height];
    v2 = new float[width][height];
    loadPixels();
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
        v1[(int)trackX][(int)trackY] = (float)0.3;
        v2[(int)trackX][(int)trackY] = (float)0.3;
      } else {
        v1[(int)trackX][(int)trackY] = randomGaussian();
        v2[(int)trackX][(int)trackY] = randomGaussian();
      }

      for (int x = 1; x < width-1; x++) {
        for (int y = 1; y < height-1; y++) {
          v1[x][y] += (z1[x-1][y] + z1[x+1][y] + z1[x][y-1] + z1[x][y+1]) * 0.25 - z1[x][y];
        }
      }

      for (int x = 1; x < width-1; x++) {
        for (int y = 1; y < height-1; y++) {
          v2[x][y] += (z2[x-1][y] + z2[x+1][y] + z2[x][y-1] + z2[x][y+1] - z1[x][y]) * 0.25 - z2[x][y];
        }
      }

      for (int x = 1; x < width-1; x++) {
        for (int y = 1; y < height-1; y++) {
          z1[x][y] += v1[x][y];
          z1[x][y] = constrain(z1[x][y], -1, 1);
        }
      }

      for (int x = 1; x < width-1; x++) {
        for (int y = 1; y < height-1; y++) {
          z2[x][y] += v2[x][y];
          z2[x][y] = constrain(z2[x][y], -1, 1);
          pixels[width*y+x] = color(v1[x][y] + 1, v2[x][y] + 1, 2);
        }
      }

      updatePixels();
    }
  }

  // Additional Classes
}