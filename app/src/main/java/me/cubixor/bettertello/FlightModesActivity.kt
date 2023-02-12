package me.cubixor.bettertello

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import me.cubixor.bettertello.databinding.ActivityFlightModesBinding
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.bettertello.tello.TelloManager
import me.cubixor.bettertello.utils.Utils
import me.cubixor.telloapi.api.Tello
import javax.inject.Inject

@AndroidEntryPoint
class FlightModesActivity : AppCompatActivity() {

    @Inject
    lateinit var tello: Tello

    @Inject
    lateinit var telloManager: TelloManager

    private lateinit var binding: ActivityFlightModesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightModesBinding.inflate(layoutInflater)
        binding.activity = this
        setContentView(binding.root)
        Utils.fullScreen(this)

        //tello = (application as App).tello
    }

    fun onBackClick(v: View) {
        finish()
    }

    fun onFlightModeClick(telloAction: TelloAction) {
        telloAction.invoke(telloManager)
        finish()
    }
}