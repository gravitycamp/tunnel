/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */ //<>//
/* !do not delete the line above, required for linking your tweak if you upload again */
import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.video.*;
import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import KinectPV2.KJoint;
import KinectPV2.*;
import java.util.*;

public class AudioInputKinect extends PApplet {

    int width;
    int height;
    Tunnel tunnel;
    FFT fft;
    //Minim minim;
    //AudioInput in;
    
    KinectPV2 kinect;
    int [] depthZero;

    PImage dot;
    PImage colors;
    float[] fftFilter;
    float spin = (float)0.003;
    float radiansPerBucket = radians(2);
    float decay = (float)0.96;
    float opacity = 20;
    float minSize = (float)0.1;
    float sizeScale = (float)0.4;
    float textScale = 0;
    PGraphics LeftWall;
    PGraphics RightWall;
    PGraphics Roof;
    double StartTime=0;
    double patternTime = 0;
    

    public AudioInputKinect(Tunnel t, int w, int h) {
        width = w;
        height = h;
        tunnel = t;
        fft = tunnel.fft;

    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        dot = loadImage("C:/TunnelGit2/src/data/dot.png");
        colors = loadImage("C:/TunnelGit2/src/data/colors.png");
        //dot = loadImage("F:/Tunnel2/src/data/dot.png");
        //colors = loadImage("F:/Tunnel2/src/data/colors.png");        
        fftFilter = new float[fft.specSize()];
        LeftWall= createGraphics(150, 32);
        RightWall= createGraphics(150, 32);
        Roof= createGraphics(150, 24);
        StartTime = millis();
   
        depthZero    = new int[ KinectPV2.WIDTHDepth * KinectPV2.HEIGHTDepth];        
        kinect = new KinectPV2(this);
        kinect.enableDepthImg(true);
        kinect.enableSkeleton3DMap(true);
        kinect.init();

     //
        
    }


    public void draw() {
      try{
        synchronized(Tunnel.class) {
            background(0);
            LeftWall.beginDraw();
            LeftWall.background(0);
            RightWall.beginDraw();
            RightWall.background(0);
            
   
            Roof.beginDraw();
            Roof.background(0,70,140);
            if (millis()-StartTime <=30)
            {
              Equilizer(LeftWall);  
              Equilizer(RightWall); 
            }
            else if (millis()-StartTime > 30)
            {
              FlyingBalls(LeftWall);  
              FlyingBalls(RightWall);     
            }






            LeftWall.endDraw();
            RightWall.endDraw();
            Roof.endDraw();
            image(LeftWall, 0, 0);
            image(Roof, 0, 32);
            image(flipImage(RightWall.get()), 0, 32+24);

            //FlyingBalls();
            //LightControl();
        }
      }
      catch(Exception e){}
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
        rectMode(CORNERS);
        int scale = Image.width/20;
        for(int i = 0; i < scale*fft.specSize(); i+=(scale/5))
        {
          stroke(135);
  //     line(i, Image.height, i, Image.height - fft.getBand(i)*35);
          Image.rect( i+Image.width/2, Image.height, i+(scale/5)+Image.width/2, Image.height - fft.getBand(i)*35 );
          Image.rect( Image.width - (i+Image.width/2), Image.height, Image.width - (i+(scale/5)+Image.width/2), Image.height - fft.getBand(i)*20 );
        }
       // LeftWall=Image;
    }

