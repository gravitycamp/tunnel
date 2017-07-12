import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class CircleDistance extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  Minim minim;
  BeatDetect beat;


  int nbCircles = 8;
  Circle[] circles;
  MyColor myColor;
  float rMax, dMin;


  public CircleDistance(Tunnel t, int w, int h, String p) {
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
    frameRate(50);

    fill(0, 60);
    rMax = (float)min(width, height)/(float)2;
    dMin = (float)max(width, height)/(float)3.5;
    circles = new Circle[nbCircles];
    initialize(true);
  }

  void initialize(Boolean p_random)
  { 
    for (int i = 0; i < nbCircles; i++)
    {
      circles[i] = new Circle(random(rMax), 
        p_random ? random(-width/3, width/3) : 0, 
        p_random ? random(-height/3, height/3) : 0);
    }
    myColor = new MyColor();
  }

  public void draw() {
    synchronized (Tunnel.class) {
      beat.detect(audio.mix);
      if (beat.isOnset())
        initialize(true);  //Change to false to make it symetrical

      noStroke();
      rect(0, 0, width, height);
      translate(width/2, height/2);
      myColor.update();
      for (int j = 0; j < nbCircles; j++)
      {
        circles[j].update();
        for (int i = j+1; i < nbCircles; i++)
        {
          connect(circles[j], circles[i]);
        }
      }
    }
  }
  void connect(Circle c1, Circle c2)
  {
    float d, x1, y1, x2, y2, r1 = c1.radius, r2 = c2.radius;
    float rCoeff = map(min(abs(r1), abs(r2)), 0, (float)rMax, (float).08, (float)1);
    int n1 = c1.nbLines, n2 = c2.nbLines;
    for (int i = 0; i < n1; i++)
    {
      x1 = c1.x + r1 * cos(i * TWO_PI / n1 + c1.theta);
      y1 = c1.y + r1 * sin(i * TWO_PI / n1 + c1.theta);
      for (int j = 0; j < n2; j++)
      {
        x2 = c2.x + r2 * cos(j * TWO_PI / n2 + c2.theta);
        y2 = c2.y + r2 * sin(j * TWO_PI / n2 + c2.theta);

        d = dist(x1, y1, x2, y2);
        if (d < dMin)
        {
          stroke((float)(myColor.R + r2/1.5), (float)(myColor.G + r2/2.2), (float)(myColor.B + r2/1.5), (float)map(d, 0, dMin, 140, 0) * rCoeff);
          line(x1, y1, x2, y2);
        }
      }
    }
  }
  public void mousePressed() {
    initialize(mouseButton == RIGHT);
  }


  // Additional Classes
  class Circle
  {
    float x, y, radius, theta = 0;
    int nbLines = (int)random(3, 25);
    float rotSpeed = (random(1) < .5 ? 1 : -1) * random((float).005, (float).034);
    float radSpeed = (random(1) < .5 ? 1 : -1) * random((float).3, (float)1.4);

    Circle(float p_radius, float p_x, float p_y)
    {
      radius = p_radius;
      x = p_x;
      y = p_y;
    }

    void update()
    {
      theta += rotSpeed;
      radSpeed *= abs(radius += radSpeed) > rMax ? -1 : 1;
    }
  }

  class MyColor
  {
    float R, G, B, Rspeed, Gspeed, Bspeed;
    final static float minSpeed = (float).2;
    final static float maxSpeed = (float).8;
    MyColor()
    {
      R = random(20, 255);
      G = random(20, 255);
      B = random(20, 255);
      Rspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
      Gspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
      Bspeed = (random(1) > .5 ? 1 : -1) * random(minSpeed, maxSpeed);
    }

    public void update()
    {
      Rspeed = ((R += Rspeed) > 255 || (R < 20)) ? -Rspeed : Rspeed;
      Gspeed = ((G += Gspeed) > 255 || (G < 20)) ? -Gspeed : Gspeed;
      Bspeed = ((B += Bspeed) > 255 || (B < 20)) ? -Bspeed : Bspeed;
    }
  }
}