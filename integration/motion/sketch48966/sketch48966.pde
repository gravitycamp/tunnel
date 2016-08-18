/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/48966*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
Stripe[] myStripes = new Stripe[100];

void setup() { 
  size(400, 400); 
  smooth();  
  noStroke();  

  for (int i=0; i<myStripes.length; i++) { 

    myStripes[i] = new Stripe(i*50, color(random(100), 0, random(100, 255)) );
  }
}

void draw() { 

  background(0); 
  for (int i=0; i<myStripes.length; i++) { 

    myStripes[i].update(); 
    myStripes[i].drawStripe();
  }
}

void mousePressed() { 
  for (int i=0; i<myStripes.length; i++) { 

    myStripes[i].checkMousePress(); 
   
  }
  
}




class Stripe {

  float x; 
  float w; 
  float xspeed; 
  color c; 
  boolean clicked; 


  // constructor
  Stripe(float xpos, color col) { 

    x = xpos; 
    w = random(10, 50); 
    c = col; 
    xspeed = random(1, 4);
    
    clicked = false; 
  }  

  void update() { 
    x+=xspeed; 
    if (x>width) {
      x = -w;
    }
  }
  void drawStripe() { 

    if (clicked) { 
      fill(blendColor(color(0,0,255), color(random(255),random(255),random(255)), ADD));
    } 
    else {
      fill(c);
    }

    rect(0, x, height, w);
  }
  
  void checkMousePress() { 
    if((mouseX > x ) && (mouseX< x + w)) {
      
       clicked = !clicked;
    }
    
  }
  
}