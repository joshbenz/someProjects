package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExerciseStruct implements Parcelable {
    private String name;
    private String url;
    private String dataString;
    private String id; //for now will be the data

    public ExerciseStruct() {
        name         = new String();
        url          = new String();
        dataString   = new String();
        id           = new String();
    }

    public ExerciseStruct(String name, String url, String dataString, String id) {
        this.name       = name;
        this.url        = url;
        this.dataString = dataString;
        this.id         = id;
    }

    public ExerciseStruct(String name, String url, String dataString) {
        this.name       = name;
        this.url        = url;
        this.dataString = dataString;
        id              = UUID.randomUUID().toString();;

    }

    public String getName()             { return name;      }
    public String getUrl()              { return url;       }
    public String getDataString()       { return dataString;}
    public String getId()               { return id;        }
    public void setName(String name)    { this.name = name; }
    public void setUrl(String url)      { this.url = url;   }
    public void setId(String id)        { this.id = id;     }
    public void setDataString(String dataString) { this.dataString = dataString; }

    @Override
    public String toString() { return "" + id + ""+ name + ";" + url + ";" + dataString + "\n"; }

    public List<String> dataStringToArray(String s) {
        String[] arr = s.split(":");
        List<String> list = new ArrayList<String>(arr.length);
        for (String str : arr) {
            list.add(str);
        }
        return list;
    }

    public List<String> dataStringToArray() {
        String[] arr = dataString.split(":");
        List<String> list = new ArrayList<String>(arr.length);
        for (String str : arr) {
            list.add(str);
        }
        return list;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(dataString);
        dest.writeString(id);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ExerciseStruct createFromParcel(Parcel in) {
            return new ExerciseStruct(in);
        }
        public ExerciseStruct[] newArray(int size) {
            return new ExerciseStruct[size];
        }
    };

    public ExerciseStruct(Parcel in) {
        name        = in.readString();
        url         = in.readString();
        dataString  = in.readString();
        id          = in.readString();
    }
}