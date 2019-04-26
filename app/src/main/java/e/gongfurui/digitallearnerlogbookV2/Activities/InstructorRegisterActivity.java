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

public class InstructorRegisterActivity extends AppCompatActivity {

    private EditText et_i_name;
    private EditText et_i_email;
    private EditText et_i_adi;
    private EditText et_i_psw;
    private EditText et_i_verifyCode;
    private boolean existAccount = false;
    private int verifyCode;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        et_i_email = findViewById(R.id.et_i_email);
        et_i_name = findViewById(R.id.et_i_name);
        et_i_adi = findViewById(R.id.et_i_ADI);
        et_i_psw = findViewById(R.id.et_i_psw);
        et_i_verifyCode = findViewById(R.id.et_i_verifyCode);
    }


    /**
     * The actions after pressing the get code button
     * */
    public void getCodeIPressed(View view) {
        //check if the email is empty
        if(et_i_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = et_i_email.getText().toString();
            Learner learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + email);
            Supervisor supervisor = OnlineDBHelper.searchSupervisorTable(LOCAL_IP +
                    "/drive/searchSupervisorByMail/" + email);
            Instructor instructor = OnlineDBHelper.searchInstructorTable(LOCAL_IP +
                    "/drive/searchInstructorByMail/" + email);
            //Check if the email has been registered before
            if (learner != null || supervisor != null || instructor != null) {
                Toast.makeText(this, "This email has been registered before",
                        Toast.LENGTH_LONG).show();
            } else {
                final String emailTo = String.valueOf(email);
                verifyCode = (int) ((Math.random() * 9 + 1) * 100000);//Generate the random verify code
                Toast.makeText(this,"Sending..... the verify code", Toast.LENGTH_LONG).show();
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
        //Check all mandatory part of the field that has been filled
        if(et_i_name.getText().toString().isEmpty()||et_i_adi.getText().toString().isEmpty()||
                et_i_email.getText().toString().isEmpty()||et_i_verifyCode.getText().toString().isEmpty()||
                et_i_psw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = et_i_name.getText().toString();
            String psw = et_i_psw.getText().toString();
            int adi = Integer.parseInt(et_i_adi.getText().toString());
            boolean isADI = OnlineDBHelper.isInADIListTable(LOCAL_IP + "/drive/searchADIList/" +
                    adi);
            //Check if the licence is valid
            if(!isADI){
                Toast.makeText(this, "The driver id is invalid", Toast.LENGTH_LONG).show();
            }
            else {
                //Check if the learner has validated the email before
                if (!existAccount) {
                    Toast.makeText(this, "You haven't validate your email",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Check if the verify code is correct
                    if (verifyCode != Integer.parseInt(et_i_verifyCode.getText().toString())) {
                        Toast.makeText(this, "The verify code is not correct! Please check it!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Instructor instructor = new Instructor(new Role(name, email, psw), adi);
                        OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertInstructor/" +
                                instructor.ADI + "&" + instructor.email + "&" + instructor.name +
                                "&" + instructor.psw);
                        Intent intent = new Intent(this, InstructorHomeActivity.class);
                        intent.putExtra("instructorMail", instructor.email);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}
