package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Box2DSprite;
import com.mygdx.game.entitites.Collectable;
import com.mygdx.game.entitites.HUD;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.ContactHandler;
import com.mygdx.game.handlers.InputHandler;
import com.mygdx.game.handlers.MapBodyBuilder;
import com.mygdx.game.screens.BaseScreen;


import java.lang.management.GarbageCollectorMXBean;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class PlayScreen extends BaseScreen {


    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private OrthographicCamera box2DCamera;

    RevoluteJoint playerMotor;

    private ContactHandler contactHandler;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Player player;
    private Array<Collectable> collectable;
    private HUD hud;
    MapBodyBuilder mapBodyBuilder;
    private boolean debug = true;

    public PlayScreen(MyGdxGame game) {

        super(game);

        // set up box2d
        world = new World(new Vector2(0, -9.81f), false);
        contactHandler = new ContactHandler();
        world.setContactListener(contactHandler);
        box2DDebugRenderer = new Box2DDebugRenderer();

        mapBodyBuilder = new MapBodyBuilder();
        // set up player
        createPlayer();

        // set up tiledmap
        createTiledMap();

        //create collectables
        createCollectable();

        // set box2d cam
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, game.V_WIDTH / pixelPerMeter, game.V_HEIGHT / pixelPerMeter);

        //set up hud
        hud = new HUD(player);

        //set background

    }


 //   @Override
    public void handleInput() {

        if (InputHandler.isPressed(InputHandler.BUTTON1) && contactHandler.getIsGrounded()) {
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(new Vector2(0, 2500 /pixelPerMeter), player.getBody().getWorldCenter(), true);
        }

        if (InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4)) {
            if (player.getBody().getLinearVelocity().x > -5) {
                playerMotor.enableMotor(false);
             player.getBody().applyLinearImpulse(
                        -100 / pixelPerMeter,
                        0,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true
                );
            }
        }

        if (InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isDown(InputHandler.BUTTON2)) {
            if (player.getBody().getLinearVelocity().x < 5) {
                playerMotor.enableMotor(false);
              player.getBody().applyLinearImpulse(
                        100 / pixelPerMeter,
                        0 / pixelPerMeter,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true
                );
            }

        }

        if (InputHandler.isPressed(InputHandler.BUTTON5)) {
            debug = true;
        }
        if (InputHandler.isPressed(InputHandler.BUTTON6)) {
            debug = false;
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 1, 1);

        //remove collectables
        Array<Body> bodies = contactHandler.getBodiesToRemove();
        for (int i = 0; i < bodies.size; i++) {
            Body body = bodies.get(i);
            collectable.removeValue((Collectable) body.getUserData(), true);
            world.destroyBody(bodies.get(i));
            player.collectCrystal();
        }
        bodies.clear();

        player.update(dt);

        for (int i = 0; i < collectable.size; i++)
            collectable.get(i).update(dt);

        if (Math.abs(player.getBody().getLinearVelocity().x) > 0 && !InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4)) {
            playerMotor.setMotorSpeed(0);
            playerMotor.enableMotor(true);

        }
    }

    @Override
    public void render(float dt) {



        spriteBatch.begin();
        MyGdxGame.backgroundSprite.draw(spriteBatch);
        spriteBatch.end();

        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();

        camera.position.set(player.getPosition().x * pixelPerMeter, player.getPosition().y * pixelPerMeter, 0);
        camera.update();

        //draw player
        spriteBatch.setProjectionMatrix(camera.combined);
        player.render(spriteBatch);

        for(int i = 0; i < collectable.size; i++) {
            collectable.get(i).render(spriteBatch);
        }

        spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(spriteBatch);


        if (debug) {
            box2DCamera.position.set(player.getPosition(), 0);
            box2DCamera.update();
            box2DDebugRenderer.render(world, box2DCamera.combined);
        }
    }


    @Override
    public void dispose() {

    }

    private void createPlayer() {

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        // create player
        bodyDef.position.set(160 / pixelPerMeter, 200 / pixelPerMeter);
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

        shape.setAsBox(3.5f / pixelPerMeter, 13 / pixelPerMeter, new Vector2(0, 1 / pixelPerMeter), 0);
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
        bodyDef.position.set(160 / pixelPerMeter, 200 / pixelPerMeter);
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

        //create sensor

        shape.setAsBox(3f / pixelPerMeter, 2 / pixelPerMeter, new Vector2(0, -18 / pixelPerMeter), 0);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_BIT_GROUND;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");
        shape.dispose();

        //create player



        player = new Player(body);
        body.setUserData(player);

    }

    private void createTiledMap() {

        // load map
        tiledMap = new TmxMapLoader().load("maps/mapaprototype.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tileSize = (int) tiledMap.getProperties().get("tilewidth");

        TiledMapTileLayer layer;

        MapBodyBuilder.buildShapes(tiledMap, tileSize, "Obstacles", world, CATEGORY_BIT_GROUND, CATEGORY_BIT_PLAYER);

        /*layer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
        createLayer(layer, CATEGORY_BIT_GROUND);*/

    }

    private void createLayer(TiledMapTileLayer layer, short bits) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();


        // all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                //get cell
                Cell cell = layer.getCell(col, row);

                //check if exists
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                //create box2d body and fixture
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * tileSize / pixelPerMeter, (row + 0.5f) * tileSize / pixelPerMeter);

                // PolygonShape chainShape = new PolygonShape();
                //chainShape.setAsBox(tileSize / 2 / pixelPerMeter, tileSize / 2 / pixelPerMeter);
                ChainShape chainShape = new ChainShape();

                Vector2[] vector2s = new Vector2[5];
                vector2s[0] = new Vector2(tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);
                vector2s[1] = new Vector2(tileSize / 2 / pixelPerMeter, tileSize / 2 / pixelPerMeter);
                vector2s[2] = new Vector2(-tileSize / 2 / pixelPerMeter, tileSize / 2 / pixelPerMeter);
                vector2s[3] = new Vector2(-tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);
                vector2s[4] = new Vector2(tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);


                chainShape.createChain(vector2s);





                fdef.friction = 1f;
                fdef.restitution = 0;
                fdef.shape = chainShape;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = CATEGORY_BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef).setUserData("ground");
            }
        }

    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void createCollectable() {
        collectable = new Array<>();

        MapLayer layer = tiledMap.getLayers().get("crystals");

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        for (MapObject mapObject : layer.getObjects()) {

            bodyDef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mapObject.getProperties().get("x") / pixelPerMeter;
            float y = (float) mapObject.getProperties().get("y") / pixelPerMeter;

            bodyDef.position.set(x, y);
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(8 / pixelPerMeter);

            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = CATEGORY_BIT_COLLECTABLE;
            fixtureDef.filter.maskBits = CATEGORY_BIT_PLAYER;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef).setUserData("crystal");

            Collectable c = new Collectable(body);
            collectable.add(c);

            body.setUserData(c);


        }


    }
}