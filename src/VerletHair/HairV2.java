package VerletHair;

import processing.core.*;
import toxi.geom.Rect;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.geom.*;

import java.util.ArrayList;
import java.util.List;

public class HairV2 {
    private final PApplet papplet;
    public int chainnum = 100;

    private VerletPhysics2D physics;
    public List<Chain> chains;

    public HairV2(PApplet p) {
        this.papplet = p;
    }

    public void config(int hairNum, int hairLen) {
        chainnum = hairNum;
        chains = new ArrayList<Chain>(chainnum);
        physics = new VerletPhysics2D();
        physics.addBehavior(new GravityBehavior2D(new Vec2D(0, 0.2f)));
        physics.setWorldBounds(new Rect(0, 0, papplet.width, papplet.height));

        for (int i = 0; i < chainnum; i++) {
            chains.add(new Chain((int) papplet.random(hairLen, hairLen + 30), hairLen / 15, 1, new Vec2D(papplet.width * 0.5f + i * 1, papplet.height * 0.5f)));
        }
    }

    public void update(PVector[] poses) {
        physics.update();

        for (int i = 0; i < chainnum; i++) {
            chains.get(i).update(new PVector(poses[i].x, poses[i].y));
        }
        display();
    }

    public void display() {
        for (VerletSpring2D i : physics.springs) {
            papplet.line(i.a.x, i.a.y, i.b.x, i.b.y);
        }
    }

    public void addNode()
    {
        for(Chain chain : chains)
        {
            chain.addNode();
        }
    }

    class Chain {
        float totalLength;
        int numPoints;
        float strength;

        VerletParticle2D head;
        List<VerletParticle2D> ParticlesList;

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

        public void addNode()
        {
            VerletParticle2D particle = new VerletParticle2D(ParticlesList.get(ParticlesList.size()-1).x, ParticlesList.get(ParticlesList.size()-1).y + 5);
            physics.addParticle(particle);
            ParticlesList.add(particle);

            VerletParticle2D previous = ParticlesList.get(ParticlesList.size() - 1);
            VerletSpring2D spring = new VerletSpring2D(previous, particle, 5, strength);
            physics.addSpring(spring);
        }
    }
}