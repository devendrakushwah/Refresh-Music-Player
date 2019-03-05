package example.com.refresh.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.refresh.R;
import example.com.refresh.utils.LyricsHelper;
public class LyricsActivity extends AppCompatActivity {

    TextView lyricsSong,lyricsArtist,lyricsText,lyricsNotFound;
    LinearLayout lyricsLayout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        lyricsSong = findViewById(R.id.lyricsSong);
        lyricsArtist = findViewById(R.id.lyricsArtist);
        lyricsText = findViewById(R.id.lyricsText);
        lyricsLayout = findViewById(R.id.lyricsLayout);
        lyricsNotFound = findViewById(R.id.lyricsNotFound);

        progressDialog = ProgressDialog.show(this, "", "Loading...");

        Intent i = getIntent();
        String song = i.getStringExtra("song");
        String artist = i.getStringExtra("artist");
        if(artist.contains("unknown")){
            artist=" ";
        }
        LyricsHelper lh = new LyricsHelper();
        String lyrics = lh.getLyrics(song,artist);
        if(!lyrics.equals("")){
            lyricsSong.setText(song);
            lyricsArtist.setText(artist);
            lyricsText.setText(lyrics);
            lyricsLayout.setVisibility(View.VISIBLE);
            lyricsNotFound.setVisibility(View.INVISIBLE);
        }
        progressDialog.dismiss();
    }
}
