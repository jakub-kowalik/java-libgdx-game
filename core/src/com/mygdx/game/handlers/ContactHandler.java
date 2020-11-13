package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entitites.Hit;

public class ContactHandler implements ContactListener {

    private int numLeftFootContacts;
    private int numRightFootContacts;
    private int numHeadContacts;
    private int numLeftContacts;
    private int numRightContacts;

    private Array<Body> wakedUp;
    private Array<Body> sawPlayer;
    private Array<Body> lostContactOfPlayer;
    private Array<Body> tmp;
    private Array<Hit> hitsMade;
    private Array<Body> bulletsConnected;

    public ContactHandler() {
        super();
        wakedUp = new Array<>();
        sawPlayer = new Array<>();
        lostContactOfPlayer = new Array<>();
        hitsMade = new Array<>();
        bulletsConnected = new Array<>();
        tmp = new Array<>();
    }

    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("leftfoot"))
            numLeftFootContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("leftfoot"))
            numLeftFootContacts++;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("rightfoot"))
            numRightFootContacts++;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("rightfoot"))
            numRightFootContacts++;

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

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("slimePlayerSensor")) {
            if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player")) {
                wakedUp.add(fixtureA.getBody());
            }
        }

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("slimePlayerSensor")) {
            if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player")) {
                wakedUp.add(fixtureB.getBody());
            }
        }

        if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals("kingPlayerSensor")
                || fixtureA.getUserData().equals("goblinPlayerSensor"))) {
            if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player")) {
                sawPlayer.add(fixtureA.getBody());
            }
        }

        if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals("kingPlayerSensor")
                || fixtureB.getUserData().equals("goblinPlayerSensor"))) {
            if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player")) {
                sawPlayer.add(fixtureB.getBody());
            }
        }

        float posAx = fixtureA.getBody().getPosition().x;
        float posBx = fixtureB.getBody().getPosition().x;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player")) {     // check for hits between a player and enemy
            boolean pushRight = posAx > posBx;
            if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("slimeBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 10f, pushRight)); // player hit
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 0f, !pushRight)); // slime hit

            } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("kingBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 10f, pushRight)); // player hit
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 1f, !pushRight)); // king hit
            } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("rockBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 10f, pushRight)); // player hit
            }
        } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player")) {
            boolean pushRight = posBx > posAx;
            if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("slimeBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 10f, pushRight)); // player hit
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 0f, !pushRight)); // slime hit

            } else if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("kingBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 10f, pushRight)); // player hit
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 1f, !pushRight)); // king hit

            } else if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("rockBody")) {
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 1f, !pushRight)); // player hit
            }
        }
        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("sword")) {     // check for hits between a player's sword and enemy
            boolean pushRight = posAx > posBx;
            if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("slimeBody")) {
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 20f, pushRight)); // slime hit

            } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("kingBody")) {
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 50f, pushRight)); // king hit

            } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("goblinBody")) {
                hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 50f, pushRight)); // goblin hit
            }

        } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("sword")) {
            boolean pushRight = posBx > posAx;
            if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("slimeBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 20f, pushRight)); // slime hit

            } else if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("kingBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 50f, pushRight)); // king hit

            } else if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("goblinBody")) {
                hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 50f, pushRight)); // goblin hit
            }
        }

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("crystal"))
            hitsMade.add(new Hit(fixtureA.getBody(), fixtureB.getBody(), 1, true));

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("crystal"))
            hitsMade.add(new Hit(fixtureB.getBody(), fixtureA.getBody(), 1, true));


        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("rockBody")) { // check if bullet hit anything
            bulletsConnected.add(fixtureB.getBody());
        } else if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("rockBody")) {
            bulletsConnected.add(fixtureA.getBody());
        }

    }

    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("leftfoot"))
            numLeftFootContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("leftfoot"))
            numLeftFootContacts--;

        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("rightfoot"))
            numRightFootContacts--;

        if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("rightfoot"))
            numRightFootContacts--;

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

        if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals("kingPlayerSensor")
                || fixtureA.getUserData().equals("goblinPlayerSensor"))) {
            if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player")) {
                lostContactOfPlayer.add(fixtureA.getBody());
            }
        }

        if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals("kingPlayerSensor")
                || fixtureB.getUserData().equals("goblinPlayerSensor"))) {
            if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player")) {
                lostContactOfPlayer.add(fixtureB.getBody());
            }
        }

    }

    public boolean getIsGrounded() {
        return numLeftFootContacts > 0 || numRightFootContacts > 0;
    }

    public boolean getIsCeiled() {
        return numHeadContacts > 0;
    }

    public boolean getIsRightContact() {
        return numRightContacts > 0;
    }

    public boolean getIsLeftContact() {
        return numLeftContacts > 0;
    }

    public Array<Hit> getHitsMade() {
        if (hitsMade.size == 0) return hitsMade;

        Array<Hit> tmp = new Array<>();
        tmp.addAll(hitsMade);
        hitsMade.clear();
        return tmp;
    }

    public Array<Body> getBulletsConnected() {
        tmp.clear();
        tmp.addAll(bulletsConnected);
        bulletsConnected.clear();
        return tmp;
    }

    public Array<Body> getWakedUp() {
        tmp.clear();
        tmp.addAll(wakedUp);
        wakedUp.clear();
        return tmp;
    }

    public Array<Body> getSawPlayer() {
        tmp.clear();
        tmp.addAll(sawPlayer);
        sawPlayer.clear();
        return tmp;
    }

    public Array<Body> getLostContactOfPlayer() {
        tmp.clear();
        tmp.addAll(lostContactOfPlayer);
        lostContactOfPlayer.clear();
        return tmp;
    }

    //detection

    public void preSolve(Contact contact, Manifold manifold) {
    }

    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }
}
