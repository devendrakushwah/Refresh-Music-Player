package example.com.refresh.models

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by ADMIN on 6/19/2017.
 */


class Songs(var songID: Long, var songTitle: String, var artist: String, var songData: String?, var dateAdded: Long) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    var msongDateAdded: Long = 0


    init {
        this.msongDateAdded = dateAdded
    }


    object Statified {
        var nameComparator: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val nameOne = song1.songTitle.toUpperCase()
            val nameTwo = song2.songTitle.toUpperCase()
            //ascending order
            nameOne.compareTo(nameTwo)
        }

        var dateComparator: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val dateOne = java.lang.Double.valueOf(song1.msongDateAdded.toDouble())
            val dateTwo = java.lang.Double.valueOf(song2.msongDateAdded.toDouble())

            //recent order order
            dateTwo.compareTo(dateOne)
        }
    }

}