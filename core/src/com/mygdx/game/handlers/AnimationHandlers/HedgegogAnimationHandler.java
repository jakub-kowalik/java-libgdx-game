package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Hedgegog;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class HedgegogAnimationHandler extends AnimationHandler {
    Hedgegog hedgegog;
    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;
    private static final int WALK_FRAME_COLS = 2, WALK_FRAME_ROWS = 1;
    private static final int IDLE_FRAME_COLS = 2, IDLE_FRAME_ROWS = 1;


    public Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    public Animation<TextureRegion> idleAnimation; // Must declare frame type (TextureRegion)
    public TextureRegion currentFrame;

    public float frameWidth;
    public float frameHeight;

    public HedgegogAnimationHandler(Hedgegog hedgegog) {
        super();
        this.hedgegog = hedgegog;
    }

    @Override
    protected void loadAnimations() {
        Texture hedgSheet = MyGdxGame.resources.getTexture("hedgegogSheet");

        TextureRegion[][] tmp = TextureRegion.split(hedgSheet,
                hedgSheet.getWidth() / FRAME_COLS,
                hedgSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames= new TextureRegion[WALK_FRAME_COLS * WALK_FRAME_ROWS];
        TextureRegion[] idleFrames = new TextureRegion[IDLE_FRAME_COLS * IDLE_FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < WALK_FRAME_ROWS; i++) {
            for (int j = 0; j < WALK_FRAME_COLS; j++) {
                walkFrames[index] = tmp[i+1][j]; // 1,0 start
                idleFrames[index] = tmp[i][j]; // 0,0 start
                index++;
            }
        }
        frameWidth = walkFrames[0].getRegionWidth();
        frameHeight = walkFrames[0].getRegionHeight();

        walkAnimation = new Animation<TextureRegion>(1/5f, walkFrames);
        idleAnimation = new Animation<TextureRegion>(2f, idleFrames);
    }

    @Override
    public void render(SpriteBatch batch) {
        currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, hedgegog.getPosition().x * pixelPerMeter - frameWidth / 2, hedgegog.getPosition().y * pixelPerMeter - frameHeight / 2);
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
