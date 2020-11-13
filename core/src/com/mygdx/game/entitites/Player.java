package com.mygdx.game.entitites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.mygdx.game.handlers.AnimationHandlers.PlayerAnimationHandler;

public class Player extends Entity {

    private int numCrystals;
    private int totalCrystals;
    private RevoluteJoint attackArm;
    public Body armBody;

    public boolean isMoving = false;
    public boolean lookingRight = true;
    public boolean isAttacking = false;

    public Player(Body body, RevoluteJoint attackArm, Body armBody, int hitpoints) {
        super(body, hitpoints);
        this.attackArm = attackArm;
        this.armBody = armBody;
    }

    @Override
    public void getHit(Hit hit) {
        hitpoints -= hit.damage;
        if (hitpoints <= 0) {
            hitpoints = 0;
            isAlive = false;
        }
    }

    @Override
    public void setAnimationHandler() {
        animationHandler = new PlayerAnimationHandler(this);
    }

    public void collectCrystal() {
        numCrystals++;
    }

    public int getNumCrystals() {
        return numCrystals;
    }

    public void setTotalCrystals(int i) {
        totalCrystals = i;
    }

    public int getTotalCrystals() {
        return totalCrystals;
    }

    public RevoluteJoint getAttackArm() {
        return attackArm;
    }

    public Body getArmBody() {
        return armBody;
    }
}
