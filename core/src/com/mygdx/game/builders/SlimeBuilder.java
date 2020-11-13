package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.entitites.Slime.Slime;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class SlimeBuilder extends EntityBuilder {
    private Player player;

    /**
     * Create new slime builder instance.
     * @param world
     * @param player
     */
    public SlimeBuilder(World world, Player player) {
        super(world);
        this.player = player;
    }

    @Override
    public Entity createEntity(Vector2 positionVector) {
        return new Slime( createSlimeBody(positionVector), player, 20 );
    }

    private Body createSlimeBody(Vector2 positionVector) {


        bodyDef.position.set(positionVector.x /pixelPerMeter, positionVector.y /pixelPerMeter);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / pixelPerMeter, 4 / pixelPerMeter, new Vector2(0, -3 / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.density = 0.2f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 1f;

        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER | CATEGORY_BIT_ENEMY;

        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("slimeBody");

        shape.setAsBox(120 / pixelPerMeter, 14 / pixelPerMeter, new Vector2(0, 7.5f / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER;


        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("slimePlayerSensor");

        return body;
    }
}
