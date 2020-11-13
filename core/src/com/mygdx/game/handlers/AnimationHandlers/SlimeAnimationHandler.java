package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Slime.Slime;

import java.util.Random;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class SlimeAnimationHandler extends AnimationHandler {
    private Slime slime;

    private static final int FRAME_COLS = 8, FRAME_ROWS = 3;
    private static final int FIRST_FRAME_COLS = 1, FIRST_FRAME_ROWS = 1;
    private static final int SECOND_FRAME_COLS = 8, SECOND_FRAME_ROWS = 1;
    private static final int THIRD_FRAME_COLS = 3, THIRD_FRAME_ROWS = 1;
    private final float[] randomColor = new float[3];

    private Animation<TextureRegion> firstAnimation;
    private Animation<TextureRegion> secondAnimation;
    private Animation<TextureRegion> deathAnimation;
    private TextureRegion currentFrame;

    private float frameWidth;
    private float frameHeight;

    public SlimeAnimationHandler(Slime slime) {
        super();
        this.slime = slime;
        Random generator = new Random();

        for (int i = 0; i < randomColor.length; i++) {
            randomColor[i] = generator.nextFloat();
        }
    }

    @Override
    protected void loadAnimations() {
        Texture slimeSheet = MyGdxGame.resources.getTexture("slimeSheet");

        TextureRegion[][] tmp = TextureRegion.split(slimeSheet,
                slimeSheet.getWidth() / FRAME_COLS,
                slimeSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] firstFrames = new TextureRegion[FIRST_FRAME_COLS * FIRST_FRAME_ROWS];
        TextureRegion[] secondFrames = new TextureRegion[SECOND_FRAME_COLS * SECOND_FRAME_ROWS];
        TextureRegion[] thirdFrames = new TextureRegion[THIRD_FRAME_COLS * THIRD_FRAME_ROWS];


        int index = 0;
        for (int i = 0; i < FIRST_FRAME_ROWS; i++) {
            for (int j = 0; j < FIRST_FRAME_COLS; j++) {
                firstFrames[index] = tmp[i][j]; // first row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < SECOND_FRAME_ROWS; i++) {
            for (int j = 0; j < SECOND_FRAME_COLS; j++) {
                secondFrames[index] = tmp[i + 1][j]; // second row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < THIRD_FRAME_ROWS; i++) {
            for (int j = 0; j < THIRD_FRAME_COLS; j++) {
                thirdFrames[index] = tmp[i + 2][j]; // second row
                index++;
            }
        }
        frameWidth = firstFrames[0].getRegionWidth();
        frameHeight = firstFrames[0].getRegionHeight();

        firstAnimation = new Animation<TextureRegion>(8f, firstFrames);
        secondAnimation = new Animation<TextureRegion>(1 / 5f, secondFrames);
        deathAnimation = new Animation<TextureRegion>(0.5f, thirdFrames);
    }

    @Override
    public void render(SpriteBatch batch) {
        switch (slime.slimeState) {
            case IDLE:
                currentFrame = secondAnimation.getKeyFrame(stateTime, true);
                break;
            case DEATH:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
                break;
            case JUMPING:
                currentFrame = firstAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (slime.lookingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (!slime.lookingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);

        batch.setColor(randomColor[0], randomColor[1], randomColor[2], 0.8f);
        //    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(currentFrame, slime.getPosition().x * pixelPerMeter - frameWidth / 2, slime.getPosition().y * pixelPerMeter - frameHeight / 2);
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
