/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/9299*@* */ //<>//
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.core.*;
import java.util.ArrayList;


public class WhiteRain extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  float trackX = 0;
  float trackY = 0;
  float trackZ = 0;
  Tunnel tunnel;

  public WhiteRain(Tunnel t, int w, int h, String p) {
    width  = w;
    height = h;
    tunnel = t;
    position = p;
  }

  public void settings() {
    size(width, height);
  }

  int rainNum = 140;
  ArrayList rain = new ArrayList();
  ArrayList splash = new ArrayList();
  float current;
  float reseed = random(0, (float) .2);

  public void setup()
  {
    colorMode(HSB, 100);
    background(0);
    frameRate(90);

    rain.add(new Rain());
    current = millis();
  }

  public void track() {
    if (Main.kinect != null) {
      Main.kinect.update();
      switch (position) {
      case "Tunnel":
      case "Wall":
      case "Ceil":
      case "RWall":
        trackX = (float)width * Main.kinect.RightHandDepthRatio;
        trackY = (float)height * Main.kinect.RightHandRaisedRatio;
        trackZ = 0;
        break;
      case "LWall":
        trackX = (float)width * Main.kinect.LeftHandDepthRatio;
        trackY = (float)height * Main.kinect.LeftHandRaisedRatio;
        trackZ = 0;
        break;
      }
    } else {
      trackX = mouseX;
      trackY = mouseY;
    }
  }

  public void draw() {
    synchronized(Tunnel.class) {
      //Main.kinect.update();
      track();
      blur(50);
      if ((millis()-current)/1000>reseed&&rain.size()<150)
      {
        rain.add(new Rain());
        float reseed = random(0, (float).2);
        current = millis();
      }

      for (int i=0; i<rain.size(); i++)
      {
        Rain rainT = (Rain) rain.get(i);
        rainT.calculate();
        rainT.draw();
        //println("position " + rainT.position.x);
        //println("depth " + trackX);
        if ((rainT.position.x>trackX*.9) && (rainT.position.x<trackX*1.1))
        {
          if (rainT.position.y>trackY)
          {

            for (int k = 0; k<random(5, 15); k++)
            {
              splash.add(new Splash(rainT.position.x, (float)(trackY)));
            }

            rain.remove(i);
            float rand = random(0, 100);
            if (rand>10&&rain.size()<150)
              rain.add(new Rain());
          }
        }
        if (rainT.position.y>(float).95*height)
        {
          for (int k = 0; k<random(5, 10); k++)
          {
            splash.add(new Splash(rainT.position.x, (float)(height)));
          }

          rain.remove(i);
          float rand = random(0, 100);
          if (rand>10&&rain.size()<150)
            rain.add(new Rain());
        }
      }

      for (int i=0; i<splash.size(); i++)
      {
        Splash spl = (Splash) splash.get(i);
        spl.calculate();
        spl.draw();
        // println("position " + spl.position.y);
        // println("depth " + width*trackX);
        if ((spl.position.x>trackX*.9) && (spl.position.x<trackX*1.1))
        {
         // if (spl.position.y>0.9*trackY)
        //    splash.remove(i);
        } 
        else if (spl.position.y>height)
          splash.remove(i);
      }
    }
  }

  void blur(float trans)
  {
    noStroke();
    fill(0, trans);
    rect(0, 0, width, height);
  }

  public class Rain
  {
    PVector position, pposition, speed;
    float col;

    public Rain()
    {
      position = new PVector(random(0, width), 0);
      pposition = position;
      speed = new PVector(0, 0);
      col = random(30, 100);
    }

    void draw()
    {
      stroke(100, col);
      strokeWeight(1);
      line(position.x, position.y, pposition.x, pposition.y);
      //ellipse(position.x,position.y,5,5);
    }

    void calculate()
    {
      pposition = new PVector(position.x, position.y);
      gravity();
    }

    void gravity()
    {
      speed.y += .2;
      speed.x += .01;
      position.add(speed);
    }
  }

  public class Splash
  {
    PVector position, speed;

    public Splash(float x, float y)
    {
      float angle = random(PI, TWO_PI);
      float distance = random((float)1, (float)5.0);
      float xx = cos(angle)*distance;
      float yy = sin(angle)*distance;
      position = new PVector(x, y);
      speed = new PVector(xx, yy);
    }

    public void draw()
    {
      strokeWeight(1);
      stroke(100, 50);
      fill(100, 100);
      ellipse(position.x, position.y, 2, 2);
    }

    void calculate()
    {
      gravity();

      speed.x*=0.98;
      speed.y*=0.98;

      position.add(speed);
    }

    void gravity()
    {
      speed.y+=.2;
    }
  }
}