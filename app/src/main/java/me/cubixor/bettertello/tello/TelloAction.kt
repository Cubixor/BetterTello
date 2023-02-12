package me.cubixor.bettertello.tello

import me.cubixor.bettertello.App
import me.cubixor.bettertello.BuildConfig
import me.cubixor.telloapi.api.FlipDirection
import me.cubixor.telloapi.api.video.SmartVideoMode

enum class TelloAction {
    TAKEOFF_LAND {
        override operator fun invoke(telloManager: TelloManager) {
            telloManager.takeoffLand()
        }
    },
    THROW_TAKEOFF_HAND_LAND {
        override fun invoke(telloManager: TelloManager) {
            telloManager.throwTakeoffHandLand()
        }
    },
    START_MOTORS {
        override fun invoke(telloManager: TelloManager) {
            telloManager.startMotors()
        }
    },
    INCREASE_EXPOSURE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.increaseExposure()
        }
    },
    DECREASE_EXPOSURE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.decreaseExposure()
        }
    },
    INCREASE_BITRATE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.increaseBitrate()
        }
    },
    DECREASE_BITRATE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.decreaseBitrate()
        }
    },
    CHANGE_SPEED {
        override fun invoke(telloManager: TelloManager) {
            telloManager.changeSpeed()
        }
    },
    CHANGE_PHOTO_VIDEO {
        override fun invoke(telloManager: TelloManager) {
            telloManager.changePhotoVideo()
        }
    },
    TAKE_PHOTO {
        override fun invoke(telloManager: TelloManager) {
            telloManager.takePhoto()
        }
    },
    STOP_ALL {
        override fun invoke(telloManager: TelloManager) {
            telloManager.stopAll()
        }
    },
    MODE_UP_AND_AWAY {
        override fun invoke(telloManager: TelloManager) {
            telloManager.handleOriginalFlightMode(SmartVideoMode.UP_AND_OUT)
        }
    },
    MODE_CIRCLE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.handleOriginalFlightMode(SmartVideoMode.CIRCLE)
        }
    },
    MODE_VIDEO_360 {
        override fun invoke(telloManager: TelloManager) {
            telloManager.handleOriginalFlightMode(SmartVideoMode.VIDEO_360)
        }
    },
    MODE_BOUNCE {
        override fun invoke(telloManager: TelloManager) {
            telloManager.modeBounce()
        }
    },
    MODE_8D_FLIPS {
        override fun invoke(telloManager: TelloManager) {
            telloManager.mode8DFlips()
        }
    },
    FLIP_FORWARD {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.FORWARD)
        }
    },
    FLIP_FORWARD_LEFT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.FORWARD_LEFT)
        }
    },
    FLIP_FORWARD_RIGHT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.FORWARD_RIGHT)
        }
    },
    FLIP_LEFT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.LEFT)
        }
    },
    FLIP_RIGHT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.RIGHT)
        }
    },
    FLIP_BACKWARD {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.BACKWARD)
        }
    },
    FLIP_BACKWARD_LEFT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.BACKWARD_LEFT)
        }
    },
    FLIP_BACKWARD_RIGHT {
        override fun invoke(telloManager: TelloManager) {
            telloManager.flip(FlipDirection.BACKWARD_RIGHT)
        }
    };

    val settingsDescription: String
        get() {
            val resources = App.instance.resources
            val resName = "settings_controller_" + name.lowercase()
            val id = resources.getIdentifier(resName, "string", BuildConfig.APPLICATION_ID)
            return resources.getString(id)
        }

    abstract operator fun invoke(telloManager: TelloManager)
}