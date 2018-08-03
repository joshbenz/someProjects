package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.csun.a380group5.fitnessapp.LoadSaveUtils;
import edu.csun.a380group5.fitnessapp.MainActivity;
import edu.csun.a380group5.fitnessapp.R;

public class LoadedExercisesActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ListView listView;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_exercises);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) { filename = (bundle.getString("filename")); }
        final List<List<ExerciseStruct>> LoadedExerciseLists = LoadSaveUtils.loadFromFile(filename, getApplicationContext());

        final List<String> idList = new ArrayList<String>();

        for(List<ExerciseStruct> a : LoadedExerciseLists) {
            idList.add(a.get(0).getId());
        }

        toolbar  = (Toolbar)  findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview_loaded_exercises);

        Button button_home = (Button) findViewById(R.id.button_home);
        button_home.setOnClickListener(this);

         final ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoadedExercisesActivity.this, android.R.layout.simple_list_item_1, idList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<ExerciseStruct> data = LoadedExerciseLists.get(position);
                Intent intent = new Intent(LoadedExercisesActivity.this, ExerciseListActivity.class);
                intent.putParcelableArrayListExtra("data.content", (ArrayList<? extends Parcelable>) data);
                intent.putExtra("TYPE", 1); //use this to notify ExerciseListActivity to hide save button
                startActivity(intent);
            }
        });

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {

                AlertDialog.Builder alertDelete = new AlertDialog.Builder(LoadedExercisesActivity.this);
                alertDelete.setTitle("Delete");
                alertDelete.setMessage("Delete Workout?");

                alertDelete.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadedExerciseLists.remove(position);
                        idList.remove(position);
                        //((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        try {
                            LoadSaveUtils.updateToFile(LoadedExerciseLists, "Workouts.json", getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(LoadedExercisesActivity.this, "Workout Removed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                alertDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDelete.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_home:
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }
}
