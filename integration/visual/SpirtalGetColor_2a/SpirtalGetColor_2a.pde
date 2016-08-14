/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/7613*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/*SpiralGetColor por Marcos Frankowicz*/
        /* 02/Janeiro/2010 */
 /* modificado em: 14/Fevereiro/2010 */

//MovieMaker mm;
//import processing.video.*;
import controlP5.*;
ControlP5 controlP5;
ControlWindow controlW;
Slider s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16;
Slider s17,s18,s19,s20; // get pixels pos
///////////////////////////////////////////////

public int num = 264; //general number for arrays and loops

public int size1 = 2;
public int size2 = 4;

public int posX1 = 250;
public int posY1 = 250;
public int posX2 = 100;
public int posY2 = 100;

public int cAlpha1 = 20;
public int cAlpha2 = 10;

public int loopIni1 = 0;
public int loopIni2 = 0;

public int loopStep1 = 5;
public int loopStep2 = 10;

public int rotateValue1 = 36;
public int rotateValue2 = 2;

public int backGround = 0;

public int getx1 = 250;
public int gety1 = 250;
public int getx2 = 250;
public int gety2 = 250;

//////////////////////////////////////////////////
PImage p,p1;
float theta;

Draw[] Rect = new Draw[num];
Draw[] Rect2 = new Draw[num];

void setup(){
  size(420,420,P2D);
//  mm = new MovieMaker(this,width,height, "spiral2.mov",30,MovieMaker.H263, MovieMaker.BEST);
  controlP5 = new ControlP5(this);
  
  s2 = controlP5.addSlider("size1",1,50,2,10,220,150,10); //10 150
  s3 = controlP5.addSlider("size2",1,50,4,10,231,150,10);
  s4 = controlP5.addSlider("cAlpha1",0,50,20,10,22,150,10);
  s5 = controlP5.addSlider("cAlpha2",0,50,10,10,33,150,10);


  s13 = controlP5.addSlider("posX1",0,400,250,10,76,150,10);
  s14 = controlP5.addSlider("posY1",0,400,250,10,87,150,10);
  s15 = controlP5.addSlider("posX2",0,400,250,10,98,150,10);
  s16 = controlP5.addSlider("posY2",0,400,250,10,109,150,10);
  
  s17 = controlP5.addSlider("getx1",0,420,250,10,150,150,10);
  s18 = controlP5.addSlider("gety1",0,420,250,10,161,150,10);
  s19 = controlP5.addSlider("getx2",0,420,250,10,172,150,10);
  s20 = controlP5.addSlider("gety2",0,420,250,10,183,150,10);

  controlW = controlP5.addControlWindow("controlP5window",10,10,220,210);

  s2.setWindow(controlW);
  s3.setWindow(controlW);
  s4.setWindow(controlW);
  s5.setWindow(controlW);

  s13.setWindow(controlW);
  s14.setWindow(controlW);
  s15.setWindow(controlW);
  s16.setWindow(controlW);
  
  s17.setWindow(controlW);
  s18.setWindow(controlW);
  s19.setWindow(controlW);
  s20.setWindow(controlW);
  background(0);
  
  p1 = loadImage("image.gif");
  p = loadImage("noise1.png");
}

void draw(){

  fill(backGround,0);
  rect(0,0,width,height);

  for(int i = 0; i<num; i++){
  Rect[i] = new Draw(i - posX1, i - posY1, size1, p);
  Rect2[i] = new Draw(i - posX2, i - posY2, size2, p1);
  }
pushMatrix();
  for(int j = loopIni1; j<num; j+=loopStep1){
    pushMatrix();
      theta -= rotateValue1;
      translate(width/2,height/2);
      rotate(radians(theta));
      Rect[j].renderRect(cAlpha1);
    popMatrix();
  }
  
  
  for(int k = loopIni2; k<num; k+=loopStep2){
    pushMatrix();
      theta += rotateValue2;
      translate(width/2,height/2);
      rotate(radians(theta));
      Rect2[k].renderRect2(cAlpha2);
    popMatrix();
  }
popMatrix();
//mm.addFrame();
 controlP5.draw();
 
}
///////////
 color getColor(PImage pi,int x, int y){
   color c = pi.get(x,y);
   return c;
  }

void keyPressed(){
  if(key == 'p'){
    noLoop();
  }
  else if(key == ' '){
    loop();
  }
/*  else if(key == 's'){
    saveFrame("Spiral2-####.png");
  }
  else if(key == 'r'){
    mm.finish();
  }*/
}

/////////////////////////
