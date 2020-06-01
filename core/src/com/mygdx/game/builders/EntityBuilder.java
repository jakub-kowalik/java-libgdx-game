package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entitites.Entity;

public abstract class EntityBuilder {
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected World world;


    public EntityBuilder(World world){
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        this.world = world;
    }

    public abstract Entity createEntity(Vector2 positionVector);
}
