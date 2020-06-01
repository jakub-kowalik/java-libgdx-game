package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.AnimationHandlers.HedgegogAnimationHandler;

public class Hedgegog extends Entity {



    TextureRegion[] walkingAnimationRegion;
    TextureRegion[] idleAnimationRegion;

    public Hedgegog(Body body) {
        super(body);
    }

    @Override
    public void setAnimationHandler() {
        animationHandler = new HedgegogAnimationHandler(this);
    }

}
