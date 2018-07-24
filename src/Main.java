import processing.core.*;
import VerletHair.*;

import java.util.ArrayList;

public class Main extends PApplet {

    Shit st;

    public void settings() {
        size(1280, 720, P2D);
        smooth(8);
    }

    public void setup() {
        st = new Shit(this);
        frameRate(100);
    }

    public void draw() {

        background(255);
        ellipse(mouseX, mouseY, 100, 100);


        st.updateFrame();
        if (keyPressed) {

            if (key == 'r') {
                st.reset();
            }
            if (key == 'c') {
                st.addShit(mouseX, mouseY);
            }
        }
    }


    static public void main(String[] args) {
        PApplet.main("Main");
    }
}
