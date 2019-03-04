package example.com.refresh.utils;

import android.os.StrictMode;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LyricsHelper {
    public String getUrlFromGoogle(String song, String artist){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "https://www.google.com/search?&q="+artist+"+"+song+"+lyrics+metrolyrics";
        try {
            Document document = Jsoup.connect(url).get();
            Element elem = document.getElementsByClass("g").first();
            Element data = elem.getElementsByTag("a").first();
            int http=data.toString().indexOf("http");
            int html=data.toString().indexOf("html");
            //Log.d("lyrics",data.toString().substring(http,html+4));
            return data.toString().substring(http,html+4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLyrics(String song, String artist){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String ans="";

        String link=getUrlFromGoogle(song,artist);
        try {
            Document document = Jsoup.connect(link).get();
            Elements elem = document.getElementsByClass("verse");
            for (Element item:elem) {
                //Log.d("lyrics",item.text());
                for (Element j : item.getAllElements()){
                    ans+=j.wholeText();
                }
                ans+="\n";
            }
            return ans;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
