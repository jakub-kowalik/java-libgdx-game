package com.mygdx.game.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import static com.mygdx.game.handlers.Box2DVariables.*;
import static com.mygdx.game.handlers.Box2DVariables.CATEGORY_BIT_COLLECTABLE;

public class PlayerBodyBuilder {
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    PolygonShape shape;
    RevoluteJoint playerMotor;
    World world;
    Body body;

    public PlayerBodyBuilder(World world, Vector2 positionVector) {
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        shape = new PolygonShape();
        this.world = world;

        this.body = createBody(bodyDef, fixtureDef, world, positionVector);
    }



    public Body createBody(BodyDef bodyDef, FixtureDef fixtureDef, World world, Vector2 positionVector) {

        // create player
        bodyDef.position.set(positionVector.x / pixelPerMeter, positionVector.y / pixelPerMeter);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);


        shape.setAsBox(4 / pixelPerMeter, 16 / pixelPerMeter);
        fixtureDef.shape = shape;
        fixtureDef.density = 10;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0f;

        //fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        // fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_COLLECTABLE;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);

        // collision box

        shape.setAsBox(4f / pixelPerMeter, 13 / pixelPerMeter, new Vector2(0, 1 / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.density = 5;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0f;

        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_COLLECTABLE;
        body.createFixture(fixtureDef).setUserData("player");

        //wheel

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(4f/pixelPerMeter);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(positionVector.x / pixelPerMeter, positionVector.y / pixelPerMeter);
        Body body2 = world.createBody(bodyDef);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 50;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 20f;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_COLLECTABLE;
        body2.createFixture(fixtureDef);
        body2.setUserData("player");
        circleShape.dispose();

        RevoluteJointDef motor = new RevoluteJointDef();
        motor.enableMotor = false;
//        motor.motorSpeed = 360 * MathUtils.degreesToRadians;
        motor.maxMotorTorque = 100;
        motor.bodyA = body;
        motor.bodyB = body2;
        motor.collideConnected = false;

        motor.localAnchorA.set(0,(-16+4f) / pixelPerMeter);
        motor.localAnchorB.set(0,0);

        playerMotor = (RevoluteJoint) world.createJoint(motor);


        //create foot sensor

        shape.setAsBox(3f / pixelPerMeter, 0.5f / pixelPerMeter, new Vector2(0, -16.5f / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.density = 0;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");

        //create head sensor

        shape.setAsBox(4f / pixelPerMeter, 0.5f / pixelPerMeter, new Vector2(0, 16.5f / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("head");

        //create left sensor

        shape.setAsBox(0.5f / pixelPerMeter, 15 / pixelPerMeter, new Vector2(-4f / pixelPerMeter, 0 ), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("left");

        //create right sensor

        shape.setAsBox(0.5f / pixelPerMeter, 15 / pixelPerMeter, new Vector2(4f / pixelPerMeter, 0), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("right");
        shape.dispose();

        return body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public PolygonShape getShape() {
        return shape;
    }

    public RevoluteJoint getPlayerMotor() {
        return playerMotor;
    }

    public Body getBody() {
        return body;
    }
}
