class Particle {
  PVector loc, vel;
  float hu;
  float xOff;
  float yOff;
  float xInc = 0.1;
  float yInc = 0.03;
  float lmult;

  Particle() {
    loc = new PVector(random(width), random(height));
    vel = new PVector();
    hu = 0;
    xOff = 0;
    yOff = 0;
  }

  void update() {
    vel.x = noise(xOff/10+loc.y/150)-0.5;
    vel.y = noise(yOff/10+loc.x/150)-0.5;
    xInc = map(noise(xInc), 0, 1, 0, 0.15);
    yInc = map(noise(yInc), 0, 1, 0, 0.03);
    mult = 3*map(noise(mult), 0, 1, 0, 90);


    vel.mult(mult);
    loc.add(vel);

    hu +=10*xInc;

    if (lmult == mult)mult++;
    if (hu > 255)hu = 0;
    if (loc.x<0)loc.x+=width;
    if (loc.x>width)loc.x-=width;
    if (loc.y<0)loc.y+=height;
    if (loc.y>height)loc.y-=height;

    lmult = mult;

    xOff +=1.25*xInc;
    yOff +=2.7*yInc;
  }

  void show() {
    strokeWeight(0.4);
    stroke(hu, 255, 255, 40);
    line(loc.x, loc.y, loc.x-vel.x, loc.y-vel.y);
  }
}