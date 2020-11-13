package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.InputHandler;
import com.mygdx.game.handlers.ParallaxBackgroundHandler;

public class MainMenuScreen extends BaseScreen {

    OrthographicCamera camera;

    SpriteBatch spriteBatch;

    private ParallaxBackgroundHandler[] backgrounds;

    public MainMenuScreen(MyGdxGame game, SpriteBatch spriteBatch) {
        super(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 426, 240);
        this.spriteBatch = spriteBatch;

        Texture bgs = MyGdxGame.resources.getTexture("bg5");
        TextureRegion layer1 = new TextureRegion(bgs, 0, 0, 426, 240);

        backgrounds = new ParallaxBackgroundHandler[1];
        backgrounds[0] = new ParallaxBackgroundHandler(layer1, camera, 0f);
        backgrounds[0].setVector(0, 0);

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i].update(delta);
            backgrounds[i].render(spriteBatch);
        }

        game.getSpriteBatch().begin();
        game.getFont().draw(game.getSpriteBatch(), "[click mouse to start] [press esc to exit]", 30, 30);
        game.getSpriteBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new PlayScreen(game));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        if (InputHandler.isPressed(InputHandler.BUTTON8)) { // end the game
            Gdx.app.exit();
        }
    }
}
