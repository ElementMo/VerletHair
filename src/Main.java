import processing.core.*;
import VerletHair.*;

public class Main extends PApplet {
    private int hairNum = 50;
    private int count = 0;
    private int tick = 0;

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
        for (int i = hairV2.chainnum - 1; i >= 0; i--) {
            poses[i] = PVector.add(new PVector(mouseX, mouseY), PVector.mult(new PVector(cos(-PI / hairNum * i), sin(-PI / hairNum * i)), 50));
        }
        background(255);
        ellipse(mouseX, mouseY, 100, 100);

        hairV2.update(poses);
        hairV2.display();

        if ((pmouseX - mouseX) > 15) {
            count += 12;
        } else if ((pmouseX - mouseX) < 1 && count > 0) {
            count--;
        }
        if (count > 0) {
            tick++;
            if (tick > 20) {
                growHair();
                tick = 0;
            }

        }
        if (count == 0) {
            deteteHair();
        }
        println(count);
    }

    public void growHair() {
        if (hairV2.physics.springs.size() == 0) {
            hairV2.config(hairNum, 8, poses);
            hairV2.addNode();

        } else {
            hairV2.addNode();
        }
    }

    public void deteteHair() {
        hairV2.removeNode();
    }

    static public void main(String[] args) {
        PApplet.main("Main");
    }
}
