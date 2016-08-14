/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/7689*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/*based on Dynamic Acceleration tutorial by Daniel Shiffman*/
/* Marcos Frankowicz */
/* 17/02/2010 */
/*if the frameRate is to slow you cam change the "num" variable*/
/*press mouse to "release" the particles from central force*/

int num = 500;

Coisa[] c = new Coisa[num];
PVector[] cLoc = new PVector[num];
PVector[] cVel = new PVector[num];
PVector[] cAcc = new PVector[num];
PVector[] cCor = new PVector[num];
void setup(){
  size(600,400,P2D);
 
  background(74,113,137);
  for(int i = 0; i<num; i++){
  cLoc[i] = new PVector(random(width),random(height));
  cVel[i] = new PVector(0.0, 0.0);
  cAcc[i] = new PVector(0.0,0.125);
  cCor[i] = new PVector(140,200,250);
  c[i] = new Coisa(cLoc[i],cVel[i],cAcc[i],cCor[i]);
  }
}

void draw(){
 noStroke();
 filter(DILATE);
 
 fill(0,20,40,15);
 rect(0,0,width,height);
 
  for(int i = 0; i<num; i++){
    c[i].go();
    PVector m = new PVector();
    m.set(width/2,pmouseY,0);
    PVector diff = PVector.sub(m,c[i].getLoc());
    diff.normalize();
    float f = 4.2;
    diff.div(f);
    c[i].setAcc(diff);
    
      if(mousePressed){
      c[i].setAcc(new PVector(0,0));
      }
  }

}

/*void keyPressed(){
  save("image.png");
}*/