import java.util.*;
import java.lang.reflect.*;
import java.awt.geom.*;
import java.awt.image.*;
import processing.core.*;
import krister.Ess.*;


public class Tunnel extends PApplet {

    int scale      = 8;
    int wallHeight = 32 * scale;
    int ceilHeight = 8 * 3 * scale;
    int width      = 150 * scale;
    int height     = wallHeight + ceilHeight + wallHeight;

    // Audio
    FFT fft;
    AudioInput audioInput;
    int bufferSize = 512;

    ArrayList<PApplet> sketches = new ArrayList();
    Wire wire = new Wire();


    public Tunnel(HashMap<String, String> mapping) {

        if (mapping.containsKey("Tunnel")) {

            loadSketch(mapping.get("Tunnel"), width, height);

        } else if ((mapping.containsKey("Wall")) &&
                    mapping.containsKey("Ceil")) {

            PApplet s1 = loadSketch(mapping.get("Wall"), width, wallHeight);
            loadSketch(mapping.get("Ceil"), width, ceilHeight);
            loadSketch(s1);

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

        frameRate(40);

        // init Audio
        //
        Ess.start(this);
        audioInput = new AudioInput(bufferSize);
        fft = new FFT(bufferSize * 2);
        audioInput.start();
        fft.damp((float).5);
        fft.equalizer(true);
        fft.limits((float).005, (float).01);


        // init Sketches
        //
        for(PApplet sketch : sketches) {
            Class c = sketch.getClass();
            String[] args = { c.getName() };
            if (sketch.frameCount == 0) {
                PApplet.runSketch(args, sketch);
                delay(200);
            }
        }

    }


    public void draw() {
        // draw combined output
        //
        PImage frame = combineSketches();
        image(frame, 0, 0);

        // scale output to correct size and send to hardware
        //
        PImage unscaled = get();
        unscaled.resize(frame.width/scale, frame.height/scale);
        //unscaled.save("/Users/skryl/Desktop/frame.jpg");

        //wire.send(unscaled);

        //println(frameCount);
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


    public void audioInputData(AudioInput theInput) {
      fft.getSpectrum(audioInput);
    }


    public float getAudioAverage() {
        return fft.averages[0];
    }


}