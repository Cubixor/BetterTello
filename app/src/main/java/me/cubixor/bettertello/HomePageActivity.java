package me.cubixor.bettertello;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sanojpunchihewa.glowbutton.GlowButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.Flow;
import me.cubixor.bettertello.api.MenuAdapter;
import me.cubixor.bettertello.api.SpinnerBackgroundCreator;
import me.cubixor.bettertello.bar.BarStateManager;
import me.cubixor.bettertello.databinding.ActivityHomePageBinding;
import me.cubixor.bettertello.utils.Utils;

public class HomePageActivity extends AppCompatActivity {

    private final List<TextView> stateBars = new ArrayList<>();
    private boolean statesExpanded = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.slide_in_home, R.anim.slide_out_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Utils.fullScreen(this);

        Activities.updateHomePageActivity(this);
        BarStateManager barStateManager = BarStateManager.getInstance();
        barStateManager.setupHomeActivity(this);

        Map<String, Integer> states = new LinkedHashMap<String, Integer>() {{
            put(getString(R.string.home_home), R.drawable.ic_round_home_48);
            put(getString(R.string.home_sensors), R.drawable.ic_round_sensors_24);
            put(getString(R.string.home_battery), R.drawable.ic_round_battery_std_24);
            put(getString(R.string.home_motors), R.drawable.ic_round_electric_bolt_24);
            put(getString(R.string.home_vgps), R.drawable.ic_round_location_on_24);
            put(getString(R.string.home_info), R.drawable.ic_round_info_24);
        }};

        final Spinner spinner = findViewById(R.id.spinner);

        SpinnerBackgroundCreator spinnerBackgroundCreator = new SpinnerBackgroundCreator();
        spinner.setPopupBackgroundDrawable(spinnerBackgroundCreator.createBackground(this, states.size()));

        // Our custom Adapter class that we created
        MenuAdapter adapter = new MenuAdapter(getApplicationContext(), states);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("selected: " + position + "   id: " + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void onImageClickEvent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_main, R.anim.slide_out_home);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onStateClick(View view) {
        System.out.println("CLICK");
    }

    public void onStatesExpandClick(View view) {
        BarStateManager barStateManager = BarStateManager.getInstance();

        if (view.getRotation() == 0) {
            barStateManager.expandStates(this, (GlowButton) view);
        } else if (view.getRotation() == -180) {
            barStateManager.shrinkStates(this, (GlowButton) view);
        }
    }

    public boolean areStatesExpanded() {
        return statesExpanded;
    }

    public void setStatesExpanded(boolean statesExpanded) {
        this.statesExpanded = statesExpanded;
    }

    public List<TextView> getStateBars() {
        return stateBars;
    }
}