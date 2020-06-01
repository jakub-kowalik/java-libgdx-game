package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.builders.CollectableBuilder;
import com.mygdx.game.builders.PlayerBuilder;
import com.mygdx.game.entitites.Collectable;
import com.mygdx.game.entitites.HUD;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.ContactHandler;
import com.mygdx.game.handlers.InputHandler;
import com.mygdx.game.handlers.MapBodyBuilder;
import com.mygdx.game.handlers.PlayerBodyBuilder;


import static com.mygdx.game.handlers.Box2DVariables.*;

public class PlayScreen extends BaseScreen {


    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private OrthographicCamera box2DCamera;


    private ContactHandler contactHandler;

    private TiledMap tiledMap;
    private float tileSize;
    private int mapWidth;
    private int mapHeight;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Player player;
    private Array<Collectable> collectable;
    private HUD hud;
    MapBodyBuilder mapBodyBuilder;
    private boolean debug = true;

    private float MAX_SPEED = 3f;

    public PlayScreen(MyGdxGame game) {

        super(game);

        // set up box2d
        world = new World(new Vector2(0, -9.81f), false);
        contactHandler = new ContactHandler();
        world.setContactListener(contactHandler);
        box2DDebugRenderer = new Box2DDebugRenderer();

        // set up player
        createPlayer();

        // set up tiledmap
        createTiledMap();

        //create collectables
        createCollectable();

        // set box2d cam
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, MyGdxGame.V_WIDTH / pixelPerMeter, MyGdxGame.V_HEIGHT / pixelPerMeter);

        //set up hud
        hud = new HUD(player);

        //set background

    }


 //   @Override
    public void handleInput() {
        player.isMoving = false;
        if (InputHandler.isPressed(InputHandler.BUTTON1) && contactHandler.getIsGrounded() && !contactHandler.getIsCeiled()) {
            player.getBody().setLinearDamping(0);
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(new Vector2(0, 10000 / pixelPerMeter), player.getBody().getWorldCenter(), true);
        }

        if (InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4)) { // && !contactHandler.getIsLeftContact()
            player.getBody().setLinearDamping(0);
            if (player.getBody().getLinearVelocity().x > -MAX_SPEED) {
                player.getBody().applyLinearImpulse(
                        -750 / pixelPerMeter,
                        0,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true
                );
            } else {
                player.getBody().setLinearVelocity(-MAX_SPEED, player.getBody().getLinearVelocity().y);
            }
            player.isMoving = true;
            player.lookingRight = false;
        }

        if (InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isDown(InputHandler.BUTTON2)) { // && !contactHandler.getIsRightContact()
            player.getBody().setLinearDamping(0);
            System.out.println("Speed: " + player.getBody().getLinearVelocity().x);
            if (player.getBody().getLinearVelocity().x < MAX_SPEED) {
                player.getBody().applyLinearImpulse(
                        750 / pixelPerMeter,
                        0 / pixelPerMeter,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true
                );
            } else {
                player.getBody().setLinearVelocity(MAX_SPEED, player.getBody().getLinearVelocity().y);
            }
            player.isMoving = true;
            player.lookingRight = true;
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

        if(contactHandler.getIsGrounded())
        if (Math.abs(player.getBody().getLinearVelocity().x) > 0 && !InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isPressed(InputHandler.BUTTON1)) {
            player.getBody().setLinearDamping(10000000);
            /*playerMotor.setMotorSpeed(0);
            playerMotor.enableMotor(true);*/

        } else
            player.getBody().setLinearDamping(0);
    }

    @Override
    public void render(float dt) {



        spriteBatch.begin();
        MyGdxGame.backgroundSprite.draw(spriteBatch);
        spriteBatch.end();

        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();

        camera.position.set(player.getPosition().x * pixelPerMeter, player.getPosition().y * pixelPerMeter, 0);
        boundCamera(camera);
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
        PlayerBuilder playerBuilder = new PlayerBuilder(world);

        player = (Player) playerBuilder.createEntity(new Vector2(160,330));
        player.getBody().setUserData(player);

    }


    private void createTiledMap() {

        // load map
        tiledMap = new TmxMapLoader().load("maps/mapprototype-testing.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tileSize = (int) tiledMap.getProperties().get("tilewidth");

        mapWidth = tiledMap.getProperties().get("width", Integer.class) * (int) tileSize;
        mapHeight = tiledMap.getProperties().get("height", Integer.class) * (int) tileSize;

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
        CollectableBuilder builder = new CollectableBuilder(world);
        collectable = new Array<>();

        MapLayer layer = tiledMap.getLayers().get("crystals");

        for (MapObject mapObject : layer.getObjects()) {

            float x = (float) mapObject.getProperties().get("x") / pixelPerMeter;
            float y = (float) mapObject.getProperties().get("y") / pixelPerMeter;

            Collectable c = (Collectable) builder.createEntity(new Vector2(x,y));
            collectable.add(c);

            c.getBody().setUserData(c);

        }
    }

    void boundCamera(Camera cam) {

        // These values likely need to be scaled according to your world coordinates.
// The left boundary of the map (x)
        int mapLeft = 0;
// The right boundary of the map (x + width)
        int mapRight = mapWidth;
// The bottom boundary of the map (y)
        int mapBottom = 0;
// The top boundary of the map (y + height)
        int mapTop = mapHeight;
// The camera dimensions, halved
        float cameraHalfWidth = cam.viewportWidth * .5f;
        float cameraHalfHeight = cam.viewportHeight * .5f;

// Move camera after player as normal

        float cameraLeft = cam.position.x - cameraHalfWidth;
        float cameraRight = cam.position.x + cameraHalfWidth;
        float cameraBottom = cam.position.y - cameraHalfHeight;
        float cameraTop = cam.position.y + cameraHalfHeight;

// Horizontal axis
        if(mapWidth < cam.viewportWidth)
        {
            cam.position.x = mapRight / 2f;
        }
        else if(cameraLeft <= mapLeft)
        {
            cam.position.x = mapLeft + cameraHalfWidth;
        }
        else if(cameraRight >= mapRight)
        {
            cam.position.x = mapRight - cameraHalfWidth;
        }

// Vertical axis
        if(mapHeight < cam.viewportHeight)
        {
            cam.position.y = mapTop / 2f;
        }
        else if(cameraBottom <= mapBottom)
        {
            cam.position.y = mapBottom + cameraHalfHeight;
        }
        else if(cameraTop >= mapTop)
        {
            cam.position.y = mapTop - cameraHalfHeight;
        }
    }
}
