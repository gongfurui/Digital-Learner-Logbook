package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class CompetencyActivity extends AppCompatActivity {
    private TextView tv_performance, tv_title, tv_condition, tv_requirements;
    private EditText et_feedback;
    private String competencyJson, learnerJson;
    private Competency competency;
    private Learner learner;


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
    public void getPrevData(){
        competencyJson = getIntent().getStringExtra("competency");
        learnerJson = getIntent().getStringExtra("learner");
        competency = new Gson().fromJson(competencyJson, Competency.class);
        learner = new Gson().fromJson(learnerJson, Learner.class);
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    public void initView(){
        tv_title = findViewById(R.id.tv_title);
        tv_performance = findViewById(R.id.tv_performance);
        tv_condition = findViewById(R.id.tv_condition);
        tv_requirements = findViewById(R.id.tv_requirements);
        et_feedback = findViewById(R.id.et_feedback);
       /* toolbar = findViewById(R.id);*/
        tv_title.setText(competency.title);
        tv_performance.setText(competency.performance);
        tv_condition.setText(competency.conditions);
        tv_requirements.setText(competency.requirements);
        et_feedback.setText(competency.comment);
    }

    public void learningPressed(View view) {
        if(competency.isFinished){
            Toast.makeText(this,"You have finished this competency. Please move to next course", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, StudyActivity.class);
            intent.putExtra("learner", new Gson().toJson(learner));
            intent.putExtra("competency", new Gson().toJson(competency));
            startActivity(intent);
        }
    }
}
