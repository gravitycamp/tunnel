import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.core.*;
import processing.serial.*;
import processing.video.*;
import KinectPV2.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;


void setup() {
  noLoop();
  
  frame.setAlwaysOnTop(true);
}

void draw() {
  Main.main();
}

void keyPressed() {
  if (keyPressed && (keyCode==DOWN))
    Main.loadNextInQueue();

}