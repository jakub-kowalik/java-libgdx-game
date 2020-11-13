package com.mygdx.game.handlers;

public class InputHandler {

    public static boolean[] keys;

    public static boolean[] pkeys;

    public static int mouseX;
    public static int mouseY;

    public static final int NUM_KEYS = 10;
    public static final int BUTTON1 = 0;
    public static final int BUTTON2 = 1;
    public static final int BUTTON3 = 2;
    public static final int BUTTON4 = 3;
    public static final int BUTTON5 = 4;
    public static final int BUTTON6 = 5;
    public static final int BUTTON7 = 6;
    public static final int BUTTON8 = 9;

    public static final int MOUSE1 = 7;
    public static final int MOUSE2 = 8;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        System.arraycopy(keys, 0, pkeys, 0, NUM_KEYS);
    }

    public static void setKey(int i, boolean b) {
        keys[i] = b;
    }

    public static void setMouseCoordinates(int a, int b) {
        mouseX = a;
        mouseY = b;
    }

    public static boolean isDown(int i) {
        return keys[i];
    }

    public static boolean isPressed(int i) {
        return (keys[i] && !pkeys[i]);
    }
}
