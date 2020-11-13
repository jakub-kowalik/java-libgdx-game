package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entitites.Collectable;
import com.mygdx.game.entitites.Entity;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class CollectableBuilder extends EntityBuilder {

    public CollectableBuilder(World world) {
        super(world);
    }

    @Override
    public Entity createEntity(Vector2 positionVector) {

        //BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(positionVector.x, positionVector.y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(8 / pixelPerMeter);

        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_COLLECTABLE;
        fixtureDef.filter.maskBits = CATEGORY_BIT_PLAYER;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData("crystal");

        return new Collectable(body);
    }
}
