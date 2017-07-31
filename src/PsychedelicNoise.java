import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class PsychedelicNoise extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
  int counter =-1;
  PImage img1 = createImage(2*width, 2*height, RGB);
  PImage img2 = createImage(2*width, 2*height, RGB);
  int[] c;  // aray of colors
  long seed = 0x0;

  public PsychedelicNoise(Tunnel t, int w, int h, String p) {
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
    // the dimensions of the image are twice the dimentions of
    // the canvas to add antialiasing when the image is reduced
    // noise detail is changed to make the pattern smooth
    noiseDetail(1, (float)0.3);

    c = new int[255];  // the array has 255 colors, one color for each possible grayscale color
    frameRate(30);
    //noLoop();
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
      //track();
      //      beat.detect(audio.mix);
      //if (beat.isOnset())
      //      generateColors();
  
      if(counter==-1){
        img1 = GenerateImage();
        img2 = GenerateImage();
      }
      
      tint(255, 255 - counter+1);  // Apply transparency without changing color
      image(img1, 0, 0, width, height);  // the image is reduce to the size of the canvas to make it smooth
      tint(255, counter+1);  // Apply transparency without changing color
      image(img2, 0, 0, width, height);  // the image is reduce to the size of 
      counter+=7;
      
      if (counter>=254)
      {
        img1 = img2;
        img2 = GenerateImage();
        counter =0;
      }
    }
  }
  public void mousePressed() { 
    //noiseSeed((long)random(0xFFFFFF));
    println(seed);
    noiseSeed(seed++);  // edited to work on openProcessing beta
    redraw();
  }

  public PImage GenerateImage()
  {
    noiseSeed(seed++);  // edited to work on openProcessing beta
    PImage img = createImage(2*width, 2*height, RGB);
      for (int i = 0; i < c.length; i += 1) {
      c[i] = color(random(255), random(255), random(255));
    }

    // create pattern
    img.loadPixels();  
    float nx, ny, nz;
    float theta = 0, phi = 0;
    float R = 5, r = 1;
    for (int x = 0; x < img.width; x += 1) {
      for (int y = 0; y < img.height; y += 1) {
        // map x and y to angles between 0 and TWO_PI
        theta = map(x, 0, img.width, 0, TWO_PI);
        phi = map(y, 0, img.height, 0, TWO_PI);

        // calculate the parameters of noise with the equation of a torus
        // this is used to make the pattern seamless
        nx = (R+r*cos(phi))*cos(theta);
        ny = (R+r*cos(phi))*sin(theta);
        nz = r*sin(phi);

        // normalize noise parameters so that the de pattern has homogeneous dimensions
        nx = norm(nx, 0, R+r);
        ny = norm(ny, 0, R+r);
        nz = norm(nz, 0, r);

        // apply noise twice and use the equivalent color on the pallete
        try {
          img.pixels[x + y*img.width] = c[floor(255*noise(floor(255*noise(nx, ny, nz)), (float)0.1))];
        } 
        catch(Exception e) {
          //  System.out.println("got interrupted!");
        }
      }
    }
    img.updatePixels();
    return img;
  }
}