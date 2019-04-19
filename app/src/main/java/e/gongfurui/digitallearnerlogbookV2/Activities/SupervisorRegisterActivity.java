package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.Utils.EmailUtil;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Role;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class SupervisorRegisterActivity extends AppCompatActivity {
    private EditText etSName;
    private EditText etSEmail;
    private EditText etSPsw;
    private EditText etSVerifyCode;
    private boolean existAccount = false;
    private int verifyCode;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews(){
        etSEmail = findViewById(R.id.et_s_email);
        etSName = findViewById(R.id.et_s_name);
        etSPsw = findViewById(R.id.et_s_psw);
        etSVerifyCode = findViewById(R.id.et_s_verifyCode);
    }


    /**
     * The actions after pressing the get code button
     * */
    public void getCodeSPressed(View view) {
        /*check if the email is empty*/
        if(etSEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = etSEmail.getText().toString();
            Learner learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + email);
            Supervisor supervisor = OnlineDBHelper.searchSupervisorTable(LOCAL_IP +
                    "/drive/searchSupervisorByMail/" + email);
            Instructor instructor = OnlineDBHelper.searchInstructorTable(LOCAL_IP +
                    "/drive/searchInstructorByMail/" + email);
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
        if(etSName.getText().toString().isEmpty()|| etSEmail.getText().toString().isEmpty()||
                etSVerifyCode.getText().toString().isEmpty()|| etSPsw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = etSName.getText().toString();
            String psw = etSPsw.getText().toString();
            /*Check if the learner has validated the email before*/
            if (!existAccount) {
                Toast.makeText(this, "You haven't validate your email",
                        Toast.LENGTH_LONG).show();
            } else {
                /*Check if the verify code is correct*/
                if(verifyCode != Integer.parseInt(etSVerifyCode.getText().toString())) {
                    Toast.makeText(this, "The verify code is not correct! Please check it!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Supervisor supervisor = new Supervisor(new Role(name, email, psw));
                    OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertSupervisor/" +
                            supervisor.email + "&" + supervisor.name + "&" + supervisor.psw);

                    Intent intent = new Intent(this, SupervisorHomeActivity.class);
                    intent.putExtra("supervisorMail", supervisor.email);
                    startActivity(intent);
                }
            }
        }
    }

}
