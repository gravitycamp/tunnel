/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/362365*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/**
* camera on sine curve
*
* @author aa_debdeb
* @date 2016/05/20
*/

ArrayList<Block> blocks;
float speed = 30;

void setup(){
  size(500, 500, P3D);
  noFill();
  stroke(0,255, 0);
  blocks = new ArrayList<Block>();
}

void draw(){
  background(0); 
  translate(width / 2, height / 2, 0);
  float time = frameCount * 0.01;
  float cy = map(sin(time), -1, 1, -height / 4, height / 4);
  float ccx = map(mouseX, 0, width, -width / 4, width / 4);
  float ccy = map(sin(time + PI / 4), -1, 1, -height / 4, height / 4);
  float ccz = -100;
  camera(0, cy, 0, ccx, ccy, ccz, 0, 1, 0); 
  ArrayList<Block> nextBlocks = new ArrayList<Block>();
  for(Block block: blocks){
    block.display();
    block.update();
    if(!block.isGone()){
      nextBlocks.add(block);
    }
  }
  blocks = nextBlocks;
  blocks.add(new Block());
}

class Block{
  PVector pos;
  float size;
  Block(){
    float x = random(-width, width);
    float y = random(-height, height);
    float z = -3000;
    pos = new PVector(x, y, z);
    size = random(30, 150);
  }
  
  void display(){
    pushMatrix();
    translate(pos.x, pos.y, pos.z);
    stroke(random(255), random(255), random(255));
    fill(random(255), random(255), random(255));
    box(size);
    popMatrix();
  }
  
  void update(){
    pos.z += speed;
  }
  
  boolean isGone(){
    return pos.z > 0;
  }
}