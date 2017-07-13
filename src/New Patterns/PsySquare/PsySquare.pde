void setup() {
  size(640, 640);
  noFill();
  stroke(0, 255, 150);
  rectMode(CENTER);
}

void draw() {
  background(20, 0, 50);
  translate(width/2, height/2);
  for (int i = 0; i < 360; i+=30) {
    float angle = sin(radians(i/2-frameCount*5))*15;
    if (angle > 1) {
      strokeWeight(angle);
    } else {
      strokeWeight(1);
    }
    pushMatrix();
    rotate(radians(i*angle/200));
    rect(0, 0, i, i);
    popMatrix();
  }
}


