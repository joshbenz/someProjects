package edu.csun.a380group5.fitnessapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import edu.csun.a380group5.fitnessapp.WorkoutGenerator.WorkoutGeneratorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonWorkoutGenerator = (Button) findViewById(R.id.button_workout_generator);

        buttonWorkoutGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WorkoutGeneratorActivity.class);
                view.getContext().startActivity(intent);}
        });
    }
}

