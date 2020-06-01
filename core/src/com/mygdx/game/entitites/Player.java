package com.mygdx.game.entitites;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.handlers.AnimationHandlers.PlayerAnimationHandler;

public class Player extends Box2DSprite {

    private int numCrystals;
    private int totalCrystals;

    public boolean isMoving = false;
    public boolean lookingRight = true;


    public Player(Body body) {
        super(body);
    }

    @Override
    public void setAnimationHandler() {
        animationHandler = new PlayerAnimationHandler(this);
    }


    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() {return numCrystals; }
    public void setTotalCrystals(int i) { totalCrystals = i;}
    public int getTotalCrystals() { return totalCrystals; }

}
