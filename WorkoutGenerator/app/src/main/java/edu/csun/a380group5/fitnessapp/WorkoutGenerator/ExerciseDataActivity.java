package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.csun.a380group5.fitnessapp.MainActivity;
import edu.csun.a380group5.fitnessapp.R;

public class ExerciseDataActivity extends AppCompatActivity implements View.OnClickListener {

    private ExerciseStruct exerciseStruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_data);

        Button button_home = (Button) findViewById(R.id.button_home);
        button_home.setOnClickListener(this);

        Button button_webview = (Button) findViewById(R.id.button_webview);
        button_webview.setOnClickListener(this);
        Toolbar toolbar  = (Toolbar)  findViewById(R.id.toolbar);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            exerciseStruct = bundle.getParcelable("exerciseData");
            List<String> exerciseData = exerciseStruct.dataStringToArray();

            TextView name  = (TextView) findViewById(R.id.textView_name);
            name.setText("Exercise Name: " + exerciseStruct.getName());

            TextView type  = (TextView) findViewById(R.id.textView_type);
            type.setText("Exercise Type: " + exerciseData.get(0));

            TextView equip = (TextView) findViewById(R.id.textView_equip);
            equip.setText("Required Equipment: " + exerciseData.get(1));

            TextView level = (TextView) findViewById(R.id.textView_level);
            level.setText("Level: " + exerciseData.get(2));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.button_home:
                intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
                break;

            case R.id.button_webview:
                intent = new Intent(v.getContext(), WebViewExercise.class);
                Log.e("aasdfads", exerciseStruct.getUrl());
                intent.putExtra("URL", exerciseStruct.getUrl());
                v.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }
}
