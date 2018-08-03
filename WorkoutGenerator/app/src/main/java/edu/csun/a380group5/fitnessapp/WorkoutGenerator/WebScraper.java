package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.util.Log;
import java.io.IOException;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper  {
    private HashSet<String> links;

    public WebScraper() {
        links = new HashSet<>();
    }

    public HashSet<String> getListLinks() {
        return links;
    }

    public void getPageLinks(String url, String searchURL) {

        if (!links.contains(url)) {
            try {
                Document doc = Jsoup.connect(url).get(); //connect to url
                Elements allLinks = doc.select("a[href^=\"" + searchURL + "\"]"); //collect all links with this address

                for (Element page : allLinks) { //add links to the hashset
                    if (links.add(page.attr("abs:href"))) {
                    }
                }

            } catch (IOException e) {
                Log.e("shit",e.getMessage());
            }
        }
    }

    public Document getPageSource(String url) {
        Document doc = new Document(url);
        try {
            doc = Jsoup.connect(url).get();
        } catch(IOException e) {
            Log.e("Dmmit", e.getMessage());
        }
        return doc;
    }
}