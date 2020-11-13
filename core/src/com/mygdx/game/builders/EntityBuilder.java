package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entitites.Entity;

public abstract class EntityBuilder {
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected World world;

    public EntityBuilder(World world){
        this.world = world;
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
    }

    public abstract Entity createEntity(Vector2 positionVector);
}
