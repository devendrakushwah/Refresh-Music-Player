package example.com.refresh.activties

import example.com.refresh.R
import example.com.refresh.fragments.SongPlayingFragment.Statified.mediaPlayer
import android.content.Intent
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.bullhead.equalizer.EqualizerFragment;

class EqualizeActivity : AppCompatActivity() {

    var equalizerFragment : EqualizerFragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalize)
        try{
        val sessionId = mediaPlayer?.getAudioSessionId();
        equalizerFragment=EqualizerFragment.newBuilder()
                .setAccentColor(Color.parseColor("#6aefae"))
                .setAudioSessionId(sessionId as Int)
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eqFrame, equalizerFragment)
                .commit();
        mediaPlayer?.setLooping(true);}
        catch (e:Exception)
        {
            var main = Intent(this@EqualizeActivity,MainActivity::class.java)
            startActivity(main)
            Toast.makeText(this,"You must play a song first!",Toast.LENGTH_SHORT).show()
        }
    }
}
