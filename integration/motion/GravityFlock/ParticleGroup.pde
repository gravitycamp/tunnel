
class ParticleGroup {

  private ArrayList particles;

  public ParticleGroup(ParticleSystem p) {
    particles = new ArrayList();
  }

  public void add(Particle p) {
    particles.add(p);
  }

  public void remove(Particle p) {
    particles.remove(p);
  }

  public ArrayList getParticles() {
    return particles;
  }

  public void run() {
    Iterator it = particles.iterator();  
    while (it.hasNext()) {
      Particle p = (Particle)it.next();  
      p.run();
    }
  }
}

