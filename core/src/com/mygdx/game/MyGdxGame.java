package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.*;
import com.mygdx.game.screens.PlayScreen;


public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;

	public static final String TITLE = "Prototype";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 3;

	public static final float STEP = 1/60f;
	private float accumulator;
	private boolean paused;

	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;

	private GameStateManager gameStateManager;

	public static ContentHandler resources;

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	public static PlayScreen currentScreen;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(new InputProcessor());

		resources = new ContentHandler();
		resources.loadTexture("entities/michal-wisniewski.png", "michal");
		resources.loadTexture("entities/pies.png", "crystals");
		resources.loadTexture("entities/player1.png", "hud");
		resources.loadTexture("entities/sky.png", "sky");


		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		currentScreen = new PlayScreen(this);

		this.setScreen(currentScreen);
		backgroundSprite =new Sprite(resources.getTexture("sky"));
	}


	@Override
	public void render () {
		super.render();

		accumulator += Gdx.graphics.getDeltaTime();
		currentScreen.update(STEP);
		currentScreen.render(STEP);
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
	public void dispose () {
		batch.dispose();

	}

	public SpriteBatch getSpriteBatch() { return spriteBatch; }
	public OrthographicCamera getCamera() { return camera; }
	public OrthographicCamera getHudCamera() { return hudCamera; }
	public boolean getPaused() { return paused; }
	public void setPaused(boolean paused) { this.paused = paused; }
}
