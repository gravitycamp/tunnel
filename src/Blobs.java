import processing.core.*;

public class Blobs extends PApplet {

    int renk = 100;
    int x;
    int y;

    int width;
    int height;
    String position = "Tunnel";
    Tunnel tunnel;

    public Blobs(Tunnel t, int w, int h, String p) {
        width  = w;
        height = h;
        tunnel = t;
        position = p;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        colorMode(HSB, 130);
        background(0);
        frameRate(25);
        smooth();
        noStroke();
        //noCursor();
    }

    public void draw() {
        synchronized(Tunnel.class) {

            if (renk < 100) {
                renk = renk + 5;
            } else {
                renk = 0;
            }

            float x = random(0, width);
            float y = random(0, height);

            float audioAverage = 20+(5+tunnel.getAudioAverage()) * 4;
            ellipse(x, y, audioAverage, audioAverage);  
            if (audioAverage > 300) {
                background(0);
            }
            noStroke();
            fill(renk, 100, 100);
        }
    }

}