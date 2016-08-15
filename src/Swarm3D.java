import processing.core.*;

class Swarm3D extends PApplet {

  particle[] Z = new particle[500];
  float colour = random(1);
  boolean tracer = true;
  int depth;
  
  int width;
  int height;
  Tunnel tunnel;
  
  float lastX;
  float lastY;
  
  public Swarm3D(Tunnel t, int w, int h) {
      width = w;
      height = h;
      tunnel = t;
  }

  public void settings()
  {
      size(width, height);
  }
  
  public void setup() {
    smooth();  
    depth = width;
    background(0);
    frameRate(25);
    
    float n = 100;
    float px, py, pz;
    float m, v, theta, phi;
    
    for(int k = 0; k < n; k++) {
      px = random(width);
      py = random(height);
      pz = random(depth);
      m = random(50);
      for(int i = (int)((Z.length-100)*k/n); i < (int)((Z.length-100)*(k+1)/n); i++) {
        v = sq(random(sqrt(m)));
        theta = random(TWO_PI);
        phi = random(TWO_PI);
        Z[i] = new particle( px+v*cos(phi)*cos(theta), py+v*cos(phi)*sin(theta), pz+v*sin(phi), 0, 0, 0, 1 );
      }
    }
    px = width/2;
    py = height/2;
    for(int i = Z.length-100; i < Z.length; i++) {
      pz = random(depth);
      v = sq(random(sqrt(width/4)));
      theta = random(TWO_PI);
      Z[i] = new particle( px+v*cos(theta), py+v*sin(theta), pz, 0, 0, 0, 1 );
    }
  
    /*
    for(int i = 0; i < Z.length; i++) {
      r = i/float(Z.length);
      Z[i] = new particle( random(width), r*height, depth/2, 0, 0, 0, 1 );
    }
    */
    
    frameRate(60);
  }
  
  public void draw() {
    synchronized (Tunnel.class) {
    
    Main.kinect.update();
    
    colorMode(RGB,255);
    float r;
    
    if( !tracer ) {
      background(0);
    }
    else {
      fill(0,10);
      rect(0,0,width,height);
    }
    
    filter(INVERT);
    
    lastX = (float)width * Main.kinect.RightHandDepthRatio;
    lastY = (float)height * Main.kinect.RightHandRaisedRatio;
    for(int i = 0; i < Z.length; i++) {
        float currentX = (float)width * Main.kinect.RightHandDepthRatio;
        float currentY = height * Main.kinect.RightHandRaisedRatio;

        Z[i].gravitate( new particle( currentX, currentY, (float)depth/2, (float)0, (float)0,(float)0, (float)0.75 ) );
        println(currentX, currentY); 
        lastX = currentX;
        lastY = currentY;

      //else if( mousePressed && mouseButton == RIGHT ) {
      //  Z[i].repel( new particle( mouseX, mouseY, depth/2, 0, 0, 0, 1 ) );
      //}
      
      if(currentX == lastX && currentY == lastY) {
        Z[i].deteriorate();
      }
  
      Z[i].update();
      r = (float)(i)/Z.length;
      colorMode(HSB,1);
      if( Z[i].magnitude/100 < 0.1 ) {
        stroke( (int)colour, (float) pow((float)r, (float)0.1), (float)0.9*sqrt(1-r), (float)(Z[i].magnitude/100+abs(Z[i].z/depth)*0.05) );
      }
      else {
        stroke( (int)colour, (float)pow((float)r,(float)0.1), (float)0.9*sqrt(1-r), (float)(0.1+abs(Z[i].z/depth)*0.05 ));
      }
      Z[i].display();
    }
  
    colour+=random((float)0.01);
    colour = colour%1;
  
    filter(INVERT);
          }
    
  }
  
