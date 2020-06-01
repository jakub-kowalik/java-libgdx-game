package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public abstract class BaseScreen implements Screen {
    protected final SpriteBatch spriteBatch;
   // protected final OrthographicCamera guiCam;
    protected final MyGdxGame game;

    protected OrthographicCamera camera;
    protected OrthographicCamera hudCamera;


    protected BaseScreen(MyGdxGame game){
        this.game = game;
        spriteBatch = game.getSpriteBatch();
        camera = game.getCamera();
        hudCamera = game.getHudCamera();
     //   guiCam = new OrthographicCamera(800, 480);
    }

    public abstract void render(float dt);

    public abstract void update(float delta);
 //   public abstract void draw();



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}