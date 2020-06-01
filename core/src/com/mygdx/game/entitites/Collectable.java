package com.mygdx.game.entitites;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.handlers.AnimationHandlers.CollectableAnimationHandler;

public class Collectable extends Entity {

    public Collectable(Body body) {
        super(body);
    }

    @Override
    public void setAnimationHandler() {
        this.animationHandler = new CollectableAnimationHandler(this);
    }

}
