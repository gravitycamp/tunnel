float rota;
float dir;

float verm = 150;
float verms = 2;

float azul = 150;
float azuls = 0.5;

float verd = 150;
float verds = 1.5;

void setup(){
  size(1920,1080);
  background(255); 
  dir = 1;
  frameRate(60);
}

void draw(){
  noStroke();
  noCursor();
  translate(width/2, height/2);
  fill(verm, verd, azul);
  
  //for(int i=0;;){}
  pushMatrix();
  rotate(rota);
  rect(0, 0, 20, 20, 5);
  popMatrix();
      
  for(int i=1;i<=60;i++){
    if(i%2==0){
      pushMatrix();
      //rotate(rota+i*2);
      rotate((rota*i*4)*dir);
      translate(i*25-i*6,0);
      rect(0, 0, 20, 20, 5);
      popMatrix();
    }
    else{
      pushMatrix();
      //rotate(-rota-i*2);
      rotate((rota+i*4)*-dir);
      translate(i*25-i*6,0);
      rect(0, 0, 20, 20, 5);
      popMatrix();
    }
  }
 

  rota += 0.05;
  
  verm += verms;
  if(verm >= 255 || verm <= 0) {verms = verms*-1;}
  if(verm <= 0) {verms = random(1,5);}
  
  verd += verds;
  if(verd >= 255 || verd <= 0) {verds = verds*-1;}
  if(verd <= 0) {verds = random(1,5);}
  
  azul += azuls;
  if(azul >= 255 || azul <= 0) {azuls = azuls*-1;}
  if(azul <= 0) {azuls = random(1,5);}
  
}

void mouseClicked(){
  dir = dir*-1;
}