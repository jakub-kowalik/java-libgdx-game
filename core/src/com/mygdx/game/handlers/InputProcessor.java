package com.mygdx.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {

    private int BTN1 = Input.Keys.UP;
    private int BTN2 = Input.Keys.LEFT;
    private int BTN3 = Input.Keys.DOWN;
    private int BTN4 = Input.Keys.RIGHT;
    private int BTN5 = Input.Keys.Z;
    private int BTN6 = Input.Keys.X;

    @Override
    public boolean keyDown(int k) {
        if(k == BTN1) {
            InputHandler.setKey(InputHandler.BUTTON1, true);
        }

        if(k == BTN2) {
            InputHandler.setKey(InputHandler.BUTTON2, true);
        }

        if(k == BTN3) {
            InputHandler.setKey(InputHandler.BUTTON3, true);
        }

        if(k == BTN4) {
            InputHandler.setKey(InputHandler.BUTTON4, true);
        }
        if(k == BTN5) {
            InputHandler.setKey(InputHandler.BUTTON5, true);
        }
        if(k == BTN6) {
            InputHandler.setKey(InputHandler.BUTTON6, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if(k == BTN1) {
            InputHandler.setKey(InputHandler.BUTTON1, false);
        }

        if(k == BTN2) {
            InputHandler.setKey(InputHandler.BUTTON2, false);
        }

        if(k == BTN3) {
            InputHandler.setKey(InputHandler.BUTTON3, false);
        }

        if(k == BTN4) {
            InputHandler.setKey(InputHandler.BUTTON4, false);
        }
        if(k == BTN5) {
            InputHandler.setKey(InputHandler.BUTTON5, false);
        }
        if(k == BTN6) {
            InputHandler.setKey(InputHandler.BUTTON6, false);
        }
        return true;
    }

}
