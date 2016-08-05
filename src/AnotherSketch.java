/**
 * Created by skryl on 8/3/16.
 */

import org.gicentre.utils.multisketch.*;

class AnotherSketch extends EmbeddedSketch
{
    float textScale;
    int   width;
    int   height;

    public AnotherSketch(int w, int h) {
        width  = w;
        height = h;
    }


    public void setup()
    {

        size(width, height);

        // textFont(createFont("SansSerif", 24), 24);
        textAlign(CENTER, CENTER);
        fill(20, 120, 20);
        textScale = 0;
    }

    public void draw()
    {
        super.draw();

        background(200, 255, 200);

        pushMatrix();
        translate(width/2, height/2);
        scale((float) 0.1+sin(textScale), 1);
        text("Hello again", 0, 0);
        popMatrix();

        textScale += 0.02;
    }
}
