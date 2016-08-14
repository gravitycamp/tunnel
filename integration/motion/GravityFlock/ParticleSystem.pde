class ParticleSystem {

  ArrayList groups;

  public ParticleSystem() {
    groups = new ArrayList();
  }

  void add(ParticleGroup g) {
    groups.add(g);
  }

  void run() {
    Iterator it = groups.iterator();  
    while (it.hasNext()) {
      ParticleGroup g = (ParticleGroup)it.next();  
      g.run();
    }
  }
}

