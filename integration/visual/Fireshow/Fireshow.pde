/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/2700*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
// Gravity Swarm
// Claudio Gonzales, July 2009
// Albuquerque, New Mexico

fireCracker[] Z;
boolean night = true;

void setup() {  
  size(500,500);
  Z = new fireCracker[75];
  frameRate(45);
  
  float phi;
  float r;
  for (int i=0;i<Z.length;i++) {
    r = random(6)+2;
    phi = random(3*HALF_PI-0.5,3*HALF_PI+0.5);
    Z[i] = new fireCracker( width/2 + random(width/5)-width/10, 
                            height, r*cos(phi), r*sin(phi), 
                            int(random(100)), int(random(50))+50, 50 );
  }
  
  smooth();
  background(255);
}

void draw() {
  if( night ) { filter(INVERT); }

  stroke(255,50);
  fill(255,50);
  rect(0,0,width,height);
  float phi, r;
  
  for (int i=0;i<Z.length;i++) {
    Z[i].update();
    Z[i].display();
    if( Z[i].dead() ) {
      phi = random(3*HALF_PI-0.5,3*HALF_PI+0.5);
      r = random(4)+4;
      Z[i].reset( width/2 + random(width/5)-width/10, 
                  height, r*cos(phi), r*sin(phi), 
                  int(random(100)), int(random(50))+50 );
    }
    
  }

  if( night ) { filter(INVERT); }

}

void mousePressed() {
  if( night ) { night = false; }
  else { night = true; }
}
