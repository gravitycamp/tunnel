import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class InsideBubbles extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;
int WIDTH = 800;
int HEIGHT = 400;
float ZOOM = (float)2.0;
int N = 150*(int)ZOOM;
float RADIUS = HEIGHT/10;
float SPEED = (float)0.0003;
float FOCAL_LENGTH = (float)0.5;
float BLUR_AMOUNT = 70;
int MIN_BLUR_LEVELS = 2;
int BLUR_LEVEL_COUNT = 4;
float ZSTEP = (float)0.008;
int BACKGROUND = color(0, 30, 30);

boolean zoomIn = false;
boolean zoomOut = false;

float xoffs = 0;
float yoffs = 0;


  public InsideBubbles(Tunnel t, int w, int h, String p) {
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

     smooth();
    noStroke();
 
    objects = new ArrayList();
    // Randomly generate the bubbles
    for (int i=0; i<N; i++) {
        objects.add(new ZObject(random(1.0f), random(1.0f), random(1.0f), color(random(20, 20), random(150, 190), random(150, 190))));
    }

    sortBubbles();
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
      track();
 //     beat.detect(audio.mix);
 //     if (beat.isOnset())
 //       generateColors();

  background(BACKGROUND);
  xoffs = (float)(xoffs*0.9 + 0.1*trackX/WIDTH);
  yoffs = (float)(yoffs*0.9 + 0.1*trackY/HEIGHT);
  
  for (int i=0; i<N; i++) {
    ZObject current = (ZObject)objects.get(i);
    current.update(zoomIn, zoomOut);
  }
  sortBubbles();
  
  for (int i=0; i<N; i++) {
    ((ZObject)objects.get(i)).draw(xoffs, yoffs);
  }
    }
  }
 // This function will draw a blurred circle, according to the "blur" parameter. Need to find a good radial gradient algorithm.
void blurred_circle(float x, float y, float rad, float blur, int col, float levels) {
    float level_distance = BLUR_AMOUNT*(blur)/levels;
    for (float i=0; i<levels*2; i++) {
      fill(col, 255*(levels*2-i)/(levels*2));
      ellipse(x, y, rad+(i-levels)*level_distance, rad+(i-levels)*level_distance);
    }
}

ArrayList objects;
void sortBubbles() {
   
    // Sort them (this ensures that they are drawn in the right order)
    float last = 0;
    ArrayList temp = new ArrayList();
    for (int i=0; i<N; i++) {
        int index = 0;
        float lowest = 100;
        for (int j=0; j<N; j++) {
            ZObject current = (ZObject)objects.get(j);
            if (current.z < lowest && current.z > last) {
                index = j;
                lowest = current.z;
            }
        }
        temp.add(objects.get(index));
        last = ((ZObject)objects.get(index)).z;
    }
    objects = temp;
}


public void mousePressed() {
  if (mouseButton == LEFT) {
    zoomIn = true;
  } else if (mouseButton == RIGHT) {
    zoomOut = true;
  }
}
public void mouseReleased() {
  if (mouseButton == LEFT) {
    zoomIn = false;
  } else if (mouseButton == RIGHT) {
    zoomOut = false;
  }
}

  // Additional Classes
class ZObject {
  float x, y, z, xsize, ysize;
  int bubble_color;
  int shaded_color;
  float vx, vy, vz;
 
  ZObject(float ix, float iy, float iz, int icolor) {
    x = ix;
    y = iy;
    z = iz;
    xsize = RADIUS;
    ysize = RADIUS;
    bubble_color = icolor;
    setColor();
    vx = random(-1, 1);
    vy = random(-1, 1);
    vz = random(-1, 1);
    float magnitude = sqrt(vx*vx + vy*vy + vz*vz);
    vx = SPEED * vx / magnitude;
    vy = SPEED * vy / magnitude;
    vz = SPEED * vz / magnitude;
  }
  
  void setColor() {
    float shade = z;
    float shadeinv = 1-shade;
    shaded_color = color( (red(bubble_color)*shade)+(red(BACKGROUND)*shadeinv),
                    (green(bubble_color)*shade)+(green(BACKGROUND)*shadeinv),
                    (blue(bubble_color)*shade)+(blue(BACKGROUND)*shadeinv));
  }
  
  void zoomIn(float step) {
    z += step;
    if (z > 1.0) {
      z = 0 + (z-1);
    }
  }
  
  void zoomOut(float step) {
    z -= step;
    if (z < 0) {
      z = 1 - (0-z);
    }
  }
 
  void update(boolean doZoomIn, boolean doZoomOut) {
    if (doZoomIn) {
      zoomIn(ZSTEP);
    }
    if (doZoomOut) {
      zoomOut(ZSTEP);
    }
    if (x <= 0) {
        vx = abs(vx);
        x = 0.0f;
    }
    if (x >= 1.0) {
        vx = -1 * abs(vx);
        x = 1;
    }
    if (y <= 0) {
        vy = abs(vy);
        y = 0.0f;
    }
    if (y >= 1) {
        vy = -1 * abs(vy);
        y = 1;
    }
    if (z < 0 || z > 1) {
        z = z % 1;
    }
    // float n = (noise(x, y) - 0.5) * 0.00001;
    // vx += n;
    // vy += n;
    x += vx;
    y += vy;
    //z += vz;
    setColor();
  }
 
  void draw(float xoffs, float yoffs) {
    float posX = (ZOOM*x*WIDTH*(1+z*z)) - ZOOM*xoffs*WIDTH*z*z;
    float posY = (ZOOM*y*HEIGHT*(1+z*z)) - ZOOM*yoffs*HEIGHT*z*z;
    float radius = z*xsize;
    if (posX> -xsize*2 && posX < WIDTH+xsize*2 && posY > -xsize*2 && posY < HEIGHT+xsize*2) {
        blurred_circle(posX, posY, radius, abs(z-FOCAL_LENGTH), shaded_color, MIN_BLUR_LEVELS + (z*BLUR_LEVEL_COUNT));
    }
  }
}

}