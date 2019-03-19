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

public class InstructorRegisterActivity extends AppCompatActivity implements SelectInterface  {

    private EditText et_i_name, et_i_dob, et_i_email, et_i_licenceID, et_i_adi, et_i_psw, et_i_verifyCode;
    private SelectDateDialog dateDialog;
    private boolean existAccount = false;
    private int verifyCode;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    /**
     * Initial the parameter involved in this class
     * */
    private void init(){
        et_i_dob = findViewById(R.id.et_i_dob);
        et_i_email = findViewById(R.id.et_i_email);
        et_i_licenceID = findViewById(R.id.et_i_licenseID);
        et_i_name = findViewById(R.id.et_i_name);
        et_i_adi = findViewById(R.id.et_i_ADI);
        et_i_psw = findViewById(R.id.et_i_psw);
        et_i_verifyCode = findViewById(R.id.et_i_verifyCode);
    }

    /**
     * Popup the date dialog
     */
    public void dobIPressed(View view) {
        dateDialog = new SelectDateDialog(this);
        dateDialog.showDateDialog(this);
    }

    /**
     * The actions after pressing the get code button
     * */
    public void getCodeIPressed(View view) {
        /*check if the email is empty*/
        if(et_i_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = et_i_email.getText().toString();
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
                                        ". This code is for register instructor account only!");

                        Looper.prepare();
                        Toast.makeText(InstructorRegisterActivity.this, "Sent the verify code", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                };
                thread.start();
                existAccount = true;
            }
        }
    }

    /**
     * The actions after pressing the submit button
     * */
    public void submitInstructorPressed(View view) {
        /*Check all mandatory part of the field that has been filled*/
        if(et_i_name.getText().toString().isEmpty()||et_i_dob.getText().toString().isEmpty()||
                et_i_licenceID.getText().toString().isEmpty()||et_i_adi.getText().toString().isEmpty()||
                et_i_email.getText().toString().isEmpty()||et_i_verifyCode.getText().toString().isEmpty()||
                et_i_psw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = et_i_name.getText().toString();
            String dob = et_i_dob.getText().toString();
            int licenceID = Integer.parseInt(et_i_licenceID.getText().toString());
            String psw = et_i_psw.getText().toString();
            int adi = Integer.parseInt(et_i_adi.getText().toString());
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
                    if(verifyCode != Integer.parseInt(et_i_verifyCode.getText().toString())) {
                        Toast.makeText(this, "The verify code is not correct! Please check it!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Instructor instructor = new Instructor(new Role(licenceID, name, email, psw, dob), adi);
                        SQLQueryHelper.insertDatabase(this, "INSERT into instructor " +
                                "(id, ADI, email, name, psw, date_of_birth)" +
                                " VALUES (" + instructor.driver_id + ", " + instructor.ADI + ", '" + instructor.email +
                                "', '" + instructor.name + "', '" + instructor.psw + "', '" + instructor.date_of_birth + "')");

                        Intent intent = new Intent(this, InstructorHomePageActivityV1.class);
                        intent.putExtra("instructor", new Gson().toJson(instructor));
                        startActivity(intent);
                    }
                }
            }
        }
    }



    @Override
    public void selectedResult(String result) {
        et_i_dob.setText(result);
    }

}
