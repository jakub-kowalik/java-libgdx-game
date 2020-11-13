package com.mygdx.game.entitites.Goblin;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.entitites.Bullet.Bullet;
import com.mygdx.game.entitites.Entity;
import com.mygdx.game.entitites.Hit;
import com.mygdx.game.entitites.Player;
import com.mygdx.game.handlers.AnimationHandlers.GoblinAnimationHandler;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class Goblin extends Entity {
    public GoblinState goblinState;

    private Pool<Bullet> rockPool;
    private Array<Entity> bullets;
    private Player player;

    private float leftXBound, rightXBound;

    public boolean lookingRight = true;
    public boolean playerIsClose = false;

    public Goblin(Body goblinBody, Player player, int hitpoints, Pool rockPool, Array<Entity> bullets) {
        super(goblinBody, hitpoints);
        this.player = player;
        this.rockPool = rockPool;
        this.bullets = bullets;
        goblinState = GoblinState.WALKING;
    }

    @Override
    public void setAnimationHandler() {
        animationHandler = new GoblinAnimationHandler(this);
    }

    @Override
    public void getHit(Hit hit) {
        changeGoblinState(GoblinState.IDLE);
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
        if(hitpoints <= 0) changeGoblinState(GoblinState.DEATH);

        switch(goblinState) {
            case ATTACK:
                throwRockAtPlayer();
                changeGoblinState(GoblinState.IDLE);
                break;

            case WALKING:
                if(playerIsClose) {
                    changeDirectionTowardsPlayer();
                    changeGoblinState(GoblinState.PREPARE_TO_ATTACK);
                }
                else {
                    changeDirectionIfOnTheEdge();
                    move(3);
                }
                break;
            case PREPARE_TO_ATTACK:
                if(animationHandler.stateTime > 1f) changeGoblinState(GoblinState.ATTACK);
                break;
            case IDLE:
                if (playerIsClose) {
                    changeDirectionTowardsPlayer();
                    changeGoblinState(GoblinState.PREPARE_TO_ATTACK);
                }
                else if(animationHandler.stateTime > 2f) changeGoblinState(GoblinState.WALKING);
                break;
            case DEATH:
                isDying = true;
                if(animationHandler.stateTime > 2f) {
                    isAlive = false;
                }
                break;
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
                        150 / pixelPerMeter,
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
                        -150 / pixelPerMeter,
                        0,
                        this.getBody().getWorldCenter().x,
                        this.getBody().getWorldCenter().y,
                        true);
            } else {
                getBody().setLinearVelocity(-maxSpeed, getBody().getLinearVelocity().y);
            }
        }
    }
    private void throwRockAtPlayer() {
        Bullet rock = rockPool.obtain();
        rock.init(body.getPosition(), lookingRight);
        rock.getBody().setUserData(rock);
        bullets.add(rock);
    }

    public void setMovementBounds(float leftXBound, float rightXBound) {
        this.leftXBound = leftXBound / pixelPerMeter;
        this.rightXBound = rightXBound / pixelPerMeter;
    }

    public void setPlayerIsClose(boolean playerIsClose) {
        this.playerIsClose = playerIsClose;
    }

    private void changeGoblinState(GoblinState newState){
        if(goblinState!= newState) {
            goblinState = newState;
            animationHandler.stateTime = 0;
        }

    }
}
