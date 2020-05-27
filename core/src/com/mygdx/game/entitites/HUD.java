package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

public class HUD {

    private Player player;

    private TextureRegion[] textureRegion;

    private BitmapFont font;



    public HUD(Player player) {

        this.player = player;

        Texture texture = MyGdxGame.resources.getTexture("hud");

        textureRegion = new TextureRegion[1];

        for (int i = 0; i < textureRegion.length; i++) {
            textureRegion[i] = new TextureRegion(texture, 0 + i * 16, 0, 32,32);
        }
        font = new BitmapFont();

    }

    public void render(SpriteBatch spriteBatch) {

        spriteBatch.begin();
        spriteBatch.draw(textureRegion[0], 5, 195);
        font.draw(spriteBatch, "Player: Michal Wisniewski", 40, 220);
        font.draw(spriteBatch, "Level: NIGHTMARE", 40, 200);
        font.draw(spriteBatch, "Times made others cringe: ", 40, 180);
        font.draw(spriteBatch, String.valueOf(player.getNumCrystals()), 220, 180);
        spriteBatch.end();

    }
}
