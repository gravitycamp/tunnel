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
//        movie = new Movie(this, "C:/TunnelGit2/src/data/glitch_mob.mp4");
        movie = new Movie(this, "/Users/skryl/Dropbox/dev/projects/gravity/tunnel/src/data/glitch_mob.mp4");
        movie.play();

        PApplet sketch = new Video(movie, 400, 400);
        String[] args = {"Video",};
        PApplet.runSketch(args, sketch);

    }


    public void draw() {
        synchronized(Tunnel.class) {

            md = movie.duration();
            mt = movie.time();

            for(int i = 0; i < fft.specSize(); i++)
            {
              float centerFrequency    = fft.getAverageCenterFrequency(i);
              // how wide is this average in Hz?
              float averageWidth = fft.getAverageBandWidth(i);   
              
              // we calculate the lowest and highest frequencies
              // contained in this average using the center frequency
              // and bandwidth of this average.
              float lowFreq  = centerFrequency - averageWidth/2;
              float highFreq = centerFrequency + averageWidth/2;
              
              // freqToIndex converts a frequency in Hz to a spectrum band index
              // that can be passed to getBand. in this case, we simply use the 
              // index as coordinates for the rectangle we draw to represent
              // the average.
              int xl = (int)fft.freqToIndex(lowFreq);
              int xr = (int)fft.freqToIndex(highFreq);
              
              
              stroke(150);
              line(i, height, i, height - fft.getBand(i)*4);
              rect( xl, height, xr, height - fft.getBand(i)*4 );
            }
            

        }
    }


    public void movieEvent(Movie movie) {
        movie.read();
    }

}