package edu.csun.a380group5.fitnessapp.WorkoutGenerator;
 import android.content.Context;
 import android.content.Intent;
 import android.net.ConnectivityManager;
 import android.net.NetworkInfo;
 import android.support.v7.app.AppCompatActivity;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.LinearLayout;
 import android.widget.Spinner;
 import android.widget.Toast;

 import edu.csun.a380group5.fitnessapp.LoadSaveUtils;
 import edu.csun.a380group5.fitnessapp.MainActivity;
 import edu.csun.a380group5.fitnessapp.R;

public class WorkoutGeneratorActivity extends AppCompatActivity implements View.OnClickListener {
     private LinearLayout layout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_workout_generator);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.spinners_linearlayout);
            layout = linearLayout;

            Button buttonGenerate = (Button) findViewById(R.id.button_generate);
            buttonGenerate.setOnClickListener(this); // calling onClick() method

            Button buttonAddExercise = (Button) findViewById(R.id.button_add_exercise);
            buttonAddExercise.setOnClickListener(this);

            Button buttonRemoveExercise = (Button) findViewById(R.id.button_remove);
            buttonRemoveExercise.setOnClickListener(this);

            Button buttonHome = (Button) findViewById(R.id.button_home);
            buttonHome.setOnClickListener(this);

            Button buttonLoadWorkout = (Button) findViewById(R.id.button_load_workout);
            buttonLoadWorkout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button_generate:
                    if(isNetworkAvailable()) {
                        WorkoutGenerator generator = new WorkoutGenerator(spinnerToDataString(), WorkoutGeneratorActivity.this);
                        generator.execute();
                    } else {
                        Toast.makeText(this, "No Network Connection!", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.button_add_exercise:
                    Spinner muscle_list = new Spinner(this);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                            getResources().getStringArray(R.array.muscle_groups));
                    muscle_list.setAdapter(spinnerArrayAdapter);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.addView(muscle_list, params);
                    break;

                case R.id.button_remove: //works as long as only spinners in this linearlayout
                    if(layout.getChildAt(layout.getChildCount()-1) instanceof Spinner) {
                        if(layout.getChildCount()-1 < 1) {
                            Toast.makeText(this, "Need at least ONE exercise!", Toast.LENGTH_SHORT).show();
                        } else {
                            layout.removeView(layout.getChildAt(layout.getChildCount() - 1));
                            layout.invalidate();
                        }
                    }
                    break;

                case R.id.button_home:
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                    break;

                case R.id.button_load_workout:
                    if(LoadSaveUtils.isFileEmpty("Workouts.json", this)) {
                        Toast.makeText(this, "No Saved Workouts!", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(v.getContext(), LoadedExercisesActivity.class);
                        intent.putExtra("filename", "Workouts.json");
                        v.getContext().startActivity(intent);
                    }
                default:
                    break;
            }
        }
    private String spinnerToDataString() {
        String dataString = "";
        Spinner spinner;

        for (int i=0; i<layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof Spinner) {
                spinner = (Spinner) layout.getChildAt(i);
                int spinner_pos = spinner.getSelectedItemPosition();
                String[] muscleValues = getResources().getStringArray(R.array.muscle_group_values);
                dataString = dataString + muscleValues[spinner_pos] + ":";
            }
        }
        return dataString;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}