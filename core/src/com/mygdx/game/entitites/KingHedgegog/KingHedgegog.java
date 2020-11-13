package com.mygdx.game.entitites.KingHedgegog;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Hit;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.AnimationHandlers.KingHedgegogAnimationHandler;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class KingHedgegog extends Entity {
    public KingState kingState;
    private Player player;

    private float leftXBound , rightXBound;

    public boolean lookingRight = true;
    public boolean playerIsClose = false;

    public KingHedgegog(Body body, Player player, int hitpoints) {
        super(body, hitpoints);
        this.player = player;
        changeKingState(KingState.WALKING);
    }

    @Override
    public void setAnimationHandler() {
        animationHandler = new KingHedgegogAnimationHandler(this);
    }

    @Override
    public void getHit(Hit hit) {
        changeKingState(KingState.IDLE);
        if(hit.damage != 0) {
            int power = hit.fromTheRight ? -50 : 50;
            body.applyLinearImpulse(
                    power,
                    50,
                    body.getWorldCenter().x,
                    body.getWorldCenter().y,
                    true);
            hitpoints-=hit.damage;
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if(hitpoints <= 0) changeKingState(KingState.DEATH);
        switch (kingState) { // for now there is no usage for idle I think. it's simpler logic that way
            case RUSHING:
                move(7.5f);
                if(animationHandler.stateTime > 1.4f) {
                    changeKingState(KingState.WALKING);    //changeKingState(KingState.IDLE);
                    getBody().setLinearVelocity(0, getBody().getLinearVelocity().y);
                }
                else if( changeDirectionIfOnTheEdge() ) {
                    getBody().setLinearVelocity(0, getBody().getLinearVelocity().y);
                    changeKingState(KingState.IGNORE_PLAYER);               //changeKingState(KingState.IDLE);
                }
                break;
            case WALKING:
                if(playerIsClose) {
                    changeKingState(KingState.PREPARE_TO_ATTACK);
                    changeDirectionTowardsPlayer();
                }
                else {
                    changeDirectionIfOnTheEdge();
                    move(3);
                }
                break;
            case PREPARE_TO_ATTACK:
                if(animationHandler.stateTime > 2f) changeKingState(KingState.RUSHING);
                break;
            case IGNORE_PLAYER:
                move(3);
                if(animationHandler.stateTime > 1f) {
                    if(playerIsClose) {
                        changeKingState(KingState.PREPARE_TO_ATTACK);
                        changeDirectionTowardsPlayer();
                    }
                    else changeKingState(KingState.WALKING);
                }
                break;
            case IDLE:
                if (playerIsClose) {
                    if( changeDirectionIfOnTheEdge() ) {
                        changeKingState(KingState.IGNORE_PLAYER);
                    }
                    else {
                        changeKingState(KingState.PREPARE_TO_ATTACK);
                        changeDirectionTowardsPlayer();
                    }
                }
                else if(animationHandler.stateTime > 2f) changeKingState(KingState.WALKING);
                break;
            case DEATH:
                isDying = true;
                if(animationHandler.stateTime > 2f) {
                    isAlive = false;
                }
        }
    }
    private void changeDirectionTowardsPlayer(){
        lookingRight = player.getPosition().x > getPosition().x;
    }

    private boolean changeDirectionIfOnTheEdge() {
        if(getPosition().x > rightXBound) {
            lookingRight = false;
            return true;
        } else if(getPosition().x < leftXBound) {
            lookingRight = true;
            return true;
        }
        return false;
    }

    private void move(float maxSpeed) {
        if(lookingRight) {
            if (getBody().getLinearVelocity().x < maxSpeed) {
                this.getBody().applyLinearImpulse(
                        300 / pixelPerMeter,
                        0,
                        this.getBody().getWorldCenter().x,
                        this.getBody().getWorldCenter().y,
                        true);
            } else {
                getBody().setLinearVelocity(maxSpeed, getBody().getLinearVelocity().y);
            }
        } else {
            if (getBody().getLinearVelocity().x > -maxSpeed) {
                this.getBody().applyLinearImpulse(
                        -300 / pixelPerMeter,
                        0,
                        this.getBody().getWorldCenter().x,
                        this.getBody().getWorldCenter().y,
                        true);
            } else {
                getBody().setLinearVelocity(-maxSpeed, getBody().getLinearVelocity().y);
            }
        }
    }

    public void setPlayerIsClose(boolean playerIsClose) {
        this.playerIsClose = playerIsClose;
    }

    private void changeKingState(KingState newState){
        if(kingState!= newState) {
            kingState = newState;
            animationHandler.stateTime = 0;
        }

    }

    public void setMovementBounds(float leftXBound, float rightXBound) {
        this.leftXBound = leftXBound / pixelPerMeter;
        this.rightXBound = rightXBound / pixelPerMeter;
    }
}

