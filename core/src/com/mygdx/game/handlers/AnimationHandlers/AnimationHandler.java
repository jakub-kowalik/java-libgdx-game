package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AnimationHandler {
    protected Texture animationSheet;

    public float stateTime;

    AnimationHandler() {
        stateTime = 0;
        loadAnimations();
    }

    protected abstract void loadAnimations();

    public void update(float dt) {
        stateTime += dt;
    }

    public abstract void render(SpriteBatch batch);

    public abstract float getFrameWidth();

    public abstract float getFrameHeight();
}
