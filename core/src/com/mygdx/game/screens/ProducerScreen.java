package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.ContentHandler;
import com.mygdx.game.handlers.ParallaxBackgroundHandler;

public class ProducerScreen extends BaseScreen {

    OrthographicCamera camera;

    SpriteBatch spriteBatch;

    private ParallaxBackgroundHandler[] backgrounds;
    private float timeToChange;
    private Music levelMusic;

    public ProducerScreen(MyGdxGame game, SpriteBatch spriteBatch) {
        super(game);

        this.spriteBatch = spriteBatch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 426, 240);

        Texture bgs2 = MyGdxGame.resources.getTexture("bg4");
        TextureRegion layer11 = new TextureRegion(bgs2, 0, 0, 426, 240);

        backgrounds = new ParallaxBackgroundHandler[1];
        backgrounds[0] = new ParallaxBackgroundHandler(layer11, camera, 0f);
        backgrounds[0].setVector(0, 0);

        levelMusic = Gdx.audio.newMusic(Gdx.files.internal("music/producer-sound.mp3"));
        levelMusic.setVolume(0.05f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        backgrounds[0].update(delta);
        backgrounds[0].render(spriteBatch);

        timeToChange += delta;
        if (timeToChange > 5.0f) {
            game.setScreen(new MainMenuScreen(game, spriteBatch));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void show() {
        levelMusic.play();
    }
}
