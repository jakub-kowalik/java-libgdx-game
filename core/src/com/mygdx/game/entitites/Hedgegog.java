package com.mygdx.game.entitites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.AnimationHandlers.PlayerAnimationHandler;

public class Hedgegog extends Box2DSprite {
    private int numCrystals;
    private int totalCrystals;

    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;
    private static final int WALK_FRAME_COLS = 2, WALK_FRAME_ROWS = 1;
    private static final int IDLE_FRAME_COLS = 2, IDLE_FRAME_ROWS = 1;

    TextureRegion[] walkingAnimationRegion;
    TextureRegion[] idleAnimationRegion;

    public Hedgegog(Body body) {

        super(body);

        Texture hedgSheet = MyGdxGame.resources.getTexture("hedgegogSheet");

        TextureRegion[][] tmp = TextureRegion.split(hedgSheet,
                hedgSheet.getWidth() / FRAME_COLS,
                hedgSheet.getHeight() / FRAME_ROWS);

        walkingAnimationRegion = new TextureRegion[WALK_FRAME_COLS * WALK_FRAME_ROWS];
        idleAnimationRegion = new TextureRegion[IDLE_FRAME_COLS * IDLE_FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < WALK_FRAME_ROWS; i++) {
            for (int j = 0; j < WALK_FRAME_COLS; j++) {
                walkingAnimationRegion[index] = tmp[i+1][j]; // 1,0 start
                idleAnimationRegion[index] = tmp[i][j]; // 0,0 start
                index++;
            }
        }
    }

    @Override
    public void setAnimationHandler() {

    }

    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() {return numCrystals; }
    public void setTotalCrystals(int i) { totalCrystals = i;}
    public int getTotalCrystals() { return totalCrystals; }

}
