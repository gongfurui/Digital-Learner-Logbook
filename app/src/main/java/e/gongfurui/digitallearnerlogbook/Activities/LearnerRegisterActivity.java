package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbook.Dialog.SelectDateDialog;
import e.gongfurui.digitallearnerlogbook.Helpers.EmailUtil;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Role;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;
import e.gongfurui.wheelviewlibrary.listener.SelectInterface;

public class LearnerRegisterActivity extends AppCompatActivity implements SelectInterface {
    private EditText et_l_name, et_l_dob, et_l_email, et_l_licenceID, et_l_adi, et_l_superID,
            et_l_psw, et_l_verifyCode;
    private SelectDateDialog dateDialog;
    private boolean haveNominated = false;
    private boolean existAccount = false;
    private boolean haveAdded = false;
    private ArrayList<Boolean> courseProgressList = new ArrayList<>();
    private ArrayList<String> courseCommentList = new ArrayList<>();
    private int verifyCode, adi, superID;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        for(int i = 0; i <= 22; i++){
            courseProgressList.add(false);
            courseCommentList.add("");
        }
    }

    /**
     * Initial the parameter involved in this class
     * */
    private void init(){
        et_l_dob = findViewById(R.id.et_l_dob);
        et_l_email = findViewById(R.id.et_l_email);
        et_l_licenceID = findViewById(R.id.et_l_licenseID);
        et_l_superID = findViewById(R.id.et_l_superID);
        et_l_name = findViewById(R.id.et_l_name);
        et_l_adi = findViewById(R.id.et_l_ADI);
        et_l_psw = findViewById(R.id.et_l_psw);
        et_l_verifyCode = findViewById(R.id.et_l_verifyCode);
    }


    /**
     * Popup the date dialog
     */
    public void dobPressed(View view) {
        dateDialog = new SelectDateDialog(this);
        dateDialog.showDateDialog(this);
    }

    /**
     * The actions after pressing the nominate button
     * */
    public void nominatePressed(View view) {
        if(!haveNominated) {
            if(!et_l_adi.getText().toString().isEmpty()){
                adi = Integer.parseInt(et_l_adi.getText().toString());
            }
            Instructor instructor = SQLQueryHelper.searchInstructorTable(this, "SELECT * FROM instructor" +
                    " WHERE ADI = '" + adi + "'");
            if (instructor == null) {
                Toast.makeText(this, "We can't find the instructor you nominate please" +
                        " check!", Toast.LENGTH_LONG).show();
            } else {
                haveNominated = true;
                Toast.makeText(this, "Congratulation! You have nominated the " +
                        "instructor: " + instructor.name + " successfully!", Toast.LENGTH_LONG).show();
            }
        }
        /*If the learner has nominated an instructor, he cannot nominate again*/
        else {
            Toast.makeText(this, "You have nominated an instructor. Please don't again",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The actions after pressing the add button
     * */
    public void addPressed(View view) {
        if(!haveAdded) {
            if(!et_l_superID.getText().toString().isEmpty()) {
                superID = Integer.parseInt(et_l_superID.getText().toString());
            }
            Supervisor supervisor = SQLQueryHelper.searchSupervisorTable(this,
                    "SELECT * FROM supervisor WHERE id = '" + superID + "'");
            if (supervisor == null) {
                Toast.makeText(this, "We can't find the supervisor you nominate please" +
                        " check!", Toast.LENGTH_LONG).show();
            } else {
                haveAdded = true;
                Toast.makeText(this, "Congratulation! You have added the " +
                        "supervisor: " + supervisor.name + " successfully!", Toast.LENGTH_LONG).show();
            }
        }
        /*If the learner has added a supervisor, he cannot add again*/
        else {
            Toast.makeText(this, "You have added a supervisor. Please don't again",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * The action after pressing the submit button
     * */
    public void submitLearnerPressed(View view) {
        /*Check all mandatory part of the field that has been filled*/
        if(et_l_name.getText().toString().isEmpty()||et_l_dob.getText().toString().isEmpty()||
                et_l_licenceID.getText().toString().isEmpty()||et_l_adi.getText().toString().isEmpty()||
                et_l_email.getText().toString().isEmpty()||et_l_verifyCode.getText().toString().isEmpty()||
                et_l_psw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = et_l_name.getText().toString();
            String dob = et_l_dob.getText().toString();
            int licenceID = Integer.parseInt(et_l_licenceID.getText().toString());
            String psw = et_l_psw.getText().toString();
            boolean isLicence = SQLQueryHelper.searchLicenceTable(this, "SELECT driver_id FROM licence" +
                    " WHERE driver_id = '" + licenceID + "' and type = 'learner'");
            /*Check if the licence is valid*/
            if(!isLicence){
                Toast.makeText(this, "The driver id is invalid", Toast.LENGTH_LONG).show();
            }
            else {
                /*Check if the learner has validated the email before*/
                if (!existAccount) {
                    Toast.makeText(this, "You haven't validate your email",
                            Toast.LENGTH_LONG).show();
                } else {
                    /*Check if the learner has nominated an instructor*/
                    if (!haveNominated) {
                        Toast.makeText(this, "Please nominate a instructor to start the " +
                                "learning process", Toast.LENGTH_LONG).show();
                    } else {
                        /*Check if the verify code is correct*/
                        if(verifyCode != Integer.parseInt(et_l_verifyCode.getText().toString())){
                            Toast.makeText(this, "The verify code is not correct! Please check it!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Learner learner = new Learner(new Role(licenceID, name, email, psw, dob),
                                    superID, adi, 0, courseProgressList, courseCommentList);
                            SQLQueryHelper.insertDatabase(this, "INSERT into learner " +
                                    "(id, email, name, psw, ADI, super_id, date_of_birth)" +
                                    " VALUES ("+learner.driver_id+", '"+learner.email+"', '"+learner.name+
                                    "', '"+learner.psw+"', "+learner.adi+", "+learner.super_id+", '"+
                                    learner.date_of_birth+"')");
                            /**!!!!!potential problem!!!!!*/
                            SQLQueryHelper.insertDatabase(this, "INSERT into instructor_learner (ADI, learner_id)" +
                                    " VALUES ("+learner.adi+", "+learner.driver_id+")");
                            if(learner.super_id != 0){
                                /**!!!!!potential problem!!!!!*/
                                SQLQueryHelper.insertDatabase(this, "INSERT into supervisor_learner (driver_id, learner_id)" +
                                        " VALUES ("+learner.super_id+", "+learner.driver_id+")");
                            }
                            Intent intent= new Intent(this, LearnerHomeActivity.class);
                            intent.putExtra("learner", new Gson().toJson(learner));
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void selectedResult(String result) {
        et_l_dob.setText(result);
    }

    /**
     * The action after pressing the get code button
     * */
    public void getCodeLPressed(View view) {
        /*check if the email is empty*/
        if(et_l_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = et_l_email.getText().toString();
            Learner learner = SQLQueryHelper.searchLearnerTable(this, "SELECT * FROM learner" +
                    " WHERE email = '" + email + "'");
            Instructor instructor = SQLQueryHelper.searchInstructorTable(this, "SELECT * FROM instructor" +
                    " WHERE email = '" + email + "'");
            Supervisor supervisor = SQLQueryHelper.searchSupervisorTable(this, "SELECT * FROM supervisor" +
                    " WHERE email = '" + email + "'");
            /*Check if the email has been registered before*/
            if (learner != null || supervisor != null || instructor != null) {
                Toast.makeText(this, "This email has been registered before",
                        Toast.LENGTH_LONG).show();
            } else {
                final String emailTo = String.valueOf(email);
                //Generate the random verify code
                verifyCode = (int) ((Math.random() * 9 + 1) * 100000);
                Toast.makeText(this,"Sending..... the verify code", Toast.LENGTH_LONG).show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        EmailUtil.getInstance().sendEmail(emailTo, "Verify Code from Digital Learner Logbook",
                                "DLL send a verify code: " + verifyCode +
                                        ", This code is for register learner account only!");

                        Looper.prepare();
                        Toast.makeText(LearnerRegisterActivity.this, "Sent the verify code", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                };
                thread.start();
                existAccount = true;
            }
        }
    }


}
