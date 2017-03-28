package com.ivieleague.bullethellvs.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ivieleague.bullethellvs.TestGame;
import com.ivieleague.event.GameHolder;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.samples = 4;
        config.foregroundFPS = 60;
        new LwjglApplication(new GameHolder(new TestGame()), config);
    }
}
