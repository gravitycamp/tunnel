/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */ //<>// //<>//
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
    double AY_depth = 0;
    double AY_Y = 0;
    boolean AY_OnLeftWall = true;
    boolean AyukitFound = true; 
    int AyukitFrame = 0;
    AudioPlayer  Ayukit_Sound;
    int scale =4;
    PGraphics LeftWall;
    PGraphics RightWall;
    PGraphics Roof; 

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
        Ayukit.resize(10,10);
        Ayukit_Sound = minim.loadFile("C:/TunnelGit2/src/data/Ayukit.mp3"); //<>//
        //Ayukit_Sound = new SoundFile(this, "C:/TunnelGit2/src/data/Ayukit.mp3");

        LeftWall= createGraphics(150*scale, 32*scale);
        RightWall= createGraphics(150*scale, 32*scale);
        Roof= createGraphics(150*scale, 24*scale);
     //   StartTime = millis();
   
        depthZero    = new int[ KinectPV2.WIDTHDepth * KinectPV2.HEIGHTDepth];        
        kinect = new KinectPV2(this);
        kinect.enableDepthImg(true);
        kinect.enableSkeleton3DMap(true);
        kinect.init();
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
        
            //we now have ratios, now draw game logic 
            //if(!AyukitFound &&(LeftWristdepth > HeadDepth+.2) && (RightWristdepth > HeadDepth+.2))    //create an ayukit at right hand y = center of ayukit
            //{
            //  AyukitFound = true;
            //  AY_depth = RightWristdepth;
            //  AY_Y = RightHandRaisedRatio;
            //  if(HeadP.x < 100) //head is on left or right side (need to update center of screen = width/2 
            //    AY_OnLeftWall = true;
            //  Ayukit_Sound.play(); 
            //  AyukitFrame = 0;
            //}
            
            //AyukitFound = true;
            AY_depth = 0.5;
            AY_Y = 0.5;
            AY_OnLeftWall = true;    
            
            //LeftWall.ellipseMode(RADIUS);
            //RightWall.ellipseMode(RADIUS);
            //LeftWall.fill( random(255), random(255), random(255), random(255)); 
          //  LeftWall.ellipse(width*depth_LeftHand_Ratio, LeftWall.height*(RightHandRaisedRatio), 3*fft.getBand(0), 3*fft.getBand(0));
            //RightWall.fill( random(150), random(255), random(255), random(255)); 
    //        RightWall.ellipse(width*depth_RightHand_Ratio, RightWall.height*(float)(LeftHandRaisedRatio), 3*fft.getBand(0), 3*fft.getBand(0));
            if(AyukitFound)
            {
              RightWall.copy(Ayukit, 0,0, Ayukit.width, Ayukit.height, (int)(RightWall.width*AY_depth-AyukitFrame), (int)(RightWall.height*AY_Y), Ayukit.width, Ayukit.height);
              AyukitFrame++;
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
}