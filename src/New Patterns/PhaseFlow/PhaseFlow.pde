ArrayList <Mover> particles;

color bgcolor;

int baseColor = 0;
int direction = 1;
float time = 0;

void setup() {  // this is run once.   
    
    // set the background color
    background(bgcolor);
  colorMode(HSB);
    bgcolor=color(random(255), random(150, 255), random(155), 10);
colorMode(HSB);

    // canvas size (Variable aren't evaluated. Integers only, please.)
    size(500, 300); 
      
    // smooth edges
    smooth();
    
    // limit the number of frames per second
    frameRate(30);
    
    // set the width of the line. 
    strokeWeight(12);
    
     particles = new ArrayList();
 
  for (int i = 0; i < 1500; i++)
  {
    Mover m = new Mover();
    particles.add (m);
  }
    
} 

void draw() {  // this is run repeatedly. 
  time=time+1;
  baseColor = baseColor+1;
  if(time>100){
      bgcolor=color(random(255),random(150,255),random(155),10);
      time=0;
      //direction=-direction;
      }
  fill(bgcolor);
  rect(0,0,width,height);


    int i = 0;
  while (i < particles.size () )
  {
    Mover m = particles.get(i);
    if(random(500)<2){m.setRandomValues();}
    m.move(0.01*direction);
    m.disp(); 
    i = i + 1;
  }
  
}

// OBJECTS ---------------------

class Mover
{
  PVector location; 
  float ellipseSize;
  color col;
 
  Mover () // Constructor
  {
    setRandomValues();
  }
 
  Mover (float x, float y, color c) // Constructor
  {
    setValues (x,y,c);
  }
 
  // SET ---------------------------
 
  void setRandomValues ()
  {
    location = new PVector (random (width), random (height));
    ellipseSize = 4; 
    colorMode(HSB);
    col=color((random(50,80)+baseColor) % 255,random(100,255),255,150);
  }
  void setValues (float x, float y, color c)
  {
    location = new PVector (x, y);
    ellipseSize = random (8, 20);   
    col=c;
 
  }
 

  // MOVE -----------------------------------------
 
  void move (float dt)
  {
    PVector newLoc = new PVector(location.x,location.y);
    PVector shift = new PVector();
    float x = location.x-width/2;
    float y = location.y-height/2;
    shift.x = y;
    shift.y = -sin(x*TWO_PI/(width))*height/8;
    shift.normalize();
    shift.mult(100*dt);
    location.add(shift);
    location.x=mod1(location.x);    
    
    
    //newLoc=flow(location,dt);
    
    //location.x=newLoc.x;
    //location.y=newLoc.y;
    colorMode(HSB);
    col=color((hue(col)+1) % 255,saturation(col),brightness(col),150);
    
  }
 
  // DISPLAY ---------------------------------------------------------------
 
  void disp ()
  {
  noStroke();
    fill (col);
    ellipse (location.x, location.y, ellipseSize, ellipseSize);
  }
  
}

PVector flow(PVector loc, float dt){
    PVector output = new PVector(loc.x,loc.y);
    PVector shift = new PVector();
    shift.x=loc.y;
    shift.y=-loc.x;
    shift.normalize();
    shift.mult(dt);
    output.add(shift);
    return(output);
    }


float mod1(float x){
    float x1=x;
    while(x1>width){x1=x1-width;}
    while(x1<0){x1=x1+width;}
    return(x1);
    }

void mouseClicked(){
    bgcolor=color(random(255),random(150,255),random(155),10);
    
  for (int i = 0; i < 40; i++)
  {
    //bouncers.get(i).c=color(random(255),random(255),random(255));
  }
    }