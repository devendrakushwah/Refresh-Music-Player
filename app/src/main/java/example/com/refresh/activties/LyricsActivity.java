package example.com.refresh.activties;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import example.com.refresh.R;

public class LyricsActivity extends AppCompatActivity {

    TextView lyricsSong,lyricsArtist,lyricsText,lyricsNotFound;
    LinearLayout lyricsLayout;
    ProgressDialog progressDialog;
    String song,artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);
        lyricsSong = findViewById(R.id.lyricsSong);
        lyricsArtist = findViewById(R.id.lyricsArtist);
        lyricsText = findViewById(R.id.lyricsText);
        lyricsLayout = findViewById(R.id.lyricsLayout);
        lyricsNotFound = findViewById(R.id.lyricsNotFound);

        lyricsNotFound.setVisibility(View.VISIBLE);

        Intent i = getIntent();
        song = i.getStringExtra("song");
        artist = i.getStringExtra("artist");
        if(artist.contains("unknown")){
            artist=" ";
        }

        new fetcher().execute(song,artist);

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
                        if(songString.equals("") || artistString.equals("")){
                            Toast.makeText(LyricsActivity.this, "Cannot be left blank", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent1 = new Intent(LyricsActivity.this,LyricsActivity.class);
                            intent1.putExtra("song",songString);
                            intent1.putExtra("artist",artistString);
                            startActivity(intent1);
                            finish();
                        }
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

class fetcher extends AsyncTask<String,String,String> {
    private Element elem1,elem2=null;
    private Element data=null;
    String metroLyrics="";
    String ans = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(LyricsActivity.this, "", "Fetching Lyrics...");
    }

    @Override
    protected String doInBackground(String... params) {
        String song = params[0];
        String artist = params[1];
        String url = "https://www.google.com/search?&q="+artist+"+"+song+"+lyrics+metrolyrics";
        try {
            //To scrap google
            Document googlePage = Jsoup.connect(url).get();
            elem1 = googlePage.getElementsByClass("g").first();
            data = elem1.getElementsByTag("a").first();
            int http=data.toString().indexOf("http");
            int html=data.toString().indexOf("html");
            metroLyrics = data.toString().substring(http,html+4);
            //To scrap metrolyrics
            Document document = Jsoup.connect(metroLyrics).get();
            Elements elem = document.getElementsByClass("verse");
            for (Element item:elem) {
                for (Element j : item.getAllElements()) {
                    ans += j.wholeText();
                }
                ans += "\n";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    protected void onPostExecute(String lyrics)
    {
        if(!lyrics.equals("")){
            lyricsSong.setText(song);
            lyricsArtist.setText(artist);
            lyricsText.setText(lyrics);
            lyricsLayout.setVisibility(View.VISIBLE);
            lyricsNotFound.setVisibility(View.INVISIBLE);
        }
        progressDialog.dismiss();
    }
}}