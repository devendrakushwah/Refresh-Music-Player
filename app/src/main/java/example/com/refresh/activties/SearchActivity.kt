package example.com.refresh.activties

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import example.com.refresh.R
import example.com.refresh.models.Songs
import android.widget.EditText
import android.widget.ListView
import example.com.refresh.adapters.SearchAdapter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.text.Editable
import android.util.Log

var searchBox: EditText? = null
var listView: ListView? = null
var adapter : SearchAdapter ?=null

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchBox = findViewById(R.id.search_box)
        listView = findViewById(R.id.searchListView)
        var searchAdapter:SearchAdapter ?= null
        searchBox?.setInputType(InputType.TYPE_CLASS_TEXT);

        listView?.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long) {
                // TODO Auto-generated method stub



            }
        }
        searchBox?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                update(searchBox?.text.toString())
                adapter?.notifyDataSetChanged()
                listView?.invalidateViews()
                listView?.refreshDrawableState()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    private fun update(key:String){
        var list :  ArrayList<Songs> = final_list(key)
        adapter = SearchAdapter(this,list)
        listView?.adapter=adapter
    }

    private fun final_list(key : String):ArrayList<Songs>{
        var result:ArrayList<Songs> = ArrayList()
        try{
            var old : ArrayList<Songs> = getSongsfromPhone()
            for (song in old){
                //Log.d("Song",song.songTitle)
                if((song.songTitle.contains(key,ignoreCase = true)) || song.artist.contains(key,ignoreCase = true)){
                    result.add(song)
                }
            }
            return result
        }
        catch (e : java.lang.Exception){
            return result
        }
    }

    private fun getSongsfromPhone(): ArrayList<Songs> {
        val arrayList = ArrayList<Songs>()
        val contentResolver = this?.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor = contentResolver?.query(songUri, null, null, null, null)
        try {
            if (songCursor != null && songCursor.moveToFirst()) {
                val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val songPathInt = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                val dateIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
                while (songCursor.moveToNext()) {
                    val currentId = songCursor.getLong(songId)
                    val currentTitle = songCursor.getString(songTitle)
                    val currentArtist = songCursor.getString(songArtist)
                    val fullpath = songCursor.getString(songPathInt)
                    val dateadded = songCursor.getLong(dateIndex)
                    arrayList.add(Songs(currentId, currentTitle, currentArtist, fullpath, dateadded))
                }
               // Log.d("full",arrayList.toString())
            }
        }
        catch(e:Exception){}
        return arrayList
    }

}
