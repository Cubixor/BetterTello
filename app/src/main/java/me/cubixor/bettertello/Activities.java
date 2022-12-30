package me.cubixor.bettertello;

import java.lang.ref.WeakReference;

import me.cubixor.bettertello.tello.VideoSettingsWindow;

public class Activities {

    private static WeakReference<VideoSettingsWindow> videoSettingsWindow = null;

    public static void updateVideoSettingsWindow(VideoSettingsWindow activity) {
        videoSettingsWindow = new WeakReference<>(activity);
    }

    public static VideoSettingsWindow getVideoSettingsWindow() {
        if (videoSettingsWindow == null || videoSettingsWindow.get() == null || !videoSettingsWindow.get().isOpen()) {
            return null;
        }

        return videoSettingsWindow.get();
    }
}
