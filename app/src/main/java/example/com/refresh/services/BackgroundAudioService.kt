package example.com.refresh.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

import example.com.refresh.fragments.SongPlayingFragment

class BackgroundAudioService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        val mPhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                // Test for incoming call, dialing call, active or on hold
                if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK ) {
                    try {
                        if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                            (SongPlayingFragment.Statified.mediaPlayer as MediaPlayer).pause()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }
        val mTelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mTelephonyMgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }
}
