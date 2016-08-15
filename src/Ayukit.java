/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */ //<>// //<>// //<>//
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

public class Ayukit extends PApplet {

    int width;
    int height;
    Tunnel tunnel;
    Minim minim;
    
    KinectPV2 kinect;
    int [] depthZero;

    //Ayukit data
    PImage Ayukit; 
    PImage Logo;
    PImage Ken;
    double AY_depth = 0;
    double AY_Y = 0;
    boolean AY_OnLeftWall = true;
    boolean AyukitFound = false; 
    int AyukitFrame = 0;
    AudioPlayer  Ayukit_Sound;
    AudioPlayer  VS_Sound;
    AudioPlayer  Stage_Sound;
    int R = (int)random(255);
    int G = (int)random(255);
    int B = (int)random(255);
    int scale = 4;  
    int dR = 1;
    int dG = 1;
    int dB = 1;
    
    float kenMove = 0;
    float kenDelta = (float).4;
    
    PGraphics LeftWall;
    PGraphics RightWall;
    PGraphics Roof; 
    float timeElapsed;

    public Ayukit(Tunnel t, int w, int h) {
        width = w;
        height = h;
        tunnel = t;
        minim = tunnel.minim;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        Ayukit = loadImage("C:/TunnelGit2/src/data/Aukit_BK.jpg");
        Logo = loadImage("C:/TunnelGit2/src/data/logo.jpg");
        Ken = loadImage("C:/TunnelGit2/src/data/Ken.jpg");
        Ayukit.resize(15,15);
        Ken.resize((int)((.6*32*scale)/Ken.height*Ken.width),(int)(.6*32*scale));
        Ken=flipImage(Ken);
        Ayukit_Sound = minim.loadFile("C:/TunnelGit2/src/data/Ayukit.mp3");        
        VS_Sound = minim.loadFile("C:/TunnelGit2/src/data/StreetFightervs.mp3");
        Stage_Sound = minim.loadFile("C:/TunnelGit2/src/data/RyuStage.mp3");
        VS_Sound.play();

        background(0);
        image(flipImage(Logo.get()), 0, 0, 150*scale, 32*scale);
        image(Logo, 0, (32+24)*scale, 150*scale, 32*scale);
        timeElapsed = millis();

        LeftWall= createGraphics(150*scale, 32*scale);
        RightWall= createGraphics(150*scale, 32*scale);
        Roof= createGraphics(150*scale, 24*scale);
     //   StartTime = millis();
   
        depthZero    = new int[ KinectPV2.WIDTHDepth * KinectPV2.HEIGHTDepth];        
        kinect = new KinectPV2(this);
        kinect.enableDepthImg(true);
        kinect.enableSkeleton3DMap(true);
        kinect.init(); //<>//
    }


    public void draw() {
      try{
        synchronized(Tunnel.class) {
          if((millis()-timeElapsed)>3500)
          {
      //      background(40,150,10);
                    Stage_Sound.play();
                    
                    
                    
            
            LeftWall.beginDraw();
            LeftWall.background(0);
            RightWall.beginDraw();
            RightWall.background(0);
            Roof.beginDraw();
            
            
            SmoothRGB();
            Roof.background(R,G,B);          
            
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
            
            //draw Ken

            kenMove +=kenDelta;
            if((kenMove> 0.4*32*scale) || (kenMove<0))
              kenDelta = - kenDelta;
            RightWall.copy(Ken, 0,0, Ken.width, Ken.height+(int)kenMove, 0, (int)kenMove, Ken.width, Ken.height+(int)kenMove);
        
            //we now have ratios, now draw game logic 
            if(!AyukitFound &&(LeftWristdepth/HeadDepth < .9) && (RightWristdepth/HeadDepth < .9))    //create an ayukit at right hand y = center of ayukit
            {
              //println(HeadP.x);
              AyukitFound = true;
              AY_depth = depth_RightHand_Ratio;
              AY_Y = RightHandRaisedRatio;
              if(HeadP.x < 100) //head is on left or right side (need to update center of screen = width/2 
                AY_OnLeftWall = true;
              Ayukit_Sound.play(); 
              AyukitFrame = 0;
            }
            
            //test data
           if(!AyukitFound && (millis()-timeElapsed)>5500)
           {
              AY_depth = 0.5;
              AY_Y = 0.5;
              AY_OnLeftWall = true;   
              AyukitFound = true;
              Ayukit_Sound.play(); 
           }
            
            if(AyukitFound)
            {
              RightWall.copy(Ayukit, 0,0, Ayukit.width, Ayukit.height, (int)(RightWall.width*AY_depth-AyukitFrame), (int)(RightWall.height*AY_Y), Ayukit.width, Ayukit.height);
              //println("X = "+ (RightWall.width*AY_depth-AyukitFrame));
              AyukitFrame+=2;
              if(RightWall.width*AY_depth-AyukitFrame <0)
                AyukitFound = false;
            }
            
            //finalize draw
            LeftWall.endDraw();
            RightWall.endDraw();
            Roof.endDraw();
            image(RightWall, 0, 0);
            image(Roof, 0, 32*scale);
            image(flipImage(LeftWall.get()), 0, (32+24)*scale);
          }
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
    
    void SmoothRGB(){
    R+=dR;
    G+=dG;    
    B+=dB;
  
    if ((R <= 5) || (R >= 250))  // if out of bounds
      dR = - dR; // swap direction  
    if ((G <= 5) || (G >= 250))  // if out of bounds
      dG = - dG; // swap direction  
    if ((B <= 5) || (B >= 250))  // if out of bounds
      dB = - dB; // swap direction  
    }
    
}