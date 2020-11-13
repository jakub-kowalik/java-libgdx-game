package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.KingHedgegog.KingHedgegog;
import com.mygdx.game.entitites.Player;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class KingHedgegogBuilder extends EntityBuilder {
    private Player player;

    public KingHedgegogBuilder(World world, Player player) {
        super(world);
        this.player = player;
    }

    @Override
    public Entity createEntity(Vector2 positionVector) {
        return new KingHedgegog( createKingBody(positionVector), player , 50 );
    }

    private Body createKingBody(Vector2 positionVector) {
        bodyDef.position.set(positionVector.x / pixelPerMeter, positionVector.y / pixelPerMeter  );
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / pixelPerMeter, 4 / pixelPerMeter, new Vector2(0, -3 / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.density = 0.8f;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.5f;

        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER;

        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("kingBody");

        shape.setAsBox(120 / pixelPerMeter, 14 / pixelPerMeter, new Vector2(0, 7.5f / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER;
        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("kingPlayerSensor");


        return body;
    }
}
