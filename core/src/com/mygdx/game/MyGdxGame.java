package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.*;
import com.mygdx.game.screens.PlayScreen;


public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;

	public static final String TITLE = "Prototype";
	public static final int V_WIDTH = 426;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 3;

	public static final float STEP = 1/60f;
	private float accumulator;
	private boolean paused;

	private SpriteBatch spriteBatch;
	private BoundedCamera camera;
	private OrthographicCamera hudCamera;

	private BitmapFont font;


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
		resources.loadTexture("entities/background-2.png", "sky");
		resources.loadTexture("entities/hedgehogsmal2l-Sheet.png", "hedgegogSheet");
		resources.loadTexture("entities/playerSpriteSheet.png", "playerSheet");

		font = new BitmapFont();


		spriteBatch = new SpriteBatch();
		camera = new BoundedCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		//currentScreen = new PlayScreen(this);

		this.setScreen(new PlayScreen(this));
		backgroundSprite =new Sprite(resources.getTexture("sky"));
	}


	@Override
	public void render () {

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
	public void dispose () {
		spriteBatch.dispose();

	}


	public SpriteBatch getSpriteBatch() { return spriteBatch; }
	public BoundedCamera getCamera() { return camera; }
	public OrthographicCamera getHudCamera() { return hudCamera; }
	public boolean getPaused() { return paused; }
	public void setPaused(boolean paused) { this.paused = paused; }
	public BitmapFont getFont() { return font; }


}
