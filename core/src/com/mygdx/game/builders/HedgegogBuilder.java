package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Hedgegog;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class HedgegogBuilder extends EntityBuilder {
    public HedgegogBuilder(World world) {
        super(world);
    }

    @Override
    public Entity createEntity(Vector2 positionVector) {
        return new Hedgegog( createHedgegogBody(positionVector) );
    }

    private Body createHedgegogBody(Vector2 positionVector) {
        bodyDef.position.set(positionVector.x / pixelPerMeter, positionVector.y / pixelPerMeter);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        Body body = world.createBody(bodyDef);

        // TODO
        return body;
    }
}
