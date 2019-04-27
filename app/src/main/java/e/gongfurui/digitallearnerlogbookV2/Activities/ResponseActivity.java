package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class ResponseActivity extends AppCompatActivity {

    TextView tvRequest;
    private String instructorName;
    private String instructorMail;
    private String learnerMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        instructorName = getIntent().getStringExtra("instructorName");
        instructorMail = getIntent().getStringExtra("instructor_mail");
        learnerMail = getIntent().getStringExtra("learner_mail");
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews(){
        tvRequest = findViewById(R.id.tvRequest);
        if(instructorName == null) {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    if (key.equals("instructor_name")) {
                        instructorName = getIntent().getExtras().getString(key);
                        instructorMail = getIntent().getExtras().getString("instructor_mail");
                        learnerMail = getIntent().getExtras().getString("learner_mail");
                    }
                }
            }
        }
        tvRequest.setText("Your instructor: " + instructorName + " wish to view your progress");
    }

    public void agreePressed(View view) {
        OnlineDBHelper.sendFCMRequest(LOCAL_IP + "/drive/sendAgreeFCM/agree&" + instructorMail +
                "&" + learnerMail);
        finish();
    }

    public void disagreePressed(View view) {
        OnlineDBHelper.sendFCMRequest(LOCAL_IP + "/drive/sendDisagreeFCM/disagree&" +
                instructorMail);
        finish();
    }
}
