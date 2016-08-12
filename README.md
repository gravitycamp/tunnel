# README


## Requirements

 - Processing 3.0


## Usage

Double click the 'src/src.pde' file to open the project in Processing. Press Play to launch the Simulator.


## Tunnel Configuration

You can modify the 'data/playlist.txt' file to configure the tunnel playlist.

Each line in the playlist must have one of the following 3 formats:

1. Specify three different patterns to apply to each tunnel surface (left wall, right wall, and ceiling are independent).
   ex. "Bounce,Gravity,Life"
2. Specify one ceiling pattern and one wall pattern. The wall pattern will be mirrored on both walls.
   ex. "Bounce,Gravity"
3. Specify one pattern. It will be stretched around the whole tunnel.
   ex. "Gravity"

## Adding New Sketches

In order for your sketches to work properly, please place them in the 'src'
directory and rename any .pde files to .java. The code will then need to be
wrapped in a Java class and a constructor method will need to be added. See
'src/Gravity.java' for a simple example.

In order to avoid blinking, wrap the contents of your sketch's draw function
in a 'synchronized' block. See 'src/Gravity.java' for an example.


### Using Microphone Audio in Your Sketches

Ones you add the proper constructor to your sketch class (see example in 'src/Gravity.java'),
you will have access to a 'tunnel' property inside your sketch. calling the 'getAudioAverage()'
method on the tunnel object will return a float value representing the low frequency audio
intensity from the tunnel's fft. See 'src/Gravity.java' for an example.


### Using Kinect Data in Your Sketches
TBD


### Using OpenGL in Your Sketches
Don't do it. It breaks shit.


## Developing in IntelliJ

You can develop and debug a bit in Processing but the IDE is very limited. IntelliJ will give you
proper code completion and much better compiler warnings. To get IntelliJ working you'll have to do
a few things first.

1. Find the folder where you installed processing and search for any *.jar files in that folder.
   Copy all these files to a new folder called 'lib_processing' inside the 'tunnel' folder.
2. Open the 'tunnel' folder in IntelliJ (it should automatically be recognized as an IntelliJ project)
3. Press the Play button.
4. Profit.
