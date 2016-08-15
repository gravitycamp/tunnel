import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.core.*;
import processing.serial.*;
import processing.video.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;
import KinectPV2.KJoint;
import KinectPV2.*;

KinectPV2 kinect;

void setup() {
  frameRate(60);
  noLoop();
  
  // init kinect
  kinect = new KinectPV2(this);
  kinect.enableDepthImg(true);
  kinect.enableSkeleton3DMap(true);
  kinect.init();
}

void draw() {
  Main.setKinect(kinect);
  Main.main();
}

void keyPressed() {
  if ((keyCode==DOWN))
    Main.loadNextInQueue();

}