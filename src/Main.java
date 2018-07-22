import processing.core.*;
import VerletHair.*;

public class Main extends PApplet {
    private int hairNum = 50;
    private int growCount = 0;
    private int growTick = 0;

    private HairV2 hairV2;
    private PVector[] poses = new PVector[hairNum];

    public void settings() {
        size(1280, 720);
    }

    public void setup() {
        strokeWeight(2);
        stroke(0, 150);

        hairV2 = new HairV2(this);
        for (int i = 0; i < hairV2.chainnum; i++) {
            poses[i] = PVector.add(new PVector(mouseX, mouseY), PVector.mult(new PVector(cos(-PI / hairNum * i), sin(-PI / hairNum * i)), 50));
        }
        hairV2.config(hairNum, 8, poses);


    }

    public void draw() {
//        for (int i = hairV2.chainnum - 1; i >= 0; i--) {
//            poses[i] = PVector.add(new PVector(mouseX, mouseY), PVector.mult(new PVector(cos(-PI / hairNum * i), sin(-PI / hairNum * i)), 50));
//        }

        for (int i = hairV2.chainnum - 1; i >= 0; i--) {
            poses[i] = new PVector(mouseX + i*2, mouseY);
        }

        background(255);
        ellipse(mouseX, mouseY, 100, 100);

        hairV2.drawGrowingHair(pmouseX, new PVector(mouseX, mouseY), poses);

//        hairV2.update(poses);
//        hairV2.display();
//
//        if ((pmouseX - mouseX) > 15) {
//            growCount += 12;
//        } else if ((pmouseX - mouseX) < 1 && growCount > 0) {
//            growCount--;
//        }
//        if (growCount > 0) {
//            growTick++;
//            if (growTick > 20) {
//                growHair();
//                growTick = 0;
//            }
//
//        }
//        if (growCount == 0) {
//            deteteHair();
//        }
    }

//    public void growHair() {
//        if (hairV2.physics.springs.size() == 0) {
//            hairV2.config(hairNum, 8, poses);
//            hairV2.addNode();
//
//        } else {
//            hairV2.addNode();
//        }
//    }
//
//    public void deteteHair() {
//        hairV2.removeNode();
//    }

    static public void main(String[] args) {
        PApplet.main("Main");
    }
}
