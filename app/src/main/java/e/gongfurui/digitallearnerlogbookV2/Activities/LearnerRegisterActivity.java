package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbookV2.Dialog.SelectDateDialog;
import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.Utils.EmailUtil;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Role;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;
import e.gongfurui.wheelviewlibrary.listener.SelectInterface;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class LearnerRegisterActivity extends AppCompatActivity implements SelectInterface {
    private EditText et_l_name;
    private EditText et_l_dob;
    private EditText et_l_email;
    private EditText et_l_licenceID;
    private EditText et_l_superEmail;
    private EditText et_l_psw;
    private EditText et_l_verifyCode;
    private SelectDateDialog dateDialog;
    private boolean existAccount = false;
    private boolean haveAdded = false;
    private ArrayList<Boolean> courseProgressList = new ArrayList<>();
    private ArrayList<String> courseCommentList = new ArrayList<>();
    private int verifyCode;
    private String superEmail;
    private String email;
    private final String LICENCE_TYPE = "learner";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        for(int i = 0; i <= 22; i++){
            courseProgressList.add(false);
            courseCommentList.add("");
        }
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews(){
        et_l_dob = findViewById(R.id.et_l_dob);
        et_l_email = findViewById(R.id.et_l_email);
        et_l_licenceID = findViewById(R.id.et_l_licenseID);
        et_l_superEmail = findViewById(R.id.et_l_superEmail);
        et_l_name = findViewById(R.id.et_l_name);
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
     * The actions after pressing the add button
     * */
    public void addPressed(View view) {
        if(!haveAdded) {
            if(!et_l_superEmail.getText().toString().isEmpty()) {
                superEmail = et_l_superEmail.getText().toString();
            }
            Supervisor supervisor = OnlineDBHelper.searchSupervisorTable(LOCAL_IP +
                    "/drive/searchSupervisorByMail/" + superEmail);
            if (supervisor == null) {
                Toast.makeText(this, "We can't find the supervisor you add please" +
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
        //Check all mandatory part of the field that has been filled
        if(et_l_name.getText().toString().isEmpty()||et_l_dob.getText().toString().isEmpty()||
                et_l_licenceID.getText().toString().isEmpty()||et_l_superEmail.getText().toString().isEmpty()||
                et_l_email.getText().toString().isEmpty()||et_l_verifyCode.getText().toString().isEmpty()||
                et_l_psw.getText().toString().isEmpty()){
            Toast.makeText(this,"You should fill all field!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = et_l_name.getText().toString();
            String dob = et_l_dob.getText().toString();
            int licenceID = Integer.parseInt(et_l_licenceID.getText().toString());
            String psw = et_l_psw.getText().toString();
            boolean isLicence = OnlineDBHelper.isInLicenceTable(LOCAL_IP +
                    "/drive/searchLicence/" + String.valueOf(licenceID) + "&" + LICENCE_TYPE);
            //Check if the licence is valid
            if(!isLicence){
                Toast.makeText(this, "The driver id is invalid", Toast.LENGTH_LONG).show();
            }
            else {
                //Check if the learner has validated the email before
                if (!existAccount) {
                    Toast.makeText(this, "You haven't validate your email",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Check if the learner has added a supervisor
                    if (!haveAdded) {
                        Toast.makeText(this, "Please nominate a instructor to start the " +
                                "learning process", Toast.LENGTH_LONG).show();
                    } else {
                        //Check if the verify code is correct
                        if(verifyCode != Integer.parseInt(et_l_verifyCode.getText().toString())){
                            Toast.makeText(this, "The verify code is not correct! Please check it!",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Learner learner = new Learner(new Role(name, email, psw),
                                    licenceID, dob,0, 0, courseProgressList, courseCommentList);
                            OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertLearner/" +
                                    learner.driver_id + "&" + learner.email + "&" + learner.name +
                                    "&" + learner.psw + "&" + learner.date_of_birth);
                            OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertSupervisorLearner/" +
                                    superEmail + "&" + learner.driver_id);
                            Intent intent= new Intent(this, LearnerHomeActivity.class);
                            intent.putExtra("learnerMail", learner.email);
                            //startActivity(intent);
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
        //check if the email is empty
        if(et_l_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
        else {
            email = et_l_email.getText().toString();
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
