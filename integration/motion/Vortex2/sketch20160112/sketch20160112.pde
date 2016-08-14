/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/294460*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/**
* Psychedelic Perlin 2D
*
* @author aa_debdeb
* @date 2016/01/12
*/

float noiseX;
float noiseY;
float noiseT;

void setup(){
  size(500, 500);
  frameRate(30);
  colorMode(HSB, 360, 100, 100);
  noiseX = random(100);
  noiseY = random(100);
  noiseT = random(100);
}

void draw(){
  loadPixels();
  for(int y = 0; y < height; y++){
    for(int x = 0; x < width; x++){
      float n = noise(noiseX + x * 0.03, noiseY + y * 0.03, noiseT + frameCount * 0.2);
      float level = int(n * 20);
      float hue = (level * 103) % 360;
      color c = color(hue, 100, 100); 
      pixels[y * width + x] = c;
    }
  }
  updatePixels();
}

