/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/9299*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.core.*;
import java.util.ArrayList;


public class WhiteRain extends PApplet {

    int width;
    int height;

    public WhiteRain(int w, int h) {
        width  = w;
        height = h;
    }

    public void settings() {
        size(width, height);
    }
    
    int rainNum = 100;
    ArrayList rain = new ArrayList();
    ArrayList splash = new ArrayList();
    float current;
    float reseed = random(0,(float) .2);
    
    public void setup()
    {
      colorMode(HSB,100);
      background(0);
    
      rain.add(new Rain());
      current = millis();
    }
    
    public void draw()
    {
      blur(50);
      
      if ((millis()-current)/1000>reseed&&rain.size()<150)
      {
        rain.add(new Rain());
        float reseed = random(0,(float) .2);
        current = millis();
      }
      
      for (int i=0 ; i<rain.size() ; i++)
      {
        Rain rainT = (Rain) rain.get(i);
        rainT.calculate();
        rainT.draw();
        if (rainT.position.y>height)
        {
          
          for (int k = 0 ; k<random(5,10) ; k++)
          {
            splash.add(new Splash(rainT.position.x,height));
          }
          
          rain.remove(i);
          float rand = random(0,100);
          if (rand>10&&rain.size()<150)
          rain.add(new Rain());
        }
      }
      
      for (int i=0 ; i<splash.size() ; i++)
      {
        Splash spl = (Splash) splash.get(i);
        spl.calculate();
        spl.draw();
        if (spl.position.y>height)
        splash.remove(i);
      }
    
    }
    
    void blur(float trans)
    {
      noStroke();
      fill(0,trans);
      rect(0,0,width,height);
    }
    
    public class Rain
    {
      PVector position,pposition,speed;
      float col;
      
      public Rain()
      {
        position = new PVector(random(0,width),0);
        pposition = position;
        speed = new PVector(0,0);
        col = random(30,100);
      }
      
      void draw()
      {
        stroke(100,col);
        strokeWeight(2);
        line(position.x,position.y,pposition.x,pposition.y);
        //ellipse(position.x,position.y,5,5);
      }
      
      void calculate()
      {
        pposition = new PVector(position.x,position.y);
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
      PVector position,speed;
      
      public Splash(float x,float y)
      {
        float angle = random(PI,TWO_PI);
        float distance = random(1,5);
        float xx = cos(angle)*distance;
        float yy = sin(angle)*distance;
        position = new PVector(x,y);
        speed = new PVector(xx,yy);
        
      }
      
      public void draw()
      {
        strokeWeight(1);
        stroke(100,50);
        fill(100,100);
        ellipse(position.x,position.y,2,2);
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