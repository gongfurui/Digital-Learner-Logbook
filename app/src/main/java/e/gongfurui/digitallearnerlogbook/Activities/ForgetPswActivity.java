package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import e.gongfurui.digitallearnerlogbook.Helpers.EmailUtil;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class ForgetPswActivity extends AppCompatActivity {

    EditText et_email, et_verify_code, et_new_psw, et_confirm_psw;
    String emailTo;
    int verifyCode;
    Learner learner;
    Instructor instructor;
    Supervisor supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

    }

    /**
     * Initial the parameter involved in this class
     * */
    private void initViews(){
        et_email = findViewById(R.id.et_mail);
        et_verify_code = findViewById(R.id.et_l_verifyCode);
        et_new_psw = findViewById(R.id.et_new_psw);
        et_confirm_psw = findViewById(R.id.et_confirm_psw);
    }

    /**
     * The reaction after pressing the button to get verify code
     * */
    public void verifyCodePressed(View view) {
        emailTo = String.valueOf(et_email.getText());
        //Generate the random verify code
        verifyCode = (int)((Math.random()*9+1)*100000);
        Toast.makeText(ForgetPswActivity.this,"Sending..... the verify code", Toast.LENGTH_LONG).show();
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
     * */
    public void changePswPressed(View view) {
        if(!String.valueOf(et_verify_code.getText()).equals(String.valueOf(verifyCode)) ){
            Toast.makeText(ForgetPswActivity.this,
                    "The verify code is not correct! Please check it!",
                    Toast.LENGTH_LONG).show();
        }
        else{
            if(String.valueOf(et_new_psw.getText()).isEmpty() || String.valueOf(et_confirm_psw.getText()).isEmpty()){
                Toast.makeText(ForgetPswActivity.this,"You should fill all field!", Toast.LENGTH_LONG).show();
            }
            else if(String.valueOf(et_new_psw.getText()).equals(String.valueOf(et_confirm_psw.getText()))){
                learner = SQLQueryHelper.searchLearnerTable(this, "SELECT * FROM learner" +
                        " WHERE email = '"+ String.valueOf(et_email.getText()) + "'");
                instructor = SQLQueryHelper.searchInstructorTable(this, "SELECT * FROM instructor" +
                        " WHERE email = '"+ String.valueOf(et_email.getText()) + "'");
                supervisor = SQLQueryHelper.searchSupervisorTable(this, "SELECT * FROM supervisor" +
                        " WHERE email = '"+ String.valueOf(et_email.getText()) + "'");
                Intent intent = new Intent(this, LoginActivity.class);
                if(learner != null){
                    SQLQueryHelper.updateDatabase(this, "UPDATE learner SET psw ='" +
                            String.valueOf(et_confirm_psw.getText()) +
                            "' WHERE email = '" +
                            String.valueOf(et_email.getText()) + "'");
                    Toast.makeText(ForgetPswActivity.this,
                            "Password changed successfully!",
                            Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else if(instructor != null){
                    SQLQueryHelper.updateDatabase(this, "UPDATE instructor SET psw ='" +
                            String.valueOf(et_confirm_psw.getText()) +
                            "' WHERE email = '" +
                            String.valueOf(et_email.getText()) + "'");
                    Toast.makeText(ForgetPswActivity.this,
                            "Password changed successfully!",
                            Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else if(supervisor != null){
                    SQLQueryHelper.updateDatabase(this, "UPDATE supervisor SET psw ='" +
                            String.valueOf(et_confirm_psw.getText()) +
                            "' WHERE email = '" +
                            String.valueOf(et_email.getText()) + "'");
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
