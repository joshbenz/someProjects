package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
   * Notes from Android Reference: "AsyncTask enables proper and easy use of the UI thread.
   * This class allows you to perform background operations and publish results on the UI thread
   * without having to manipulate threads and/or handlers."
   *
   * Must be subclassed: AsyncTask Parameters:
   * "1. Params, the type of the parameters sent to the task upon execution.
   *  2. Progress, the type of the progress units published during the background computation.
   *  3. Result, the type of the result of the background computation."
   *
   * Rules for using AsyncTask:
   * "The AsyncTask class must be loaded on the UI thread. This is done automatically as of JELLY_BEAN.
   *  The task instance must be created on the UI thread.
   *  execute(Params...) must be invoked on the UI thread.
   *  Do not call onPreExecute(), onPostExecute(Result), doInBackground(Params...), onProgressUpdate(Progress...) manually.
   *  The task can be executed only once (an exception will be thrown if a second execution is attempted.)"
   */

public class WorkoutGenerator extends AsyncTask<String, Integer, List<ExerciseStruct>>  {
    private final String baseURL = "https://www.bodybuilding.com/exercises/finder/lookup/filter/muscle/id/";
    private String searchURL = "https://www.bodybuilding.com/exercises/detail/view/name/";
    private ProgressDialog dialog;
    private String params;
    private Context context;
    String[] messages = {"Spinning the Hamster...", "Shoveling coal into the server", "Programming the Flux Capacitor",
            "go ahead -- hold your breath", "follow the white rabbit", "Testing data on Timmy... We're going to need another Timmy",
            "HELP!, I'm being held hostage, and forced to write the stupid lines!", "The gods contemplate your fate...",
            "Adjusting data for your IQ...", "Just stalling to simulate activity...", "Caching internet locally...",
            "Let this abomination unto you begin"};


    WorkoutGenerator(String s, Context c) {
        params = s;
        context = c;
        dialog = new ProgressDialog(context);
    }
    @Override
    protected void onPreExecute() {

        dialog.setMessage("Generating...");
        dialog.show();
    }

    protected void onProgressUpdate(Integer... progress) {
        dialog.setMessage(messages[progress[0]]);
    }


    @Override
    protected List<ExerciseStruct> doInBackground(String... str) {
        List<ExerciseStruct> theList = new ArrayList<ExerciseStruct>();
        Random random       = new Random();
        WebScraper scraper  = new WebScraper();
        String exerciseName = new String(), theExerciseDataString = new String();
        int exerciseIndex = 0;

        String[] theParams = params.split(":");

        for(String s : theParams) {
            scraper.getPageLinks(baseURL + s + "/", searchURL);
            HashSet<String> allLinksSet = scraper.getListLinks();
            List<String> allLinksList = new ArrayList<String>(allLinksSet);

            boolean dups = false;
            do {
                dups = hasDuplicates(theList, allLinksList.size());
                if(!dups) { theList.remove(theList.size()-1); }

                exerciseIndex = random.nextInt(allLinksSet.size());
                exerciseName = getExerciseName(allLinksList.get(exerciseIndex));
                theExerciseDataString = getExerciseDataString(allLinksList.get(exerciseIndex));
                publishProgress(random.nextInt(messages.length));

                theList.add(new ExerciseStruct(exerciseName, allLinksList.get(exerciseIndex), theExerciseDataString));
                } while(!dups);

            }
        return theList;
    }


    @Override
    protected  void onPostExecute(List<ExerciseStruct> exerciseStructs) {
        super.onPostExecute(exerciseStructs);
        ArrayList<ExerciseStruct> data = new ArrayList<ExerciseStruct>(exerciseStructs.size());
        for(ExerciseStruct e : exerciseStructs) { data.add(e); }

        Intent intent = new Intent(context, ExerciseListActivity.class);
        intent.putParcelableArrayListExtra("data.content", data);

        dialog.dismiss();
        context.startActivity(intent);
    }

    private boolean hasDuplicates(List<ExerciseStruct> list, int possNumUnique) {
        if(list.size() >= possNumUnique) { return true; } //pigeon hole principle?

        List<String> urlList = new ArrayList<String>(list.size());
        for(ExerciseStruct e : list) { urlList.add(e.getUrl()); }
        Set<String> urlset = new HashSet<String>(urlList); //takes advantage of definition of a Set. Unique elements

       return urlList.size() == urlset.size();
    }

    private String getExerciseDataString(String url) {
        final String urlExType = "https://www.bodybuilding.com/exercises/finder/lookup/filter/exercisetype/";
        final String urlExEquipt = "https://www.bodybuilding.com/exercises/finder/lookup/filter/equipment/"; //these appear to be unique
        final String urlExLevel = "https://www.bodybuilding.com/exercises/finder/lookup/filter/level/";
        Elements linkExType = new Elements(), linkExEquipt = new Elements(), linkExLevel = new Elements();
        List<String> tmpstringsType = new ArrayList<String>();
        List<String> tmpstringsEquipt = new ArrayList<String>();
        List<String> tmpstringsLevel = new ArrayList<String>();
        String[] stringtype, stringequipt, stringlevel;

        WebScraper scraper = new WebScraper();
        Document doc = scraper.getPageSource(url);

        linkExType      = doc.select("a[href^=\"" + urlExType + "\"]");
        linkExEquipt    = doc.select("a[href^=\"" + urlExEquipt + "\"]");
        linkExLevel     = doc.select("a[href^=\"" + urlExLevel + "\"]");

        for (Element link : linkExType)     { tmpstringsType.add(link.attr("abs:href"));    }
        for (Element link : linkExEquipt)   { tmpstringsEquipt.add(link.attr("abs:href"));  }
        for (Element link : linkExLevel)    { tmpstringsLevel.add(link.attr("abs:href"));   }

        stringtype = tmpstringsType.get(0).split("exercisetype/");
        stringequipt = tmpstringsEquipt.get(0).split("equipment/");
        stringlevel = tmpstringsLevel.get(0).split("level/");

        return new String(stringtype[2] + ":" + stringequipt[2] + ":" + stringlevel[2]);
    }

    //Parses a given URL for the name of the exercise at the end
    private String getExerciseName(String s) {
        String result = "";
        String[] tmpArr = s.split("name/");
        String name = tmpArr[1];

        if (name.contains("-")) {
            tmpArr = name.split("-");
            for(int i=0; i<tmpArr.length; i++) {
                String str = tmpArr[i];
                tmpArr[i] = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                result = result + tmpArr[i] + " ";
            }
        } else {
            result = tmpArr[0].substring(0, 1).toUpperCase() + tmpArr[0].substring(1).toLowerCase();
        }
        return result;
    }
}