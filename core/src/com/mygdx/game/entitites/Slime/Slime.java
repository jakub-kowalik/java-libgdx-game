package com.mygdx.game.entitites.Slime;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Hit;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.AnimationHandlers.SlimeAnimationHandler;

import java.util.Random;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class Slime extends Entity {
    private Player player;

    public SlimeState slimeState;

    protected float lastJumpTimeStamp;
    public boolean lookingRight;
    protected Random random;

    public Slime(Body body, Player player, int hitpoints) {
        super(body, hitpoints);
        this.player = player;
        random = new Random();
        animationHandler.stateTime = 0;
        slimeState = SlimeState.IDLE;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(hitpoints <= 0) changeSlimeState(SlimeState.DEATH);
        switch (slimeState) {
            case IDLE:
                break;
            case JUMPING:
                if(animationHandler.stateTime - lastJumpTimeStamp > (2f + random.nextFloat() * 10f )) {
                    lastJumpTimeStamp = animationHandler.stateTime;
                    jump();
                }
                break;
            case DEATH:
                isDying = true;
                if(animationHandler.stateTime > 2f) {
                    isAlive = false;
                }
                break;
        }
    }

    @Override
    public void setAnimationHandler() {
        this.animationHandler = new SlimeAnimationHandler(this);
    }

    public void jump(){
        int power = (player.getPosition().x - this.getPosition().x ) > 0 ? 150 : -150;
        this.lookingRight = power >= 0;

        power += random.nextFloat() * power;

        this.getBody().applyLinearImpulse(
                power / pixelPerMeter,
                350 / pixelPerMeter,
                this.getBody().getWorldCenter().x,
                this.getBody().getWorldCenter().y,
                true
        );
    }

    @Override
    public void getHit(Hit hit) {
        int power = hit.fromTheRight ? -20 : 20;
        body.applyLinearImpulse(
                power,
                20,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true);
        hitpoints-=hit.damage;

    }

    public void changeSlimeState(SlimeState newState) {
        if(slimeState != newState) {
            animationHandler.stateTime = 0;
            slimeState = newState;
        }
    }
}
