package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.BoundedCamera;
import com.mygdx.game.handlers.ContentHandler;
import com.mygdx.game.handlers.InputInterpreter;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.ProducerScreen;


public class MyGdxGame extends Game {
    SpriteBatch batch;
    Texture img;

    public static final String TITLE = "Prototype";

    public static final int V_WIDTH = 426;

    public static final int V_HEIGHT = 240;

    public static final int SCALE = 3;

    public float STEP = 1 / 240f;
    private float accumulator;
    private boolean paused;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private BitmapFont font;

    public static ContentHandler resources;

    public static Texture backgroundTexture;

    public static Sprite backgroundSprite1;

    public static Sprite backgroundSprite2;

    public static PlayScreen currentScreen;

    @Override
    public void create() {

        //Gdx.graphics.setVSync(true);

        Gdx.input.setInputProcessor(new InputInterpreter());

        resources = new ContentHandler();
        resources.loadTexture("entities/michal-wisniewski.png", "michal");
        resources.loadTexture("entities/pies.png", "crystals");
        resources.loadTexture("entities/player1.png", "hud");
        resources.loadTexture("entities/background-layer1.png", "bg1");
        resources.loadTexture("entities/background-layer2.png", "bg2");
        resources.loadTexture("entities/background-layer3.png", "bg3");
        resources.loadTexture("entities/producer-screen.png", "bg4");
        resources.loadTexture("entities/menu-screen.png", "bg5");
        resources.loadTexture("entities/hedgehogsmal2l-Sheet.png", "hedgegogSheet");
        resources.loadTexture("entities/playerSpriteSheet.png", "playerSheet");
        resources.loadTexture("entities/slime-spritesheet5.png", "slimeSheet");
        resources.loadTexture("entities/kinghedgehog-sprite.png", "kingHedgegogSheet");
        resources.loadTexture("entities/sword.png", "sword");
        resources.loadTexture("entities/heart.png", "heart");
        resources.loadTexture("entities/goblin-spritesheet.png", "goblinSheet");
        resources.loadTexture("entities/rock.png", "rock");

        font = new BitmapFont();

        spriteBatch = new SpriteBatch();
        camera = new BoundedCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        //currentScreen = new PlayScreen(this);

        this.setScreen(new ProducerScreen(this, spriteBatch));
        backgroundSprite1 = new Sprite(resources.getTexture("bg1"));
        backgroundSprite2 = new Sprite(resources.getTexture("bg1"));
    }

    @Override
    public void render() {
        super.render();


/*		accumulator += Gdx.graphics.getDeltaTime();
		currentScreen.update(STEP);
		currentScreen.render(STEP);
		InputHandler.update();*/

/*		while(accumulator >= STEP) {
			accumulator -= STEP;
			currentScreen.update(accumulator);
			currentScreen.render(accumulator);
			InputHandler.update();
		}*/

/*		spriteBatch.setProjectionMatrix(hudCamera.combined);
		spriteBatch.begin();
		spriteBatch.draw(resources.getTexture("hedgehogsmall"), 0, 0);
		spriteBatch.end();*/

/*		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public BoundedCamera getCamera() {
        return (BoundedCamera) camera;
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public BitmapFont getFont() {
        return font;
    }
}
