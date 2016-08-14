class particle {
  float ox, oy;
  float x, y;
  float xx, yy;
  float vx;
  float vy;
  int life;
  int age;
  int colour;

  particle( float ix, float iy, float vx0, float vy0, int C, int L ) {
    x = 0;
    y = 0;
    xx = 0;
    yy = 0;
    ox = ix;
    oy = iy;
    vx = vx0;
    vy = vy0;
    colour = C;
    life = L;
    age = L;
  }

  void reset( float ix, float iy, float vx0, float vy0, int C, int L ) {
    x = 0;
    y = 0;
    xx = 0;
    yy = 0;
    ox = ix;
    oy = iy;
    vx = vx0;
    vy = vy0;
    colour = C;
    life = L;
    age = L;
  }

  void move() {

    if( age > 0 ) {
      xx = x;
      yy = y;
      x += vx;
      y += vy;
      age--;
      
      // air resistance
      vx *= 0.95;
      vy *= 0.95;
  
      
      if( oy + y > width ) {
        y = width - oy;
        vy = -vy;
      }
  
      colorMode(HSB,100);
      float temp;
      
      temp = float(age)/float(life);
      stroke( color( colour, log((exp(1)-1)*temp+1)*100, 100-log((exp(1)-1)*temp+1)*100 ) );
      line(ox+xx,oy+yy,ox+x,oy+y);
      //println("x: " + (ox+x) + " y: " + (oy+y) + "\n");
      //println("xx: " + (ox+xx) + " yy: " + (oy+yy) + "\n");
      
      colorMode(RGB,255);
    }
  }
  
  boolean dead() {
    if( age > 0 ) {
      return false;
    }
    return true;
  }
}


