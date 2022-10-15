package me.cubixor.bettertello;

import java.lang.ref.WeakReference;

import me.cubixor.bettertello.tello.VideoSettingsWindow;

public class Activities {

    private static WeakReference<HomePageActivity> homePage = null;
    private static WeakReference<SettingsActivity> settings = null;
    private static WeakReference<FragmentSettingsController> settingsControllerFragment = null;
    private static WeakReference<VideoSettingsWindow> videoSettingsWindow = null;

    public static void updateHomePageActivity(HomePageActivity activity) {
        homePage = new WeakReference<>(activity);
    }

    public static void updateSettingsActivity(SettingsActivity activity) {
        settings = new WeakReference<>(activity);
    }

    public static void updateSettingsControllerFragment(FragmentSettingsController activity) {
        settingsControllerFragment = new WeakReference<>(activity);
    }

    public static void updateVideoSettingsWindow(VideoSettingsWindow activity) {
        videoSettingsWindow = new WeakReference<>(activity);
    }

    public static HomePageActivity getHomePage() {
        if (homePage == null || homePage.get() == null) {
            return null;
        }

        return homePage.get();
    }

    public static SettingsActivity getSettings() {
        if (settings == null || settings.get() == null) {
            return null;
        }

        return settings.get();
    }

    public static FragmentSettingsController getSettingsControllerFragment() {
        if (settingsControllerFragment == null || settingsControllerFragment.get() == null) {
            return null;
        }

        return settingsControllerFragment.get();
    }

    public static VideoSettingsWindow getVideoSettingsWindow() {
        if (videoSettingsWindow == null || videoSettingsWindow.get() == null || !videoSettingsWindow.get().isOpen()) {
            return null;
        }

        return videoSettingsWindow.get();
    }
}
