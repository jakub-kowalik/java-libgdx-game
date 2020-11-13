package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entitites.Bullet.Bullet;
import com.mygdx.game.entitites.Entity;

import static com.mygdx.game.handlers.Box2DVariables.*;
import static com.mygdx.game.handlers.Box2DVariables.CATEGORY_BIT_PLAYER;

public class BulletBuilder extends EntityBuilder {

    public BulletBuilder(World world) {
        super(world);
    }


    @Override
    public Entity createEntity(Vector2 positionVector) {
        return new Bullet(createBulletBody(positionVector), 50);
    }


    public Body createBulletBody(Vector2 positionVector) {
        bodyDef.position.set(positionVector.x / pixelPerMeter, positionVector.y / pixelPerMeter);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(.1f);
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER;

        body.setFixedRotation(false);
        body.createFixture(fixtureDef).setUserData("rockBody");

        return body;
    }
}
