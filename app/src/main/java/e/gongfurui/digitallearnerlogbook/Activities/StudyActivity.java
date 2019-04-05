package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigDecimal;

import e.gongfurui.digitallearnerlogbook.Utils.EmailUtil;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class StudyActivity extends AppCompatActivity implements Runnable {

    private double sec;
    private double min;
    private double hour;
    int verifyCode;
    private Handler timer;
    private TextView tvTimeCovered;
    private EditText etADI;
    private EditText etVerify;
    private EditText etFeedback;
    private Switch switchIApprove;
    String learnerJson;
    String competencyJson;
    private boolean isApproved;
    private Learner learner;
    private Instructor instructor;
    private Competency competency;
    private AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        learnerJson = getIntent().getStringExtra("learner");
        competencyJson = getIntent().getStringExtra("competency");
        learner = new Gson().fromJson(learnerJson, Learner.class);
        competency = new Gson().fromJson(competencyJson, Competency.class);
        timer = new Handler();
        hour = 0;
        min = 0;
        sec = 0;
        timer.postDelayed(this,1000);
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews(){
        tvTimeCovered = findViewById(R.id.tv_timeCovered);
    }

    @Override
    public void run() {
        //sec++;
        //min++;
        hour++;
        if(sec == 60) {
            min++;
            sec = 0;
        }
        if(min == 60) {
            hour ++;
            min = 0;
        }
        tvTimeCovered.setText((int) hour + "h " +(int) min + "m " +(int) sec + "s");
        timer.postDelayed(this,1000);
    }


    /**
     * Establish the AlertDialog
     * */
    public void showDialog(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.certify_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        alert = builder.setTitle("Certify...")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", null)
                .create();
        alert.show();
    }


    /**
     * The certification process after finishing the study
     * */
    public void finishPressed(View view) {
        timer.removeCallbacks(this);// stop the timer
        double total_time1 = sec / 3600 + min / 60 + hour;
        BigDecimal bd=new BigDecimal(total_time1);
        final double total_time = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        showDialog(this);
        etADI = alert.getWindow().findViewById(R.id.et_ADI);
        etVerify = alert.getWindow().findViewById(R.id.et_verify);
        etFeedback = alert.getWindow().findViewById(R.id.et_feedback);
        switchIApprove = alert.getWindow().findViewById(R.id.switch_insApprove);

        switchIApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isApproved = true;
                } else {
                    isApproved = false;
                }
                System.out.println("Result is: " + isApproved);
            }
        });

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //check whether the user put the verify code
                if(etVerify.getText().toString().isEmpty()) {
                    Toast.makeText(StudyActivity.this,
                            "The verify code is empty. Please check!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //check whether the verify code is correct
                    if (verifyCode != Integer.parseInt(etVerify.getText().toString())) {
                        Toast.makeText(StudyActivity.this,
                                "The verify code is wrong. Please check!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        //this the updated time after certification
                        double certified_time = learner.time + total_time;
                        if(certified_time >= 120){
                            certified_time = 120;
                        }
                        /*Update the student progress, and course comment list*/
                        SQLQueryHelper.updateDatabase(StudyActivity.this, "UPDATE " +
                                "learner SET time =" + certified_time + " WHERE id = " +
                                learner.driver_id);
                        if(isApproved) {
                            SQLQueryHelper.insertDatabase(StudyActivity.this,
                                    "INSERT into courseFeedback " +
                                            "(course_id, learner_id, instructor_name, feedback)" +
                                            " VALUES ("+competency.cID+","+learner.driver_id+"," +
                                            " '"+instructor.name+"', '"+ etFeedback.getText().toString()+"')");
                            SQLQueryHelper.updateDatabase(StudyActivity.this, "UPDATE " +
                                    "learner SET c" + competency.cID + " =" + 1 + " WHERE id = " +
                                    learner.driver_id);
                        }
                        SQLQueryHelper.updateDatabase(StudyActivity.this, "UPDATE " +
                                "learner SET c" + competency.cID + "c = '" +
                                etFeedback.getText().toString() + "' WHERE id = " +
                                learner.driver_id);
                        Intent intent = new Intent(StudyActivity.this,
                                LearnerHomeActivity.class);
                        intent.putExtra("learnerID", learner.driver_id);
                        startActivity(intent);
                        alert.dismiss();
                    }
                }
            }
        });

        //The action involved with functions for sending the verify code based on the ADI number
        alert.getWindow().findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ADI = 0;
                if(!etADI.getText().toString().isEmpty()) ADI = Integer.parseInt(etADI.getText().toString());
                instructor = SQLQueryHelper.searchInstructorTable(StudyActivity.this, "SELECT * FROM instructor" +
                        " WHERE ADI = "+ ADI);
                String emailTo = "";
                if(instructor != null) emailTo = instructor.email;
                if(emailTo.isEmpty()){
                    Toast.makeText(StudyActivity.this, "The ADI you enter is wrong. Please check!", Toast.LENGTH_LONG).show();
                }
                else {
                    //Generate the random verify code
                    verifyCode = (int) ((Math.random() * 9 + 1) * 100000);
                    Toast.makeText(StudyActivity.this, "Sending..... the verify code", Toast.LENGTH_LONG).show();
                    final String finalEmailTo = emailTo;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            EmailUtil.getInstance().sendEmail(finalEmailTo, "Verify Code from Digital Learner Logbook",
                                    "DLL send a verify code: " + verifyCode +
                                            ", This code is for instructor to certify progress only!");

                            Looper.prepare();
                            Toast.makeText(StudyActivity.this, "Sent the verify code", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    };
                    thread.start();
                }
            }
        });


    }



}
