package me.cubixor.bettertello.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Map;

import me.cubixor.bettertello.R;

public class MenuAdapter extends ArrayAdapter<String> {

    Context context;
    Map<String, Integer> statesList;

    public MenuAdapter(@NonNull Context context, Map<String, Integer> states) {
        super(context, R.layout.activity_home_page, new ArrayList<>(states.keySet()));
        this.context = context;
        this.statesList = states;
    }


    // Override these methods and instead return our custom view (with image and text)
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // Function to return our custom View (View with an image and text)
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.simple_spinner_dropdown, parent, false);

        // Image and TextViews
        TextView state = row.findViewById(R.id.text);
        ImageView flag = row.findViewById(R.id.img);

        // Get flag image from drawables folder
        String text = (String) statesList.keySet().toArray()[position];
        Drawable drawable = ContextCompat.getDrawable(context, statesList.get(text));

        //Set state abbreviation and state flag
        state.setText(text);
        flag.setImageDrawable(drawable);

        return row;
    }
}
