package com.ivieleague.zeldarando.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ivieleague.event.GameHolder;
import com.ivieleague.zeldarando.TestGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.samples = 4;
        config.foregroundFPS = 30;
        new LwjglApplication(new GameHolder(new TestGame()), config);
    }
}
