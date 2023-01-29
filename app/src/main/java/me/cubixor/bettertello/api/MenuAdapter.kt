package me.cubixor.bettertello.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import me.cubixor.bettertello.R

class MenuAdapter(context: Context, private val statesList: Map<String, Int>) : ArrayAdapter<String?>(
    context, R.layout.activity_home_page, ArrayList(statesList.keys)
) {
    // Override these methods and instead return our custom view (with image and text)
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    // Function to return our custom View (View with an image and text)
    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.simple_spinner_dropdown, parent, false)

        // Image and TextViews
        val state = row.findViewById<TextView>(R.id.text)
        val flag = row.findViewById<ImageView>(R.id.img)

        // Get flag image from drawables folder
        val text = statesList.keys.toTypedArray()[position]
        val drawable = ContextCompat.getDrawable(context, statesList[text]!!)

        //Set state abbreviation and state flag
        state.text = text
        flag.setImageDrawable(drawable)
        return row
    }
}