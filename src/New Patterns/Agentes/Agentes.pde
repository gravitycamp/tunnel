Ball theBalls[];
int numBalls = 50;

void setup() {
  size(600, 600);
  frameRate(25);

  theBalls = new Ball[numBalls];
  for (int i = 0; i < numBalls; ++i) {
    float ballSize = constrain(20 + (noise(randomGaussian()) * 4), 1, 100);
    theBalls[i] = new Ball(random(width-80)+40, random(height-80)+40, ballSize);
    theBalls[i].randomiseDirection();
  }
 
 
  //minim = new Minim(this);
  //soundFx = new AudioPlayer[1];
  //soundFx[0] = minim.loadFile("musica.mp3");
 // soundFx[0].play();
  background(0);
}

void draw() {
  fill(#ffff00,20); 

  rect(0,0,600,600);
  for (int i = 0; i < numBalls; ++i) {
    theBalls[i].move();
    theBalls[i].display();
  }
  
  
}  


class Ball {

 
  float x;       
  float y;        
  float size;     
  float speed;    
  float xdirection;
  float ydirection;
  float omega;    
  
  Ball(float x_, float y_, float size_) {
    x = x_;
    y = y_;
    size = size_;
    
    colorMode(HSB,360,100,100);
    
    speed = 0;
    xdirection = 0;
    ydirection = 0;
    omega = 0;
  }
  
  
  void randomiseDirection() {
    speed = random(5);
    xdirection = random(360);
    ydirection = random(360);
    omega = randomGaussian() * 0.3;
  }
  
  void move() {
    float dx, dy; 
    dx = cos(radians(xdirection)*(noise(1)*80)) * speed;
    dy = sin(radians(ydirection)*(noise(1)*40)) * speed;
    x += dx;
    y += dy;
    xdirection += omega;
    ydirection += omega;
    checkBounds();
  }
  

  void checkBounds() {
    if (x <= 0 + size*2 || x >= width - size*2){
      xdirection += 180;
      xdirection = xdirection % 360;
    }
    if( y <= 0 + size*2 || y >= height - size*2){
      ydirection += 180;
      ydirection = ydirection % 360;
    }
  }
      


  
  void display() {
    noStroke();
    if(xdirection<180|| ydirection > 180){
      fill(350,100,100);
      ellipse(x, y, size, size);
      fill(350,60,100);
      ellipse(x,y,size/2,size/2);
      fill(350,0,100);
      ellipse(x,y,size/4,size/4);
    }
    else{
      fill(10,100,100);
      ellipse(x, y, size, size);
      fill(10,50,100);
      ellipse(x,y,size/2,size/2);
      fill(10,10,100);
      ellipse(x,y,size/4,size/4);
    }
    
  }
}