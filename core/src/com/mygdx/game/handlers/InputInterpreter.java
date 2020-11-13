package com.mygdx.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputInterpreter extends InputAdapter {

    private int BTN1 = Input.Keys.W;
    private int BTN2 = Input.Keys.A;
    private int BTN3 = Input.Keys.S;
    private int BTN4 = Input.Keys.D;
    private int BTN5 = Input.Keys.Z;
    private int BTN6 = Input.Keys.X;
    private int BTN7 = Input.Keys.SPACE;
    private int BTN8 = Input.Keys.ESCAPE;

    private int MS1 = Input.Buttons.LEFT;
    private int MS2 = Input.Buttons.RIGHT;

    @Override
    public boolean keyDown(int k) {
        if (k == BTN1) {
            InputHandler.setKey(InputHandler.BUTTON1, true);
        }

        if (k == BTN2) {
            InputHandler.setKey(InputHandler.BUTTON2, true);
        }

        if (k == BTN3) {
            InputHandler.setKey(InputHandler.BUTTON3, true);
        }

        if (k == BTN4) {
            InputHandler.setKey(InputHandler.BUTTON4, true);
        }
        if (k == BTN5) {
            InputHandler.setKey(InputHandler.BUTTON5, true);
        }
        if (k == BTN6) {
            InputHandler.setKey(InputHandler.BUTTON6, true);
        }
        if (k == BTN7) {
            InputHandler.setKey(InputHandler.BUTTON7, true);
        }

        if (k == BTN8) {
            InputHandler.setKey(InputHandler.BUTTON8, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if (k == BTN1) {
            InputHandler.setKey(InputHandler.BUTTON1, false);
        }

        if (k == BTN2) {
            InputHandler.setKey(InputHandler.BUTTON2, false);
        }

        if (k == BTN3) {
            InputHandler.setKey(InputHandler.BUTTON3, false);
        }

        if (k == BTN4) {
            InputHandler.setKey(InputHandler.BUTTON4, false);
        }
        if (k == BTN5) {
            InputHandler.setKey(InputHandler.BUTTON5, false);
        }
        if (k == BTN6) {
            InputHandler.setKey(InputHandler.BUTTON6, false);
        }
        if (k == BTN7) {
            InputHandler.setKey(InputHandler.BUTTON7, false);
        }

        if (k == BTN8) {
            InputHandler.setKey(InputHandler.BUTTON8, false);
        }
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == MS1) {
            InputHandler.setKey(InputHandler.MOUSE1, true);
            InputHandler.setMouseCoordinates(screenX, screenY);
        }

        if (button == MS2) {
            InputHandler.setKey(InputHandler.MOUSE2, true);
            InputHandler.setMouseCoordinates(screenX, screenY);
        }

        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == MS1) {
            InputHandler.setKey(InputHandler.MOUSE1, false);
        }

        if (button == MS2) {
            InputHandler.setKey(InputHandler.MOUSE2, false);
        }

        return false;
    }
}
