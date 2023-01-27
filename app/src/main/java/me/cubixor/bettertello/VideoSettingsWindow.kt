package me.cubixor.bettertello

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import me.cubixor.bettertello.databinding.VideoSettingsBinding

class VideoSettingsWindow : DialogFragment() {
    private val viewModel: VideoSettingsViewModel by viewModels()
    private lateinit var binding: VideoSettingsBinding
    //private lateinit var blurThread: Thread

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        binding = VideoSettingsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)


        binding.exposureBar.setOnSeekBarChangeListener(onExposureChange())
        binding.bitrateBar.setOnSeekBarChangeListener(onBitrateChange())
        binding.qualitySwitch.setOnCheckedChangeListener(onQualityChangeListener())

        val dialog = builder.create()
        val window = dialog.window!!
        window.setBackgroundDrawableResource(R.drawable.video_settings_bg)
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //Setup blur
/*
        val blurView = Utils.setupBlur(requireActivity(), binding.videoSettingsBlur, 1f)
        blurThread = Thread {
            while (true) {
                blurView.invalidate()
            }
        }
        blurThread.start()
*/

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        try {
            val f = binding.iframeTextBox.text.toString().toFloat()
            viewModel.updateIFrameInterval(f)
            //blurThread.interrupt()
        } catch (ignored: NumberFormatException) {
        }
    }

    private fun onExposureChange(): OnSeekBarChangeListener {
        return object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.updateExposure(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    private fun onBitrateChange(): OnSeekBarChangeListener {
        return object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.updateBitrate(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
    }

    private fun onQualityChangeListener(): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean -> viewModel.updateQuality(isChecked) }
    }
}