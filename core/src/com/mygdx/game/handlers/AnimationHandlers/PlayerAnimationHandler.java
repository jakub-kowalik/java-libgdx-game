package com.mygdx.game.handlers.AnimationHandlers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entitites.Player;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class PlayerAnimationHandler extends AnimationHandler {
    Player player;
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 10, FRAME_ROWS = 2;
    private static final int WALK_FRAME_COLS = 10, WALK_FRAME_ROWS = 1;
    private static final int IDLE_FRAME_COLS = 2, IDLE_FRAME_ROWS = 1;

    // Objects used
    public Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    public Animation<TextureRegion> idleAnimation; // Must declare frame type (TextureRegion)
    public TextureRegion currentFrame;

    public float frameWidth;
    public float frameHeight;

    public PlayerAnimationHandler(Player player) {
        super();
        this.player = player;
    }

    @Override
    protected void loadAnimations() {
        // Load the sprite sheet as a Texture
        animationSheet = MyGdxGame.resources.getTexture("playerSheet");

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(animationSheet,
                animationSheet.getWidth() / FRAME_COLS,
                animationSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The AnimationHandler constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[WALK_FRAME_COLS * WALK_FRAME_ROWS];
        TextureRegion[] idleFrames = new TextureRegion[IDLE_FRAME_COLS * IDLE_FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < IDLE_FRAME_ROWS; i++) {
            for (int j = 0; j < IDLE_FRAME_COLS; j++) {
                idleFrames[index] = tmp[i][j]; // 0,0 start
                index++;
            }
        }

        index = 0;
        for (int i = 0; i < WALK_FRAME_ROWS; i++) {
            for (int j = 0; j < WALK_FRAME_COLS; j++) {
                walkFrames[index] = tmp[i+1][j]; // 1,0 start
                index++;
            }
        }


        frameWidth = walkFrames[0].getRegionWidth();
        frameHeight = walkFrames[0].getRegionHeight();

        // Initialize the AnimationHandler with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(1/5f, walkFrames);
        idleAnimation = new Animation<TextureRegion>(2f, idleFrames);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (player.isMoving)
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        else
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        if(player.lookingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if(!player.lookingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        batch.draw(currentFrame, player.getPosition().x * pixelPerMeter - frameWidth / 2, player.getPosition().y * pixelPerMeter - frameHeight / 2);
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
