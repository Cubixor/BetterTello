package me.cubixor.bettertello

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.cubixor.bettertello.databinding.FragmentSettingsDroneBinding

@AndroidEntryPoint
class FragmentSettingsDrone : Fragment() {

    private lateinit var binding: FragmentSettingsDroneBinding
    private val viewModel: FragmentSettingsDroneViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsDroneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        view.isFocusableInTouchMode = true
        view.requestFocus()

        binding.lowBatteryThreshold.seekBar.setOnSeekBarChangeListener(onSeekBarChange(viewModel::updateLowBatteryThreshold, 10))
        binding.attitudeLimit.seekBar.setOnSeekBarChangeListener(onSeekBarChange(viewModel::updateAttitudeLimit, 10))
        binding.heightLimit.seekBar.setOnSeekBarChangeListener(onSeekBarChange(viewModel::updateHeightLimit, 2))
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateWiFiSSID(binding.wifiSSID.editText.text.toString())
        viewModel.updateWifiPassword(binding.wifiSSID.editText.text.toString())
    }

    private fun onSeekBarChange(f: (Int) -> Unit, add: Int = 0): OnSeekBarChangeListener {
        return object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) f.invoke(progress + add)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

        }
    }

}