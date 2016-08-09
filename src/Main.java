import processing.core.*;
import java.util.HashMap;


class Main {
        
//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("RWall", "Gravity");
//        put("LWall", "Gravity");
//        put("Ceil",  "Gravity");
//    }};

    static HashMap<String, String> mapping = new HashMap<String, String>(){{
        put("RWall", "GlitchMob");
        put("LWall", "Circle");
        put("Ceil", "Circle");
    }};

//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("Tunnel", "Gravity");
//    }};

    public static void main(String... args) {
        PApplet tunnel = new Tunnel(mapping);
        String[] sketchArgs = { "Tunnel"};
        PApplet.runSketch(sketchArgs, tunnel);
    }

}