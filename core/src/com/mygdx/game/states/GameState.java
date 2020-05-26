package com.mygdx.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gameStateManager;
    protected MyGdxGame game;

    protected SpriteBatch spriteBatch;
    protected OrthographicCamera camera;
    protected OrthographicCamera hudCamera;

    protected GameState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        game = gameStateManager.getGame();
        spriteBatch = game.getSpriteBatch();
        camera = game.getCamera();
        hudCamera = game.getHudCamera();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
