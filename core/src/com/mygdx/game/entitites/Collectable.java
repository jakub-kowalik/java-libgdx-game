package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.AnimationHandlers.CollectableAnimationHandler;

public class Collectable extends Box2DSprite {

    public Collectable(Body body) {
        super(body);
    }

    @Override
    public void setAnimationHandler() {
        this.animationHandler = new CollectableAnimationHandler(this);
    }

}
