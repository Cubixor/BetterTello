package me.cubixor.bettertello

import android.view.InputEvent
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cubixor.bettertello.controller.Controller
import me.cubixor.bettertello.controller.ControllerManager
import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.bettertello.utils.KeyCodes.keyCodes
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FragmentSettingsControllerViewModel @Inject constructor(
    private val controllerManager: ControllerManager,
    private val appSettings: AppSettingsRepository
) : ViewModel() {


    private val _buttonText = MutableLiveData(EnumMap<TelloAction, String>(TelloAction::class.java))
    val buttonText: LiveData<EnumMap<TelloAction, String>> = _buttonText
    private val _currentButton = MutableLiveData<TelloAction?>()
    val currentButton: LiveData<TelloAction?> = _currentButton
    private val _selectedController = MutableLiveData(controllerManager.getFirstController())
    val selectedController: LiveData<Controller> = _selectedController


    private fun setMapping(telloAction: TelloAction, keyCode: Int) {
        updateButtonText(telloAction, keyCodes[keyCode])
        appSettings.addControllerMapping(selectedController.value!!, telloAction, keyCode)
    }

    private fun clearMapping(telloAction: TelloAction) {
        updateButtonText(telloAction, "")
        appSettings.removeControllerMapping(selectedController.value!!, telloAction)
    }

    fun resetMappings() {
        appSettings.resetMappings(selectedController.value!!)
        _currentButton.value = null
        updateButtonsTexts()
    }

    private fun updateButtonText(telloAction: TelloAction?, key: String) {
        val map = EnumMap(buttonText.value!!)
        map.replace(telloAction, key)
        _buttonText.value = map
    }

    private fun updateButtonsTexts() {
        val controller = selectedController.value
        val map = EnumMap<TelloAction, String>(TelloAction::class.java)
        for (telloAction in TelloAction.values()) {
            val key = controller!!.getKeyByAction(telloAction)
            var text = ""
            if (key != -1) {
                text = keyCodes[key]
            }
            map[telloAction] = text
        }
        _buttonText.value = map
    }

    fun selectController(pos: Int) {
        _selectedController.value = appSettings.controllers[pos]
        updateButtonsTexts()
    }

    fun changeCurrentButton(telloAction: TelloAction) {
        val currentButtonValue = currentButton.value

        //Check if user clicked already selected this button
        if (currentButtonValue != null && currentButtonValue == telloAction) {

            //If true, remove that mapping
            clearMapping(telloAction)
            _currentButton.value = null
        } else {
            _currentButton.value = telloAction
        }
    }

    val controllersSpinner: Map<String, Int>
        get() {
            val controllers: MutableMap<String, Int> = LinkedHashMap()
            for (controller in appSettings.controllers) {
                controllers[controller.name] = R.drawable.ic_round_sports_esports_24
            }
            return controllers
        }

    fun onGenericMotion(event: MotionEvent): Boolean {
        return controllerManager.onGenericMotionEvent(event, ::processInputEvent)
    }

    fun onKey(keyCode: Int, event: KeyEvent): Boolean {
        return controllerManager.onKeyDown(keyCode, event, ::processInputEvent)
    }

    private fun processInputEvent(keyCode: Int, event: InputEvent): Boolean {
        val telloAction = currentButton.value ?: return true

        //Check if any button is selected
        val controller = controllerManager.getControllerByID(event.device.descriptor)

        //Check if selected controller has been used
        if (controller == null || controller != selectedController.value) return true


        //Check if the key was already assigned to something and delete previous mapping
        if (controller.mappings.containsKey(keyCode)) {
            clearMapping(controller.mappings[keyCode]!!)
        }
        setMapping(telloAction, keyCode)
        _currentButton.value = null
        return true
    }
}