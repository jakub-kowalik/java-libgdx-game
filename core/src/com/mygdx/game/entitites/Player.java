package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.MyGdxGame;

public class Player extends Box2DSprite {

    private int numCrystals;
    private int totalCrystals;

    public Player(Body body) {

        super(body);

        Texture texture = MyGdxGame.resources.getTexture("michal");
        TextureRegion[] textureRegion = TextureRegion.split(texture, 16, 32)[0];

        setAnimationHandler(textureRegion, 1/12f);
    }

    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() {return numCrystals; }
    public void setTotalCrystals(int i) { totalCrystals = i;}
    public int getTotalCrystals() { return totalCrystals; }
}