  public void keyPressed() {
    
    if( key == ' ' ) {
    
      float r, choice = random(1);
      
      for(int i = 0; i < Z.length; i++) {
        
        r = i/(float)Z.length;
    
        if( choice > 0.8 ) {
          // Slice
          Z[i].reset( r*width, r*height, random(depth), 0, 0, 0, 1 );
        }
  
        else if( choice > 0.6 ) {
          // Plane
          Z[i].reset( random(width), r*height, depth/2, 0, 0, 0, 1 );
        }
        
        else if( choice > 0.4 ) {
          // X
          if( r < 0.5 )
            Z[i].reset( (1-2*r)*width, 2*r*height, 2*r*depth, 0, 0, 0, 1 );
          else
            Z[i].reset( (1-(2*r-1))*width, (1-(2*r-1))*height, (2*r-1)*depth, 0, 0, 0, 1 );
        }
        
        else if( choice > 0.2 ) {
          // Smooth Curve
          Z[i].reset( (1-r)*width, sqrt(r)*height, r*depth, 0, 0, 0, 1 );
        }
    
        else { 
          // Swirl
          Z[i].reset( height/2+r*cos(r*10*PI)*height/2, height/2+r*sin(r*10*PI)*height/2, r*depth, 0, 0, 0, 1 );
        }
      }
    
    }
    else if( key == ENTER ) {
      tracer = !tracer;
    }
      
  }
  
  public class particle {
    
    float x;
    float y;
    float z;
    float px;
    float py;
    float magnitude;
    float theta;
    float phi;
    float mass;
    
    particle( float dx, float dy, float dz, float V, float T, float P, float M ) {
      x = dx;
      y = dy;
      z = dz;
      px = dx;
      py = dy;
      magnitude = V;
      theta = T;
      phi = P;
      mass = M;
    }
    
    void reset ( float dx, float dy, float dz, float V, float T, float P, float M ) {
      x = dx;
      y = dy;
      z = dz;
      px = dx;
      py = dy;
      magnitude = V;
      theta = T;
      phi = P;
      mass = M;
    }
    
    void gravitate( particle C ) {
      float dx, dy, dz;
      float F, t, p;
      if( sq( x - C.x ) + sq( y - C.y ) + sq( z - C.z ) != 0 ) {
        F = mass * C.mass;
        // magnitude
  
        dx = ( mass * x + C.mass * C.x ) / ( mass + C.mass );
        dy = ( mass * y + C.mass * C.y ) / ( mass + C.mass );
        dz = ( mass * z + C.mass * C.z ) / ( mass + C.mass );
        // find point to which particle is being attracted (dx,dy,dz)
        
        t = atan2( dy-y, dx-x );                          // find yaw angle
        p = atan2( dz-z, sqrt( sq(dy-y) + sq(dx-x) ) ) ;  // find depth angle
        
        dx = F * cos(p) * cos(t);
        dy = F * cos(p) * sin(t);
        dz = F * sin(p);
  
        dx += magnitude * cos(phi) * cos(theta);
        dy += magnitude * cos(phi) * sin(theta);
        dz += magnitude * sin(phi);
        
        magnitude = sqrt( sq(dx) + sq(dy) + sq(dz) );
        theta = atan2( dy, dx );
        phi = atan2( dz, sqrt( sq(dx) + sq(dy) ) );
  
      }
    }
  
    void repel( particle C ) {
      float dx, dy, dz;
      float F, t, p;
      if( sq( x - C.x ) + sq( y - C.y ) + sq( z - C.z ) != 0 ) {
        F = mass * C.mass;
        // magnitude
  
        dx = ( mass * x + C.mass * C.x ) / ( mass + C.mass );
        dy = ( mass * y + C.mass * C.y ) / ( mass + C.mass );
        dz = ( mass * z + C.mass * C.z ) / ( mass + C.mass );
        // find point to which particle is being attracted (dx,dy,dz)
        
        t = atan2( y-dy, x-dx );                          // find yaw angle
        p = atan2( z-dz, sqrt( sq(dy-y) + sq(dx-x) ) ) ;  // find depth angle
        
        dx = F * cos(p) * cos(t);
        dy = F * cos(p) * sin(t);
        dz = F * sin(p);
  
        dx += magnitude * cos(phi) * cos(theta);
        dy += magnitude * cos(phi) * sin(theta);
        dz += magnitude * sin(phi);
        
        magnitude = sqrt( sq(dx) + sq(dy) + sq(dz) );
        theta = atan2( dy, dx );
        phi = atan2( dz, sqrt( sq(dx) + sq(dy) ) );
  
      }
    }
    
    void deteriorate() {
      magnitude *= 0.925;
    }
    
    void update() {
      
      x += magnitude * cos(phi) * cos(theta);
      y += magnitude * cos(phi) * sin(theta);
      z += magnitude * sin(phi);
  
    }
    
    void display() {
      line(px,py,x,y);
      px = x;
      py = y;
    }
  }
}