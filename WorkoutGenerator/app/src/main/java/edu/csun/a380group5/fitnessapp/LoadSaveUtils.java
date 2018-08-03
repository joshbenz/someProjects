package edu.csun.a380group5.fitnessapp;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.csun.a380group5.fitnessapp.WorkoutGenerator.ExerciseStruct;

public class LoadSaveUtils {


    public static void appendToFile(List<ExerciseStruct> list, String filename, Context context) throws IOException {
        String filepath = context.getFilesDir().getPath().toString() + "/" + filename;
        File file = new File(filepath);

        if(!file.exists()) { file.createNewFile(); }

        try(Writer writer = new FileWriter(file, true)) { //note that the true allows us to append
             Gson gson = new GsonBuilder().create();
            writer.append(gson.toJson(list));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateToFile(List<List<ExerciseStruct>>  list, String filename, Context context) throws IOException {
        String filepath = context.getFilesDir().getPath().toString() + "/" + filename;
        File file = new File(filepath);

        if(!file.exists()) { file.createNewFile(); }

        file.delete();
        file.createNewFile();

        try(Writer writer = new FileWriter(file)) { //note that the true allows us to append
            Gson gson = new GsonBuilder().create();
            for(List<ExerciseStruct> e : list) {
                writer.append(gson.toJson(e));
                writer.flush();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<ExerciseStruct>> loadFromFile(String filename, Context context) {
        List<ExerciseStruct> list;
        List<List<ExerciseStruct>> result = new ArrayList<>();

        String filepath = context.getFilesDir().getPath().toString() + "/" + filename;
        File file = new File(filepath);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Type listType = new TypeToken<ArrayList<ExerciseStruct>>(){}.getType();

        try {
            JsonStreamParser parser = new JsonStreamParser(new FileReader(filepath));
            while(parser.hasNext()) {
               list = new Gson().fromJson(parser.next(), listType);
                result.add(list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        for(List<ExerciseStruct> a : result) {
            Log.e("safadsf", "LISTTTSSS");
            for(ExerciseStruct e : a) {
                Log.e("asdfadsf", e.toString());
            }
        }
*/
        return result;
    }

    public static boolean isFileEmpty(String filename, Context context) {
        String filepath = context.getFilesDir().getPath().toString() + "/" + filename;
        File file = new File(filepath);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<List<ExerciseStruct>> possibleList = loadFromFile(filename, context);
        return (possibleList.isEmpty());
    }
}
