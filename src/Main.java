import processing.core.*;
import VerletHair.*;

public class Main extends PApplet {
    private int hairNum = 80;

    private Hair hair;
    private HairV2 hairV2;
    private PVector[] poses = new PVector[hairNum];

    public void settings() {
        size(1280, 720);
    }

    public void setup() {
//        hair = new Hair(this);
//        hair.config(hairNum, 150);

        hairV2 = new HairV2(this);
        hairV2.config(hairNum, 150);
        strokeWeight(2);
        stroke(0,150);
    }

    public void draw() {
        for (int i = 0; i < hairNum; i++) {
            poses[i] = PVector.add(new PVector(mouseX, mouseY), PVector.mult(new PVector(cos(-PI / hairNum * i), sin(-PI / hairNum * i)), 50));
        }

        background(255);
        ellipse(mouseX, mouseY, 100, 100);
//        hair.update(poses);
//        hair.display();

        hairV2.update(poses);
        hairV2.display();


    }

    static public void main(String[] args) {
        PApplet.main("Main");
    }
}
