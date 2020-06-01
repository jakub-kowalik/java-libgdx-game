package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

import static com.mygdx.game.handlers.Box2DVariables.pixelPerMeter;

public class BoundedCamera extends OrthographicCamera {



    public void setBoundedPosition(float x, float y, float z, float mapWidth, float mapHeight) {
        this.position.set(x * pixelPerMeter, y * pixelPerMeter, 0);
        boundCamera(this, mapWidth, mapHeight);
    }


    private void boundCamera(Camera cam, float mapWidth, float mapHeight) {

        // These values likely need to be scaled according to your world coordinates.
// The left boundary of the map (x)
        int mapLeft = 0;
// The right boundary of the map (x + width)
        int mapRight = (int) mapWidth;
// The bottom boundary of the map (y)
        int mapBottom = 0;
// The top boundary of the map (y + height)
        int mapTop = (int) mapHeight;
// The camera dimensions, halved
        float cameraHalfWidth = cam.viewportWidth * .5f;
        float cameraHalfHeight = cam.viewportHeight * .5f;

// Move camera after player as normal

        float cameraLeft = cam.position.x - cameraHalfWidth;
        float cameraRight = cam.position.x + cameraHalfWidth;
        float cameraBottom = cam.position.y - cameraHalfHeight;
        float cameraTop = cam.position.y + cameraHalfHeight;

// Horizontal axis
        if(mapWidth < cam.viewportWidth)
        {
            cam.position.x = mapRight / 2f;
        }
        else if(cameraLeft <= mapLeft)
        {
            cam.position.x = mapLeft + cameraHalfWidth;
        }
        else if(cameraRight >= mapRight)
        {
            cam.position.x = mapRight - cameraHalfWidth;
        }

// Vertical axis
        if(mapHeight < cam.viewportHeight)
        {
            cam.position.y = mapTop / 2f;
        }
        else if(cameraBottom <= mapBottom)
        {
            cam.position.y = mapBottom + cameraHalfHeight;
        }
        else if(cameraTop >= mapTop)
        {
            cam.position.y = mapTop - cameraHalfHeight;
        }
    }
}
