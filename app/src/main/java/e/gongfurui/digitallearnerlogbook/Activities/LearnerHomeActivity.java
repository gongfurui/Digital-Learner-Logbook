package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class LearnerHomeActivity extends AppCompatActivity {

    TextView learnerName, learnerID, tv_learnerName, tv_learnerID, tv_learnerDob, tv_learnerEmail;
    Learner learner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_home);
        String learnerJson=getIntent().getStringExtra("learner");
        learner=new Gson().fromJson(learnerJson, Learner.class);
        initViews();
    }

    /**
     * Initial the parameter involved in this class
     * */
    private void initViews() {
        tv_learnerName = findViewById(R.id.tv_learnerName);
        tv_learnerID = findViewById(R.id.tv_learnerDriverID);
        tv_learnerDob = findViewById(R.id.tv_learnerDob);
        tv_learnerEmail = findViewById(R.id.tv_learnerEmail);
        tv_learnerName.setText(learner.name);
        tv_learnerDob.setText(learner.date_of_birth);
        tv_learnerID.setText(String.valueOf(learner.driver_id));
        tv_learnerEmail.setText(learner.email);
    }

}
