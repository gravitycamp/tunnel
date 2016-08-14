/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/9258*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
int maxNum = 15;
int num = 0;
particle[] Z = new particle[maxNum];

boolean time = true;
int t=-1;

void setup() {
  smooth();
  size(500,500);
  colorMode(HSB,1);
  frameRate(30);
  
  addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
      mouseWheel(evt.getWheelRotation());
  }});

  float r, phi;
  
  r = random(width/12, width/6);
  phi = random(TWO_PI);
  
}

void draw() {
  
  background(0);
  float r, h;
  
  if(time) {
    
  for(int i = 0; i < num; i++) {
    for(int j = 0; j < num; j++) {
      if( j!=i ) {

        if(Z[i].charge==Z[j].charge) {
          Z[i].repel(Z[j]);
        }
        else { 
          Z[i].gravitate(Z[j]);
        }

      }
    }
    Z[i].update();    
    Z[i].deteriorate();    
  }
  
  }
  
  for(int i = 0; i < num; i++) { // render lines
    for(int j = 0; j < num; j++) {
      if( j!=i ) {
        r = sqrt( (sq(Z[i].x-Z[j].x)+sq(Z[i].y-Z[j].y))/(sq(width)+sq(height)));
        if(Z[i].charge==Z[j].charge) {
          stroke(0,1,1,pow(1-r,3));
        }
        else {
          stroke(0.6,1,1,pow(1-r,3)); 
        }
        line( Z[i].x, Z[i].y, Z[j].x, Z[j].y );
      }
    }
  }
  
  for(int i = 0; i < num; i++) { // render particles over lines
    Z[i].display(0.4,0.7);
  }
  
  if(t>0) {
    if(t<150){t++;}
    stroke(1);
    fill(0.7);
    ellipse( mouseX, mouseY, t, t );
    stroke(0.4);
    fill(0.4);
    rect( mouseX-t*0.35,mouseY-t*0.07,t*0.7,t*0.14);
    if( mouseButton == LEFT ) {
      rect( mouseX-t*0.07,mouseY-t*0.35,t*0.14,t*0.7);
    }

    //stroke(1);
    //fill(0.7);
    //ellipse( mouseX, mouseY, t, t );
  }
  
}


void mousePressed() {
  if( num<maxNum ) {
    t=1;
  }
}

void mouseReleased() {
  float r, phi;
  
  r = sqrt( sq(mouseX-pmouseX) + sq(pmouseY-pmouseY) );
  phi = atan2( mouseY-pmouseY, mouseX-pmouseX );
  
  if( num<maxNum ) {
    num++;
    if( mouseButton == LEFT ) {
      Z[num-1] = new particle( mouseX, mouseY, r, phi, t/10.0, true );
    }
    else {
      Z[num-1] = new particle( mouseX, mouseY, r, phi, t/10.0, false );
    }
    t=-1;
  }
}

void mouseWheel(int delta) {
  float r=map(-delta,0,5,0,2);
  for(int i = 0; i < num; i++) {
    Z[i].magnitude+=r;
  }
}

void keyPressed() {
  time=!time;
}



