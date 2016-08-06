import processing.core.*;
import java.util.HashMap;


class Main {

//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("RWall", "Test");
//        put("LWall", "Test");
//        put("Ceil",  "Test");
//    }};

    static HashMap<String, String> mapping = new HashMap<String, String>(){{
        put("Wall", "Blobs");
        put("Ceil", "Circle");
    }};

//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("Tunnel", "Circle");
//    }};

    public static void main(String... args) {
        PApplet tunnel = new Tunnel(mapping);
        String[] sketchArgs = { "Tunnel"};
        PApplet.runSketch(sketchArgs, tunnel);
    }

}