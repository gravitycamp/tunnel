import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.video.*;
import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import java.util.*;

public class PatternsTimer extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;
  FFT fft;
  Minim minim;
  //AudioInput in;

  PImage dot;
  PImage colors;
  float[] fftFilter;
  float spin = (float)0.003;
  float radiansPerBucket = radians(2);
  float decay = (float)0.096;
  float opacity = 20;
  float minSize = (float)0.05;
  float sizeScale = (float)0.1;
  float textScale = 0;
  PGraphics LeftWall;
  PGraphics RightWall;
  PGraphics Roof;
  double StartTime=0;
  double patternTime = 0;
  int MaxBrightness = 190;
  int R = (int)random(MaxBrightness);
  int G = (int)random(MaxBrightness);
  int B = (int)random(MaxBrightness);
  int scale;  
  int counter = 0;
  int dR = 1;
  int dG = 2;
  int dB = 3;

  public PatternsTimer(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
    fft = tunnel.fft;
    scale = h/88;
  }

  public void settings() {
    size(width, height);
  }

  public void setup() {
    frameRate(40);
    dot = loadImage("C:/DeepPsyTunnel/src/data/dot1.png");
    colors = loadImage("C:/DeepPsyTunnel/src/data/colors.png");
    fftFilter = new float[fft.specSize()];

    LeftWall= createGraphics(150*scale, 32*scale);
    RightWall= createGraphics(150*scale, 32*scale);
    Roof= createGraphics(150*scale, 24*scale);
    StartTime = millis();
  }

  float trackX = 0;
  float trackY = 0;
  float trackZ = 0;
  float trackXL = 0;
  float trackXR = 0;
  float trackYL = 0;
  float trackYR = 0;
  boolean IsTracking= false;

  public void track() {
    if (Main.kinect != null) {
      Main.kinect.update();
      IsTracking = Main.kinect.IsTracking;

      switch (position) {
      case "Tunnel":
        trackXL = (float)LeftWall.width * Main.kinect.LeftHandDepthRatio;
        trackXR = (float)RightWall.width * Main.kinect.RightHandDepthRatio;
        trackYL = (float)LeftWall.height * Main.kinect.LeftHandRaisedRatio;
        trackYR = (float)RightWall.height * Main.kinect.RightHandRaisedRatio;
      case "Wall":
      case "Ceil":
        break;
      case "RWall":
        break;
      case "LWall":
        break;
      }
    } else {
      trackXL = mouseX;
      trackXR = mouseX;
      trackYL = mouseY;
      trackYR = mouseY;
    }
  }

  public void draw() {
    try {
      synchronized(Tunnel.class) {
        track();
        background(0);
        LeftWall.beginDraw();
        LeftWall.background(0);
        RightWall.beginDraw();
        RightWall.background(0);
        Roof.beginDraw();
        Roof.background(0, 70, 140);
        if (millis()-StartTime <=10*1000)
        {
          Equilizer(LeftWall);  
          Equilizer(RightWall);
        } else if (millis()-StartTime < 20*1000)  //seconds to play upto
        {
          FlyingBalls(LeftWall);  
          FlyingBalls(RightWall);  
          SmoothRGB(); //<>//
          Roof.background(R, G, B);
        } else if (millis()-StartTime < 40*1000)
        {
          LightControl(LeftWall, RightWall);  
          SmoothRGB(); //<>//
          Roof.background(R, G, B);
        } else
        { 
          Equilizer(LeftWall);  
          Equilizer(RightWall);
          SmoothRGB(); //<>//
          Roof.background(R, G, B);
          Roof.stroke(MaxBrightness-R,MaxBrightness-G,MaxBrightness-B);
          strokeWeight(20*scale);
          Roof.line(counter, 0, counter, 24*scale);
          counter+=scale;
          counter%=Roof.width;
        }

        LeftWall.endDraw();
        RightWall.endDraw();
        Roof.endDraw();
        image(RightWall, 0, 0);
        image(Roof, 0, 32*scale);
        image(flipImage(LeftWall.get()), 0, (32+24)*scale);

      }
    }
    catch(Exception e) {
    }
  }  

  private PImage flipImage(PImage image)
  {
    BufferedImage img = (BufferedImage) image.getNative();
    AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
    tx.translate(0, -img.getHeight(null));
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    return new PImage(op.filter(img, null));
  }

  public void Equilizer(PGraphics Image) {
    rectMode(CORNER);

    for (int i = 0; i < 70; i+=scale)
    {
      Image.colorMode(HSB);
      Image.stroke(i*2, 135, 135);
      Image.fill(i*2, 135, 135);
      float amp = 3*fft.getBand(i);
      if (amp*.8 > height)
        amp = (float)(height*.8);
      Image.rect( i+Image.width/2, 0, scale, amp);
      Image.rect( Image.width - (i+Image.width/2), 0, -1*scale, amp );
      ellipseMode(CENTER);
      Image.ellipse((float)(.2*Image.width), (float)(.5*Image.height), amp, amp);
      Image.ellipse((float)(.8*Image.width), (float)(.5*Image.height), amp, amp);
    }
  }

  public void FlyingBalls(PGraphics Image) {
    strokeCap(ROUND);
    for (int i = 0; i < fftFilter.length; i++) {
      fftFilter[i] = max(fftFilter[i] * decay, log(1 + fft.getBand(i)) * (float)(1 + i * 0.01));
    }

    for (int i = 0; i < fftFilter.length; i += 3) {   
      tint(colors.get((int)map(i, 0, (fftFilter.length)-1, 0, colors.width-1), colors.height/2));
      blendMode(ADD);

      float size = Image.height * (minSize + sizeScale * fftFilter[i]);
      PVector center = new PVector((float)(Image.height * (fftFilter[i] * 0.15)), 0);
      center.rotate(millis() * spin + i * radiansPerBucket);
      center.add(new PVector((float)(Image.width * 0.5), (float)(Image.height * 0.5)));

      Image.image(dot, center.x - size/2, center.y - size/2, size, size);
      //fill(colors.get((int)map(i, 0, fftFilter.length-1, 0, colors.width-1), colors.height/2));
      //ellipseMode(CORNER);
      //ellipse(center.x - size/2, center.y - size/2, size/15, size/15);
      //ellipse(center.x + size/2, center.y - size/2, size/15, size/15);
      //ellipse(center.x - size/2, center.y + size/2, size/15, size/15);
      //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
      //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
    }
  }

  public void LightControl(PGraphics ImageL, PGraphics ImageR) {
    ImageL.ellipseMode(RADIUS);
    ImageR.ellipseMode(RADIUS);
    ImageL.fill( random(255), random(255), random(255), random(255)); 
    ImageL.ellipse(trackXL, (1-trackYR), 2*fft.getBand(0), 2*fft.getBand(0));
    ImageR.fill( random(150), random(255), random(255), random(255)); 
    ImageR.ellipse(trackXR, (float)(1-trackYL), 3*fft.getBand(0), 3*fft.getBand(0));
  }

  public void NewAnimation(PImage Image)
  {
  }

  
  //color fading function
  void SmoothRGB() {
    R+=dR;
    G+=dG;    
    B+=dB;
    if ((R <= 1) || (R >= MaxBrightness))  // if out of bounds
      dR = - dR; // swap direction  
    if ((G <= 1) || (G >= MaxBrightness))  // if out of bounds
      dG = - dG; // swap direction  
    if ((B <= 1) || (B >= MaxBrightness))  // if out of bounds
      dB = - dB; // swap direction
  }
}