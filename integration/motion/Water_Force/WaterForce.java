import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class WaterForce extends PApplet {

/*based on Dynamic Acceleration tutorial by Daniel Shiffman*/
/* Marcos Frankowicz */
/* 17/02/2010 */
/*if the frameRate is to slow you cam change the "num" variable*/
/*press mouse to "release" the particles from central force*/

int num = 20000;

Coisa[] c = new Coisa[num];
PVector[] cLoc = new PVector[num];
PVector[] cVel = new PVector[num];
PVector[] cAcc = new PVector[num];
PVector[] cCor = new PVector[num];
public void setup(){
  size(600,400,P2D);
 
  background(74,113,137);
  for(int i = 0; i<num; i++){
  cLoc[i] = new PVector(random(width),random(height));
  cVel[i] = new PVector(0.0f, 0.0f);
  cAcc[i] = new PVector(0.0f,0.125f);
  cCor[i] = new PVector(140,200,250);
  c[i] = new Coisa(cLoc[i],cVel[i],cAcc[i],cCor[i]);
  }
}

public void draw(){
 noStroke();
 filter(DILATE);
 
 fill(0,20,40,15);
 rect(0,0,width,height);
 
  for(int i = 0; i<num; i++){
    c[i].go();
    PVector m = new PVector();
    m.set(width/2,pmouseY,0);
    PVector diff = PVector.sub(m,c[i].getLoc());
    diff.normalize();
    float f = 4.2f;
    diff.div(f);
    c[i].setAcc(diff);
    
      if(mousePressed){
      c[i].setAcc(new PVector(0,0));
      }
  }

}

/*void keyPressed(){
  save("image.png");
}*/

class Coisa{ // "Thing"
  PVector loc;
  PVector vel;
  PVector acc;
  PVector colors;
  float maxVel;
  Coisa(PVector loc_, PVector vel_, PVector acc_, PVector colors_){
    loc = loc_.get();
    vel = vel_.get();
    acc = acc_.get();
    colors = colors_.get();
    maxVel = 7;
  }
  
  public void go(){
    move();
    render();
    border();
  }
  
  public void move(){
    vel.add(acc);
    loc.add(vel);
    if(vel.mag() > maxVel){
      vel.normalize();
      vel.mult(maxVel);
    }
  }
  
  public void border(){
    if((loc.y > height+100) || (loc.y < -100)){
      vel.y *= -0.5f;
    }
    if((loc.x < -100) || (loc.x > width+100)){
      vel.x *= -0.5f;
    }
  }
  
  public void render(){
    int r =(int) colors.x;
    int g =(int) colors.y;
    int b =(int) colors.z;
    
    stroke(r,g,b,30);
    point(loc.x,loc.y);
  }
  
  //geters e seters
  public PVector getVel(){
    return vel.get();
  }
  
  public PVector getLoc(){
    return loc.get();
  }
  
  public PVector getAcc(){
    return acc.get();
  }
  
  public float getMaxVel(){
    return maxVel;
  }
  
  public PVector getColor(){
    return colors.get();
  }
  
  public void setVel(PVector v){
    vel = v.get();
  }
  
  public void setLoc(PVector v){
    loc = v.get();
  }
  
  public void setAcc(PVector v){
    acc = v.get();
  }
  
  public void setMaxVel(float f){
    maxVel = f;
  }
  
  public void setColor(PVector v){
    colors = v.get();
  }
  
  
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "WaterForce" });
  }
}
