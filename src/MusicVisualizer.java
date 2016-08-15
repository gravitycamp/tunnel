import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;


public class MusicVisualizer extends PApplet {

    int   width;
    int   height;
    Tunnel tunnel;
    Minim minim;
    BeatDetect beat;
    int  r = 200;
    float rad = 70;
    AudioInput in;

    public MusicVisualizer(Tunnel t, int w, int h) {
        width  = w;
        height = h;
        tunnel = t;
        in =tunnel.in;
        
    }

  public void settings() {
    size(width, height);
  }
  public void setup()
  {

    minim = new Minim(this);
  
    beat = new BeatDetect();
    background(0);
  }
  
  public void draw()
  { 
    background(0);
    float t = map(mouseX, 0, width, 0, 1);
    beat.detect(in.mix);
    fill(1711896, 20);
    noStroke();
    rect(0, 0, width, height);
    translate(width/2, height/2);
    noFill();
    fill(-1, 10);
    if (beat.isOnset()) rad = (float)(rad*0.9);
    else rad = 70;
    ellipse(0, 0, 2*rad, 2*rad);
    stroke(-1, 50);
    int bsize = in.bufferSize();
    for (int i = 0; i < bsize - 1; i+=5)
    {
      float x = (r)*cos(i*2*PI/bsize);
      float y = (r)*sin(i*2*PI/bsize);
      float x2 = (r + in.left.get(i)*100)*cos(i*2*PI/bsize);
      float y2 = (r + in.left.get(i)*100)*sin(i*2*PI/bsize);
      line(x, y, x2, y2);
    }
    beginShape();
    noFill();
    stroke(-1, 50);
    for (int i = 0; i < bsize; i+=30)
    {
      float x2 = (r + in.left.get(i)*100)*cos(i*2*PI/bsize);
      float y2 = (r + in.left.get(i)*100)*sin(i*2*PI/bsize);
      vertex(x2, y2);
      pushStyle();
      stroke(-1);
      strokeWeight(2);
      point(x2, y2);
      popStyle();
    }
    endShape();
  }
}