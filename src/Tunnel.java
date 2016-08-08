import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.core.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.analysis.*;


public class Tunnel extends PApplet implements AudioListener {

    int scale      = 8;
    int wallHeight = 32 * scale;
    int ceilHeight = 8 * 3 * scale;
    int width      = 150 * scale;
    int height     = wallHeight + ceilHeight + wallHeight;

    // Audio
    Minim minim;
    AudioInput in;
    FFT fft;


    ArrayList<PApplet> sketches = new ArrayList();
    Wire wire = new Wire();


    public Tunnel(HashMap<String, String> mapping) {

        // init Audio
        //
        minim = new Minim(this);
        in = minim.getLineIn();
        fft = new FFT(in.bufferSize(), in.sampleRate());
        fft.logAverages(22,1);
        in.addListener(this);

        // init Sketches
        //
        if (mapping.containsKey("Tunnel")) {

            loadSketch(mapping.get("Tunnel"), width, height);

        } else if ((mapping.containsKey("Wall")) &&
                    mapping.containsKey("Ceil")) {

            PApplet s1 = loadSketch(mapping.get("Wall"), width, wallHeight);
            loadSketch(mapping.get("Ceil"), width, ceilHeight);
            loadSketch(mapping.get("Wall"), width, wallHeight);
            //loadSketch(s1);

        } else if ((mapping.containsKey("RWall")) &&
                    mapping.containsKey("LWall") &&
                    mapping.containsKey("Ceil")) {

            loadSketch(mapping.get("RWall"), width, wallHeight);
            loadSketch(mapping.get("LWall"), width, ceilHeight);
            loadSketch(mapping.get("Ceil"),  width, wallHeight);
        } else { exitInvalid(); }

    }


    public void settings() {
        size(width, height);
    }


    public void setup() {

        frameRate(60);


        // Run sketches
        //
        for(PApplet sketch : sketches) {
            Class c = sketch.getClass();
            String[] args = { c.getName() };
            if (sketch.frameCount == 0) {
                PApplet.runSketch(args, sketch);
                //delay(1000);
            }
        }

    }


    public void draw() {
        synchronized(Tunnel.class) {
            // draw combined output
            //
            PImage frame = combineSketches();
            image(frame, 0, 0);

            // scale output to correct size and send to hardware
            //
            PImage unscaled = get();
            unscaled.resize(frame.width / scale, frame.height / scale);
            //unscaled.save("/Users/skryl/Desktop/frame.jpg");

            //wire.send(unscaled);

            //println(frameCount);
        }
    }


    private PImage combineSketches() {
        PGraphics output = createGraphics(width, height, JAVA2D);
        int height = 0;

        output.beginDraw();

        if (sketches.size() == 1) {
            output.image(sketches.get(0).get(), 0, height);
        } else {
            PApplet s1 = sketches.get(0);
            PApplet s2 = sketches.get(1);
            PApplet s3 = sketches.get(2);

            output.image(flipImage(s1.get()), 0, 0);
            output.image(s2.get(), 0, s1.height);
            output.image(s3.get(), 0, s1.height + s2.height);
        }

        output.endDraw();
        return output;
    }


    private PApplet loadSketch(String sketchName, int width, int height) {
        try {
            Class sketchClass = Class.forName(sketchName);
            Constructor c  = sketchClass.getConstructor(Tunnel.class, Integer.TYPE, Integer.TYPE);
            PApplet sketch = (PApplet) c.newInstance(this, width, height);
            sketches.add(sketch);
            return sketch;
        } catch (Exception e) {
            println(e.getClass().getName());
            println(e.getMessage());
            exitInvalid();
        }
        return null;
    }


    private void loadSketch(PApplet sketch) {
      sketches.add(sketch);
    }


    private void exitInvalid() {
        println("Invalid Tunnel Configuration");
        System.exit(0);
    }


    private PImage flipImage(PImage image)
    {
        BufferedImage img = (BufferedImage) image.getNative();
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return new PImage(op.filter(img, null));
    }


    // feed FFT when samples are available
    //
    public synchronized void samples(float[] samp)
    {
        fft.forward(in.mix);
    }

    public synchronized void samples(float[] sampL, float[] sampR)
    {
        fft.forward(in.mix);
    }


    public float getAudioAverage() {
        return fft.getAvg(2);
    }


}