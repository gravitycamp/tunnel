/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/9299*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.core.*;
import java.util.ArrayList;


public class NoiseField extends PApplet {

  int width;
  int height;
  Tunnel tunnel;

  public NoiseField(Tunnel t, int w, int h) {
      width  = w;
      height = h;
      tunnel = t;
  }

  public void settings() {
      size(width, height);
  }
  
  int NUM_PARTICLES = 50;
  ParticleSystem p;
  public void setup()
  {
    smooth();
    background(0);
    p = new ParticleSystem();
  }
  
  public void draw(){
    synchronized (Tunnel.class) {
      //println(frameCount);
      noStroke();
      fill(0,5);
      rect(0,0,width,height);
      p.update();
      p.render();
    }
  }
  
  class Particle
  {
    PVector position, velocity;
  
    Particle()
    {
      position = new PVector(random(width),random(height));
      velocity = new PVector(); 
    }
    
    void update()
    {
      velocity.x = (float)(20*(noise(mouseX/10+position.y/100)-0.5));  //change to kinect //<>//
      velocity.y = (float)(20*(noise(mouseY/10+position.x/100)-0.5));
      position.add(velocity);
      
      if(position.x<0)position.x+=width;
      if(position.x>width)position.x-=width;
      if(position.y<0)position.y+=height;
      if(position.y>height)position.y-=height;
    }
  
    void render()
    {
      stroke(blendColor(color(0,0,255), color(random(255),random(255),random(255)), ADD));
      line(position.x,position.y,position.x-velocity.x,position.y-velocity.y);
    }
  }
  
  class ParticleSystem
  {
    Particle[] particles;
    
    ParticleSystem()
    {
      particles = new Particle[NUM_PARTICLES];
      for(int i = 0; i < NUM_PARTICLES; i++)
      {
        particles[i]= new Particle();
      }
    }
    
    void update()
    {
      for(int i = 0; i < NUM_PARTICLES; i++)
      {
        particles[i].update();
      }
    }
    
    void render()
    {
      for(int i = 0; i < NUM_PARTICLES; i++)
      {
        particles[i].render();
      }
    }
  }
}