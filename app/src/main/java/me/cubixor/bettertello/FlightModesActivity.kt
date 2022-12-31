package me.cubixor.bettertello

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.cubixor.bettertello.databinding.ActivityFlightModesBinding
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.bettertello.utils.Utils
import me.cubixor.telloapi.api.Tello

class FlightModesActivity : AppCompatActivity() {

    private var tello: Tello? = null
    private lateinit var binding: ActivityFlightModesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightModesBinding.inflate(layoutInflater)
        binding.activity = this
        setContentView(binding.root)
        Utils.fullScreen(this)

        tello = (application as App).tello
    }

    fun onBackClick(v: View) {
        finish()
    }

    fun onFlightModeClick(telloAction: TelloAction) {
        telloAction.invoke(tello)
        finish()
    }
}