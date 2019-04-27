package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Competency;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

public class CompetencyActivity extends AppCompatActivity {
    TextView tvPerformance;
    TextView tvTitle;
    TextView tvCondition;
    TextView tvRequirements;
    EditText etFeedback;
    String competencyJson;
    String learnerJson;
    Button btnStartLearn;
    private Competency competency;
    private Learner learner;
    private String supervisorMail;
    private String instructorMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competency);
        getPrevData();
        initView();
    }

    /**
     * Get the parameter from the intent passed from the last activity
     * */
    public void getPrevData() {
        competencyJson = getIntent().getStringExtra("competency");
        learnerJson = getIntent().getStringExtra("learner");
        supervisorMail = getIntent().getStringExtra("supervisorMail");
        instructorMail = getIntent().getStringExtra("instructorMail");
        competency = new Gson().fromJson(competencyJson, Competency.class);
        learner = new Gson().fromJson(learnerJson, Learner.class);
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    public void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvPerformance = findViewById(R.id.tv_performance);
        tvCondition = findViewById(R.id.tv_condition);
        tvRequirements = findViewById(R.id.tv_requirements);
        etFeedback = findViewById(R.id.et_feedback);
        btnStartLearn = findViewById(R.id.btn_startLearn);

        tvTitle.setText(competency.title);
        tvPerformance.setText(competency.performance);
        tvCondition.setText(competency.conditions);
        tvRequirements.setText(competency.requirements);
        etFeedback.setText(learner.courseCommentList.get(competency.cID - 1));
        if(supervisorMail != null || instructorMail != null) {
            btnStartLearn.setVisibility(View.INVISIBLE);
        }
    }

    public void learningPressed(View view) {
        if(competency.isFinished){
            Toast.makeText(this,"You have finished this competency." +
                    " Please move to next course", Toast.LENGTH_LONG).show();
        }
        else {
            if(supervisorMail == null) {
                Intent intent = new Intent(this, StudyActivity.class);
                intent.putExtra("learner", new Gson().toJson(learner));
                intent.putExtra("competency", new Gson().toJson(competency));
                startActivity(intent);
            }
        }
    }
}
