package me.cubixor.bettertello

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import me.cubixor.bettertello.databinding.ActivitySettingsBinding
import me.cubixor.bettertello.utils.Utils

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.fullScreen(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val navView = binding.navigationView
        setupWithNavController(navView, navController)
    }

    fun onBackClick(v: View) {
        finish()
    }


/*
    private View lastClickedView;
    private Fragment currentSection;


    public void onSectionClick(View view) {

        if (lastClickedView != null) {
            lastClickedView.setBackgroundResource(R.drawable.button_background);
        }
        lastClickedView = view;
        view.setBackgroundResource(R.drawable.button_clicked_background);

        openSection(view.getId());

    }


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



    public Fragment getCurrentSection() {
        return currentSection;
    }
*/
}