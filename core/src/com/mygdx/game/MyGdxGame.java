package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.*;


public class MyGdxGame extends ApplicationAdapter {
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


	@Override
	public void create () {

		Gdx.input.setInputProcessor(new InputProcessor());

		resources = new ContentHandler();
		resources.loadTexture("entities/hedgehogsmall-Sheet.png", "hedgehogsmall");
		resources.loadTexture("entities/obrazek11.png", "crystals");

		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		gameStateManager = new GameStateManager(this);
		img = new Texture("badlogic.jpg");
	}


	@Override
	public void render () {

		accumulator += Gdx.graphics.getDeltaTime();
		while(accumulator >= STEP) {
			accumulator -= STEP;
			gameStateManager.update(STEP);
			gameStateManager.render();
			InputHandler.update();
		}

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
