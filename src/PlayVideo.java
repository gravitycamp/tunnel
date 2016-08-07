import ddf.minim.*; 
import processing.video.*;
import processing.core.*;

public class PlayVideo extends PApplet {

    float textScale;
    int   width;
    int   height;
    Movie  movie;
    //Tunnel tunnel;

    public PlayVideo(int w, int h, Movie m) {
        width  = w;
        height = h;
        movie = m;
      //  tunnel = t;
    }

    public void settings() {
        size(width, height);
      //  fullScreen();
    }

  public void setup () {
  background(0);
      movie = new Movie(this, "C:/TunnelGit/gravity/src/processing/Glitch_Mob/The Glitch Mob_Beyond Monday.mp4");
      movie.play();
  }  
  

  public void draw () {
  image(movie, 0, 0);
  }
}