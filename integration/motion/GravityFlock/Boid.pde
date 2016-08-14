import java.util.*;

class Boid extends Particle {

  private GravityFlock parent;

  /**
   * 	 * radius
   	 */
  private float r;

  /**
   * 	 * Maximum steering force
   	 */
  private float maxforce; 

  private float colorR;
  private float colorG;
  private float colorB;

  public static final float MIN_VELOCITY = 2.0f;
  public static final float MAX_VELOCITY = 4.0f;

  public static final float MIN_FORCE = 0.15f;
  public static final float MAX_FORCE = 0.25f;

  public static final float DESIRED_SEPARATION = 50.0f;
  public static final float MAX_NEIGHBOR_DISTANCE = 75.0f;

  public static final float DEAD_DISTANCE = 4.0f;
  public static final int MAX_TRAIL_LENGTH = 7;

  LinkedList trail;

  public Boid(GravityFlock p, PVector l) {
    this(p, l, MAX_VELOCITY, MAX_FORCE);
  }

  public Boid(GravityFlock p, PVector l, float ms, float mf) {
    super(p.physics, new PVector(), new PVector(p.random(-1 * ms, ms), p.random(-1 * ms, ms)), l);
    parent = p;

    //r = 2.0f;
    r = p.random(1, 4);
    colorR = p.random(1, 255);
    colorG = p.random(1, 255);
    colorB = p.random(1, 255);

    setMaxSpeed(ms);
    maxforce = mf;

    trail = new LinkedList();
  }

  void run() {
    if ( MAX_TRAIL_LENGTH > 0 ) {
      trail.add(new PVector(loc.x, loc.y, loc.y));
      if ( trail.size() >= MAX_TRAIL_LENGTH ) {
        trail.remove();
      }
    }

    flock(parent.flock.getParticles());
    update();
    borders();
    render();
  }

  // We accumulate a new acceleration each time based on three rules
  void flock(ArrayList boids) {
    PVector sep = separate(boids);   // Separation
    PVector ali = align(boids);      // Alignment
    PVector coh = cohesion(boids);   // Cohesion

    // Arbitrarily weight these forces
    //    sep.mult(2.0f);
    //    	ali.mult(1.0f);
    //    coh.mult(1.0f);

    // Add the force vectors to acceleration
    acc.add(sep);
    acc.add(ali);
    acc.add(coh);
  }


  /**
   * A method that calculates a steering vector towards a target
   * @param target
   * @param slowdown if true, slows down as it approaches the target
   */
  PVector steer(PVector target) {
    PVector steer;  // The steering vector
    PVector desired = PVector.sub(target,loc);  // A vector pointing from the location to the target
    float d = desired.mag(); // Distance from the target is the magnitude of the vector

    // If the distance is greater than 0, calculate steering (otherwise return zero vector)
    if (d > 0) {
      // Normalize desired
      desired.normalize();
      desired.mult( getMaxSpeed() );

      // Steering = Desired minus Velocity
      steer = PVector.sub(desired, vel);
      steer.limit(maxforce);  // Limit to maximum steering force
    } 
    else {
      steer = new PVector(0,0);
    }
    return steer;
  }

  /**
   * draw the boid - Draw a triangle rotated in the direction of velocity
   */
  void render() {
    parent.fill(colorR, colorG, colorB);
    //    parent.stroke(colorR, colorG, colorB);
    parent.ellipse(loc.x,loc.y, r, r);

    if ( MAX_TRAIL_LENGTH > 0 ) {
      float x = 255;
      for (ListIterator iter = trail.listIterator(trail.size()); iter.hasPrevious();) {
        PVector v = (PVector)iter.previous();
        x -= (255/MAX_TRAIL_LENGTH);
        parent.fill(colorR, colorG, colorB, x);
        parent.ellipse(v.x,v.y, r, r);
      }
    }
  }

  /**
   * shift object around if it has hit a border
   */
  void borders() {
    if (loc.x < -r) loc.x = parent.width + r;
    if (loc.y < -r) loc.y = parent.height + r;
    if (loc.x > parent.width + r) loc.x = -r;
    if (loc.y > parent.height + r) loc.y = -r;
  }


  /**
   * Method checks for nearby boids and steers away
   * @param boids
   * @return
   */
  PVector separate (ArrayList boids) {
    PVector sum = new PVector(0,0,0);
    int count = 0;

    Iterator it = boids.iterator();  
    while (it.hasNext()) {
      Particle other = (Particle)it.next();  

      float d = PVector.dist(loc, other.loc);

      // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
      if ( d > 0  && d < DESIRED_SEPARATION ) {
        // Calculate vector pointing away from neighbor
        PVector diff = PVector.sub(loc, other.loc);
        diff.normalize();
        diff.div(d);        // Weight by distance
        sum.add(diff);
        count++;            // Keep track of how many
      }
    }
    // Average -- divide by how many
    if (count > 0) {
      sum.div(count);
    }
    return sum;
  }

  /**
   * For every nearby boid in the system, calculate the average velocity
   * @param boids
   * @return
   */
  PVector align (ArrayList boids) {

    PVector sum = new PVector(0,0,0);
    int count = 0;

    Iterator it = boids.iterator();  
    while (it.hasNext()) {
      Particle other = (Particle)it.next();  

      float d = PVector.dist(loc,other.loc);
      if (d < MAX_NEIGHBOR_DISTANCE) {
        sum.add(other.vel);
        count++;
      }
    }
    if (count > 0) {
      sum.div(count);
      sum.limit(maxforce);
    }
    return sum;
  }

  /**
   * For the average location (i.e. center) of all nearby boids, calculate steering vector towards that location
   * @param boids
   * @return
   */
  PVector cohesion (ArrayList boids) {
    PVector sum = new PVector(0,0,0);   // Start with empty vector to accumulate all locations
    int count = 0;

    Iterator it = boids.iterator();  
    while (it.hasNext()) {
      Particle other = (Particle)it.next();  

      float d = PVector.dist(loc,other.loc);
      if ( d < MAX_NEIGHBOR_DISTANCE ) {
        sum.add(other.loc); // Add location
        count++;
      }
    }
    if (count > 0) {
      sum.div(count);
      return steer(sum);  // Steer towards the location
    }
    return sum;
  }

  public boolean dead() {
    Iterator it = parent.black_holes.getParticles().iterator();  
    while (it.hasNext()) {
      Particle p = (Particle)it.next();  
      float d = PVector.dist(loc, p.loc);

      if ( d <= DEAD_DISTANCE ) {
        return true;
      }
    }
    return false;
  }
}

