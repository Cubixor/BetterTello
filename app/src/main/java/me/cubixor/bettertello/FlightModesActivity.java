package me.cubixor.bettertello;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.utils.Utils;
import me.cubixor.telloapi.api.Tello;

public class FlightModesActivity extends AppCompatActivity {

    private Tello tello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_flight_modes);

        Utils.fullScreen(this);

        tello = MainActivity.getActivity().getTello();
    }


    public void onBackClick(View view) {
        finish();
    }

    public void onFlightModeClick(View view) {
        TelloAction telloAction = getActionByViewID(view);

        telloAction.invoke(tello);

        finish();
    }

    private TelloAction getActionByViewID(View view) {



        switch (view.getId()) {
            case (R.id.flightModeUpAndAway):
                return TelloAction.MODE_UP_AND_AWAY;
            case (R.id.flightModeCircle):
                return TelloAction.MODE_CIRCLE;
            case (R.id.flightMode360Video):
                return TelloAction.MODE_VIDEO_360;
            case (R.id.flightModeBounce):
                return TelloAction.MODE_BOUNCE;
            case (R.id.flightMode8DFlips):
                return TelloAction.MODE_8D_FLIPS;
        }

        return null;
    }

}