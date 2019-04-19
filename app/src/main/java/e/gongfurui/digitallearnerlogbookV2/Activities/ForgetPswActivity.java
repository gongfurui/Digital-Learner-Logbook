package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Utils.EmailUtil;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class ForgetPswActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etVerifyCode;
    private EditText etNewPsw;
    private EditText etConfirmPsw;
    private String emailTo;
    private int verifyCode;
    Learner learner;
    Instructor instructor;
    Supervisor supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();

    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        etEmail = findViewById(R.id.et_mail);
        etVerifyCode = findViewById(R.id.et_l_verifyCode);
        etNewPsw = findViewById(R.id.et_new_psw);
        etConfirmPsw = findViewById(R.id.et_confirm_psw);
    }

    /**
     * The reaction after pressing the button to get verify code
     * @param view the view from the ForgetPswActivity
     * */
    public void verifyCodePressed(View view) {
        emailTo = String.valueOf(etEmail.getText());
        //Generate the random verify code
        verifyCode = (int)((Math.random() * 9 + 1) * 100000);
        Toast.makeText(ForgetPswActivity.this,
                "Sending..... the verify code", Toast.LENGTH_LONG).show();
        Thread thread = new Thread(){
            @Override
            public void run() {
                EmailUtil.getInstance().sendEmail(emailTo,"Verify Code from Digital Learner Logbook",
                        "DLL send a verify code: " + verifyCode +
                                ", This code is for reset the password only!" );

                Looper.prepare();
                Toast.makeText(ForgetPswActivity.this,"Sent the verify code", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        };
        thread.start();

    }

    /**
     * The reaction after pressing the button to change the password
     * @param view the view from the ForgetPswActivity
     * */
    public void changePswPressed(View view) {
        if(!String.valueOf(etVerifyCode.getText()).equals(String.valueOf(verifyCode)) ){
            Toast.makeText(ForgetPswActivity.this,
                    "The verify code is not correct! Please check it!",
                    Toast.LENGTH_LONG).show();
        }
        else{
            if(String.valueOf(etNewPsw.getText()).isEmpty() || String.valueOf(etConfirmPsw.getText()).isEmpty()){
                Toast.makeText(ForgetPswActivity.this,"You should fill all field!", Toast.LENGTH_LONG).show();
            }
            else if(String.valueOf(etNewPsw.getText()).equals(String.valueOf(etConfirmPsw.getText()))){
                String email = String.valueOf(etEmail.getText());
                String confirmedPsw = String.valueOf(etConfirmPsw.getText());
                learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP + "/drive/searchLearnerByMail/"
                        + email);
                supervisor = OnlineDBHelper.searchSupervisorTable(LOCAL_IP + "/drive/searchSupervisorByMail/"
                        + email);
                instructor = OnlineDBHelper.searchInstructorTable(LOCAL_IP + "/drive/searchInstructorByMail/"
                        + email);
                Intent intent = new Intent(this, LoginActivity.class);
                if(learner != null) {
                    OnlineDBHelper.updateTable(LOCAL_IP + "/drive/updateLearnerPsw/"
                            + email + "&" + confirmedPsw);
                    Toast.makeText(ForgetPswActivity.this,
                            "Password changed successfully!",
                            Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else if(instructor != null){
                    OnlineDBHelper.updateTable(LOCAL_IP + "/drive/updateInstructorPsw/"
                            + email + "&" + confirmedPsw);
                    Toast.makeText(ForgetPswActivity.this,
                            "Password changed successfully!",
                            Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else if(supervisor != null){
                    OnlineDBHelper.updateTable(LOCAL_IP + "/drive/updateSupervisorPsw/"
                            + email + "&" + confirmedPsw);
                    Toast.makeText(ForgetPswActivity.this,
                            "Password changed successfully!",
                            Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ForgetPswActivity.this,
                            "You don't have the account yet. Please register one!",
                            Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(ForgetPswActivity.this,
                        "The confirmed password is different! Please double check!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
