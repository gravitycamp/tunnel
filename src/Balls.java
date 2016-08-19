import processing.core.*;

public class Balls extends PApplet {

    int width;
    int height;
    String position = "Tunnel";
    Tunnel tunnel;

    Ball[] balls = { new Ball(150 / 2, 32 / 2, 80, 255), };


    public Balls(Tunnel t, int w, int h, String p) {
        width  = w;
        height = h;
        tunnel = t;
        position = p;
    }

    public void settings() {
        size(width, height);
    }


    public void setup() {
       // frameRate(45);
       // background(255);
        noStroke();
        smooth();

    }

    public void draw() {
      try{
        synchronized(Tunnel.class) {

            background(60, 120, 200);
            for (int i = 0; i < balls.length; i++) {  // balls move to average audio size radius continuously
                ellipse(balls[i].x, balls[i].y, balls[i].r + tunnel.getAudioAverage() * 30, balls[i].r + tunnel.getAudioAverage() * 30);
                fill(0, 0, 0, 255);
            }
            if (tunnel.getAudioAverage() > 0.9) {   //make new balls if above this audio level threshold
                Ball b = new Ball(random(width), random(height), random(80), (int) random(255));
                balls = (Ball[]) append(balls, b);
            }
            
            if((abs(Main.kinect.LeftWrist.x - Main.kinect.RightWrist.x)<30) &&(abs(Main.kinect.LeftWrist.x - Main.kinect.RightWrist.x)>0))
            {
              balls=null;
            }
              
        } 
      }
      catch(Exception e){}
    }


    class Ball {
        float x, y, r, m;
        int col;

        Ball() { }

        Ball(float x, float y, float r, int col) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.col = col;
            m = r * (float) .1;
        }
    }

}