import processing.core.*;
import java.util.*;
import java.text.SimpleDateFormat;
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
  static String playlistPath[] = {"C:/DeepPsyTunnel/src/data/PlayListFull.txt", "C:/DeepPsyTunnel/src/data/PlayListParty.txt", "C:/DeepPsyTunnel/src/data/PlayListInteractive.txt", "C:/DeepPsyTunnel/src/data/PlayListChill.txt", "C:/DeepPsyTunnel/src/data/PlayListBurn.txt"};
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
  static   int loops = 0;

  public static void setKinect(Kinect k) {
    kinect = k;
  }

  public static void main(String... args) {
    
    //wire.SetupCom();
    SimpleDateFormat formatter = new SimpleDateFormat("HH");
    int hours = Integer.parseInt(formatter.format(System.currentTimeMillis()));
    System.out.println(hours);
    //Load Playlist according to the time
    if(hours < 2 || hours >=18)  //party after 6pm to 2am  
      loadPlaylist(1);
    else if(hours < 4 && hours >=2)  //interactive after 6pm to 2am  
      loadPlaylist(2);
    else if(hours < 18 && hours >=4)  //interactive after 6pm to 6pm 
      loadPlaylist(3);

    // tunnel.exec("C:/DeepPsyTunnel/src/Restart.bat");

    TimerTask exitApp = new TimerTask() {
      public void run() {
        //open("rundll32 SHELL32.DLL,ShellExec_RunDLL " + "C:/TunnelGit2/src/Restart.bat");
        //tunnel.launch("C:/TunnelGit2/src/Restart.bat");
        try {
          Runtime.getRuntime().exec("cmd.exe /c start C:\\DeepPsyTunnel\\src\\Restart.bat"); //<>//
        } 
        catch(IOException ie) {
          ie.printStackTrace();
        }
        System.exit(0);
      }
    };

    RestartTimer.schedule(exitApp, new Date(System.currentTimeMillis()+2*60*60*1000)); //restart every 2 hours minutes

    while (true) {
      Play();
    }
  }

  public static void Play()
  {
    if (queueIndex+1 >= queue.size())
      queueIndex = -1;
    if (queueIndex==0)
      System.out.println( "total play lists: "+ (loops++));
    int playSeconds = 1000* Integer.parseInt(queue.get(queueIndex+1).get("Time"));
    long millis = System.currentTimeMillis();
    try {
      loadNextInQueue();
      //TWait = new 
      t.sleep(playSeconds - millis % 1000);
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
      tunnel.dispose();
    } 
    catch(Exception e) {
    }
    System.gc();
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
        mapping.put("Scale", sketches[0].trim());
        mapping.put("Time", sketches[1].trim());
        if (sketches.length == 3) {
          mapping.put("Tunnel", sketches[2].trim());
        } else if (sketches.length == 4) {
          mapping.put("Wall", sketches[2].trim());
          mapping.put("Ceil", sketches[3].trim());
        } else {
          mapping.put("LWall", sketches[2].trim());
          mapping.put("Ceil", sketches[3].trim());
          mapping.put("RWall", sketches[4].trim());
        }
        queue.add(mapping);
      }
    } 
    catch (IOException e) {
    }
  }
}