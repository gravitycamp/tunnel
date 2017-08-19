import processing.core.*;
//import java.util.*;

class Black extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  public Black(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
  }
  public void settings()
  {
    size(width, height);
  }
  public void setup() {
    background(0);
  }
  public void draw() {
    synchronized (Tunnel.class) {
    }
  }
}