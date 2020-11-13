package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

public class ParallaxBackgroundHandler {
    private TextureRegion image;
    private OrthographicCamera gameCam;
    private float scale;

    private float x;
    private float y;
    private int numDrawX;
    private int numDrawY;

    private float dx;
    private float dy;

    public ParallaxBackgroundHandler(TextureRegion image, BoundedCamera gameCam, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        numDrawX = MyGdxGame.V_WIDTH / image.getRegionWidth() + 1;
        numDrawY = MyGdxGame.V_HEIGHT / image.getRegionHeight() + 1;
    }

    public ParallaxBackgroundHandler(TextureRegion image, OrthographicCamera gameCam, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        numDrawX = MyGdxGame.V_WIDTH / image.getRegionWidth() + 1;
        numDrawY = MyGdxGame.V_HEIGHT / image.getRegionHeight() + 1;
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        x += (dx * scale) * dt;
        y += (dy * scale) * dt;
    }

    public void render(SpriteBatch sb) {

        float x = ((this.x + gameCam.viewportWidth / 2 - gameCam.position.x) * scale) % image.getRegionWidth();
        float y = 0;

        sb.begin();

        int colOffset = x > 0 ? -1 : 0;
        int rowOffset = y > 0 ? -1 : 0;
        for (int row = 0; row < numDrawY; row++) {
            for (int col = 0; col < numDrawX; col++) {
                sb.draw(image, x + (col + colOffset) * image.getRegionWidth(), y);
            }
        }

        sb.end();
    }
}