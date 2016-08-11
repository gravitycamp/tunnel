import processing.video.*;
import processing.core.*;

public class Video extends PApplet {

    int   width;
    int   height;
    Movie  movie;

    public Video(Movie m, int w, int h) {
        width  = w;
        height = h;
        movie = m;
    }

    public void settings() {
        size(width, height);
        //fullScreen(2);
    }

  public void setup () {
      background(0);
  }
  

  public void draw () {
      image(movie, 0, 0);
  }
}