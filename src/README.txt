# README


## Requirements

 - Processing 3.0


## Usage and Configuration


Double click the 'src.pde' file to open it in Processing.

In order for your sketches to work properly, please place them in the top level
directory and rename any .pde files to .java. The sketch code then needs to be
wrapped in a Java class and a constructor method needs to be added (to set the sketch
size). See Test.java for a simple example.

Once your sketches have been added, modify the Main.java file to configure the tunnel output.

The framework supports 3 types of configuration:

1. Specify three different patterns to apply to each tunnel surface
2. Specify a ceiling pattern and a wall pattern. The wall pattern will be mirrored on both walls.
3. Specify one pattern which will be stretched around the whole tunnel.

Simply uncomment the configuration you want, comment out the rest and press Play!


## Development and Debugging

You can debug a bit in Processing but the IDE is very limited. You can open the project in
IntelliJ for more advanced work but be aware that certain processing libraries may not be
available in your CLASS_PATH.
