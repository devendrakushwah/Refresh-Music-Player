package example.com.refresh.activties;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.search_lyrics_option);
        item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lyrics_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_lyrics_option:
                //Toast.makeText(this,"Search",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setPadding(14,8,14,8);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText song = new EditText(this);
                song.setHint("Song Title");
                final EditText artist = new EditText(this);
                artist.setHint("Song Artist");
                linearLayout.addView(song);
                linearLayout.addView(artist);
                builder.setView(linearLayout);
                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String songString = song.getText().toString();
                        String artistString = artist.getText().toString();
                        Intent intent1 = new Intent(LyricsActivity.this,LyricsActivity.class);
                        intent1.putExtra("song",songString);
                        intent1.putExtra("artist",artistString);
                        startActivity(intent1);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
