package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class ContactHandler implements ContactListener {

    private int numFootContacts;
    private int numHeadContacts;
    private int numLeftContacts;
    private int numRightContacts;

    private Array<Body> bodiesToRemove;

    public ContactHandler() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    public void beginContact(Contact contact) {



        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot"))
            numFootContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot"))
            numFootContacts++;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("head"))
            numHeadContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("head"))
            numHeadContacts++;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("left"))
            numLeftContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("left"))
            numLeftContacts++;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("right"))
            numRightContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("right"))
            numRightContacts++;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("crystal"))
            bodiesToRemove.add(fixtureA.getBody());
        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("crystal"))
            bodiesToRemove.add(fixtureB.getBody());

        System.out.println("1 " + numFootContacts);



    }

    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA == null || fixtureB == null) return;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot"))
            numFootContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot"))
            numFootContacts--;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("head"))
            numHeadContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("head"))
            numHeadContacts--;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("left"))
            numLeftContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("left"))
            numLeftContacts--;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("right"))
            numRightContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("right"))
            numRightContacts--;

        System.out.println("2 " + numFootContacts);
    }

    public boolean getIsGrounded() { return numFootContacts > 0; }
    public boolean getIsCeiled() { return numHeadContacts > 0; }
    public boolean getIsRightContact() { return numRightContacts > 0; }
    public boolean getIsLeftContact() { return numLeftContacts > 0; }
    public Array<Body> getBodiesToRemove() { return bodiesToRemove; }

    //detection
    public void preSolve(Contact contact, Manifold manifold) {}

    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}


}
