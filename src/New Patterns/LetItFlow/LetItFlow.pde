int numParticles = 2500;

Particle[] particles = new Particle[numParticles];

float mult = 150;
void setup() {
  //size(800, 800);
  fullScreen();
  frameRate(10000);
  smooth();
  colorMode(HSB);
  for (int i = 0; i < particles.length; i++) {
    particles[i] = new Particle();
  }
  fill(0, 10);
}

void draw() {
  rect(-1, -1, width+1, height+1);
  for (Particle p : particles) {
    p.update();
    p.show();
  }
}
