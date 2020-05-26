package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class ContentHandler {

    private HashMap<String, Texture> textureHashMap;

    public ContentHandler() {
        textureHashMap = new HashMap<>();
    }

    public void loadTexture(String path, String key) {
        Texture texture = new Texture(Gdx.files.internal((path)));
        textureHashMap.put(key, texture);
    }

    public Texture getTexture(String key) {
        return textureHashMap.get(key);
    }

    public void disposeTexture(String key) {
        Texture texture = textureHashMap.get(key);
        if (texture != null)
            texture.dispose();
    }

}
