import processing.core.*;
import java.util.*;

class PortraitPainter extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  String[] imgNames = {"C:/DeepPsyTunnel/src/data/portrait1.jpg", "C:/DeepPsyTunnel/src/data/portrait2.jpg", "C:/DeepPsyTunnel/src/data/portrait3.jpg"};
  PImage img;
  int imgIndex = 0;


  public PortraitPainter(Tunnel t, int w, int h, String p) {
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
    nextImage();
  }

  public void draw() {
    synchronized (Tunnel.class) {
      translate(width/2, height/2);

      int index = 0;

      for (int y = 0; y < img.height; y+=1) {
        for (int x = 0; x < img.width; x+=1) {
          int odds = (int)random(10000);  //make this number smaller for better resolution in getting detail

          if (odds < 1) {
            int pixelColor = img.pixels[index];
            pixelColor = color(red(pixelColor), green(pixelColor), blue(pixelColor), 100);

            pushMatrix();
            translate(x-img.width/2, y-img.height/2);
            rotate(radians(random(-90, 90)));

            // Paint by layers from rough strokes to finer details
            if (frameCount < 20) {
              // Big rough strokes
              paintStroke(random(150, 250), pixelColor, (int)random(20, 40));
            } else if (frameCount < 50) {
              // Thick strokes
              paintStroke(random(75, 125), pixelColor, (int)random(8, 12));
            } else if (frameCount < 300) {
              // Small strokes
              paintStroke(random(30, 60), pixelColor, (int)random(1, 4));
            } else if (frameCount < 350) {
              // Big dots
              paintStroke(random(5, 20), pixelColor, (int)random(5, 15));
            } else if (frameCount < 600) {
              // Small dots
              paintStroke(random(1, 10), pixelColor, (int)random(1, 7));
            }

            popMatrix();
          }

          index += 1;
        }
      }

      if (frameCount > 600) {
        noLoop();
      }
    }
  }
  public void mousePressed() {
    nextImage();
  }

  void nextImage() {
    background(255);
    loop();
    frameCount = 0;

    img = loadImage(imgNames[imgIndex]);
    img.loadPixels();

    imgIndex += 1;
    if (imgIndex >= imgNames.length) {
      imgIndex = 0;
    }
  }

  void paintStroke(float strokeLength, int strokeColor, int strokeThickness) {
    float stepLength = (float)(strokeLength/4.0);

    // Determines if the stroke is curved. A straight line is 0.
    float tangent1 = 0;
    float tangent2 = 0;

    float odds = random((float)1.0);

    if (odds < 0.7) {
      tangent1 = random(-strokeLength, strokeLength);
      tangent2 = random(-strokeLength, strokeLength);
    } 

    // Draw a big stroke
    noFill();
    stroke(strokeColor);
    strokeWeight(strokeThickness);
    curve(tangent1, -stepLength*2, 0, -stepLength, 0, stepLength, tangent2, stepLength*2);

    int z = 1;

    // Draw stroke's details
    for (int num = strokeThickness; num > 0; num --) {
      float offset = random(-50, 25);
      int newColor = color(red(strokeColor)+offset, green(strokeColor)+offset, blue(strokeColor)+offset, random(100, 255));

      stroke(newColor);
      strokeWeight((int)random(0, 3));
      curve(tangent1, -stepLength*2, z-strokeThickness/2, -stepLength*random((float)0.9, (float)1.1), z-strokeThickness/2, stepLength*random((float)0.9, (float)1.1), tangent2, stepLength*2);

      z += 1;
    }
  }
}

// Additional Classes