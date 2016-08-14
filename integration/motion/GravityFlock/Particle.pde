class Particle {
  PVector loc;
  PVector vel;
  PVector acc;
  ParticleSystem parent;

  private float maxspeed = 0f;
  public boolean resetAcceleration = true;
  private boolean _dead = false;

  Particle(ParticleSystem p, PVector a, PVector v, PVector l) {
    parent = p;
    acc = a.get();
    vel = v.get();
    loc = l.get();
  }

  float getMass() {
    return 1.0f;
  }

  void setMaxSpeed(float s) {
    maxspeed = s;	  
  }

  float getMaxSpeed() {
    return maxspeed;	  
  }

  void run() {
    update();
    render();
  }

  // Method to update location
  void update() {
    vel.add(acc);

    // Limit speed
    if ( maxspeed > 0 ) {
      vel.limit(maxspeed);
    }

    loc.add(vel);

    // Reset acceleration to 0 each cycle
    if ( resetAcceleration ) {
      acc.set(0,0,0);
    }
  }

  // Method to display
  void render() {

  }


  public boolean dead() {
    return _dead;
  }
}

