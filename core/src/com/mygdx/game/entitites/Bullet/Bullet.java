package com.mygdx.game.entitites.Bullet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Hit;
import com.mygdx.game.handlers.AnimationHandlers.BulletAnimationHandler;
import java.util.Random;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class Bullet extends Entity implements Pool.Poolable {

    private Random random;

    public Bullet(Body body, int hitpoints) {
        super(body, hitpoints);
        random = new Random();
    }

    @Override
    public void setAnimationHandler() {
        this.animationHandler = new BulletAnimationHandler(this);
    }

    @Override
    public void getHit(Hit hit) {
        reset();
    }

    @Override
    public void reset() {
        isAlive = false;
    }
    public void init(Vector2 positionVector, boolean flyingRight) {
        isAlive = true;
        body.setTransform(positionVector, 0);   //
        throwRock(flyingRight);
    }

    private void throwRock(boolean flyingRight) {
        float rand = 0.1f +  ((float) random.nextInt(12) ) / 100;
        float power = flyingRight ? rand : -rand;
        System.out.println(power);
        body.applyLinearImpulse(
                power,
                0.1f,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true);
    }
}
