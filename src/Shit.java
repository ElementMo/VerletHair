import com.thomasdiewald.liquidfun.java.DwWorld;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics2D;
import org.jbox2d.dynamics.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.collision.shapes.*;

import geomerative.*;


public class Shit {
    DwWorld world;
    private final PApplet papplet;
    private int shitCount = 99;

    RShape grp;
    RPoint[] points;
    Vec2 vertices[];


    public Shit(PApplet p) {
        this.papplet = p;

        RG.init(papplet);
        grp = RG.loadShape("shit.svg");
        grp = RG.centerIn(grp, papplet.g);
        float scale = 0.002f;
        RG.setPolygonizer(RG.ADAPTATIVE);
        RG.setPolygonizer(RG.UNIFORMLENGTH);
        RG.setPolygonizerLength(300);
        points = grp.getPoints();
        vertices = new Vec2[points.length];
        for (int i = 0; i < points.length; i++) {
            System.out.printf("vertices[%d] = new Vec2(%.2ff, %.2ff); \n", i, points[i].x * scale, -points[i].y * scale);
            vertices[i] = new Vec2(points[i].x * scale, -points[i].y * scale);
        }

        reset();
    }


    public void reset() {

        if (world != null) {
            world.release();
        }
        world = null;

        world = new DwWorld(papplet, 40);
    }

    public void updateFrame() {

        world.update();
        world.applyTransform((PGraphics2D) papplet.g);
        world.display((PGraphics2D) papplet.g);

        world.displayDebugDraw((PGraphics2D) papplet.g);

    }

    public void addShit(float posx, float posy) {
        shitCount++;
        if (shitCount > 50) {
            float x = papplet.map(posx - papplet.width / 2, 0, papplet.width, 0, 32.0f);
            float y = papplet.map(-posy + papplet.height, 0, papplet.height, 0, 18.0f);
            initScene(x, y);
            shitCount = 0;
        }
    }

    public void initScene(float posx, float posy) {

        Body ground = null;
        {
            BodyDef bd = new BodyDef();
            ground = world.createBody(bd);

            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(-16.0f, 0.0f), new Vec2(16.0f, 0.0f));
            ground.createFixture(shape, 0.0f);
        }


        PolygonShape shape = new PolygonShape();
        shape.set(vertices, 8);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;


        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posx, posy);
        Body body = world.createBody(bd);
        body.createFixture(fd);

        world.bodies.addAll();
        world.removeLostBodies();
//        world.removeLostParticles();

    }
}
