import processing.core.*;
import java.util.HashMap;


/*

The framework supports 3 types of configuration:

1. Specify all three patterns to apply to each tunnel surface
2. Specify two patterns. The Wall pattern will be mirrored on both walls.
3. Specify one pattern which will be stretched around the whole tunnel.

*/

class Main {

//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("RWall", "Test");
//        put("LWall", "Test");
//        put("Ceil",  "Test");
//    }};

    static HashMap<String, String> mapping = new HashMap<String, String>(){{
        put("Wall", "WhiteRain");
        put("Ceil", "Circle");
    }};

//    static HashMap<String, String> mapping = new HashMap<String, String>(){{
//        put("Tunnel", "WhiteRain");
//    }};

    public static void main(String... args) {
        PApplet tunnel = new Tunnel(mapping);
        String[] sketchArgs = { "Tunnel"};
        PApplet.runSketch(sketchArgs, tunnel);
    }

}