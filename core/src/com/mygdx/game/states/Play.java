package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entitites.Collectable;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.ContactHandler;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.InputHandler;

import static com.mygdx.game.handlers.Box2DVariables.*;

public class Play extends GameState {


    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private OrthographicCamera box2DCamera;


    private ContactHandler contactHandler;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Player player;
    private Array<Collectable> collectable;

    public Play(GameStateManager gameStateManager) {

        super(gameStateManager);

        // set up box2d
        world = new World(new Vector2(0, -9.81f), true);
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
        box2DCamera.setToOrtho(false, game.V_WIDTH / pixelPerMeter, game.V_HEIGHT / pixelPerMeter);


    }


    @Override
    public void handleInput() {

        if (InputHandler.isPressed(InputHandler.BUTTON1) && contactHandler.getIsGrounded()) {
            player.getBody().applyForceToCenter(0, 200, true);
        }

        if (InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4)) {
            player.getBody().applyForceToCenter(-10, 0, true);
        }

        if (InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isDown(InputHandler.BUTTON2)) {
            player.getBody().applyForceToCenter(10, 0, true);
        }

        if (InputHandler.isPressed(InputHandler.BUTTON5)) {

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
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();

        //draw player
        spriteBatch.setProjectionMatrix(camera.combined);
        player.render(spriteBatch);

        for(int i = 0; i < collectable.size; i++) {
            collectable.get(i).render(spriteBatch);
        }

        box2DDebugRenderer.render(world, box2DCamera.combined);
    }

    @Override
    public void dispose() {

    }

    private void createPlayer() {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        // create player
        bdef.position.set(160 / pixelPerMeter, 200 / pixelPerMeter);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);


        shape.setAsBox(5 / pixelPerMeter, 2 / pixelPerMeter);
        fdef.shape = shape;
        fdef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fdef.filter.maskBits = CATEGORY_BIT_GROUND | CATEGORY_BIT_COLLECTABLE;
        fdef.restitution = 0f;
        body.createFixture(fdef).setUserData("player");
        body.createFixture(fdef);

        //create foot sensor

        shape.setAsBox(5 / pixelPerMeter, 2 / pixelPerMeter, new Vector2(0, -2 / pixelPerMeter), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = CATEGORY_BIT_PLAYER;
        fdef.filter.maskBits = CATEGORY_BIT_GROUND;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

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

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
        createLayer(layer, CATEGORY_BIT_GROUND);

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
                ChainShape chainShape = new ChainShape();

                Vector2[] vector2s = new Vector2[5];
                vector2s[0] = new Vector2(tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);
                vector2s[1] = new Vector2(tileSize / 2 / pixelPerMeter, tileSize / 2 / pixelPerMeter);
                vector2s[2] = new Vector2(-tileSize / 2 / pixelPerMeter, tileSize / 2 / pixelPerMeter);
                vector2s[3] = new Vector2(-tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);
                vector2s[4] = new Vector2(tileSize / 2 / pixelPerMeter, -tileSize / 2 / pixelPerMeter);


                chainShape.createChain(vector2s);

                fdef.friction = 0.5f;
                fdef.shape = chainShape;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = CATEGORY_BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef).setUserData("ground");
            }
        }

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
