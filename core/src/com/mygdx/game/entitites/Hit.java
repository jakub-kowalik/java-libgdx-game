package com.mygdx.game.entitites;

import com.badlogic.gdx.physics.box2d.Body;

public class Hit {

    public Hit(Body hitBody, Body attackerBody, float damage, boolean fromTheRight) {
        this.fromTheRight = fromTheRight;
        this.damage = damage;
        this.hitBody = hitBody;
        this.attackerBody = attackerBody;
    }

    public Body hitBody;

    public boolean fromTheRight;

    public float damage;

    public Body attackerBody;
}
