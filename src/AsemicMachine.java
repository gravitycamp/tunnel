import processing.core.*;
import java.util.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

class AsemicMachine extends PApplet {

  int width;
  int height;
  String position = "Tunnel";
  Tunnel tunnel;

  // Audio Support
  AudioInput audio;
  BeatDetect beat;

  float R, G, B, Rspeed, Gspeed, Bspeed;

  Hand h, f;
  int lastMillis;

  void generateColors() {
    R = random(150, 255);
    G = random(150, 255);
    B = random(150, 255);
    Rspeed = (random(1) > .5 ? 1 : -1) * random((float).8, (float)1.5);
    Gspeed = (random(1) > .5 ? 1 : -1) * random((float).8, (float)1.5);
    Bspeed = (random(1) > .5 ? 1 : -1) * random((float).8, (float)1.5);
  }

  public AsemicMachine(Tunnel t, int w, int h, String p) {
    width = w;
    height = h;
    tunnel = t;
    position = p;
    audio = tunnel.in;
  }

  public void settings()
  {
    size(width, height);
  }

  public void setup() {
    frameRate(70);
    smooth();
    beat = new BeatDetect();
    background(0, 0, 0);
 //  Hand( float xi, float yi, float vxi, float vyi, float sxi, float syi, Pen peni, int a1, int a2, int a3, int a4 )
    //h = new Hand((float)50, (float)(1*height/4), (float)4*6, (float)0, (float)5.0, (float)5.0, new Nib(3, color(0, 0, 100)), 7, 3, -2, 19);
    f = new Hand((float)width/4, (float)(height/2), (float)4*6, (float)0, (float)5.0, (float)5.0, new Nib(3, color(100, 0, 0)), -3, 5, -7, 23);
    lastMillis = millis();
  }

  float trackX = 0;
  float trackY = 0;
  float ptrackX = 0;
  float ptrackY = 0;
  float trackZ = 0;

  public void track() {
    if (Main.kinect != null) {
      Main.kinect.update();
      switch (position) {
      case "Tunnel":
      case "Wall":
      case "Ceil":
      case "RWall":
        trackX = (float)width * Main.kinect.RightHandDepthRatio;
        trackY = (float)height * Main.kinect.RightHandRaisedRatio;
        trackZ = 0;
        break;
      case "LWall":
        trackX = (float)width * Main.kinect.LeftHandDepthRatio;
        trackY = (float)height * Main.kinect.LeftHandRaisedRatio;
        trackZ = 0;
        break;
      }
      ptrackX = trackX;
      ptrackY = trackY;
    } else {
      ptrackX = pmouseX;
      ptrackY = pmouseY;
      trackX = mouseX;
      trackY = mouseY;
    }
  }

  public void draw() {
    synchronized (Tunnel.class) {
      //    track();

      beat.detect(audio.mix);
      if (beat.isOnset())
        generateColors();

      int tmp = millis();
      float dt = (float)((tmp-lastMillis)/1000.0);
      lastMillis = tmp;
    background(0, 0, 0);

      dt = min( dt, (float)(1/20.0));

      dt *= (float)1.5;
      //h.update( dt );
      f.update( dt );
    }
  }

  // Additional Classes
  class Hand
  {
    Pen pen;
    int na;
    Arm []a;
    float xpos ;
    float ypos ;
    float vx, vy;
    float sx, sy;
    float px, py;


    Hand( float xi, float yi, float vxi, float vyi, float sxi, float syi, Pen peni, int a1, int a2, int a3, int a4 )
    {
      pen = peni;

      na = 4;
      a = new Arm[na];

      a[0] = new Arm( 3, 0, 2*PI/a1);
      a[1] = new Arm( 3, 0, 2*PI/a2);
      a[2] = new Arm( 3, 0, 2*PI/a3);
      a[3] = new Arm( 4, 0, 2*PI/a4);

      xpos = xi;
      ypos = yi;
      vx = vxi;
      vy = vyi;
      sx = sxi;
      sy = syi;
    }

