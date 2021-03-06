import java.util.*; //<>//
import java.util.ArrayList;
//import processing.core.*;
import processing.core.PVector;
import processing.core.PApplet.*;
import KinectPV2.KJoint;
import KinectPV2.*;

// Kinect Controller Class
public class Kinect
{
  KinectPV2 kinect;
  public int [] RawDepth;
  public ArrayList<KSkeleton> skeletonArray;
  public KJoint[] joints;
  int Body_Leader_Index = 0;

  // Individual JOINTS
  public PVector RightWrist = new PVector(0, 0, 0);
  public PVector LeftWrist = new PVector(0, 0, 0);
  public PVector LeftKnee = new PVector(0, 0, 0);
  public PVector RightKnee = new PVector(0, 0, 0);
  public PVector Head = new PVector(0, 0, 0);
  public float RightWristDepth = 0;
  public float LeftWristDepth = 0;
  public float HeadDepth = 0;
  public float RightHandRaisedRatio = 0;
  public float LeftHandRaisedRatio = 0;
  public float RightHandDepthRatio = 0;
  public float LeftHandDepthRatio =0;
  public float RightHandSideRatio = 0;
  public float LeftHandSideRatio = 0;
  public float HandDistance = 0;
  public boolean RightHandOpen = false;
  public boolean LeftHandOpen = false;
  public boolean IsTracking;

  public Kinect(KinectPV2 k) {
    kinect = k;
    kinect.enableDepthImg(true);
    kinect.enableSkeleton3DMap(true);
    kinect.init();
  }

  public KinectPV2 pv2() {
    return kinect;
  }

  public void update() {
    skeletonArray = kinect.getSkeleton3d();
    RawDepth = kinect.getRawDepthData();
    if (1==skeletonArray.size())
      Body_Leader_Index = 0;
    for (int i = 0; i < skeletonArray.size(); i++) {
      KSkeleton skeleton = (KSkeleton) skeletonArray.get(i);
      IsTracking = skeleton.isTracked();
      if (IsTracking) {
        joints = skeleton.getJoints();

        //set the leader
        PVector LeaderRightWrist = joints[KinectPV2.JointType_WristRight].getPosition();
        PVector LeaderLeftWrist = joints[KinectPV2.JointType_WristLeft].getPosition();
        PVector LeaderRightKnee = joints[KinectPV2.JointType_KneeRight].getPosition();
        PVector LeaderLeftKnee = joints[KinectPV2.JointType_KneeLeft].getPosition();
        PVector LeaderHead = joints[KinectPV2.JointType_Head].getPosition();   
        float LeaderRightHandRaisedRatio = 1 - (float)(LeaderRightWrist.y-LeaderRightKnee.y*.85)/(LeaderHead.y - LeaderRightKnee.y);
        float LeaderLeftHandRaisedRatio = 1 - (float)(LeaderLeftWrist.y-LeaderLeftKnee.y*.85)/(LeaderHead.y - LeaderLeftKnee.y);

//        System.out.println("LeaderRightHandRaisedRatio: "+i+" " + LeaderRightHandRaisedRatio);
  //      System.out.println("LeaderLeftHandRaisedRatio: "+i+ " " +LeaderLeftHandRaisedRatio);

        if (skeletonArray.size()>1  && LeaderRightHandRaisedRatio < .1 && LeaderLeftHandRaisedRatio < .1)
        {
    //      System.out.println("i is: "+i);
          Body_Leader_Index = i;
        }

        if (Body_Leader_Index == i)  //update only for the leader!
        {
    //      System.out.println("real leader is" + Body_Leader_Index);
    //      System.out.println("total bodies " + skeletonArray.size()); 

          RightWrist = joints[KinectPV2.JointType_WristRight].getPosition();
          LeftWrist = joints[KinectPV2.JointType_WristLeft].getPosition();
          RightKnee = joints[KinectPV2.JointType_KneeRight].getPosition();
          LeftKnee = joints[KinectPV2.JointType_KneeLeft].getPosition();
          Head = joints[KinectPV2.JointType_Head].getPosition();   
          RightWristDepth = joints[KinectPV2.JointType_WristRight].getZ();
          LeftWristDepth = joints[KinectPV2.JointType_WristLeft].getZ();
          HeadDepth = joints[KinectPV2.JointType_Head].getZ();
          if (KinectPV2.HandState_Open == joints[KinectPV2.JointType_HandLeft].getState())
            LeftHandOpen = true;
          else
            LeftHandOpen =false; 
          if (KinectPV2.HandState_Open == joints[KinectPV2.JointType_HandRight].getState())
            RightHandOpen = true;
          else
            RightHandOpen =false; 
          //Ratio calculation and calibration
          RightHandDepthRatio = RightWristDepth/5; //4 is as deep as you can go!
          LeftHandDepthRatio = LeftWristDepth/5; //4 is as deep as you can go!
          RightHandRaisedRatio = 1 - (float)(RightWrist.y-RightKnee.y*.85)/(Head.y - RightKnee.y);
          LeftHandRaisedRatio = 1 - (float)(LeftWrist.y-LeftKnee.y*.85)/(Head.y - LeftKnee.y);
          RightHandSideRatio = (float)((RightWrist.x+1)/2);
          LeftHandSideRatio = (float)((RightWrist.x)/2);
          HandDistance = (float)(Math.hypot(RightWrist.x-LeftWrist.x, RightWrist.y-LeftWrist.y)/RightHandDepthRatio/3.7);
        }
      }
    }
  }
}