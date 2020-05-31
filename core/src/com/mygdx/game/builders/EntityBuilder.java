package com.mygdx.game.builders;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.mygdx.game.entitites.Box2DSprite;

public abstract class EntityBuilder {

    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected PolygonShape shape;
    protected RevoluteJoint playerMotor;
    protected World world;
    protected Body body;
    protected Box2DSprite entity;


  //  public abstract
}
