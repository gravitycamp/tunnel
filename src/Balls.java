import processing.core.*;

public class Balls extends PApplet {

    int width;
    int height;
    Tunnel tunnel;

    Ball[] balls = { new Ball(150 / 2, 32 / 2, 80, 255), };


    public Balls(Tunnel t, int w, int h) {
        width  = w;
        height = h;
        tunnel = t;
    }

    public void settings() {
        size(width, height);
    }


    public void setup() {
        frameRate(45);
        background(255);
        noStroke();
        smooth();

    }

    public void draw() {
        synchronized(Tunnel.class) {

            background(60, 120, 200);
            for (int i = 0; i < balls.length; i++) {
                ellipse(balls[i].x, balls[i].y, balls[i].r + tunnel.getAudioAverage() * 400, balls[i].r + tunnel.getAudioAverage() * 400);
                fill(0, 0, 0, 255);
            }
            if (tunnel.getAudioAverage() > 0.3) {
                Ball b = new Ball(random(width), random(height), random(80), (int) random(255));
                balls = (Ball[]) append(balls, b);
            }

        }
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
