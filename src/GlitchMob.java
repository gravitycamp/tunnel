/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */ //<>//
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.video.*;
import processing.core.*;
import ddf.minim.*;
//import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import KinectPV2.KJoint;
import KinectPV2.*;
import java.util.*;

public class GlitchMob extends PApplet {

    int width;
    int height;
    Tunnel tunnel;
    Movie movie;
    FFT fft;
    //Minim minim;
    //AudioInput in;
    
    KinectPV2 kinect;
    int [] depthZero;

    float md;
    float mt;
    PImage dot;
    PImage colors;
    float[] fftFilter;
    float spin = (float)0.003;
    float radiansPerBucket = radians(2);
    float decay = (float)0.96;
    float opacity = 20;
    float minSize = (float)0.1;
    float sizeScale = (float)0.5;

    public GlitchMob(Tunnel t, int w, int h) {
        width = w;
        height = h;
        tunnel = t;
        fft = tunnel.fft;
        //minim = new Minim(this);
        //in = minim.getLineIn();
        //fft = new FFT(in.bufferSize(), in.sampleRate());
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        //movie = new Movie(this, "C:/TunnelGit2/src/data/glitch_mob.mp4");
        movie = new Movie(this, "F:/Tunnel2/src/data/glitch_mob.mp4");
        movie.loop();

        PApplet sketch = new Video(movie, 400, 400);
        String[] args = {"Video",};
        PApplet.runSketch(args, sketch);
        
        //dot = loadImage("C:/TunnelGit2/src/data/dot.png");
        //colors = loadImage("C:/TunnelGit2/src/data/colors.png");
        dot = loadImage("F:/Tunnel2/src/data/dot.png");
        colors = loadImage("F:/Tunnel2/src/data/colors.png");
        
        fftFilter = new float[fft.specSize()];
        
        depthZero    = new int[ KinectPV2.WIDTHDepth * KinectPV2.HEIGHTDepth];        
        kinect = new KinectPV2(this);
        kinect.enableDepthImg(true);
        kinect.enableSkeleton3DMap(true);
        kinect.init();
        
        
    }


    public void draw() {

        synchronized(Tunnel.class) {

            md = movie.duration();
            mt = movie.time();
            background(0);
            
            if(mt<0.01*md)
              Equilizer();
            else if(mt <0.02*md)
              FlyingBalls();
            else 
              LightControl();

        }

    }  

    public void Equilizer() {
        rectMode(CORNERS);
        int scale = width/20;
        for(int i = 0; i < scale*fft.specSize(); i+=(scale/5))
        {
          stroke(135);
  //      line(i, height, i, height - fft.getBand(i)*8);
          rect( i+width/2, height, i+(scale/5)+width/2, height - fft.getBand(i)*35 );
          rect( width - (i+width/2), height, width - (i+(scale/5)+width/2), height - fft.getBand(i)*35 );
        }
    }

    public void FlyingBalls(){
      {
     //   fft.forward(in.mix);
        for (int i = 0; i < fftFilter.length; i++) {
          fftFilter[i] = max(fftFilter[i] * decay, log(1 + fft.getBand(i)) * (float)(1 + i * 0.01));
        }
        
        for (int i = 0; i < fftFilter.length; i += 3) {   
        //  color rgb = colors.get((map(i, 0, fftFilter.length-1, 0, colors.width-1)), colors.height/2);
          tint(colors.get((int)map(i, 0, fftFilter.length-1, 0, colors.width-1), colors.height/2));
          blendMode(ADD);
       
          float size = height * (minSize + sizeScale * fftFilter[i]);
          PVector center = new PVector((float)(width * (fftFilter[i] * 0.2)), 0);
          center.rotate(millis() * spin + i * radiansPerBucket);
          center.add(new PVector((float)(width * 0.5), (float)(height * 0.5)));
       
          image(dot, center.x - size/2, center.y - size/2, size, size);
          fill(colors.get((int)map(i, 0, fftFilter.length-1, 0, colors.width-1), colors.height/2));
          ellipseMode(CORNER);
          //ellipse(center.x - size/2, center.y - size/2, size/15, size/15);
          //ellipse(center.x + size/2, center.y - size/2, size/15, size/15);
          //ellipse(center.x - size/2, center.y + size/2, size/15, size/15);
          //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
          //ellipse(center.x + size/2, center.y + size/2, size/15, size/15);
        }
      }
    }
    public void LightControl(){
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
        ellipse(width*depth_LeftHand_Ratio, height*(1-LeftHandRaisedRatio), 5*fft.getBand(0), 5*fft.getBand(0));
        ellipse(width*depth_RightHand_Ratio, height*(float)(1-RightHandRaisedRatio), 5*fft.getBand(0), 5*fft.getBand(0));
      
      
      
    }



    public void movieEvent(Movie movie) {
        movie.read();
    }
}