package com.mygdx.game.entitites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.MyGdxGame;

public class Collectable extends Box2DSprite {

    public Collectable(Body body) {
        super(body);

        Texture texture = MyGdxGame.resources.getTexture("crystals");
        TextureRegion[] textureRegion = TextureRegion.split(texture, 16, 16)[0];

        setAnimationHandler(textureRegion, 1/12f);
    }
}
