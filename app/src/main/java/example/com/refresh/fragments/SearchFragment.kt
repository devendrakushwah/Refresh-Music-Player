package example.com.refresh.fragments


import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import example.com.refresh.R
import example.com.refresh.adapters.SearchAdapter
import example.com.refresh.models.Songs

class SearchFragment : Fragment() {

    var searchBox: EditText? = null
    var recyclerView: RecyclerView? = null
    var songList : ArrayList<Songs> = ArrayList()
    var adapter : SearchAdapter ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view =inflater.inflate(R.layout.fragment_search, container, false)

        // Inflate the layout for this fragment
        searchBox = view.findViewById(R.id.search_box)
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        searchBox?.setInputType(InputType.TYPE_CLASS_TEXT);

        searchBox?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                update(searchBox?.text.toString())
                adapter?.notifyDataSetChanged()
                recyclerView?.refreshDrawableState()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        adapter = SearchAdapter(songList , activity)
        val mLayoutManager = LinearLayoutManager(activity)
        (recyclerView as RecyclerView).layoutManager = mLayoutManager
        (recyclerView as RecyclerView).itemAnimator = DefaultItemAnimator()
        (recyclerView as RecyclerView).adapter = adapter
        return view
    }

    private fun update(key:String){
        songList =final_list(key)
        var list :  ArrayList<Songs> = final_list(key)
        adapter = SearchAdapter(list,activity)
        recyclerView?.adapter= adapter
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
            songList =result
            return result
        }
        catch (e : java.lang.Exception){
            songList =result
            return result
        }
    }

    private fun getSongsfromPhone(): ArrayList<Songs> {
        val arrayList = ArrayList<Songs>()
        val contentResolver = activity?.contentResolver
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
        songList =arrayList
        return arrayList
    }

}
