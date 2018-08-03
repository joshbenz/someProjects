package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.csun.a380group5.fitnessapp.LoadSaveUtils;
import edu.csun.a380group5.fitnessapp.MainActivity;
import edu.csun.a380group5.fitnessapp.R;

public class ExerciseListActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ListView listView;
    private List<ExerciseStruct> exerciseStructList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Button button_home = (Button) findViewById(R.id.button_home);
        button_home.setOnClickListener(this);

        Button button_save = (Button) findViewById(R.id.button_workout_save);
        button_save.setOnClickListener(this);

        toolbar  = (Toolbar)  findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview_exercises);

        final ArrayList<ExerciseStruct> exerciseStructs = this.getIntent().getParcelableArrayListExtra("data.content");
        int viewType = this.getIntent().getIntExtra("TYPE", -1);
        exerciseStructList = exerciseStructs;

        if(viewType == 1) { button_save.setVisibility(View.INVISIBLE); }

        List<String> namesList = new ArrayList<String>(exerciseStructs.size());
        for(ExerciseStruct e : exerciseStructs) { namesList.add(e.getName()); }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExerciseListActivity.this, android.R.layout.simple_list_item_1, namesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseDataActivity.class);
                intent.putExtra("exerciseData", exerciseStructs.get(position));
                startActivity(intent);
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
            case R.id.button_workout_save:
                final AlertDialog.Builder alertSave = new AlertDialog.Builder(this);
                final EditText editText = new EditText(this);

                alertSave.setTitle("Save");
                alertSave.setMessage("Save Workout As");
                alertSave.setView(editText);

                alertSave.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = editText.getText().toString();
                        if(userText.isEmpty()) { userText = "Untitled Workout"; }
                        exerciseStructList.get(0).setId(userText);

                        try {
                            LoadSaveUtils.appendToFile(exerciseStructList, "Workouts.json", getApplicationContext());
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Workout Saved!", Toast.LENGTH_SHORT).show();

                    }
                });

                alertSave.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertSave.show();

            default:
                break;
        }
    }
}
