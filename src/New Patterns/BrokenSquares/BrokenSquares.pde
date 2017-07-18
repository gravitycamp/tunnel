//Peter Farrell 5/29/15
//inspired by Zulko's blog post
//http://zulko.github.io/blog/2014/09/20/vector-animations-with-python/
//looks like broken squares but it's rotating stars!

int rotAngle = 0;
float offset = 0;

void setup(){
  size(200,200);
  background(0);
 
}

void draw(){
  frameRate(20);
  background(0);
  rotate(radians(10));
  starField(offset);
  rotAngle += 1;
  offset += 1;
  if (offset == 56){
    offset = 0;
  }
}

void starField(float offSet){
  for (int i=0;i<6;i++){
    for(int j = 0;j<6;j++){
      if((i+j)%2==0){
      star1(40*1.4*i-offSet,40*1.4*j-offSet);
      }else{
      star2(40*1.4*i-offSet,40*1.4*j-offSet);
      }
    }
  }
}

void star1(float x, float y){
  pushMatrix();
  translate(x,y);
  rotate(radians(rotAngle));
  rectMode(CENTER);
  //rect(0,0,40,40);
  pushMatrix();
  for(int k=0;k<4;k++){
    if(k%2==0){
      fill(255,0,0);
    }else{
      fill(0,0,255);
    }
    stroke(255);
    triangle(0,0,-40,-30,0,-30);
    rotate(radians(90));
  }
  popMatrix();
  popMatrix();
}

void star2(float x, float y){
  pushMatrix();
  translate(x,y);
  rotate(radians(rotAngle));
  rectMode(CENTER);
  //rect(0,0,40,40);
  pushMatrix();
  for(int k=0;k<4;k++){
    if(k%2==1){
      fill(255,0,0);
    }else{
      fill(0,0,255);
    }
    stroke(255);
    triangle(0,0,-40,-30,0,-30);
    rotate(radians(90));
  }
  popMatrix();
  popMatrix();
}