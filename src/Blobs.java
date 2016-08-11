/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.core.*;
import krister.Ess.*;

public class Blobs extends PApplet {

    int renk = 100;
    int x;
    int y;

    int width;
    int height;
    Tunnel tunnel;

    public Blobs(Tunnel t, int w, int h) {
        width  = w;
        height = h;
        tunnel = t;
    }

    public void settings() {
        size(width, height);
    }


    public void setup() {
        colorMode(HSB, 100);
        background(100);
        smooth();
        noStroke();
        noCursor();
    }


    public void draw() {
        synchronized(Tunnel.class) {

            if (renk < 100) {
                renk = renk + 1;
            } else {
                renk = 0;
            }

            float x = random(0, width);
            float y = random(0, height);

            ellipse(x, y, tunnel.getAudioAverage() * 100, tunnel.getAudioAverage() * 100);

            if (tunnel.getAudioAverage() > 1) {
                background(0);
            }

            noStroke();
            fill(renk, 100, 100);

        }
    }

}