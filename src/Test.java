import processing.core.*;

public class Test extends PApplet {

    float textScale;
    int   width;
    int   height;
    Tunnel tunnel;


    public Test(Tunnel t, int w, int h) {
        width  = w;
        height = h;
        tunnel = t;
    }

    public void settings() {
        size(width, height);
    }


    public void setup()
    {
        //textFont(createFont("SansSerif", 24), 24);
        textAlign(CENTER, CENTER);
        fill(20, 120, 20);
        textScale = 0;
    }


    public void draw()
    {
        background(200, 255, 200);

        pushMatrix();
        translate(width/2, height/2);
        scale((float) 0.1+sin(textScale), 1);
        text("Hello World!", 0, 0);
        popMatrix();

        textScale += 0.02;
    }
}
