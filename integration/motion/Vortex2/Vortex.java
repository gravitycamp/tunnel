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

public class Vortex extends PApplet {

int num = 300;
float[][] pos = new float[num][2];
float[][] upPos = new float[num][2];
float t;
boolean filterBlur = false;
public void setup(){
  frameRate(60);
  background(0);
  size(300,300,P2D);
  noCursor();
  for(int i = 0; i<num; i++){
    float x = 2*PI;
    float y = 2*PI;
    pos[i][0] = noise(width);
    pos[i][1] = random(height);
    upPos[i][0] = sin(-x*tan(y));
    upPos[i][1] = cos(-y*tan(x));
  }
}
  
  public void draw(){
    int a =(int) map(mouseX,0,width,150,300);
    int b =(int) map(mouseY,0,height,60,500);
    int c =(int) map(mouseY,height,0,110,150);
    
    colorMode(HSB,a,b,100,c);

    if(filterBlur == true){
    filter(BLUR,0.8f);
    }
    fill(0,3);
    rect(0,0,width,height);
    t+=.002f;
    translate(width/2,height/2);
    for(int i = 0; i<num; i++){
      rotate(radians(t-2));
      pos[i][0] += upPos[i][0];
      pos[i][1] += upPos[i][1];
      for(int j = 0; j<num; j++){
        float distance = dist(pos[i][0],pos[i][1],pos[j][0],pos[j][1]);
        if (distance < 1){
          strokeWeight(1);
          stroke(random(100,200),100,random(150,255),4);
          noFill();
          beginShape();
            vertex(-pos[i][0],-pos[i][1]);
            bezierVertex(-pos[i][0],-pos[i][0],
                      sin(-pos[i][0])*sin(-pos[j][1]),cos(-pos[i][1])*sin(-pos[j][1]),
                      -pos[i][1],-pos[i][1]);
           endShape();
        }
      }
    if(pos[i][0] < -num){
        pos[i][0] = +num;
      }
      if(pos[i][0] > +num){
        pos[i][0] = -num;
      }
      
      if(pos[i][1] < -num){
        pos[i][1] = +num;
      }
      if(pos[i][1] > +num){
        pos[i][1] = -num;
      }
    }
  }
public void keyPressed(){
  filterBlur = !filterBlur;
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "Vortex" });
  }
}
