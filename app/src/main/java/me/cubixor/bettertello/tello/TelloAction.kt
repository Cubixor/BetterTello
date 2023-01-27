package me.cubixor.bettertello.tello

import me.cubixor.bettertello.App
import me.cubixor.bettertello.BuildConfig
import me.cubixor.telloapi.api.FlipDirection
import me.cubixor.telloapi.api.Tello
import me.cubixor.telloapi.api.video.SmartVideoMode

enum class TelloAction {
    TAKEOFF_LAND {
        override operator fun invoke(tello: Tello) {
            if (!tello.droneState.iseMSky()) {
                tello.takeOff()
            } else {
                tello.land(false)
            }
        }
    },
    THROW_TAKEOFF_HAND_LAND {
        override operator fun invoke(tello: Tello) {
            if (!tello.droneState.iseMSky()) {
                tello.throwTakeOff()
            } else {
                tello.palmLand()
            }
        }
    },
    START_MOTORS {
        override operator fun invoke(tello: Tello) {
            tello.startMotors()
        }
    },
    INCREASE_EXPOSURE {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.updateExposure(appSettings.exposure.value!! + 1)
        }
    },
    DECREASE_EXPOSURE {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.updateExposure(appSettings.exposure.value!! - 1)
        }
    },
    INCREASE_BITRATE {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.updateBitrate(appSettings.bitrate.value!! + 1)
        }
    },
    DECREASE_BITRATE {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.updateBitrate(appSettings.bitrate.value!! - 1)
        }
    },
    CHANGE_SPEED {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.switchFastMode()
        }
    },
    CHANGE_PHOTO_VIDEO {
        override operator fun invoke(tello: Tello) {
            val appSettings = App.getInstance().appSettingsRepository
            appSettings.switchPhotoMode()
        }
    },
    TAKE_PHOTO {
        override operator fun invoke(tello: Tello) {
            tello.videoInfo.takePicture()
        }
    },
    STOP_ALL {
        override operator fun invoke(tello: Tello) {
            if (tello.videoInfo.isSmartVideoRunning) {
                val smartVideoMode = tello.videoInfo.smartVideoMode
                tello.videoInfo.toggleSmartVideo(smartVideoMode, false)
            }
            tello.bounceMode(false)
            App.getInstance().telloStateManager.setSmartModeRunning(false)
            App.getInstance().telloStateManager.setFlipsMode(false)
        }
    },
    MODE_UP_AND_AWAY {
        override operator fun invoke(tello: Tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.UP_AND_OUT)
        }
    },
    MODE_CIRCLE {
        override operator fun invoke(tello: Tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.CIRCLE)
        }
    },
    MODE_VIDEO_360 {
        override operator fun invoke(tello: Tello) {
            handleOriginalFlightMode(tello, SmartVideoMode.VIDEO_360)
        }
    },
    MODE_BOUNCE {
        override operator fun invoke(tello: Tello) {
            STOP_ALL.invoke(tello)
            tello.bounceMode(true)
            App.getInstance().telloStateManager.setSmartModeRunning(true)
        }
    },
    MODE_8D_FLIPS {
        override operator fun invoke(tello: Tello) {
            val stateManager = App.getInstance().telloStateManager
            stateManager.switchFlipsMode()
        }
    },
    FLIP_FORWARD {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.FORWARD)
        }
    },
    FLIP_FORWARD_LEFT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.FORWARD_LEFT)
        }
    },
    FLIP_FORWARD_RIGHT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.FORWARD_RIGHT)
        }
    },
    FLIP_LEFT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.LEFT)
        }
    },
    FLIP_RIGHT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.RIGHT)
        }
    },
    FLIP_BACKWARD {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.BACKWARD)
        }
    },
    FLIP_BACKWARD_LEFT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.BACKWARD_LEFT)
        }
    },
    FLIP_BACKWARD_RIGHT {
        override operator fun invoke(tello: Tello) {
            handleFlip(tello, FlipDirection.BACKWARD_RIGHT)
        }
    };

    val settingsDescription: String
        get() {
            val resources = App.getInstance().res
            val resName = "settings_controller_" + name.lowercase()
            val id = resources.getIdentifier(resName, "string", BuildConfig.APPLICATION_ID)
            return resources.getString(id)
        }

    fun handleOriginalFlightMode(tello: Tello, smartVideoMode: SmartVideoMode) {
        val stop = tello.videoInfo.isSmartVideoRunning && tello.videoInfo.smartVideoMode == smartVideoMode
        STOP_ALL.invoke(tello)
        if (!stop) {
            tello.videoInfo.toggleSmartVideo(smartVideoMode, true)
        }
    }

    abstract operator fun invoke(tello: Tello)

    companion object {
        fun handleFlip(tello: Tello, direction: FlipDirection?) {
            if (tello.droneState.batteryPercentage > 50) {
                tello.flip(direction)
            }
        }
    }
}