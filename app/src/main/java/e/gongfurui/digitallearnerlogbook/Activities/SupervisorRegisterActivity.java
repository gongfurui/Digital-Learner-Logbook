package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.Dialog.SelectDateDialog;
import e.gongfurui.digitallearnerlogbook.Helpers.EmailUtil;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Role;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;
import e.gongfurui.wheelviewlibrary.listener.SelectInterface;

public class SupervisorRegisterActivity extends AppCompatActivity implements SelectInterface {
    private EditText et_s_name, et_s_dob, et_s_email, et_s_licenceID, et_s_psw, et_s_verifyCode;
    private SelectDateDialog dateDialog;
    private boolean existAccount = false;
    private int verifyCode;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    /**
     * Initial the parameter involved in this class
     * */
    private void init(){
        et_s_dob = findViewById(R.id.et_s_dob);
        et_s_email = findViewById(R.id.et_s_email);
        et_s_licenceID = findViewById(R.id.et_s_licenseID);
        et_s_name = findViewById(R.id.et_s_name);
        et_s_psw = findViewById(R.id.et_s_psw);
        et_s_verifyCode = findViewById(R.id.et_s_verifyCode);
    }


    /**
     * Popup the date dialog
     */
    public void dobSPressed(View view) {
        dateDialog = new SelectDateDialog(this);
        dateDialog.showDateDialog(this);
    }

    /**
     * The actions after pressing the get code button
     * */
    public void getCodeSPressed(View view) {
        /*check if the email is empty*/
        if(et_s_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = et_s_email.getText().toString();
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
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        EmailUtil.getInstance().sendEmail(emailTo, "Verify Code from Digital Learner Logbook",
                                "DLL send a verify code: " + verifyCode +
                                        ". This code is for register supervisor account only!");

                        Looper.prepare();
                        Toast.makeText(SupervisorRegisterActivity.this, "Sent the verify code", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                };
                thread.start();
                existAccount = true;
            }
        }
    }

    /**
     * The action after pressing the submit button
     * */
    public void submitSupervisorPressed(View view) {
        /*Check all mandatory part of the field that has been filled*/
        if(et_s_name.getText().toString().isEmpty()||et_s_dob.getText().toString().isEmpty()||
                et_s_licenceID.getText().toString().isEmpty()||et_s_email.getText().toString().isEmpty()||
                et_s_verifyCode.getText().toString().isEmpty()||et_s_psw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = et_s_name.getText().toString();
            String dob = et_s_dob.getText().toString();
            int licenceID = Integer.parseInt(et_s_licenceID.getText().toString());
            String psw = et_s_psw.getText().toString();
            boolean isLicence = SQLQueryHelper.searchLicenceTable(this, "SELECT driver_id FROM licence" +
                    " WHERE driver_id = '" + licenceID + "' and type = 'full'");
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
                    /*Check if the verify code is correct*/
                    if(verifyCode != Integer.parseInt(et_s_verifyCode.getText().toString())) {
                        Toast.makeText(this, "The verify code is not correct! Please check it!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Supervisor supervisor = new Supervisor(new Role(licenceID, name, email, psw, dob));
                        SQLQueryHelper.insertDatabase(this, "INSERT into supervisor " +
                                "(id, email, name, psw, date_of_birth)" +
                                " VALUES (" + supervisor.driver_id + ", '" + supervisor.email +
                                "', '" + supervisor.name + "', '" + supervisor.psw + "', '" + supervisor.date_of_birth + "')");

                        Intent intent = new Intent(this, SupervisorHomePageActivityV1.class);
                        intent.putExtra("supervisor", new Gson().toJson(supervisor));
                        startActivity(intent);
                    }
                }
            }
        }
    }


    @Override
    public void selectedResult(String result) {
        et_s_dob.setText(result);
    }
}
