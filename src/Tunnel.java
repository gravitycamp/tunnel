import processing.core.*;
import org.gicentre.utils.multisketch.*;

import java.awt.*;
import java.util.*;


public class Tunnel extends PApplet {

    int scale = 4;
    int wallWidth  = 32 * scale;
    int ceilWidth  = 8 * 3 * scale;
    int height     = 150 * scale;
    int width      = wallWidth + ceilWidth + wallWidth;

    ArrayList<EmbeddedSketch> sketches = new ArrayList();
    Wire wire          = new Wire();


    public void setup() {

        size(width, height);
        frameRate(1);
        setLayout(new FlowLayout(0,0,0));

        EmbeddedSketch sketch1 = new AnotherSketch(wallWidth, height, 270);
        EmbeddedSketch sketch2 = new AnotherSketch(ceilWidth, height, 0);
        EmbeddedSketch sketch3 = new AnotherSketch(wallWidth, height, 90);

        sketches.add(sketch1);
        sketches.add(sketch2);
        sketches.add(sketch3);

        SketchPanel sp1 = new SketchPanel(this, sketch1);
        SketchPanel sp2 = new SketchPanel(this, sketch2);
        SketchPanel sp3 = new SketchPanel(this, sketch3);

        add(sp1);
        add(sp2);
        add(sp3);

        sketch1.setIsActive(true);
        sketch2.setIsActive(true);
        sketch3.setIsActive(true);
    }


    public void draw() {
        PImage frame = combineFrames();
        wire.send(frame);
        println(frameCount);
    }


    public PImage combineFrames() {
        PGraphics output = createGraphics(width, height, JAVA2D);
        output.beginDraw();
        output.image(sketches.get(0).get(), 0, 0);
        output.image(sketches.get(1).get(), wallWidth, 0);
        output.image(sketches.get(2).get(), wallWidth + ceilWidth, 0);
        output.endDraw();
        output.save("/Users/skryl/Desktop/frame.jpg");
        return output;
    }


    public static void main(String... args) {
      PApplet.main("Tunnel");
    }

}