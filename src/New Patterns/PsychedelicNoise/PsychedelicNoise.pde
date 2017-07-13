PImage img;  // image used to draw the pattern
color[] c;  // aray of colors


/*-------------*/


void setup() {
  size(500, 500);
  
  // the dimensions of the image are twice the dimentions of
  // the canvas to add antialiasing when the image is reduced
  img = createImage(2*width, 2*height, RGB);   
  
  // noise detail is changed to make the pattern smooth
  noiseDetail(1, 0.3);
  
  c = new color[255];  // the array has 255 colors, one color for each possible grayscale color
  
  noLoop();
}


/*-------------*/


void draw() {
  // fill the array with random colors
  for (int i = 0; i < c.length; i += 1) {
    c[i] = color(random(255), random(255), random(255));
  }
  
  // create pattern
  img.loadPixels();  
  float nx, ny, nz;
  float theta = 0, phi = 0;
  float R = 5, r = 1;
  for (int x = 0; x < img.width; x += 1) {
    for (int y = 0; y < img.height; y += 1) {
      // map x and y to angles between 0 and TWO_PI
      theta = map(x, 0, img.width, 0, TWO_PI);
      phi = map(y, 0, img.height, 0, TWO_PI);
      
      // calculate the parameters of noise with the equation of a torus
      // this is used to make the pattern seamless
      nx = (R+r*cos(phi))*cos(theta);
      ny = (R+r*cos(phi))*sin(theta);
      nz = r*sin(phi);
      
      // normalize noise parameters so that the de pattern has homogeneous dimensions
      nx = norm(nx, 0, R+r);
      ny = norm(ny, 0, R+r);
      nz = norm(nz, 0, r);
      
      // apply noise twice and use the equivalent color on the pallete
      try{
      img.pixels[x + y*img.width] = c[floor(255*noise(floor(255*noise(nx, ny, nz)), (float)0.1))];
      
      } catch(Exception e) {
        //  System.out.println("got interrupted!");
      }    }
  }
  img.updatePixels();
  
  // display pattern
  image(img, 0, 0, width, height);  // the image is reduce to the size of the canvas to make it smooth
}


/*-------------*/


void mousePressed() { 
  noiseSeed((long)random(0xFFFFFF));
//noiseSeed(undefined);  // edited to work on openProcessing beta
  redraw();
}


/*-------------*/


void keyPressed() {
  save("image.png");
}