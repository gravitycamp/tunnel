/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/1004*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/**
 * GravityFlock
 *
 *
 * by Colin Mitchell <a href="http://muffinlabs.com/">muffinlabs.com</a>
 *
 * <p>Boid Flocking code with the addition of a 'black hole' which sucks boids in
 * if they get too close.  For every Boid that dies, a new one is generated.</p>
 *
 * <p>Add additional black holes by clicking on the applet.  You need to reload
 * to start anew.</p>
 *
 * <p>based on code from Daniel Shiffman <http://www.shiffman.net></p>
 */

int BOID_COUNT = 200;

ParticleGroup flock;
ParticleGroup black_holes;
ParticleSystem physics;

void setup() {
  size(600, 600);
  background(0);
  colorMode(RGB,255,255,255,100);

  physics = new ParticleSystem();
  flock = new ParticleGroup(physics);
  black_holes = new ParticleGroup(physics);

  physics.add(flock);
  physics.add(black_holes);

  // Add an initial set of boids into the system
  for (int i = 0; i < BOID_COUNT; i++) {
    PVector v = new PVector( random(0, width), random(0, height));
    addBoid(v);
  }

  for (int i = 0; i < 1; i++) {
    PVector v = new PVector( random(0, width), random(0, height), 0);
    addBlackHole(v);
  }

  smooth();
  frameRate(30);
} // setup

void draw() {
  // comment this out to keep old points on the screen
  background(0);

  // keep track of how many particles we need to add
  int count = 0;
  for (Iterator<Particle> iter = flock.getParticles().iterator(); iter.hasNext();) {
    Particle p = iter.next();
    if ( p.dead() ) {
      iter.remove();
      count++;
    }
  }

  for (int i = 0; i < count; i++) {
    PVector v;

    int wall = int(random(0,4));
    float px, py;
    switch (wall) {
    case 0:
      px = 0;
      py = random(0, height);
      break;
    case 1:
      px = width;
      py = random(0, height);
      break;
    case 2:
      px = random(0, width);
      py = height;
      break;
    default: 
      px = random(0, width);
      py = 0;
    }

    v = new PVector( px, py);
    addBoid(v);
  }

  physics.run();
} // draw

// Add a new black hole into the System
void mousePressed() {
  addBlackHole(new PVector(mouseX, mouseY, 0));
}

void addBoid(PVector v) {
  Boid b = new Boid(this, v, random(Boid.MIN_VELOCITY, Boid.MAX_VELOCITY), random(Boid.MIN_FORCE, Boid.MAX_FORCE));
  flock.add(b);
}

void addBlackHole(PVector v) {
  BlackHole bh = new BlackHole(physics, this, v, random(0.5f, 1.5f));
  black_holes.add(bh);
}