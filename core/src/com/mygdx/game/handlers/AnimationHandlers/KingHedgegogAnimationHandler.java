package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.KingHedgegog.KingHedgegog;

import java.util.Random;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class KingHedgegogAnimationHandler extends AnimationHandler {
    private KingHedgegog kingHedgegog;

    private static final int FRAME_COLS = 7, FRAME_ROWS = 4;
    private static final int IDLE_FRAME_COLS = 7, IDLE_FRAME_ROWS = 1;
    private static final int RUSH_FRAME_COLS = 2, RUSH_FRAME_ROWS = 1;
    private static final int SLEEP_FRAME_COLS = 5, SLEEP_FRAME_ROWS = 1;
    private static final int DEATH_FRAME_COLS = 3, DEATH_FRAME_ROWS = 1;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> rushAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> sleepAnimation;
    private Animation<TextureRegion> deathAnimation;
    private TextureRegion currentFrame;

    private float frameWidth;
    private float frameHeight;

    private float colorTint;

    public KingHedgegogAnimationHandler(KingHedgegog kingHedgegog) {
        this.kingHedgegog = kingHedgegog;
        colorTint = (new Random().nextFloat()) * (1f - 0.3f) + 0.3f;
    }

    @Override
    protected void loadAnimations() {
        Texture kingSheet = MyGdxGame.resources.getTexture("kingHedgegogSheet");
        TextureRegion[][] tmp = TextureRegion.split(kingSheet,
                kingSheet.getWidth() / FRAME_COLS,
                kingSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] idleFrames = new TextureRegion[IDLE_FRAME_COLS * IDLE_FRAME_ROWS];
        TextureRegion[] rushFrames = new TextureRegion[RUSH_FRAME_COLS * RUSH_FRAME_ROWS];
        TextureRegion[] sleepFrames = new TextureRegion[SLEEP_FRAME_COLS * SLEEP_FRAME_ROWS];
        TextureRegion[] deathFrames = new TextureRegion[DEATH_FRAME_COLS * DEATH_FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < IDLE_FRAME_ROWS; i++) {
            for (int j = 0; j < IDLE_FRAME_COLS; j++) {
                idleFrames[index] = tmp[i][j]; // first row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < RUSH_FRAME_ROWS; i++) {
            for (int j = 0; j < RUSH_FRAME_COLS; j++) {
                rushFrames[index] = tmp[i + 1][j]; // second row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < SLEEP_FRAME_ROWS; i++) {
            for (int j = 0; j < SLEEP_FRAME_COLS; j++) {
                sleepFrames[index] = tmp[i + 2][j]; // third row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < DEATH_FRAME_ROWS; i++) {
            for (int j = 0; j < DEATH_FRAME_COLS; j++) {
                deathFrames[index] = tmp[i + 3][j]; // fourth row
                index++;
            }
        }
        frameWidth = idleFrames[0].getRegionWidth();
        frameHeight = idleFrames[0].getRegionHeight();

        idleAnimation = new Animation<TextureRegion>(1f, idleFrames);
        rushAnimation = new Animation<TextureRegion>(1 / 10f, rushFrames);
        walkAnimation = new Animation<TextureRegion>(1 / 5f, rushFrames); // slower rush
        sleepAnimation = new Animation<TextureRegion>(1f, sleepFrames);
        deathAnimation = new Animation<TextureRegion>(0.6f, deathFrames);

    }

    @Override
    public void render(SpriteBatch batch) {
        switch (kingHedgegog.kingState) {
            case PREPARE_TO_ATTACK:
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;
            case WALKING:
            case IGNORE_PLAYER:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case RUSHING:
                currentFrame = rushAnimation.getKeyFrame(stateTime, true);
                break;
            case DEATH:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
        }

        if (kingHedgegog.lookingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (!kingHedgegog.lookingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);

        batch.setColor(colorTint, colorTint, colorTint, 1f);
        batch.draw(currentFrame, kingHedgegog.getPosition().x * pixelPerMeter - frameWidth / 2, kingHedgegog.getPosition().y * pixelPerMeter - frameHeight / 2);
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public float getFrameWidth() {
        return frameWidth;
    }

    @Override
    public float getFrameHeight() {
        return frameHeight;
    }
}
