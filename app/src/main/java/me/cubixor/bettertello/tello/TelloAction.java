package me.cubixor.bettertello.tello;

import android.view.View;
import android.widget.Button;

import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.utils.Utils;
import me.cubixor.telloapi.api.FlipDirection;
import me.cubixor.telloapi.api.Tello;
import me.cubixor.telloapi.api.video.SmartVideoMode;
import me.cubixor.telloapi.api.video.VideoMode;

public enum TelloAction {

    TAKEOFF_LAND {
        @Override
        public void invoke(Tello tello) {
            if (!tello.getDroneState().iseMSky()) {
                tello.takeOff();
            } else {
                tello.land(false);
            }
        }
    }, THROW_TAKEOFF_HAND_LAND {
        @Override
        public void invoke(Tello tello) {
            if (!tello.getDroneState().iseMSky()) {
                tello.throwTakeOff();
            } else {
                tello.palmLand();
            }

        }
    }, START_MOTORS {
        @Override
        public void invoke(Tello tello) {
            tello.startMotors();
        }
    }, INCREASE_EXPOSURE {
        @Override
        public void invoke(Tello tello) {
            AppSettings appSettings = AppSettings.getInstance();

            int exposure = appSettings.getExposure() + 1;
            if (exposure > 18) {
                return;
            }

            appSettings.setExposure(exposure);
        }
    }, DECREASE_EXPOSURE {
        @Override
        public void invoke(Tello tello) {
            AppSettings appSettings = AppSettings.getInstance();

            int exposure = appSettings.getExposure() - 1;
            if (exposure < 0) {
                return;
            }

            appSettings.setExposure(exposure);
        }
    }, INCREASE_BITRATE {
        @Override
        public void invoke(Tello tello) {
            AppSettings appSettings = AppSettings.getInstance();

            int bitrate = appSettings.getBitrate() + 1;
            if (bitrate > 5) {
                return;
            }

            appSettings.setBitrate(bitrate);
        }
    }, DECREASE_BITRATE {
        @Override
        public void invoke(Tello tello) {
            AppSettings appSettings = AppSettings.getInstance();

            int bitrate = appSettings.getBitrate() - 1;
            if (bitrate < 0) {
                return;
            }

            appSettings.setBitrate(bitrate);
        }
    }, CHANGE_SPEED {
        @Override
        public void invoke(Tello tello) {
            Button button = MainActivity.getActivity().findViewById(R.id.slowFastButton);

            if (!tello.getDroneAxis().isFastMode()) {
                button.setText(R.string.fast_letter);
                tello.getDroneAxis().setFastMode(true);
            } else {
                button.setText(R.string.slow_letter);
                tello.getDroneAxis().setFastMode(false);
            }
        }
    }, CHANGE_PHOTO_VIDEO {
        @Override
        public void invoke(Tello tello) {
            View view = MainActivity.getActivity().findViewById(R.id.changePhotoVideoButton);

            if (view.getRotation() % 360 == 0) {
                view.animate().rotationBy(-360).setDuration(500);
            }

            if (tello.getVideoInfo().getVideoMode().equals(VideoMode.PHOTO)) {
                tello.getVideoInfo().setVideoMode(VideoMode.VIDEO);
                MainActivity.getActivity().findViewById(R.id.takePicRecordButton).setBackgroundResource(R.drawable.video_btn);
            } else {
                tello.getVideoInfo().setVideoMode(VideoMode.PHOTO);
                MainActivity.getActivity().findViewById(R.id.takePicRecordButton).setBackgroundResource(R.drawable.photo_btn);
            }
        }
    }, STOP_ALL {
        @Override
        public void invoke(Tello tello) {
            if (tello.getVideoInfo().isSmartVideoRunning()) {
                SmartVideoMode smartVideoMode = tello.getVideoInfo().getSmartVideoMode();
                tello.getVideoInfo().toggleSmartVideo(smartVideoMode, false);
            }

            tello.bounceMode(false);

            View flipsView = MainActivity.getActivity().findViewById(R.id.flipsView);
            flipsView.setVisibility(View.INVISIBLE);
        }
    }, MODE_UP_AND_AWAY {
        @Override
        public void invoke(Tello tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.UP_AND_OUT);
        }
    }, MODE_CIRCLE {
        @Override
        public void invoke(Tello tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.CIRCLE);
        }
    }, MODE_VIDEO_360 {
        @Override
        public void invoke(Tello tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.VIDEO_360);
        }
    }, MODE_BOUNCE {
        @Override
        public void invoke(Tello tello) {
            STOP_ALL.invoke(tello);

            tello.bounceMode(true);
            MainActivity.getActivity().setFlightModeRunning(true);
        }
    }, MODE_8D_FLIPS {
        @Override
        public void invoke(Tello tello) {
            MainActivity mainActivity = MainActivity.getActivity();
            View flipsView = mainActivity.findViewById(R.id.flipsView);

            if (flipsView.getVisibility() == View.VISIBLE) {
                flipsView.setVisibility(View.INVISIBLE);
                MainActivity.getActivity().setFlightModeRunning(false);
            } else {
                flipsView.setVisibility(View.VISIBLE);
                MainActivity.getActivity().setFlightModeRunning(true);
            }
        }
    }, FLIP_FORWARD {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.FORWARD);
        }
    }, FLIP_FORWARD_LEFT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.FORWARD_LEFT);
        }
    }, FLIP_FORWARD_RIGHT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.FORWARD_RIGHT);
        }
    }, FLIP_LEFT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.LEFT);
        }
    }, FLIP_RIGHT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.RIGHT);
        }
    }, FLIP_BACKWARD {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.BACKWARD);
        }
    }, FLIP_BACKWARD_LEFT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.BACKWARD_LEFT);
        }
    }, FLIP_BACKWARD_RIGHT {
        @Override
        public void invoke(Tello tello) {
            handleFlip(tello, FlipDirection.BACKWARD_RIGHT);
        }
    };

/*    public static TelloAction fromName(String name) {
        for (TelloAction telloAction : TelloAction.values()) {
            if (telloAction.getName().equals(name)) {
                return telloAction;
            }
        }
        return null;
    }*/

    public static void handleFlip(Tello tello, FlipDirection direction) {
        if (tello.getDroneState().getBatteryPercentage() > 50) {
            tello.flip(direction);
        }

    }

    public String getSettingsDescription() {
        return Utils.getStr("settings_controller_" + getName());
    }

    public void handleOriginalFlightMode(Tello tello, SmartVideoMode smartVideoMode) {
        boolean stop = tello.getVideoInfo().isSmartVideoRunning() && tello.getVideoInfo().getSmartVideoMode().equals(smartVideoMode);

        STOP_ALL.invoke(tello);

        if (!stop) {
            tello.getVideoInfo().toggleSmartVideo(smartVideoMode, true);
        }
    }

    public String getName() {
        return toString().toLowerCase();
    }

    public abstract void invoke(Tello tello);
}