    public void FlyingBalls(PGraphics Image){
      for (int i = 0; i < fftFilter.length; i++) {
        fftFilter[i] = max(fftFilter[i] * decay, log(1 + fft.getBand(i)) * (float)(1 + i * 0.01));
      }
      
      for (int i = 0; i < fftFilter.length; i += 3) {   
      //  color rgb = colors.get((map(i, 0, fftFilter.length-1, 0, colors.width-1)), colors.height/2);
        tint(colors.get((int)map(i, 0, fftFilter.length-1, 0, colors.width-1), colors.height/2));
        blendMode(ADD);
     
        float size = Image.height * (minSize + sizeScale * fftFilter[i]);
        PVector center = new PVector((float)(Image.width * (fftFilter[i] * 0.2)), 0);
        center.rotate(millis() * spin + i * radiansPerBucket);
        center.add(new PVector((float)(Image.width * 0.5), (float)(Image.height * 0.5)));
     
        Image.image(dot, center.x - size/2, center.y - size/2, size, size);
        fill(colors.get((int)map(i, 0, fftFilter.length-1, 0, colors.width-1), colors.height/2));
        ellipseMode(CORNER);
        //ellipse(center.x - size/2, center.y - size/2, size/15, size/15);
        //ellipse(center.x + size/2, center.y - size/2, size/15, size/15);
        //ellipse(center.x - size/2, center.y + size/2, size/15, size/15);
        //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
        //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
      }
    }
    public void LightControl(){
      fill(150, 0, 0);
      pushMatrix();
      translate(width / 2, height / 2);
    //  textAlign(CENTER, CENTER);
      textSize(20); 
        
      scale((float) 0.1 + sin(textScale), 1);
      text("INTERACTIVE", 110, +55);
      popMatrix();
      textScale += 0.02;
      
      float RightHandRaisedRatio = 0;
      float LeftHandRaisedRatio = 0;
      float depth_RightHand_Ratio = 0;
      float depth_LeftHand_Ratio =0;
      ArrayList<KSkeleton> skeletonArray =  kinect.getSkeleton3d();
      int [] DepthRaw = kinect.getRawDepthData();
      //individual JOINTS
      PVector RightWristP;
      PVector LeftWristP;
      PVector LeftKneeP;
      PVector RightKneeP;
      PVector HeadP = null;
      float RightWristdepth = 0;
      float LeftWristdepth = 0;
      float HeadDepth = 0;
      for (int i = 0; i < skeletonArray.size(); i++) {
        KSkeleton skeleton = (KSkeleton) skeletonArray.get(i);
        if (skeleton.isTracked()) {
          KJoint[] joints = skeleton.getJoints();
          RightWristP = joints[KinectPV2.JointType_WristRight].getPosition();
          LeftWristP = joints[KinectPV2.JointType_WristLeft].getPosition();
          RightKneeP = joints[KinectPV2.JointType_KneeRight].getPosition();
          LeftKneeP = joints[KinectPV2.JointType_KneeLeft].getPosition();
          HeadP = joints[KinectPV2.JointType_Head].getPosition();   
          RightWristdepth = joints[KinectPV2.JointType_WristRight].getZ();
          LeftWristdepth = joints[KinectPV2.JointType_WristLeft].getZ();
          HeadDepth = joints[KinectPV2.JointType_Head].getZ(); 
          //Ratio calculation and calibration
          depth_RightHand_Ratio = RightWristdepth/5; //4 is as deep as you can go!
          depth_LeftHand_Ratio = LeftWristdepth/5; //4 is as deep as you can go!
          RightHandRaisedRatio = (float)(RightWristP.y-RightKneeP.y*.85)/(HeadP.y - RightKneeP.y);
          LeftHandRaisedRatio = (float)(LeftWristP.y-LeftKneeP.y*.85)/(HeadP.y - LeftKneeP.y);
        }
      }
        
        //we now have ratios, now draw
        
        ellipseMode(RADIUS);
        println(LeftHandRaisedRatio + " new = " + (.2+1/(LeftHandRaisedRatio+.4)));
        fill( random(150), random(150), random(150), random(150)); 
        ellipse(width*depth_LeftHand_Ratio, height*(1-LeftHandRaisedRatio), 5*fft.getBand(0), 5*fft.getBand(0));
        fill( random(150), random(150), random(150), random(150)); 
        ellipse(width*depth_RightHand_Ratio, height*(float)(1-RightHandRaisedRatio), 5*fft.getBand(0), 5*fft.getBand(0));      
      
    }




}