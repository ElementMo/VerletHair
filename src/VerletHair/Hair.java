package VerletHair;

import processing.core.*;
import toxi.geom.Rect;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.geom.*;

import java.util.ArrayList;
import java.util.List;

public class Hair {
    private final PApplet p;
    public int chainnum = 100;

    private VerletPhysics2D physics;
    private Chain[] chains;

    public Hair(PApplet p) {
        this.p = p;
    }

    public void config(int hairNum, int hairLen) {
        chainnum = hairNum;
        chains = new Chain[chainnum];
        physics = new VerletPhysics2D();
        physics.addBehavior(new GravityBehavior2D(new Vec2D(0, 0.9f)));
        physics.setWorldBounds(new Rect(0, 0, p.width, p.height));

        for (int i = 0; i < chainnum; i++) {
            chains[i] = new Chain((int) p.random(hairLen, hairLen + 30), hairLen / 15, 1, new Vec2D(p.width * 0.5f + i * 1, p.height * 0.5f));
        }
    }

    public void update(PVector[] poses) {
        physics.update();

        for (int i = 0; i < chainnum; i++) {
            chains[i].update(new PVector(poses[i].x, poses[i].y));
        }
        display();
    }

    public void display() {
        for (VerletSpring2D i : physics.springs) {
            p.line(i.a.x, i.a.y, i.b.x, i.b.y);
        }
    }

    class Chain {
        float totalLength;
        int numPoints;
        float strength;

        VerletParticle2D head;

        Chain(float len, int num, float _strength, Vec2D initPos) {

            totalLength = len;
            numPoints = num;
            strength = _strength;

            float deltalen = totalLength / numPoints;

            List<VerletParticle2D> ParticlesList = new ArrayList<VerletParticle2D>();

            for (int i = 0; i < numPoints; i++) {
                VerletParticle2D particle = new VerletParticle2D(initPos.x, initPos.y + i * deltalen);
                physics.addParticle(particle);
                ParticlesList.add(particle);

                if (i > 0) {
                    VerletParticle2D previous = ParticlesList.get(i - 1);
                    VerletSpring2D spring = new VerletSpring2D(particle, previous, deltalen, strength);
                    physics.addSpring(spring);
                }
            }
            head = ParticlesList.get(0);
        }

        public void update(PVector pos) {
            head.x = pos.x;
            head.y = pos.y;
        }

    }
}