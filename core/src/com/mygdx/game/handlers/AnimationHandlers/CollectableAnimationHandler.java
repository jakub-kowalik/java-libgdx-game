package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Collectable;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class CollectableAnimationHandler extends AnimationHandler {
    Collectable collectable;
    public Animation<TextureRegion> animation;

    public float frameWidth;
    public float frameHeight;

    public CollectableAnimationHandler(Collectable collectable){
        super();
        this.collectable = collectable;
    }

    @Override
    protected void loadAnimations() {
        Texture texture = MyGdxGame.resources.getTexture("crystals");
        TextureRegion[] animationFrames = TextureRegion.split(texture, 16, 16)[0];

        frameWidth = animationFrames[0].getRegionWidth();
        frameHeight = animationFrames[0].getRegionHeight();

        animation = new Animation<TextureRegion>(1/5f, animationFrames);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime, true), collectable.getPosition().x * pixelPerMeter - frameWidth / 2, collectable.getPosition().y * pixelPerMeter - frameHeight / 2 );
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
