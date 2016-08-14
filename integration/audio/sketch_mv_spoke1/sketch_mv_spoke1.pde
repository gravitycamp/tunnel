/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/191445*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
import ddf.minim.*;
import ddf.minim.analysis.*;

Minim minim;
AudioPlayer myPlayer;
FFT fftL;
FFT fftR;

float[][] _spectrumArr;
int _barScaling;
color _barColorL;
color _barColorR;
color _barColorL2;
color _barColorR2;
float _barWeight;
color _bgColor;
float _rotationSpeed = 0.025;
float _hilight = 4.5;
float _noiseSeed;

void setup()
{
  size(800, 480);
  frameRate(30);
  colorMode(HSB, 360, 100, 100, 100);

  minim = new Minim(this);
  myPlayer = minim.loadFile("groove.mp3", 1024);
  myPlayer.loop();
  fftL = new FFT( myPlayer.bufferSize(), myPlayer.sampleRate() );
  fftR = new FFT( myPlayer.bufferSize(), myPlayer.sampleRate() );

  _spectrumArr = new float[2][9];
  _barScaling = 85;
  _barWeight = 1.25;
  _barColorL = color(188, 80, 30);
  _barColorR = color(188, 80, 100);
  _barColorL2 = color(310, 80, 40);
  _barColorR2 = color(310, 50, 100);
  _bgColor = color(0, 0, 0, 30);
}

void draw()
{
  _noiseSeed += 0.005;
  screenRefresh();
  translate(width*(0.5+0.1*(noise(_noiseSeed)-0.5)), height*(0.5+0.1*(noise(_noiseSeed)-0.5)));
  rotate(_rotationSpeed*frameCount);

  // R channel
   fftR.forward( myPlayer.right );
   for (int i = 0; pow(2,i+1)-1 < fftR.specSize(); i++)
   {
   int bandWidth = int(pow(2, i));
   int stBand = int(pow(2, i)-1);
   _spectrumArr[1][i] = 0;
   for (int v = 0; v < bandWidth; v++)
   {
   _spectrumArr[1][i] += fftR.getBand(stBand+v);
   }
   drawSpoke(1, i, _barColorR, _barColorR2);
   }
   
   rotate(PI/18);
   
  //L channel
  fftL.forward( myPlayer.left );
  for (int i = 0; pow(2,i+1)-1 < fftL.specSize(); i++)
  {
    int bandWidth = int(pow(2, i));
    int stBand = int(pow(2, i)-1);
    _spectrumArr[0][i] = 0;
    for (int v = 0; v < bandWidth; v++)
    {
      _spectrumArr[0][i] += fftL.getBand(stBand+v);
    }
    drawSpoke(0, i, _barColorL, _barColorL2);
  }
}

void stop()
{
  myPlayer.close();
  minim.stop();
  super.stop();
}


void drawSpoke(int LR, int i, color barColor, color barColor2) {
  rotate(TWO_PI/9);
  noFill();
  println(log(_spectrumArr[LR][i]+1));
  if(log(_spectrumArr[LR][i]+1)>_hilight){
    stroke(barColor2);
  }else{
    stroke(barColor);
  }
  //strokeWeight(2);
  strokeWeight(_barWeight*log(_spectrumArr[LR][i]+1));
  smooth();
  line(random(0,10), random(0,10), log(_spectrumArr[LR][i]+1)*_barScaling, 0);
}

void screenRefresh() {
  noStroke();
  fill(_bgColor);
  rect(0, 0, width, height);
}

