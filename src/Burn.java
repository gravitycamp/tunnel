import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;


class Burn extends PApplet {

  Tunnel tunnel;
  String position;
  int width;
  int height;

  FFT fft;
  float[] fftFilter;
  AudioInput audio;
  BeatDetect beat;

  static final int NbFlames = 70;
  static final int NbFumesPerFlame = 10;
  ArrayList <Flame> flames = new ArrayList<Flame>();

  static int NbRows;
  static int NbCols;
  static int NbPixels;
  static final int NbRows_Ceil = 3;
  static final int NbRows_Wall = 6;
  static final int NbRows_Tunnel = 11;
  static final int NbCols_Tunnel = 24;
  ArrayList <Pixel> pixels = new ArrayList<Pixel>();

  float x = 0;
  float y = 0;

  int frameCounter = 0;
  int currentNbFlames = 0;
  int currentPixelIdx = 0;

  static final int CeilBurnStartTime      = 0; // s
  static final int BurnWarmupDurationTime = 45; // s
  static final int DeadPixelStartTime     = 60; // s
  static final int DeadPixelIntervalTime = 700; // ms

  static Timer timeGlobal;
  static Timer timeInterval;

  public Burn (Tunnel t, int w, int h, String p) {
    tunnel = t;
    width = w;
    height = h;
    position = p;
    fft = tunnel.fft;
    audio = tunnel.in;
    System.out.println(position + " : " + width + " x " + height);
    switch (position) {
      case "Wall":
      case "LWall":
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
        // Create audio objects
        beat = new BeatDetect();
        beat.setSensitivity(300);
        fftFilter = new float[fft.specSize()];
        // Create the flames collection
        for (int i = 0; i < NbFlames; i++) {
          Flame flame = new Flame(NbFumesPerFlame);
          flames.add(flame);
        }
        // Create the pixels collection
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
    timeGlobal = new Timer("s");
    timeInterval = new Timer("ms");
    timeGlobal.start();
    timeInterval.start();
  }

  public void draw() {
    synchronized (Tunnel.class) {
      frameCounter++;
/*
      switch (position) {
        case "Ceil":
          if (timeGlobal.now() > CeilBurnStartTime) {
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
*/
          background(0);
          // Process flames
          if (timeGlobal.now() > BurnWarmupDurationTime) {
            currentNbFlames = NbFlames;
          } else {
            // Polynomial burn ;)
            float ratio = pow((float)timeGlobal.now()/(float)BurnWarmupDurationTime, 2);
            currentNbFlames = 1 + (int)(ratio*(float)NbFlames);
            if (currentNbFlames > NbFlames) currentNbFlames = NbFlames; // Prevent overflow
          }
          for (int i = 0; i < currentNbFlames; i++) {
            flames.get(i).update();
          }
/*
          Process flames on ceil
          for (int j = 0; j < 100; j++) {
            y = x/100;
            beginShape();
            for (int i = 0; i < width; i++) {
              vertex(i, y);
              y = y + (float)((noise(y/100,i/100)-0.5)*4);
            }
            endShape();
            x += 1;
          }
*/
          // Process pixels
          boolean flag = true;
          if (timeGlobal.now() > DeadPixelStartTime && (timeInterval.now() > DeadPixelIntervalTime)) {
            flag = false;
          }
          // beat.detect(audio.mix);
          // for (int i = 0; i < fftFilter.length; i++) {
          //   fftFilter[i] = max(fftFilter[i], log(1+fft.getBand(i)) * (float)(1+i*0.01));
          // }
          // if (timeGlobal.now() > DeadPixelStartTime) {
          //   beat.detect(audio.mix);
          //   flag = beat.isOnset() ? false : true; // Option #1
          //   flag = ( tunnel.getAudioAverage() < 20 ); // Option #2
          //   flag = !( fft.getBand(1) > 200 ); // Option #3
          // }
          for (int i = 0; i < NbPixels; i++) {
            if (currentPixelIdx < NbPixels && flag == false) {
              pixels.get(currentPixelIdx++).markToDie();
              flag = true; // One pixel marked as dead for current frame
              timeInterval.reset();
            }
            pixels.get(i).update();
          }
/*
      }
*/
    }
  }

  // Timer class
  class Timer {

    String unit;
    int timer;
    int StartTime;
    int StartStop;
    int StopDuration;
    boolean isRunning;

    Timer (String uniti) {
      unit = uniti;
      timer = 0;
      StartTime = 0;
      StartStop = 0;
      StopDuration = 0;
      isRunning = false;
    }

    private int tick() {
      switch (unit) {
        case "ms":
          return millis();
        case "s":
          return millis()/1000;
        default:
          return millis();
      }
    }

    void start() {
      if (isRunning) {
        StopDuration += tick() - StartStop;
      } else {
        StartTime = tick();
        isRunning = true;
      }
    }

    void stop() {
      if (isRunning) {
        StartStop = tick();
      }
    }

    void reset() {
      if (isRunning) {
        StopDuration = 0;
        StartTime = tick();
      }
    }

    int now () {
      return tick() - StartTime;
    }

    int elapsed () {
      return tick() - StartTime - StopDuration;
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
          // When pixel is alive, no stroke, hence no drawing
          // noFill();
          // stroke(255);
          // rect(xc, yc, width, height);
          break;
        case sFULLWHITE:
          fill(255);
          stroke(255);
          rect(xc, yc, width, height);
          // Pixel will start collapsing during the 5th frame
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
