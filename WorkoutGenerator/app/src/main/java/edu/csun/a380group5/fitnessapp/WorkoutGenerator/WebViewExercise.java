package edu.csun.a380group5.fitnessapp.WorkoutGenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import edu.csun.a380group5.fitnessapp.MainActivity;
import edu.csun.a380group5.fitnessapp.R;

public class WebViewExercise extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_exercise);

        Button button_home = (Button) findViewById(R.id.button_home);
        button_home.setOnClickListener(this);
        WebView webView = (WebView) findViewById(R.id.webview);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null) { webView.loadUrl(bundle.getString("URL")); }
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
