import processing.core.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Runtime;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import KinectPV2.KJoint;
import KinectPV2.*;


class Main {

  //static String playlistPath = "/Users/skryl/Dropbox/dev/projects/gravity/tunnel/src/data/playlist.txt";
  static String playlistPath[] = {"C:/DeepPsyTunnel/src/data/playlist1.txt", "C:/DeepPsyTunnel/src/data/playlist2.txt", "C:/DeepPsyTunnel/src/data/playlist3.txt"};
  static int ListIndex = 0;
  static Tunnel tunnel;
  static ArrayList<HashMap<String, String>> queue = new ArrayList();
  static int queueIndex = -1;
  static int duration = 100000;
  static int elapsedTime = 0;
  static Wire wire = new Wire();
  static Timer playTimer = new Timer();
  static Timer RestartTimer = new Timer();
  public static Kinect kinect;
  static Thread t = new Thread();

  public static void setKinect(Kinect k) {
    kinect = k;
  }

  public static void main(String... args) {

    //wire.SetupCom();
    loadPlaylist(1);

    //  tunnel.exec("C:/TunnelGit2/src/Restart.bat");
    /*    
     TimerTask exitApp = new TimerTask() {
     public void run() {
     //open("rundll32 SHELL32.DLL,ShellExec_RunDLL " + "C:/TunnelGit2/src/Restart.bat");
     //tunnel.launch("C:/TunnelGit2/src/Restart.bat");
     try {
     Runtime.getRuntime().exec("cmd.exe /c start C:\\TunnelGit2\\src\\Restart.bat");
     } 
     catch(IOException ie) {
     ie.printStackTrace();
     }
     
     System.exit(0);
     }
     };
     
     RestartTimer.schedule(exitApp, new Date(System.currentTimeMillis()+30*60*1000)); //restart every 30 minutes
     */
    while (true) {
      Play();
    }
  }

  public static void Play()
  {
    int playSeconds = 1000* Integer.parseInt(queue.get(queueIndex+1).get("Time"));
    long millis = System.currentTimeMillis();
    try {
      loadNextInQueue();
      //TWait = new 
      t.sleep(playSeconds - millis % 1000);
      System.out.println("done sleeping for queue: " + (queueIndex));
    } 
    catch(InterruptedException e) {
      System.out.println("got interrupted!");
    }
  }

  public static void loadNextInQueue() {
    queueIndex = ++queueIndex % queue.size();
    t.stop();
    try { 
      tunnel.kill();
    } 
    catch(Exception e) {
    }
    tunnel = new Tunnel(queue.get(queueIndex), wire);
    System.out.println("Pattern Index: " + queueIndex);
    PApplet.runSketch(new String[]{"Tunnel"}, tunnel);
    elapsedTime = 0;
  }

  public static void loadPreviusInQueue() {
    t.stop();
    queueIndex = --queueIndex % queue.size();
    if (queueIndex<0)
      queueIndex = queue.size()-1;
    System.out.println("Pattern Index: " + queueIndex);
    try { 
      tunnel.kill();
    } 
    catch(Exception e) {
    }
    tunnel = new Tunnel(queue.get(queueIndex), wire);
    PApplet.runSketch(new String[]{"Tunnel"}, tunnel);
    elapsedTime = 0;
  }

  public static void loadNextPlaylist() {
    if (++ListIndex >= playlistPath.length)
      ListIndex = 0;
    queue.clear();
    System.out.println("ListIndex: " + ListIndex);
    loadPlaylist(ListIndex);
    queueIndex = -1;
    Play();
  }

  public static void loadPreviousPlaylist() {
    if (--ListIndex < 0)
      ListIndex = playlistPath.length-1;
    queue.clear();
    System.out.println("ListIndex: " + ListIndex);  
    loadPlaylist(ListIndex);
    queueIndex = -1;
    Play();
  }

  public static void loadPlaylist(int ListIndex) {
    System.out.println(playlistPath[ListIndex]);
    try (Stream<String> stream = Files.lines(Paths.get(playlistPath[ListIndex]))) {
      Object[] lines = stream.toArray();

      for (Object line : lines) {
        HashMap<String, String> mapping = new HashMap();

        String[] sketches = ((String) line).split(",");
        mapping.put("Time", sketches[0].trim());
        if (sketches.length == 2) {
          mapping.put("Tunnel", sketches[1].trim());
        } else if (sketches.length == 3) {
          mapping.put("Wall", sketches[1].trim());
          mapping.put("Ceil", sketches[2].trim());
        } else {
          mapping.put("LWall", sketches[1].trim());
          mapping.put("Ceil", sketches[2].trim());
          mapping.put("RWall", sketches[3].trim());
        }
        queue.add(mapping);
      }
    } 
    catch (IOException e) {
    }
  }
}