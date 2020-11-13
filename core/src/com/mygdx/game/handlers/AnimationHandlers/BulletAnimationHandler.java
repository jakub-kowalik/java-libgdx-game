package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Bullet.Bullet;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class BulletAnimationHandler extends AnimationHandler {
    private Bullet bullet;

    private float frameWidth, frameHeight;
    private Sprite sprite;

    public BulletAnimationHandler(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    protected void loadAnimations() {
        sprite = new Sprite(MyGdxGame.resources.getTexture("rock"));
        frameHeight = sprite.getHeight();
        frameWidth = sprite.getWidth();
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.setPosition(bullet.getPosition().x * pixelPerMeter - frameWidth / 2, bullet.getPosition().y * pixelPerMeter - frameHeight / 2);
        sprite.draw(batch);
    }

    @Override
    public float getFrameWidth() {
        return frameWidth;
    }

    @Override
    public float getFrameHeight() {
        return frameHeight;
    }
}
