# README


## Requirements

 - Processing 3.0


## Usage

Double click the 'src/src.pde' file to open the project in Processing. Press Play to launch the Simulator.


## Tunnel Configuration

You can modify the Main.java file to configure the tunnel output.

The tunnel supports 3 types of configuration:

1. Specify three different patterns to apply to each tunnel surface (both walls and ceiling are independent).
2. Specify one ceiling pattern and one wall pattern. The wall pattern will be mirrored on both walls.
3. Specify one pattern. It will be stretched around the whole tunnel.

Simply uncomment the configuration you want, change the sketch names to the ones you want to run and press Play!


## Adding New Sketches

In order for your sketches to work properly, please place them in the top level
directory and rename any .pde files to .java. The sketch code then needs to be
wrapped in a Java class and a constructor method needs to be added See 'Test.java'
for a simple example.


## Developing in IntelliJ

You can develop and debug a bit in Processing but the IDE is very limited. IntelliJ will give you
proper code completion and much better compiler warnings. To get IntelliJ working you'll have to do
a few things first.

1. Find the folder where you installed processing and search for any *.jar files in that folder.
   Copy all these files to a new folder called 'lib_processing' inside the 'tunnel' folder.
2. Open the 'tunnel' folder in IntelliJ (it should automatically be recognized as an IntelliJ project)
3. Press the Play button.
4. Profit.
