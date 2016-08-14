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

public class magnetChain extends PApplet {

int maxNum = 15;
int num = 0;
particle[] Z = new particle[maxNum];

boolean time = true;
int t=-1;

public void setup() {
  smooth();
  size(500,500);
  colorMode(HSB,1);
  frameRate(30);
  
  addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
      mouseWheel(evt.getWheelRotation());
  }});

  float r, phi;
  
  r = random(width/12, width/6);
  phi = random(TWO_PI);
  
}

public void draw() {
  
  background(0);
  float r, h;
  
  if(time) {
    
  for(int i = 0; i < num; i++) {
    for(int j = 0; j < num; j++) {
      if( j!=i ) {

        if(Z[i].charge==Z[j].charge) {
          Z[i].repel(Z[j]);
        }
        else { 
          Z[i].gravitate(Z[j]);
        }

      }
    }
    Z[i].update();    
    Z[i].deteriorate();    
  }
  
  }
  
  for(int i = 0; i < num; i++) { // render lines
    for(int j = 0; j < num; j++) {
      if( j!=i ) {
        r = sqrt( (sq(Z[i].x-Z[j].x)+sq(Z[i].y-Z[j].y))/(sq(width)+sq(height)));
        if(Z[i].charge==Z[j].charge) {
          stroke(0,1,1,pow(1-r,3));
        }
        else {
          stroke(0.6f,1,1,pow(1-r,3)); 
        }
        line( Z[i].x, Z[i].y, Z[j].x, Z[j].y );
      }
    }
  }
  
  for(int i = 0; i < num; i++) { // render particles over lines
    Z[i].display(0.4f,0.7f);
  }
  
  if(t>0) {
    if(t<150){t++;}
    stroke(1);
    fill(0.7f);
    ellipse( mouseX, mouseY, t, t );
  }
  
}


public void mousePressed() {
  if( num<maxNum ) {
    t=1;
  }
}

public void mouseReleased() {
  float r, phi;
  
  r = sqrt( sq(mouseX-pmouseX) + sq(pmouseY-pmouseY) );
  phi = atan2( mouseY-pmouseY, mouseX-pmouseX );
  
  if( num<maxNum ) {
    num++;
    if( mouseButton == LEFT ) {
      Z[num-1] = new particle( mouseX, mouseY, r, phi, t/10.0f, true );
    }
    else {
      Z[num-1] = new particle( mouseX, mouseY, r, phi, t/10.0f, false );
    }
    t=-1;
  }
}

public void mouseWheel(int delta) {
  float r=map(-delta,0,5,0,2);
  for(int i = 0; i < num; i++) {
    Z[i].magnitude+=r;
  }
}

public void keyPressed() {
  time=!time;
}



class particle {
  
  float x;
  float y;
  float px;
  float py;
  float magnitude;
  float angle;
  float mass;
  boolean charge;
  
  particle( float dx, float dy, float V, float A, float M, boolean C ) {
    x = dx;
    y = dy;
    px = dx;
    py = dy;
    magnitude = V;
    angle = A;
    mass = M;
    charge = C;
  }
  
  public void reset( float dx, float dy, float V, float A, float M, boolean C ) {
    x = dx;
    y = dy;
    px = dx;
    py = dy;
    magnitude = V;
    angle = A;
    mass = M;
    charge = C;
  }
  
  public void gravitate( particle Z ) {
    float F, mX, mY, A;
    if( sq( x - Z.x ) + sq( y - Z.y ) > 1 ) {
      F = 5*Z.mass/mass;
      F /= sqrt( sq( x - Z.x ) + sq( y - Z.y ) );
      if( sqrt(sq( x - Z.x ) + sq( y - Z.y )) < 10 ) {
        F = 0.1f;
      }
      A = atan2( Z.y-y, Z.x-x );
      
      mX = F * cos(A);
      mY = F * sin(A);
      
      mX += magnitude * cos(angle);
      mY += magnitude * sin(angle);
      
      magnitude = sqrt( sq(mX) + sq(mY) );
      angle = atan2( mY, mX );
    }
  }

  public void repel( particle Z ) {
    float F, mX, mY, A;
    if( sq( x - Z.x ) + sq( y - Z.y ) > 1 ) {
      F = 5*Z.mass/mass;
      F /= sqrt( sq( x - Z.x ) + sq( y - Z.y ) );
      if( sqrt(sq( x - Z.x ) + sq( y - Z.y )) < 10 ) {
        F = 0.1f;
      }
      A = atan2( y-Z.y, x-Z.x );
      
      mX = F * cos(A);
      mY = F * sin(A);
      
      mX += magnitude * cos(angle);
      mY += magnitude * sin(angle);
      
      magnitude = sqrt( sq(mX) + sq(mY) );
      angle = atan2( mY, mX );
    }
  }
  
  public void deteriorate() {
    magnitude *= 0.999f;
  }
  
  public void update() {
    
    x += magnitude * cos(angle);
    y += magnitude * sin(angle);
    
    if( x+mass*5 > width ) { // Impact with right wall
      x = width-mass*5;
      angle = atan2(sin(angle),-cos(angle));
    }
    if( x < mass*5 ) { // Impact with left wall
      x = mass*5;
      angle = atan2(sin(angle),-cos(angle));
    }
    if( y+mass*5 > height ) { // Impact with bottom wall
      y = height-mass*5;
      angle = atan2(-sin(angle),cos(angle));
    }
    if( y < mass*5 ) { // Impact with top wall
      y = mass*5;
      angle = atan2(-sin(angle),cos(angle));
    }
    
  }
  
  public void display(float detail, float back) {
    stroke(1);
    fill(back);
    ellipse( x, y, mass*10, mass*10 );
    stroke(detail);
    fill(detail);
    rect( x-mass*3.5f,y-mass*0.7f,mass*7,mass*1.4f);
    if(charge) {
      rect( x-mass*0.7f,y-mass*3.5f,mass*1.4f,mass*7);
    }
    px = x;
    py = y;
  }
  
  
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "magnetChain" });
  }
}
