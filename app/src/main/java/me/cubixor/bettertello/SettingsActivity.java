package me.cubixor.bettertello;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.databinding.ActivitySettingsBinding;
import me.cubixor.bettertello.utils.Utils;

public class SettingsActivity extends AppCompatActivity {

    private View lastClickedView;
    private Fragment currentSection;

    private AppSettings appSettings;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils.fullScreen(this);

        appSettings = AppSettings.getInstance();


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationView navView = binding.navigationView;
        NavigationUI.setupWithNavController(navView, navController);
    }


    public void onBackClick(View view) {
        //finish();
    }

    public void onSectionClick(View view) {
/*
        if (lastClickedView != null) {
            lastClickedView.setBackgroundResource(R.drawable.button_background);
        }
        lastClickedView = view;
        view.setBackgroundResource(R.drawable.button_clicked_background);

        openSection(view.getId());
*/
    }

/*
    public void openSection(int viewID) {
        Fragment newFragment = chooseSection(viewID);
        if (newFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.settingsFragmentView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            currentSection = newFragment;
        }
    }

    private Fragment chooseSection(int viewID) {
        switch (viewID) {
            case R.id.settingsControllerButton: {
                if (appSettings.getControllers().isEmpty()) {
                    return new SettingsControllerEmptyFragment();
                } else {
                    return new FragmentSettingsController();
                }
            }
            case R.id.settingsGeneralButton:
                return new SettingsGeneralFragment();
        }
        return null;
    }
*/


    public Fragment getCurrentSection() {
        return currentSection;
    }

}