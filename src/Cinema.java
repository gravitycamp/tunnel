/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.video.*;
import processing.core.*;

public class Cinema extends PApplet {

    int width;
    int height;
    Tunnel tunnel;
    Movie movie;

    float md;
    float mt;

    public Cinema(Tunnel t, int w, int h) {
        width = w;
        height = h;
        tunnel = t;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        movie = new Movie(this, "/Users/skryl/Dropbox/dev/projects/gravity/tunnel/src/data/glitch_mob.mp4");
        movie.play();

//        PApplet sketch = new Video(movie, 400, 400);
//        String[] args = {"PlayVideo",};
//        PApplet.runSketch(args, sketch);

    }


    public void draw() {
        synchronized(Tunnel.class) {

            md = movie.duration();
            mt = movie.time();
            //println(mt);
            image(movie, 0, 0);

        }
    }


    public void movieEvent(Movie movie) {
        movie.read();
    }

}