/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import processing.video.*;
import processing.core.*;
import ddf.minim.analysis.*;

public class GlitchMob extends PApplet {

    int width;
    int height;
    Tunnel tunnel;
    Movie movie;
    FFT fft;

    float md;
    float mt;

    public GlitchMob(Tunnel t, int w, int h) {
        width = w;
        height = h;
        tunnel = t;
        fft = tunnel.fft;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        movie = new Movie(this, "C:/TunnelGit2/src/data/glitch_mob.mp4");
        movie.play();

        PApplet sketch = new Video(movie, 400, 400);
        String[] args = {"Video",};
        PApplet.runSketch(args, sketch);

    }


    public void draw() {
      try{
        synchronized(Tunnel.class) {

            md = movie.duration();
            mt = movie.time();
            background(0);

  
            for(int i = 0; i < fft.specSize(); i++)
            {
              stroke(150);
              line(i, height, i, height - fft.getBand(i)*8);
//              rect( xl, height, xr, height - fft.getAvg(i)*4 );
            }
        }
      }
    catch(Exception e){}
    }  

        



    public void movieEvent(Movie movie) {
        movie.read();
    }

}