package me.cubixor.bettertello;


import android.view.InputEvent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import me.cubixor.bettertello.controller.Controller;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.utils.KeyCodes;

public class FragmentSettingsControllerViewModel extends ViewModel {

    private final AppSettings appSettings;
    private final MutableLiveData<HashMap<TelloAction, String>> buttonText = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<TelloAction> currentButton = new MutableLiveData<>();
    private final MutableLiveData<Controller> selectedController = new MutableLiveData<>();

    public FragmentSettingsControllerViewModel() {
        appSettings = AppSettings.getInstance();

        selectedController.setValue(ControllerUtils.selectController());
    }


    public LiveData<HashMap<TelloAction, String>> getButtonText() {
        return buttonText;
    }

    public LiveData<TelloAction> getCurrentButton() {
        return currentButton;
    }

    public LiveData<Controller> getSelectedController() {
        return selectedController;
    }


    private void setMapping(TelloAction telloAction , int keyCode){
        updateButtonText(telloAction, KeyCodes.keyCodes[keyCode]);
        appSettings.addControllerMapping(getSelectedController().getValue(), telloAction, keyCode);
    }

    private void clearMapping(TelloAction telloAction) {
        updateButtonText(telloAction, "");
        appSettings.removeControllerMapping(getSelectedController().getValue(), telloAction);
    }

    public void resetMappings() {
        appSettings.resetMappings(getSelectedController().getValue());
        currentButton.setValue(null);
        updateButtonsTexts();
    }

    private void updateButtonText(TelloAction telloAction, String key) {
        HashMap<TelloAction, String> map = new HashMap<>(getButtonText().getValue());
        map.replace(telloAction, key);
        buttonText.setValue(map);
    }

    public void updateButtonsTexts() {
        Controller controller = getSelectedController().getValue();
        HashMap<TelloAction, String> map = new HashMap<>();

        for (TelloAction telloAction : TelloAction.values()) {
            int key = controller.getKeyByAction(telloAction);
            String text = "";
            if (key != -1) {
                text = KeyCodes.keyCodes[key];
            }

            map.put(telloAction, text);
        }

        buttonText.setValue(map);
    }

    public void selectController(int pos) {
        selectedController.setValue(appSettings.getControllers().get(pos));
        updateButtonsTexts();
    }

    public void changeCurrentButton(TelloAction telloAction) {
        TelloAction currentButtonValue = getCurrentButton().getValue();

        //Check if user clicked already selected this button
        if (currentButtonValue != null && currentButtonValue.equals(telloAction)) {

            //If true, remove that mapping
            clearMapping(telloAction);
            currentButton.setValue(null);
        } else {
            currentButton.setValue(telloAction);
        }

    }

    public Map<String, Integer> getControllersSpinner() {
        Map<String, Integer> controllers = new LinkedHashMap<>();

        for (Controller controller : appSettings.getControllers()) {
            controllers.put(controller.getName(), R.drawable.ic_round_sports_esports_24);
        }

        return controllers;
    }


    public boolean processInputEvent(int keyCode, InputEvent event) {
        TelloAction telloAction = getCurrentButton().getValue();

        //Check if any button is selected
        if (telloAction == null) {
            return true;
        }

        Controller controller = ControllerUtils.getControllerByID(event.getDevice().getDescriptor());

        //Check if selected controller has been used
        if (!controller.equals(getSelectedController().getValue())) {
            return true;
        }


        //Check if the key was already assigned to something and delete previous mapping
        if (controller.getMappings().containsKey(keyCode)) {
            clearMapping(controller.getMappings().get(keyCode));
        }

        setMapping(telloAction,keyCode);
        currentButton.setValue(null);

        return true;
    }


}
