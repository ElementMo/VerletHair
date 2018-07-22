package VerletHair;

import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.GravityBehavior2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HairV2 {
    private final PApplet papplet;
    public int chainnum = 50;

    public VerletPhysics2D physics;
    public List<Chain> chains;

    private boolean hairDrop = false;
    private int tick;

    public HairV2(PApplet p) {
        this.papplet = p;
    }

    public void config(int hairNum, int hairLen, PVector[] initPos) {
        chainnum = hairNum;
        chains = new ArrayList<Chain>(chainnum);
        physics = new VerletPhysics2D();
        physics.addBehavior(new GravityBehavior2D(new Vec2D(0, 0.9f)));
//        physics.setWorldBounds(new Rect(0, 0, papplet.width, papplet.height));

        for (int i = 0; i < chainnum; i++) {
            chains.add(new Chain((int) papplet.random(hairLen, hairLen + 32), hairLen / 8, 1, new Vec2D(initPos[i].x + i * 1, initPos[i].y)));
        }
        hairDrop = false;
        tick = 0;
    }

    public void update(PVector[] poses) {
        physics.update();

        if (!hairDrop) {
            for (int i = 0; i < chainnum; i++) {
                chains.get(i).update(new PVector(poses[i].x, poses[i].y));
            }
        }
        display();
    }

    public void display() {
        for (VerletSpring2D i : physics.springs) {
            papplet.line(i.a.x, i.a.y, i.b.x, i.b.y);
        }
    }

    public void addNode() {
        if (physics.springs.size() < 1000) {
            for (Chain chain : chains) {
                chain.addNode();
            }
        }
    }

    public void removeNode() {
        tick++;
        hairDrop = true;
//        System.out.println(physics.springs.size() + " " + chainnum);
        if (tick > 50) {
            for (int i = physics.springs.size() - 1; i >= 0; i--) {
                VerletSpring2D tailSping = physics.springs.get(i);
                physics.removeSpringElements(tailSping);
            }
        }
//        if (physics.springs.size() > 0) {
//            int gap = physics.springs.size() / chainnum;
//            System.out.println(physics.springs.size() + " " + chainnum + " " + gap);
//            for (int i = chainnum; i >= 1; i--) {
//                VerletSpring2D tailSping = physics.springs.get(i * gap - 1);
//                physics.removeSpringElements(tailSping);
//            }
//        }
    }

    class Chain {
        float totalLength;
        int numPoints;
        float strength;

        VerletParticle2D head;
        List<VerletParticle2D> ParticlesList;
        VerletSpring2D newSpringSegment;

        Chain(float len, int num, float _strength, Vec2D initPos) {

            totalLength = len;
            numPoints = num;
            strength = _strength;

            float deltalen = totalLength / numPoints;

            ParticlesList = new ArrayList<VerletParticle2D>();

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

        public void addNode() {
            VerletParticle2D particle = new VerletParticle2D(ParticlesList.get(ParticlesList.size() - 1).x, ParticlesList.get(ParticlesList.size() - 1).y + 8);
            physics.addParticle(particle);
            ParticlesList.add(particle);

            VerletParticle2D previous = ParticlesList.get(ParticlesList.size() - 2);
            newSpringSegment = new VerletSpring2D(previous, particle, papplet.random(4, 12), strength);
            physics.addSpring(newSpringSegment);
        }

    }
}