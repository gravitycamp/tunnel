import processing.core.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Gravity extends PApplet {
    float textScale;
    int width;
    int height;
    String position = "Tunnel";
    Tunnel tunnel;
   // AudioPlayer  Music;
    //Minim minim;

    public Gravity(Tunnel t, int w, int h, String p) {
        width  = w;
        height = h;
        tunnel = t;
        position = p;
        textScale = 0;
        //minim = tunnel.minim;
    }

    public void settings() {
        size(width, height);
    }

    public void setup()
    {
        background(0);
      //  noStroke();
        fill(200, 100, 0);
        textAlign(CENTER, CENTER);
        textSize(height);
        try {
        Runtime.getRuntime().exec("cmd.exe /c start C:\\Users\\Garage-PC\\Music\\Playlists\\Ceremony.wpl");
        } 
        catch(IOException ie) {
          ie.printStackTrace();
        }
        delay(500);
        
    }
    
    public void draw() {
        synchronized (Tunnel.class) {
            background(0);
            fill(200,200,200);
            for(int i=0;i<10;i++)
            {
              ellipse(random(0,width),random(0,height),width/150,width/150);
            }        
            fill(random(0, 200), random(0, 200), random(0, 200));
            pushMatrix();
            translate(width / 2, height / 2);
            scale((float) 0.1 + sin(textScale), 1);
            text("<<GRAVITY>>", 0, -5);
            popMatrix();
            textScale += 0.02;
        }
    }
}