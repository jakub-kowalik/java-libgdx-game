package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.mygdx.game.handlers.AnimationHandlers.AnimationHandler;

public abstract class Entity {

    protected Body body;
    protected AnimationHandler animationHandler;
    public Entity(Body body) {
        this.body = body;
        setAnimationHandler();
    }
    public abstract void setAnimationHandler();

    public void update(float dt) {
        animationHandler.update(dt);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        animationHandler.render(spriteBatch);
        spriteBatch.end();

    }

    public Body getBody() {return body; }
    public Vector2 getPosition() { return body.getPosition(); }

}
