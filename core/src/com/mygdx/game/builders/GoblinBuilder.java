package com.mygdx.game.builders;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.entitites.Bullet.Bullet;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Goblin.Goblin;
import com.mygdx.game.entitites.Player;

import static com.mygdx.game.handlers.Box2DVariables.*;


public class GoblinBuilder extends EntityBuilder {
    private Player player;
    private Pool<Bullet> rockPool;
    private Array<Entity> activeBullets;

    public GoblinBuilder(World world, Player player, Pool rockPool, Array<Entity> activeBullets) {
        super(world);
        this.player = player;
        this.rockPool = rockPool;
        this.activeBullets = activeBullets;
    }

    @Override
    public Entity createEntity(Vector2 positionVector) {
        return new Goblin( createGoblinBody(positionVector), player , 50, rockPool, activeBullets );
    }

    private Body createGoblinBody(Vector2 positionVector) {
        bodyDef.position.set(positionVector.x /pixelPerMeter, positionVector.y /pixelPerMeter);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / pixelPerMeter, 8 / pixelPerMeter, new Vector2(0, 0/ pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_PLAYER | CATEGORY_BIT_ENEMY;
        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("goblinBody");

        shape.setAsBox(120 / pixelPerMeter, 14 / pixelPerMeter, new Vector2(0, 7.5f / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_ENEMY;
        fixtureDef.filter.maskBits = CATEGORY_BIT_PLAYER;
        body.setFixedRotation(true);
        body.createFixture(fixtureDef).setUserData("goblinPlayerSensor");

        return body;
    }
}
