import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class src extends PApplet {














Kinect kinect;

public void setup() {
  frameRate(60);
  noLoop();
  
  // Init kinect
  kinect = new Kinect(new KinectPV2(this));
}

public void draw() {
  Main.setKinect(kinect);
  Main.main();
}

public void keyPressed() {
  if ((keyCode==DOWN))
    Main.loadNextInQueue();

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "src" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
