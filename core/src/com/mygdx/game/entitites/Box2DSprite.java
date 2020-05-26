package com.mygdx.game.entitites;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.handlers.AnimationHandler;

public class Box2DSprite {

    protected Body body;
    protected AnimationHandler animationHandler;
    protected float width;
    protected float height;
    public Box2DSprite(Body body) {
        this.body = body;
        animationHandler = new AnimationHandler();
    }

    public void setAnimationHandler(TextureRegion[] textureRegion, float delay) {
        animationHandler.setFrames(textureRegion, delay);
        width = textureRegion[0].getRegionWidth();
        height = textureRegion[0].getRegionHeight();
    }

    public void update(float dt) {
        animationHandler.update(dt);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(animationHandler.getFrame(), body.getPosition().x * pixelPerMeter - width / 2, body.getPosition().y * pixelPerMeter - height / 2);
        spriteBatch.end();

    }

    public Body getBody() {return body; }
    public Vector2 getPosition() { return body.getPosition(); }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

}
