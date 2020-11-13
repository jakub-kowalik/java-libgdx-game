package com.mygdx.game.entitites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MyGdxGame;

public class HUD {

    private Player player;

    private TextureRegion[] textureRegion;
    private TextureRegion[] heartFrames;

    private BitmapFont font;

    private int enemiesLeft;
    private int collectablesLeft;
    private boolean isGameOver;

    private TextButton button;

    public HUD(Player player, int enemiesLeft, int collectablesLeft) {
        this.enemiesLeft = enemiesLeft;
        this.collectablesLeft = collectablesLeft;

        this.player = player;

        Texture texture = MyGdxGame.resources.getTexture("hud");

        textureRegion = new TextureRegion[1];

        for (int i = 0; i < textureRegion.length; i++) {
            textureRegion[i] = new TextureRegion(texture, i * 16, 0, 32, 32);
        }

        Texture heart = MyGdxGame.resources.getTexture("heart");

        heartFrames = new TextureRegion[1];

        for (int i = 0; i < heartFrames.length; i++) {
            heartFrames[i] = new TextureRegion(heart, i * 16, 0, 16, 16);
        }

        font = new BitmapFont();
        font.getData().setScale(0.5f);
        font.setColor(Color.BLACK);
    }

    public void render(SpriteBatch spriteBatch) {

        spriteBatch.begin();
        spriteBatch.draw(textureRegion[0], 5, 195);
        font.draw(spriteBatch, "FPS: ", 40, 220);
        font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 60, 220);
        for (int i = 0; i < player.hitpoints / 10; i++) {
            spriteBatch.draw(heartFrames[0], 40 + i * 8, 225, 8, 8);
        }
        font.draw(spriteBatch, "Enemies left: " + enemiesLeft, 40, 210);
        font.draw(spriteBatch, "Coins left: " + collectablesLeft, 40, 200);
        if (isGameOver) {
            font.getData().setScale(1f);
            font.draw(spriteBatch, "Game over, press SPACE to end", 120, 220);

            font.getData().setScale(0.5f);
        }

        spriteBatch.end();
    }

    public void updateHudInfo(int enemiesLeft, int collectablesLeft, boolean isGameOver) {
        this.enemiesLeft = enemiesLeft;
        this.collectablesLeft = collectablesLeft;
        this.isGameOver = isGameOver;
    }
}
