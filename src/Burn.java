import java.util.*;
import processing.core.*;


class Burn extends PApplet {

  Tunnel tunnel;
  int width;
  int height;
  String position = "Tunnel";

  static final int NbFlames = 50;
  static final int NbFumesPerFlame = 40;
  ArrayList <Flame> flames = new ArrayList<Flame>();

  static final int NbRows = 11;
  static final int NbColumns = 15;
  static final int NbPixels = NbRows*NbColumns;
  ArrayList <Pixel> pixels = new ArrayList<Pixel>();

  int frameCounter = 0;
  int currentPixelIdx = 0;

  public Burn (Tunnel t, int w, int h, String p) {
    tunnel = t;
    width = w;
    height = h;
    position = p;
    System.out.println("Width    : " + h);
    System.out.println("Height   : " + w);
    System.out.println("Position : " + p);
  }

  public void settings () {
    size(width, height);
  }

  public void setup () {
    //background(0);
    smooth();
    frameRate(20);
    // Create the flames collection
    for (int i = 0; i < NbFlames; i++) {
      Flame flame = new Flame(NbFumesPerFlame);
      flames.add(flame);
    }
    // Create the pixels collection
    int pWidth = width/NbColumns;
    int pHeight = 1+height/NbRows;
    for (int i = 0; i < NbColumns; i++) {
      for (int j = 0; j < NbRows; j++) {
        Pixel pixel = new Pixel(
            i*pWidth + pWidth/2,
            j*pHeight + pHeight/2,
            pWidth, pHeight);
        pixels.add(pixel);
      }
    }
    Collections.shuffle(pixels);
  }

  public void draw() {
    synchronized (Tunnel.class) {
      background(0);
      frameCounter++;
      // Process flames
      for (int i = 0; i < NbFlames; i++) {
        flames.get(i).update();
      }
      // counter++;
      // if (counter == maxCounter)
      // {
      //   counter = 0;
      //   numFlames++;
      //   if (numFlames == FinalnumFlames)
      //     numFlames--;
      //   frameRate(++rate);
      //   if (rate == finalFrameRate)
      //     rate--;
      // }
      // Process pixels
      boolean flag = false;
      for (int i = 0; i < NbPixels; i++) {
        if (frameCounter%10 == 0 && currentPixelIdx < NbPixels && flag == false) {
          pixels.get(currentPixelIdx++).markToDie();
          flag = true; // One pixel marked as dead for current frame
        }
        pixels.get(i).update();
      }
    }
  }

  // Flame class
  class Flame {

    int fumes;
    int[] xcFumes;
    int[] ycFumes;

    Flame (int fumesi) {
      fumes = fumesi;
      xcFumes = new int[fumes];
      ycFumes = new int[fumes];
      for (int i = 0; i < fumes; i++) {
        xcFumes[i] = 0;
        ycFumes[i] = 0;
      }
    };

    void update () {
      for (int i = 0; i < fumes-1; i++) {
        xcFumes[i] = xcFumes[i+1];
        ycFumes[i] = ycFumes[i+1];
        ycFumes[i] -= 5;
        xcFumes[i] += random(-10, 10);
      }
      xcFumes[fumes-1] = (int)random(width);
      ycFumes[fumes-1] = height-(int)random(height/2);
      for (int i = 0; i < fumes; i++) {
        noStroke();
        fill(230, 200-3*i, 20, 200);
        ellipse(xcFumes[i], ycFumes[i], i, i);
      }
    }
  }

  // Pixel class
  class Pixel {

    int xc, yc;
    int wc, hc;
    int width;
    int height;

    final static int sALIVE     = 1;
    final static int sFULLWHITE = 2;
    final static int sCOLLAPSE  = 3;
    final static int sFULLBLACK = 4;

    int frame = 0;
    int state = sALIVE;

    Pixel (int xi, int yi , int wi, int hi) {
      xc = xi;
      yc = yi;
      wc = wi;
      hc = hi;
      width = wi;
      height = hi;
    };

    void markToDie () {
      nextState();
    }

    private void nextState () {
      state++;
      frame = 0;
    }

    void update () {
      frame++;
      rectMode(CENTER);
      switch (state) {
        case sALIVE:
          noFill();
          stroke(255);
          rect(xc, yc, width, height);
          break;
        case sFULLWHITE:
          fill(255);
          stroke(255);
          rect(xc, yc, width, height);
          if ( frame==5 ) nextState();
          break;
        case sCOLLAPSE:
          // Outer
          fill(0);
          stroke(0);
          rect(xc, yc, width, height);
          // Inner
          fill(255);
          stroke(255);
          rect(xc, yc, wc--, hc--);
          if ( wc==0 || hc==0 ) nextState();
          break;
        case sFULLBLACK:
          fill(0);
          stroke(0);
          rect(xc, yc, width, height);
          break;
        default:
          fill(128);
          stroke(255);
          rect(xc, yc, width, height);
      }
    }
  }
}