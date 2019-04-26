package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import e.gongfurui.digitallearnerlogbookV2.R;

public class ResponseActivity extends AppCompatActivity {

    TextView tvRequest;
    private String instructorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        instructorName = getIntent().getStringExtra("instructorName");
        initViews();
    }

    private void initViews(){
        tvRequest = findViewById(R.id.tvRequest);
        if(instructorName == null) {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    if (key.equals("instructor_name")) {
                        tvRequest.setText("Your instructor: " + getIntent().getExtras().getString(key) +
                                " wish to view your progress");
                    }
                }
            }
        }
        else tvRequest.setText("Your instructor: " + instructorName + " wish to view your progress");
    }

    public void agreePressed(View view) {

    }

    public void disagreePressed(View view) {

    }
}
