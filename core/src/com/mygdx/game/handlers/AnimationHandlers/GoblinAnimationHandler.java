package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Goblin.Goblin;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class GoblinAnimationHandler extends AnimationHandler {
    private Goblin goblin;

    private static final int FRAME_COLS = 5, FRAME_ROWS = 4;
    private static final int FIRST_FRAME_COLS = 1, FIRST_FRAME_ROWS = 1;
    private static final int SECOND_FRAME_COLS = 5, SECOND_FRAME_ROWS = 1;
    private static final int THIRD_FRAME_COLS = 2, THIRD_FRAME_ROWS = 1;
    private static final int FOURTH_FRAME_COLS = 4, FOURTH_FRAME_ROWS = 1;

    private Animation<TextureRegion> firstAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> thirdAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private TextureRegion currentFrame;

    private float frameWidth;
    private float frameHeight;

    public GoblinAnimationHandler(Goblin goblin) {
        this.goblin = goblin;
    }

    @Override
    protected void loadAnimations() {
        Texture goblinSheet = MyGdxGame.resources.getTexture("goblinSheet");
        TextureRegion[][] tmp = TextureRegion.split(goblinSheet,
                goblinSheet.getWidth() / FRAME_COLS,
                goblinSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] firstFrames= new TextureRegion[FIRST_FRAME_COLS * FIRST_FRAME_ROWS];
        TextureRegion[] secondFrames = new TextureRegion[SECOND_FRAME_COLS * SECOND_FRAME_ROWS];
        TextureRegion[] thirdFrames = new TextureRegion[THIRD_FRAME_COLS * THIRD_FRAME_ROWS];
        TextureRegion[] fourthFrames = new TextureRegion[FOURTH_FRAME_COLS* FOURTH_FRAME_ROWS];

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
                secondFrames[index] = tmp[i+1][j]; // second row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < THIRD_FRAME_ROWS; i++) {
            for (int j = 0; j < THIRD_FRAME_COLS; j++) {
                thirdFrames[index] = tmp[i+2][j]; // second row
                index++;
            }
        }
        index = 0;
        for (int i = 0; i < FOURTH_FRAME_ROWS; i++) {
            for (int j = 0; j < FOURTH_FRAME_COLS; j++) {
                fourthFrames[index] = tmp[i+3][j]; // second row
                index++;
            }
        }
        frameWidth = firstFrames[0].getRegionWidth();
        frameHeight = firstFrames[0].getRegionHeight();

        firstAnimation = new Animation<TextureRegion>(8f, firstFrames);
        deathAnimation = new Animation<TextureRegion>(0.2f, secondFrames);
        thirdAnimation = new Animation<TextureRegion>(1f, thirdFrames);
        walkingAnimation = new Animation<TextureRegion>(0.5f, fourthFrames);

    }

    @Override
    public void render(SpriteBatch batch) {
        switch (goblin.goblinState) {
            case ATTACK:
                currentFrame = firstAnimation.getKeyFrame(stateTime, true);
                break;
            case IDLE:
            case PREPARE_TO_ATTACK:
                currentFrame = thirdAnimation.getKeyFrame(stateTime, false);
                break;
            case DEATH:
                currentFrame = deathAnimation.getKeyFrame(stateTime, false);
                break;
            case WALKING:
                currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(!goblin.lookingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if(goblin.lookingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);

        batch.draw(currentFrame, goblin.getPosition().x * pixelPerMeter - frameWidth / 2, goblin.getPosition().y * pixelPerMeter - frameHeight / 2);
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
