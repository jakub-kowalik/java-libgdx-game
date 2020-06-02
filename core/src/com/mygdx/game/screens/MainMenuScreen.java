package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.MyGdxGame;

public class MainMenuScreen extends BaseScreen {


    OrthographicCamera camera;

    public MainMenuScreen(MyGdxGame game) {
        super(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin();
        game.getFont().draw(game.getSpriteBatch(), "Bro...", 100, 150);
        game.getFont().draw(game.getSpriteBatch(), "that pretty cringe bro ngl.", 100, 130);
        game.getFont().draw(game.getSpriteBatch(), "[click to dissapoint your family]", 100, 110);
        game.getSpriteBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new PlayScreen(game));
            dispose();
        }
    }

    @Override
    public void update(float dt) {

    }
}
