package example.com.refresh.fragments

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import example.com.refresh.R
import example.com.refresh.adapters.FavouriteContentAdapter
import example.com.refresh.database.EchoDatabase
import example.com.refresh.fragments.FavouriteFragment.Statified.meediaPlayer
import example.com.refresh.fragments.SongPlayingFragment.Statified.mediaPlayer
import example.com.refresh.models.Songs
import example.com.refresh.utils.CurrentSongHelper

class FavouriteFragment : Fragment() {

    var noFavourites: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var favouriteDatabase: EchoDatabase? = null
    var playPauseButton: ImageButton? = null
    var playPauseHelper = CurrentSongHelper()
    var songTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var _getSongs: ArrayList<Songs>? = null
    var refreshList: ArrayList<Songs>? = null
    var _activity: Activity? = null

    object Statified {
        var meediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _activity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        _activity = activity
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        display_favorites_by_searching()
        bottomBar_setup()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.favouritefragment, container, false)
        activity?.title = "Favorites"
        playPauseHelper.isPlaying = false
        favouriteDatabase = EchoDatabase(activity)
        noFavourites = view?.findViewById(R.id.noFavourites) as TextView
        nowPlayingBottomBar = view.findViewById(R.id.hiddenBar) as RelativeLayout
        songTitle = view.findViewById(R.id.songTitle) as TextView
        playPauseButton = view.findViewById(R.id.playpauseButton) as ImageButton
        (nowPlayingBottomBar as RelativeLayout).isClickable = false
        recyclerView = view.findViewById(R.id.favouriteRecycler) as RecyclerView
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val item = menu?.findItem(R.id.action_sort)
        val item1 = menu?.findItem(R.id.action_search)
        item?.isVisible = false
        item1?.isVisible = false
    }

    override fun onResume() {
        super.onResume()
    }

    fun display_favorites_by_searching() {
        if (favouriteDatabase?.checkSize() as Int > 0) {
            noFavourites?.visibility = View.INVISIBLE
            refreshList = ArrayList<Songs>()
            _getSongs = (favouriteDatabase as EchoDatabase).queryDBforList()
            val fetchList = getListfromStorage()
            if (fetchList != null) {
                for (i in 0..fetchList?.size-1) {
                    for (j in 0.._getSongs?.size as Int-1) {
                        if ((_getSongs as ArrayList<Songs>).get(j).songID === fetchList?.get(i).songID) {
                            (refreshList as ArrayList<Songs>).add((_getSongs as ArrayList<Songs>)[j])
                        } else {
                        }
                    }

                }

            }
            if ((refreshList as ArrayList<Songs>).size === 0) {
                recyclerView?.visibility = View.INVISIBLE
                noFavourites?.visibility = View.VISIBLE
            }

            val _mainScreenAdapter = FavouriteContentAdapter(activity, refreshList as ArrayList<Songs>)
            val mLayoutManager = LinearLayoutManager(activity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = _mainScreenAdapter
            recyclerView?.setHasFixedSize(true)


        } else {
            recyclerView?.visibility = View.INVISIBLE
            noFavourites?.visibility = View.VISIBLE
        }
    }

    fun getListfromStorage(): ArrayList<Songs>? {

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

                do {
                    val currentId = songCursor.getLong(songId)
                    val currentTitle = songCursor.getString(songTitle)
                    val currentArtist = songCursor.getString(songArtist)
                    val fullpath = songCursor.getString(songPathInt)
                    val dateAddedIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
                    arrayList.add(Songs(currentId, currentTitle, currentArtist, fullpath, dateAddedIndex.toLong()))
                } while (songCursor.moveToNext())
            } else {
                return null
            }
            songCursor.close()}
        catch(e:Exception){}
            return arrayList
    }


    fun bottomBar_setup() {
        nowPlayingBottomBar?.isClickable = false
        bottomBarOnClickHandlers()
        try {
            songTitle?.text = SongPlayingFragment.Statified.currentTrackHelper
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songTitle?.text = SongPlayingFragment.Statified.currentTrackHelper
                SongPlayingFragment.Staticated.on_song_complete()
            })
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                playPauseHelper.isPlaying = true
                nowPlayingBottomBar?.visibility = View.VISIBLE
            } else {
                playPauseHelper.isPlaying = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bottomBarOnClickHandlers() {

        nowPlayingBottomBar?.setOnClickListener({
            try {
                val fr = SongPlayingFragment()
                meediaPlayer = SongPlayingFragment.Statified.mediaPlayer
                val _fetch_from_Songs_Fragment = SongPlayingFragment.Statified.fetchSongs
                val bundle_to_be_sent = Bundle()
                bundle_to_be_sent.putString("songTitle", SongPlayingFragment.Statified.currentSongHelper.songTitle)
                bundle_to_be_sent.putString("songArtist", SongPlayingFragment.Statified.currentSongHelper.songArtist)
                bundle_to_be_sent.putInt("songPosition", SongPlayingFragment.Statified.currentSongHelper.currentPosition)
                bundle_to_be_sent.putInt("SongId", (SongPlayingFragment.Statified.currentSongHelper).songId.toInt())
                bundle_to_be_sent.putString("FavBottomBar", "success")
                bundle_to_be_sent.putParcelableArrayList("songsData", _fetch_from_Songs_Fragment)

                fr.arguments = bundle_to_be_sent
                fragmentManager?.beginTransaction()
                        ?.replace(R.id.details_fragment, fr)
                        ?.addToBackStack("SongPlayingFragment")
                        ?.commit()

            } catch (e: Exception) {
                Log.d("Favorite error",e.printStackTrace().toString())
                Toast.makeText(_activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
        playPauseButton?.setOnClickListener({
            if (playPauseHelper.isPlaying as Boolean) {

                SongPlayingFragment.Statified.mediaPlayer?.pause()
                playPauseHelper.TrackPosition = mediaPlayer?.getCurrentPosition() as Int
                playPauseHelper.isPlaying = false
                playPauseButton?.setBackgroundResource(R.drawable.play)
            } else {
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(playPauseHelper.TrackPosition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playPauseHelper.isPlaying = true
                playPauseButton?.setBackgroundResource(R.drawable.pause)
            }
        })

    }

}