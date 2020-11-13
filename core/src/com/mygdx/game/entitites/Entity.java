package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.handlers.AnimationHandlers.AnimationHandler;

public abstract class Entity {

    public boolean isAlive;
    public boolean isDying;
    protected int hitpoints;
    protected Body body;
    protected AnimationHandler animationHandler;


    public Entity(Body body, int hitpoints) {
        isAlive = true;
        isDying = false;
        this.body = body;
        this.hitpoints = hitpoints;
        setAnimationHandler();
    }

    public abstract void setAnimationHandler();

    public abstract void getHit(Hit hit);

    public void update(float dt) {
        animationHandler.update(dt);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        animationHandler.render(spriteBatch);
        spriteBatch.end();

    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
