package me.cubixor.bettertello.data

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.cubixor.bettertello.App
import me.cubixor.bettertello.controller.Controller
import java.io.Serializable
import java.util.*

class AppSettings : Serializable {
    private val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    var exposure: Int
        get() = sharedPref.getInt("exposure", 0)
        set(exposure) {
            editor.putInt("exposure", exposure)
            editor.apply()
        }
    var bitrate: Int
        get() = sharedPref.getInt("bitrate", 0)
        set(bitrate) {
            editor.putInt("bitrate", bitrate)
            editor.apply()
        }
    var quality: Boolean
        get() = sharedPref.getBoolean("quality", false)
        set(quality) {
            editor.putBoolean("quality", quality)
            editor.apply()
        }
    var iFrameInterval: Float
        get() = sharedPref.getFloat("iFrameInterval", 2f)
        set(iFrameInterval) {
            editor.putFloat("iFrameInterval", iFrameInterval)
            editor.apply()
        }
    var isFastMode: Boolean
        get() = sharedPref.getBoolean("fastMode", false)
        set(fastMode) {
            editor.putBoolean("fastMode", fastMode)
            editor.apply()
        }
    var isPhotoMode: Boolean
        get() = sharedPref.getBoolean("photoMode", true)
        set(photoMode) {
            editor.putBoolean("photoMode", photoMode)
            editor.apply()
        }

    fun loadControllers(): List<Controller> {
        val gson = Gson()
        val type = object : TypeToken<LinkedList<Controller?>?>() {}.type
        val controllersString = sharedPref.getString("controllers", "")
        return if (controllersString == "") {
            LinkedList()
        } else {
            LinkedList(
                gson.fromJson(
                    controllersString, type
                )
            )
        }
    }

    fun saveControllers(controllers: List<Controller?>?) {
        val gson = Gson()
        val controllersString = gson.toJson(controllers)
        editor.putString("controllers", controllersString)
        editor.commit()
    }
}