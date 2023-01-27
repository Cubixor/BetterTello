package me.cubixor.bettertello;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import me.cubixor.bettertello.api.MenuAdapter;
import me.cubixor.bettertello.api.SpinnerBackgroundCreator;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.databinding.FragmentSettingsControllerBinding;


public class FragmentSettingsController extends Fragment implements View.OnKeyListener, View.OnGenericMotionListener {

    private FragmentSettingsControllerBinding binding;
    private FragmentSettingsControllerViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FragmentSettingsControllerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_controller, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
        view.setOnGenericMotionListener(this);

        binding.setFragment(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        updateSpinner(viewModel.getSelectedController().getValue().getInAppID());
    }

    public void updateSpinner(int toSelect) {
        Map<String, Integer> controllers = viewModel.getControllersSpinner();

        Spinner spinner = binding.controllerTitle;

        SpinnerBackgroundCreator spinnerBackgroundCreator = new SpinnerBackgroundCreator();
        spinner.setPopupBackgroundDrawable(spinnerBackgroundCreator.createBackground(getView().getContext(), controllers.size()));

        MenuAdapter adapter = new MenuAdapter(getView().getContext(), controllers);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setSelection(toSelect);
    }


    @Override
    public boolean onGenericMotion(View v, MotionEvent event) {
        if (ControllerUtils.checkDpadDevice(event)) {
            int keyCode = ControllerUtils.dpadAxisToKey(event);

            if (keyCode != -1) {
                viewModel.processInputEvent(keyCode, event);

                return true;
            }
        }

        //TODO Configurable axis
        return ControllerUtils.checkGenericMotionEvent(event);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (ControllerUtils.checkKeyDownEvent(event)) {
            return viewModel.processInputEvent(keyCode, event);
        }
        return false;
    }


    public FragmentSettingsControllerViewModel getViewModel() {
        return viewModel;
    }
}