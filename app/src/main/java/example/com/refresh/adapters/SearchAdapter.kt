package example.com.refresh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import example.com.refresh.R
import example.com.refresh.models.Songs

import java.util.ArrayList

class SearchAdapter(context: Context, users: ArrayList<Songs>) : ArrayAdapter<Songs>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val result = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
        }
        val tvTitle = convertView!!.findViewById(R.id.search_title) as TextView
        val tvArtist = convertView!!.findViewById(R.id.search_artist) as TextView
        tvTitle.setText(result!!.songTitle)
        tvArtist.setText(result!!.artist)
        return convertView
    }
}