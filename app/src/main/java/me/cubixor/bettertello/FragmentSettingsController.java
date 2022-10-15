package me.cubixor.bettertello;

import android.os.Bundle;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.LinkedHashMap;
import java.util.Map;

import me.cubixor.bettertello.api.MenuAdapter;
import me.cubixor.bettertello.api.SpinnerBackgroundCreator;
import me.cubixor.bettertello.controller.Controller;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.databinding.FragmentSettingsControllerBinding;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.utils.KeyCodes;
import me.cubixor.bettertello.utils.Utils;


public class FragmentSettingsController extends Fragment {

    private final AppSettings appSettings;

    public FragmentSettingsController() {
        appSettings = AppSettings.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activities.updateSettingsControllerFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSettingsControllerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_controller, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!appSettings.getControllers().isEmpty()) {
            setControllerSettings(appSettings.getControllers().get(0));
        }

        Controller toSelect = null;
        for (InputDevice inputDevice : ControllerUtils.getConnectedInputDevices()) {
            Controller controller = ControllerUtils.getControllerByID(inputDevice.getDescriptor());
            if (controller != null) {
                toSelect = controller;
                break;
            }
        }
        updateSpinner(toSelect);
    }

    public void updateSpinner(Controller toSelect) {
        Map<String, Integer> controllers = new LinkedHashMap<>();

        for (Controller controller : appSettings.getControllers()) {
            controllers.put(controller.getName(), R.drawable.ic_round_sports_esports_24);
        }

        if (controllers.isEmpty()) {
            return;
        }


        Spinner spinner = getView().findViewById(R.id.controllerTitle);

        SpinnerBackgroundCreator spinnerBackgroundCreator = new SpinnerBackgroundCreator();
        spinner.setPopupBackgroundDrawable(spinnerBackgroundCreator.createBackground(getView().getContext(), controllers.size()));


        MenuAdapter adapter = new MenuAdapter(getView().getContext(), controllers);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setControllerSettings(appSettings.getControllers().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        if (toSelect != null) {
            spinner.setSelection(appSettings.getControllers().indexOf(toSelect));
        }
    }

    public void setControllerSettings(Controller controller) {
        for (TelloAction telloAction : TelloAction.values()) {
            Button btn = getView().findViewById(Utils.getViewByAction(telloAction)).findViewById(R.id.controllerButton);
            btn.setBackgroundResource(R.drawable.button_background);

            int key = controller.getKeyByAction(telloAction);

            if (key == -1) {
                btn.setText("");
                continue;
            }

            btn.setText(KeyCodes.keyCodes[key]);
        }
    }


}