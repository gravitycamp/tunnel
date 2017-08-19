import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class Burn extends PApplet {

  Tunnel tunnel;
  String position;
  int width;
  int height;

  AudioInput audio;
  BeatDetect beat;
  float[] fftFilter;
  FFT fft;
  float StartTime=0;

  static final int NbFlames = 70;
  static final int NbFumesPerFlame = 10;
  ArrayList <Flame> flames = new ArrayList<Flame>();

  static int NbRows;
  static int NbCols;
  static int NbPixels;
  static final int NbRows_Ceil = 3;//note used
  static final int NbRows_Tunnel = 11; //note used
  
  static final int NbRows_Wall = 6;
  static final int NbCols_Tunnel = 24;
  ArrayList <Pixel> pixels = new ArrayList<Pixel>();

  float x = 0;
  float y = 0;

  int frameCounter = 0;
  int currentPixelIdx = 0;

  static final int CeilBurnTick = 50;
  static final int DeadPixelTick = 100;

  public Burn (Tunnel t, int w, int h, String p) {
    tunnel = t;
    width = w;
    height = h;
    position = p;
    audio = tunnel.in;
    fft = tunnel.fft;
    System.out.println(position + " : " + width + " x " + height);
    switch (position) {
      case "Wall":
      case "LWall":
        NbRows = NbRows_Wall;
        NbCols = NbCols_Tunnel;
      case "RWall":
        NbRows = NbRows_Wall;
        NbCols = NbCols_Tunnel;
        break;
      case "Ceil":
        NbRows = NbRows_Ceil;
        NbCols = NbCols_Tunnel;
        break;
      case "Tunnel":
      default:
        NbRows = NbRows_Tunnel;
        NbCols = NbCols_Tunnel;
    }
    NbPixels = NbRows*NbCols;
  }

  public void settings () {
    size(width, height);
  }

  public void setup () {
    smooth();
    background(0);
    frameRate(20);
                  StartTime = millis();
    switch (position) {
      case "Ceil":
        blendMode(ADD);
        stroke(200, 100, 50, 10);
        noFill();
        break;
      case "Wall":
      case "LWall":
      case "RWall":
      case "Tunnel":
      default:
        // Create the flames collection
        for (int i = 0; i < NbFlames; i++) {
          Flame flame = new Flame(NbFumesPerFlame);
          flames.add(flame);
        }
        // Create the pixels collection
        beat = new BeatDetect();
          beat.setSensitivity(300);  
    fftFilter = new float[fft.specSize()];

        int pWidth = width/NbCols;
        int pHeight = height/NbRows;
        for (int i = 0; i < NbCols; i++) {
          for (int j = 0; j < NbRows; j++) {
            Pixel pixel = new Pixel(
                i*pWidth + pWidth/2,
                j*pHeight + pHeight/2,
                pWidth-1, pHeight-1);
            pixels.add(pixel);
          }
        }
        Collections.shuffle(pixels);
    }
  }

  public void draw() {
    synchronized (Tunnel.class) {
      frameCounter++;
      switch (position) {
        case "Ceil":  //this never happens
          if (frameCounter > CeilBurnTick) {
            for (int j = 0; j < 100; j++) {
              y = x/100;
              beginShape();
              for (int i = 0; i < width; i++) {
                vertex(i, y);
                y = y + (float)((noise(y/100, i/100)-0.5)*4);
              }
              endShape();
              x++;
            }
          }
          break;
        case "Wall":
        case "LWall":
        case "RWall":
        case "Tunnel":
        default:
          background(0);
          // Process flames
          for (int i = 0; i < NbFlames; i++) {
            flames.get(i).update();
          }
          //filter(BLUR, 2);
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
          // Process flames on ceil
          // for (int j=0; j<100; j++) {
          //   y=x/100;
          //   beginShape();
          //   for (int i=0; i<width; i++) {
          //     vertex(i, y);
          //     y=y+(float)((noise(y/100, i/100)-0.5)*4);
          //   }
          //   endShape();
          //   x=x+1;
          // }
          // Process pixels
          beat.detect(audio.mix);
          
          for (int i = 0; i < fftFilter.length; i++) {
            fftFilter[i] = max(fftFilter[i], log(1 + fft.getBand(i)) * (float)(1 + i * 0.01));
          }


          boolean flag = true;
          println(StartTime - millis());
          if(frameCounter > DeadPixelTick && (millis()-StartTime > 250))
            flag = false;

 //         if (frameCounter > DeadPixelTick) {
                          
            //flag = beat.isOnset() ? false : true;
           // println(tunnel.getAudioAverage());
          //  flag = if(20>tunnel.getAudioAverage());
//        flag = !(fft.getBand(1) >200);
  //        }
          for (int i = 0; i < NbPixels; i++) {
            if (currentPixelIdx < NbPixels && flag == false) {
              pixels.get(currentPixelIdx++).markToDie();
              flag = true; // One pixel marked as dead for current frame
              StartTime = millis();

            }
            pixels.get(i).update();
          }
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
        xcFumes[i] += random(-2, 2);
        ycFumes[i] -= 5;
      }
      xcFumes[fumes-1] = (int)random(width);
      ycFumes[fumes-1] = height+fumes-(int)random(height/10);
      for (int i = 0; i < fumes; i++) {
        noStroke();
        fill(230, 190-6*i, 20, 200);
        ellipse(xcFumes[i], ycFumes[i]-i, i, i*2);
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
 //         rect(xc, yc, width, height);
          break;
        case sFULLWHITE:
          fill(255);
          stroke(255);
          rect(xc, yc, width, height);
          if ( frame==4 ) nextState();
          break;
        case sCOLLAPSE:
          // Outer
          fill(0);
          stroke(0);
          rect(xc, yc, width, height);
          // Inner
          fill(255);
          stroke(255);
          rect(xc, yc, wc, hc);
          wc -= 1; hc -= 1;
          if ( wc<=0 || hc<=0 ) nextState();
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