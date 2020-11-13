package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.builders.*;
import com.mygdx.game.entitites.*;
import com.mygdx.game.entitites.Bullet.Bullet;
import com.mygdx.game.entitites.Goblin.Goblin;
import com.mygdx.game.entitites.KingHedgegog.KingHedgegog;
import com.mygdx.game.entitites.Slime.Slime;
import com.mygdx.game.entitites.Slime.SlimeState;
import com.mygdx.game.handlers.*;


import static com.mygdx.game.MyGdxGame.V_HEIGHT;
import static com.mygdx.game.MyGdxGame.V_WIDTH;
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
    private TiledMapTileLayer floorLayer;
    private TiledMapTileLayer foliageLayer;
    private TiledMapTileLayer backgroundLayer;

    // entities
    private Player player;
    private Player player2;
    private Array<Entity> allEntities;
    private Pool<Bullet> bulletPool;

    private int enemiesLeft = 0;
    private int collectablesLeft = 0;

    private HUD hud;
    private boolean debug = false;
    private boolean isGameOver = false;

    private float MAX_SPEED = 3f;

    private Music levelMusic;
    private Music collectableMusic;

    private ParallaxBackgroundHandler[] backgrounds;

    private Filter filter;

    public PlayScreen(MyGdxGame game) {

        super(game);

        this.spriteBatch = game.getSpriteBatch();
        // set up box2d
        world = new World(new Vector2(0, -9.81f), false);
        contactHandler = new ContactHandler();
        world.setContactListener(contactHandler);
        box2DDebugRenderer = new Box2DDebugRenderer();

        allEntities = new Array<>();

        // set up player
        createPlayer(new Vector2(32, 80));

        // set up tiledmap
        createTiledMap();

        //bullets
        createBulletPool();

        //create collectables
        createCollectable();

        // create slimes
        createSlimes();

        //create kingHedgegogs
        createKingHedgegogs();

        //create goblins
        createGoblins();

        // set box2d cam
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, V_WIDTH / pixelPerMeter, V_HEIGHT / pixelPerMeter);
        camera = new BoundedCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

        //set up hud
        hud = new HUD(player, enemiesLeft, collectablesLeft);

        levelMusic = Gdx.audio.newMusic(Gdx.files.internal("music/forest3.wav"));
        levelMusic.setLooping(true);
        collectableMusic = Gdx.audio.newMusic(Gdx.files.internal("music/collectable.wav"));
        collectableMusic.setLooping(false);

        Texture bgs = MyGdxGame.resources.getTexture("bg1");
        TextureRegion layer1 = new TextureRegion(bgs, 0, 0, 426, 240);
        Texture bgs1 = MyGdxGame.resources.getTexture("bg2");
        TextureRegion clouds = new TextureRegion(bgs1, 0, 0, 426, 240);
        Texture bgs2 = MyGdxGame.resources.getTexture("bg3");
        TextureRegion mountains = new TextureRegion(bgs2, 0, 0, 426, 240);

        backgrounds = new ParallaxBackgroundHandler[3];
        backgrounds[0] = new ParallaxBackgroundHandler(layer1, camera, 0f);
        backgrounds[1] = new ParallaxBackgroundHandler(clouds, camera, 0.5f);
        backgrounds[2] = new ParallaxBackgroundHandler(mountains, camera, 1f);
    }

    public void handleInput() {
        if (isGameOver && InputHandler.isPressed(InputHandler.BUTTON7)) { // end the game
            Gdx.app.exit();
        }

        if (InputHandler.isPressed(InputHandler.BUTTON8)) { // end the game
            Gdx.app.exit();
        }

        if (!player.isAlive) return;

        player.isMoving = false;
        if (InputHandler.isPressed(InputHandler.BUTTON1) && contactHandler.getIsGrounded() && !contactHandler.getIsCeiled()) {
            player.getBody().setLinearDamping(0);
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyLinearImpulse(new Vector2(0, 15000 / pixelPerMeter), player.getBody().getWorldCenter(), true);
        }

        if (InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4) && !player.isAttacking) { // && !contactHandler.getIsLeftContact()
            player.getBody().setLinearDamping(0);
            if (player.getBody().getLinearVelocity().x > -MAX_SPEED) {
                player.getBody().applyLinearImpulse(
                        -750 / pixelPerMeter,
                        0,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true);
            } else {
                player.getBody().setLinearVelocity(-MAX_SPEED, player.getBody().getLinearVelocity().y);
            }
            if (player.lookingRight) {
                player.getAttackArm().setLimits((float) Math.toRadians(-45), (float) Math.toRadians(135));
                player.getAttackArm().setMotorSpeed(-1000);
            }
            player.isMoving = true;
            player.lookingRight = false;

        }

        if (InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isDown(InputHandler.BUTTON2) && !player.isAttacking) { // && !contactHandler.getIsRightContact()
            player.getBody().setLinearDamping(0);
            if (player.getBody().getLinearVelocity().x < MAX_SPEED) {
                player.getBody().applyLinearImpulse(
                        750 / pixelPerMeter,
                        0 / pixelPerMeter,
                        player.getBody().getWorldCenter().x,
                        player.getBody().getWorldCenter().y,
                        true);
            } else {
                player.getBody().setLinearVelocity(MAX_SPEED, player.getBody().getLinearVelocity().y);
            }
            if (!player.lookingRight) {
                player.getAttackArm().setLimits((float) Math.toRadians(-135), (float) Math.toRadians(45));
                player.getAttackArm().setMotorSpeed(1000);
            }
            player.isMoving = true;
            player.lookingRight = true;

        }
        if (InputHandler.isDown(InputHandler.BUTTON4) && InputHandler.isDown(InputHandler.BUTTON2)) {
            player.getBody().setLinearVelocity(new Vector2(0, player.getBody().getLinearVelocity().y));
        }

        if (InputHandler.isPressed(InputHandler.BUTTON5)) {
            debug = true;
        }
        if (InputHandler.isPressed(InputHandler.BUTTON6)) {
            debug = false;
        }

        if (InputHandler.isPressed(InputHandler.MOUSE1)) {
            if (!player.lookingRight) {
                if (player.getAttackArm().getJointAngle() < (float) Math.toRadians(-44)) {
                    player.isAttacking = true;
                    if (contactHandler.getIsGrounded())
                        player.getBody().setLinearDamping(10000000);
                    filter = player.getAttackArm().getBodyB().getFixtureList().get(0).getFilterData();
                    filter.maskBits = CATEGORY_BIT_COLLECTABLE | CATEGORY_BIT_ENEMY;
                    player.getAttackArm().getBodyB().getFixtureList().get(0).setFilterData(filter);
                    player.getAttackArm().setMotorSpeed(25);
                }
            } else {
                if (player.getAttackArm().getJointAngle() > (float) Math.toRadians(44)) {
                    player.isAttacking = true;
                    if (contactHandler.getIsGrounded())
                        player.getBody().setLinearDamping(10000000);
                    filter = player.getAttackArm().getBodyB().getFixtureList().get(0).getFilterData();
                    filter.maskBits = CATEGORY_BIT_COLLECTABLE | CATEGORY_BIT_ENEMY;
                    player.getAttackArm().getBodyB().getFixtureList().get(0).setFilterData(filter);
                    player.getAttackArm().setMotorSpeed(-25);
                }
            }
        }

        if (InputHandler.isPressed(InputHandler.MOUSE2)) {

        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        world.step(dt, 1, 1);

        for (int i = 0; i < allEntities.size; i++) {
            if (!allEntities.get(i).isAlive) {
                if (allEntities.get(i) instanceof Collectable) {
                    player.collectCrystal();
                    collectableMusic.play();
                    world.destroyBody(allEntities.get(i).getBody());
                    allEntities.removeIndex(i);
                    collectablesLeft--;
                } else if (allEntities.get(i) instanceof Bullet) {
                    world.destroyBody(allEntities.get(i).getBody());
                    allEntities.removeIndex(i);
                } else { // enemy or player
                    world.destroyBody(allEntities.get(i).getBody());
                    allEntities.removeIndex(i);
                    enemiesLeft--;
                }
            }
        }

        Array<Body> wakedUpSlimes = contactHandler.getWakedUp();
        for (int i = 0; i < wakedUpSlimes.size; i++) {
            Object bodyData = wakedUpSlimes.get(i).getUserData();

            for (int j = 0; j < allEntities.size; j++) { // znajdz tego zbudzonego slime'a w oryginalnej tablicy
                if (allEntities.get(j).getBody().getUserData().equals(bodyData)) {

                    if (allEntities.get(j) instanceof Slime) {
                        Slime slime = (Slime) allEntities.get(j);
                        slime.changeSlimeState(SlimeState.JUMPING);
                    }
                    break;
                }
            }
        }

        Array<Body> enemiesThatSawPlayer = contactHandler.getSawPlayer();
        for (int i = 0; i < enemiesThatSawPlayer.size; i++) {
            Object bodyData = enemiesThatSawPlayer.get(i).getUserData();

            for (int j = 0; j < allEntities.size; j++) { // znajdz tego wkurzonego kinga w oryginalnej tablicy
                if (allEntities.get(j).getBody().getUserData().equals(bodyData)) {
                    Entity entity = allEntities.get(j);

                    if (entity instanceof KingHedgegog) {
                        KingHedgegog kingHedgegog = (KingHedgegog) entity;
                        kingHedgegog.setPlayerIsClose(true);
                    } else if (entity instanceof Goblin) {
                        Goblin goblin = (Goblin) entity;
                        goblin.setPlayerIsClose(true);
                    }
                    break;
                }
            }
        }

        Array<Body> enemiesThatLostPlayer = contactHandler.getLostContactOfPlayer();
        for (int i = 0; i < enemiesThatLostPlayer.size; i++) {
            Object bodyData = enemiesThatLostPlayer.get(i).getUserData();

            for (int j = 0; j < allEntities.size; j++) { // znajdz tego smutnego kinga w oryginalnej tablicy
                if (allEntities.get(j).getBody().getUserData().equals(bodyData)) {
                    Entity entity = allEntities.get(j);
                    if (entity instanceof KingHedgegog) {
                        KingHedgegog kingHedgegog = (KingHedgegog) entity;
                        kingHedgegog.setPlayerIsClose(false);
                    } else if (entity instanceof Goblin) {
                        Goblin goblin = (Goblin) entity;
                        goblin.setPlayerIsClose(false);
                    }
                    break;
                }
            }
        }

        Array<Hit> hitsMade = contactHandler.getHitsMade(); // wszystkie hity w tej turze
        for (int i = 0; i < hitsMade.size; i++) {
            Object hitBodyData = hitsMade.get(i).hitBody.getUserData();
            Object attackerBodyData = hitsMade.get(i).attackerBody.getUserData();
            for (int j = 0; j < allEntities.size; j++) { // znajdz kto dostal hita
                if (allEntities.get(j).getBody().getUserData().equals(hitBodyData)) {
                    for (int k = 0; k < allEntities.size; k++) { // znajdz attackera
                        if (allEntities.get(k).getBody().getUserData().equals(attackerBodyData)) {
                            Entity entityHit = allEntities.get(j);
                            Entity entityAttacker = allEntities.get(k);

                            if (!entityAttacker.isDying && !entityHit.isDying) entityHit.getHit(hitsMade.get(i));
                        }
                    }
                }
            }
        }

        Array<Body> bulletsConnected = contactHandler.getBulletsConnected();
        for (int i = 0; i < bulletsConnected.size; i++) {
            Object bodyData = enemiesThatLostPlayer.get(i).getUserData();

            for (int j = allEntities.size - 1; j >= 0; j--) { // znajdz tego bulleta
                if (allEntities.get(j).getBody().getUserData().equals(bodyData)) {
                    Bullet bullet = (Bullet) allEntities.get(j);
                    bullet.reset();
                }
            }
        }

        for (int i = 0; i < allEntities.size; i++)
            allEntities.get(i).update(dt);

        hud.updateHudInfo(enemiesLeft, collectablesLeft, isGameOver);

        if (contactHandler.getIsGrounded())
            if (Math.abs(player.getBody().getLinearVelocity().x) > 0 && !InputHandler.isDown(InputHandler.BUTTON2) && !InputHandler.isDown(InputHandler.BUTTON4) && !InputHandler.isPressed(InputHandler.BUTTON1)) {
                player.getBody().setLinearDamping(10000000);

            /*playerMotor.setMotorSpeed(0);
            playerMotor.enableMotor(true);*/

            } else {
                player.getBody().setLinearDamping(0);
            }

        if ((player.getAttackArm().getJointAngle() > (float) Math.toRadians(135))) {
            player.isAttacking = false;
            player.getBody().setLinearDamping(0);
            filter = player.getAttackArm().getBodyB().getFixtureList().get(0).getFilterData();
            filter.maskBits = 0;
            player.getAttackArm().getBodyB().getFixtureList().get(0).setFilterData(filter);
            player.getAttackArm().setMotorSpeed(-100);
        }

        if ((player.getAttackArm().getJointAngle() < (float) Math.toRadians(-135))) {
            player.isAttacking = false;
            player.getBody().setLinearDamping(0);
            filter = player.getAttackArm().getBodyB().getFixtureList().get(0).getFilterData();
            filter.maskBits = 0;
            player.getAttackArm().getBodyB().getFixtureList().get(0).setFilterData(filter);
            player.getAttackArm().setMotorSpeed(100);
        }

        //check if end of the game
        if ((enemiesLeft == 0 && collectablesLeft == 0) || !player.isAlive) {
            isGameOver = true;
        }
    }

    @Override
    public void render(float dt) {
        update(dt);

        InputHandler.update();

        // draw bgs
        for (ParallaxBackgroundHandler background : backgrounds) {
            background.render(spriteBatch);
        }

   /*     MyGdxGame.backgroundSprite1.setPosition(player.getPosition().x / 2, 0);
        MyGdxGame.backgroundSprite2.setPosition(player.getPosition().x / 3, 0);

        spriteBatch.begin();
        MyGdxGame.backgroundSprite1.draw(spriteBatch);
        MyGdxGame.backgroundSprite2.draw(spriteBatch);
        spriteBatch.end();*/

        camera.setBoundedPosition(player.getPosition().x, player.getPosition().y, 0, mapWidth, mapHeight);
        camera.update();

        orthogonalTiledMapRenderer.setView(camera);

        orthogonalTiledMapRenderer.getBatch().begin();
        orthogonalTiledMapRenderer.renderTileLayer(backgroundLayer);
        orthogonalTiledMapRenderer.getBatch().end();

        orthogonalTiledMapRenderer.getBatch().begin();
        orthogonalTiledMapRenderer.renderTileLayer(foliageLayer);
        orthogonalTiledMapRenderer.getBatch().end();

        //draw player
        spriteBatch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < allEntities.size; i++) {
            allEntities.get(i).render(spriteBatch);
        }

        orthogonalTiledMapRenderer.getBatch().begin();
        orthogonalTiledMapRenderer.renderTileLayer(floorLayer);
        orthogonalTiledMapRenderer.getBatch().end();

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

    private void createPlayer(Vector2 vector2) {
        PlayerBuilder playerBuilder = new PlayerBuilder(world);

        player = (Player) playerBuilder.createEntity(vector2);
        player.getBody().setUserData(player);
        player.getArmBody().setUserData(player);
        allEntities.add(player);
    }

    private void createBulletPool() {
        final BulletBuilder bulletBuilder = new BulletBuilder(world);
        bulletPool = new Pool<Bullet>() {        // bullet pool.
            @Override
            protected Bullet newObject() {
                return new Bullet(bulletBuilder.createBulletBody(new Vector2(0, 0)), 1);
            }
        };
    }

    private void createSlimes() {
        SlimeBuilder slimeBuilder = new SlimeBuilder(world, player);

        MapLayer layer = tiledMap.getLayers().get("slimes");
        for (MapObject mapObject : layer.getObjects()) {

            float x = (float) mapObject.getProperties().get("x");
            float y = (float) mapObject.getProperties().get("y");

            Slime newSlime = (Slime) slimeBuilder.createEntity(new Vector2(x, y));
            newSlime.getBody().setUserData(newSlime);
            allEntities.add(newSlime);
            enemiesLeft++;
        }
    }

    private void createGoblins() {
        GoblinBuilder goblinBuilder = new GoblinBuilder(world, player, bulletPool, allEntities);

        MapLayer layer = tiledMap.getLayers().get("goblins");
        for (MapObject mapObject : layer.getObjects()) {

            float x = (float) mapObject.getProperties().get("x");
            float y = (float) mapObject.getProperties().get("y");
            float leftXBound = (float) mapObject.getProperties().get("leftXBound");
            float rightXBound = (float) mapObject.getProperties().get("rightXBound");

            Goblin newGoblin = (Goblin) goblinBuilder.createEntity(new Vector2(x, y));
            newGoblin.setMovementBounds(leftXBound, rightXBound);
            newGoblin.getBody().setUserData(newGoblin);
            allEntities.add(newGoblin);
            enemiesLeft++;
        }
    }

    private void createKingHedgegogs() {
        KingHedgegogBuilder kingHedgegogBuilder = new KingHedgegogBuilder(world, player);

        MapLayer layer = tiledMap.getLayers().get("kingHedgehogs");
        for (MapObject mapObject : layer.getObjects()) {

            float x = (float) mapObject.getProperties().get("x");
            float y = (float) mapObject.getProperties().get("y");
            float leftXBound = (float) mapObject.getProperties().get("leftXBound");
            float rightXBound = (float) mapObject.getProperties().get("rightXBound");

            KingHedgegog newKing = (KingHedgegog) kingHedgegogBuilder.createEntity(new Vector2(x, y));
            newKing.setMovementBounds(leftXBound, rightXBound);
            newKing.getBody().setUserData(newKing);
            allEntities.add(newKing);
            enemiesLeft++;
        }
    }


    private void createTiledMap() {
        // load map
        tiledMap = new TmxMapLoader().load("maps/new-map.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tileSize = (int) tiledMap.getProperties().get("tilewidth");

        mapWidth = tiledMap.getProperties().get("width", Integer.class) * (int) tileSize;
        mapHeight = tiledMap.getProperties().get("height", Integer.class) * (int) tileSize;

        floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");

        foliageLayer = (TiledMapTileLayer) tiledMap.getLayers().get("foliage");

        backgroundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("background");

        short maskBits = CATEGORY_BIT_PLAYER | CATEGORY_BIT_ENEMY;
        MapBodyBuilder.buildShapes(tiledMap, tileSize, "Obstacles", world, CATEGORY_BIT_GROUND, maskBits);

        /*layer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
        createLayer(layer, CATEGORY_BIT_GROUND);*/
    }

    private void createCollectable() {
        CollectableBuilder builder = new CollectableBuilder(world);

        MapLayer layer = tiledMap.getLayers().get("crystals");

        for (MapObject mapObject : layer.getObjects()) {

            float x = (float) mapObject.getProperties().get("x") / pixelPerMeter;
            float y = (float) mapObject.getProperties().get("y") / pixelPerMeter;

            Collectable c = (Collectable) builder.createEntity(new Vector2(x, y));
            allEntities.add(c);

            c.getBody().setUserData(c);
            collectablesLeft++;
        }
    }

    @Override
    public void show() {
        levelMusic.setVolume(0.2f);
        levelMusic.play();
    }

    @Override
    public void hide() {
        levelMusic.stop();
    }
}
