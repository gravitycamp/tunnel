import processing.core.*;

public class Infinity extends PApplet {

    int width;
    int height;

    public Infinity(int w, int h) {
        width  = w;
        height = h;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        background(0, 30, 100);
    }

    public void draw() {
        translate(width/2, height/2);

        for (int i = -90; i < 90; i++) {
            for (float q = 1; q < 2; q+=0.2) {
                float a = q*180;
                float x = sin(radians(i+frameCount+a))*100;
                float y = sin(radians(i+frameCount+a))*cos(radians(i+frameCount+a))*75;
                float s = cos(radians(i))*q*5;
                ellipse(x*q, y*q, s, s);
            }
        }
    }

}