    void update( float dt )
    {
      int i, j;
      float rx = 1;
      float ry = 0;
      float l = dist(0, 0, vx, vy);
      if ( l!=0)
      {
        rx = vx/l;
        ry = vy/l;
      }


      for (i=0; i<na; i++)
      {
        a[i].update(dt);
      }

      float [] ax = new float[na];
      float [] ay = new float[na];
      float x = 0;
      float y = 0;
      for ( i=0; i<na; i++)
      {
        x += cos(a[i].a)*a[i].l;
        y += sin(a[i].a)*a[i].l;
        ax[i] = xpos + x*sx;
        ay[i] = ypos + y*sy;
      }

      pushMatrix();
      translate( (float)(width*0.8-xpos),(float)0);
      x *= sx;
      y *= sy;
      float xp = xpos + x*rx - y*ry;
      float yp = ypos + y*rx + x*ry;
      pen.drawTo( xp, yp);
      pen.render();

      // draw machine [
      fill(0);
      noStroke();
      for ( i=na-1; i>=0; i--)
      {
        noStroke();
        fill(0);
        if ( i<na-1)
          ellipse(ax[i], ay[i], 20, 20);
        else
          ellipse(xpos, ypos, 24, 24);
        stroke(0);
        strokeWeight(12);
        if ( i>0 )
          line(ax[i-1], ay[i-1], ax[i], ay[i]);
        else
          line(xpos, ypos, ax[i], ay[i]);
      }

      for ( i=na-1; i>=0; i--)
      {
        noStroke();
        if ( i<na-1)
        {
          fill(0);
          ellipse(ax[i], ay[i], 16, 16);
          fill(255);
          ellipse(ax[i], ay[i], 14, 14);
        }
        stroke(0);
        strokeWeight(8);
        if ( i>0 )
          line(ax[i-1], ay[i-1], ax[i], ay[i]);
        else
          line(xpos, ypos, ax[i], ay[i]);
        stroke(255);
        strokeWeight(6);
        if ( i>0 )
          line(ax[i-1], ay[i-1], ax[i], ay[i]);
        else
          line(xpos, ypos, ax[i], ay[i]);

        if (i==0)
        {
          noStroke();
          fill(0);
          ellipse(xpos, ypos, 20, 20);
          //fill(255);
          fill(color(constrain(R, 100, 255), constrain(G, 100, 255), constrain(B, 100, 255)));
          ellipse(xpos, ypos, 18, 18);
        }
      }
      // ] machine
      popMatrix();

      xpos += dt*vx;
      ypos += dt*vy;

      if ( false && xpos >width )
      {
        xpos = 0;
        ypos += height*3/9;
        pen.first = true;
      }
    }
  }
  // -----------
  class Arm
  {
    float l;
    float a;
    float w;

    Arm( float li, float ai, float wi )
    {
      l = li;
      a = ai;
      w = wi;
    }

    void update( float dt )
    {
      a += w*dt;
      if ( a>PI ) a -= 2*PI;
      if ( a<-PI ) a+= 2*PI;
    }
  }

  // -----------
  class Pen
  {
    boolean first;
    float px, py;
    float sz;
    int c;

    Pen( )
    {
      first = true;
      sz =(float)1.0;
      c = color(0, 0, 0);
    }

    Pen( float szi, int ci )
    {
      first = true;
      sz = szi;
      c = ci;
    }
    void drawTo( float x, float y )
    {
      if ( first )
      {
        px = x;
        py = y;
        first = false;
        return;
      }
      if ( dist(px, py, x, y) <2 )
        return;
      //stroke(c);
      stroke(color(constrain(R, 100, 255), constrain(G, 100, 255), constrain(B, 100, 255)));
      strokeWeight(sz);
      line( px, py, x, y);
      px = x;
      py = y;
    }

    void render()
    {
    }
  }

  // -----------
  int maxq = 500;
  class Nib extends Pen
  {
    int nq;
    float[][] xq;
    float[][] yq;

    Nib( float szi, int ci )
    {
      nq = 0;
      xq = new float[maxq][4];
      yq = new float[maxq][4];
      first = true;
      sz = szi;
      c = ci;
    }
    void drawTo( float x, float y )
    {
      if ( first )
      {
        px = x;
        py = y;
        first = false;
        return;
      }
      if ( dist(px, py, x, y) <6 )
        return;

      if ( nq>=maxq )
      {
        int discard = 100;
        nq -= discard;
        for (int j=0; j<nq; j++)
        {
          for (int i=0; i<4; i++)
          {
            xq[j][i] = xq[j+discard][i];
            yq[j][i] = yq[j+discard][i];
          }
        }
      }

      xq[nq][0]=px-sz;
      yq[nq][0]=py-sz;
      xq[nq][1]=x-sz;
      yq[nq][1]=y-sz;
      xq[nq][2]=x+sz;
      yq[nq][2]=y+sz;
      xq[nq][3]=px+sz;
      yq[nq][3]=py+sz;

      ++nq;
      px = x;
      py = y;
    }

    void render( )
    {
      int i;
      for ( i=0; i<nq; i++)
      {
        //fill(c);
        fill(color(constrain(R, 100, 255), constrain(G, 100, 255), constrain(B, 100, 255)));
        noStroke();
        quad( xq[i][0], yq[i][0],
          xq[i][1], yq[i][1],
          xq[i][2], yq[i][2],
          xq[i][3], yq[i][3]);
      }
    }
  }
}
