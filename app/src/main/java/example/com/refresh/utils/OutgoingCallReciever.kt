package example.com.refresh.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import example.com.refresh.activties.MainActivity
import example.com.refresh.fragments.SongPlayingFragment

class OutgoingCallReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TODO("OutgoingCallReciever.onReceive() is not implemented")
        if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            try {
                MainActivity.Staticated.notificationManager?.cancel(1978)

                if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                    (SongPlayingFragment.Statified.mediaPlayer as MediaPlayer).pause()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
