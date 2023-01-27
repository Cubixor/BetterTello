package me.cubixor.bettertello

import android.os.Bundle
import android.view.*
import android.view.View.OnGenericMotionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.cubixor.bettertello.api.MenuAdapter
import me.cubixor.bettertello.api.SpinnerBackgroundCreator
import me.cubixor.bettertello.controller.ControllerUtils
import me.cubixor.bettertello.databinding.FragmentSettingsControllerBinding

class FragmentSettingsController : Fragment(), View.OnKeyListener, OnGenericMotionListener {
    private lateinit var binding: FragmentSettingsControllerBinding
    val viewModel: FragmentSettingsControllerViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsControllerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(this)
        view.setOnGenericMotionListener(this)

        updateSpinner(viewModel.selectedController.value!!.inAppID)
    }

    private fun updateSpinner(toSelect: Int) {
        val controllers = viewModel.controllersSpinner
        val spinner = binding.controllerTitle
        val spinnerBackgroundCreator = SpinnerBackgroundCreator()
        spinner.setPopupBackgroundDrawable(spinnerBackgroundCreator.createBackground(requireView().context, controllers.size))
        val adapter = MenuAdapter(requireView().context, controllers)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinner.adapter = adapter
        spinner.setSelection(toSelect)
    }

    override fun onGenericMotion(v: View, event: MotionEvent): Boolean {
        if (ControllerUtils.checkDpadDevice(event)) {
            val keyCode = ControllerUtils.dpadAxisToKey(event)
            if (keyCode != -1) {
                viewModel.processInputEvent(keyCode, event)
                return true
            }
        }

        //TODO Configurable axis
        return ControllerUtils.checkGenericMotionEvent(event)
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        return if (ControllerUtils.checkKeyDownEvent(event)) {
            viewModel.processInputEvent(keyCode, event)
        } else false
    }
}