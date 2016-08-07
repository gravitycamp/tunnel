/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/70780*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */

import ddf.minim.*; 
import processing.video.*;
import processing.core.*;

public class GlitchMob extends PApplet {

    float textScale;
    int   width;
    int   height;
    Tunnel tunnel;


    public GlitchMob(Tunnel t, int w, int h) {
        width  = w;
        height = h;
        tunnel = t;
    }

    public void settings() {
        size(width, height);
    }
  
  
  Minim girl ;
  Movie myMovie;
  AudioPlayer song ;
  
  int renk = 100 ;
  int x ;
  int y ;
  
  float md;
  float mt;
  
  public void setup () {
  //  myMovie = new Movie(this, "C:/TunnelGit/gravity/src/processing/Glitch_Mob/The Glitch Mob_Beyond Monday.mp4");
   // myMovie.play();
    
  //  size (600,600) ;
    colorMode (HSB,100) ;
    background(100) ;
    smooth ();
    noStroke();
    noCursor();
    // Construction of the minim object 
    //girl = new Minim (this) ;
    
    // Load the Audio File 
   // song = girl.loadFile ("girl.mp3", 512) ;
    myMovie = new Movie(this, "C:/TunnelGit/gravity/src/processing/Glitch_Mob/The Glitch Mob_Beyond Monday.mp4");
  
  
    PApplet sketch = new PlayVideo(400,400, myMovie);
    String[] args = { "PlayVideo", };          
    PApplet.runSketch(args, sketch);
    
    // Play the file as a loop
    //song.loop () ;
    

    
    
    
    
  }

  
  public void draw () {
    md = myMovie.duration();
    mt = myMovie.time(); 
    println(mt);
    delay(1);
    int t1 = 23;
    int R,G,B;
    if((int)mt == t1)
    {
      R=255;
      G=0;
      B=0;
      flash(R,G,B);
    }
  }
    void flash(int R, int G, int B)
    {
      background(R,G,B);
    }
    
    void circle()
    {
         float amount = 20, num = 0;
        fill(0, 40);
        rect(-1, -1, width + 1, height + 1);
    
        float maxX = map(tunnel.getAudioAverage() * 1000, 0, width, 1, 250);
        //println(maxX);
    
        translate(width / 2, height / 2);
        for (int i = 0; i < 360; i += amount) {
            float x = sin(radians(i + num)) * maxX;
            float y = cos(radians(i + num)) * maxX;
    
            float x2 = sin(radians(i + amount - num)) * maxX;
            float y2 = cos(radians(i + amount - num)) * maxX;
            noFill();
            bezier(x, y, x + x2, y + y2, x2 - x, y2 - y, x2, y2);
            bezier(x, y, x - x2, y - y2, x2 + x, y2 + y, x2, y2);
            fill(0, 150, 255);
            ellipse(x, y, 5, 5);
            ellipse(x2, y2, 5, 5);
        }
    
        num += 0.5;
    }
    
      void movieEvent(Movie movie) {
      myMovie.read();
    }    
      
    
    
    //if (renk < 100) {
    //  renk = renk +1 ;
    //}
    //else {
    //  renk = 0 ;
    //}
    
    //for (int i = 0 ; i < song.bufferSize () - 1 ; i++) {
    //  float x = random(0,600);
    //  float y = random (0,600) ;
  
    //  if (i == 0) {
    //  ellipse(x,y,song.left.get(i)*400,song.right.get(i)*400);
    //  }
      
    //  if (song.left.get(i) > 0.7) { background (0) ; // changes the bg if the pitch is low
     
    //  }
    // // else {
    //   // background (0,0,0,0) ;
    //  //}
    
    //}
    
    //noStroke() ;
    //fill (renk,100,100) ;
    
  //}
  
  
  
  
  
  
  
  
  
  
  
  public void keyPressed () {
     if (key=='s') {
       saveFrame("exports/img-####.tiff") ; // to save the frames you want
       
     }
}
}