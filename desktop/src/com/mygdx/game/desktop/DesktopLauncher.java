package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = MyGdxGame.TITLE;
		config.width = MyGdxGame.V_WIDTH * MyGdxGame.SCALE;
		config.height = MyGdxGame.V_HEIGHT * MyGdxGame.SCALE;
		//config.vSyncEnabled = true;
		//config.fullscreen = true;

		new LwjglApplication(new MyGdxGame(), config);

	}
}
