package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class LoginActivity extends AppCompatActivity{

    private EditText etUserName;
    private EditText etPsw;
    private CheckBox cbRemember;
    Learner learner;
    Instructor instructor;
    Supervisor supervisor;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String ACCOUNTKEY = "MyAccount";
    private final String PSWKEY = "MyPassword";
    private final String CHECKKEY = "MyCheck";
    private boolean isAutoStore;// the parameter decides whether we store the account after logout



    String account;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get the sharedPreferences instance
        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);
        //get the sharedPreferences editor
        editor = sharedPreferences.edit();
        initViews();
        isAutoStore = sharedPreferences.getBoolean(CHECKKEY, false);
        if (isAutoStore) {
            cbRemember.setChecked(true);
            etUserName.setText(sharedPreferences.getString(ACCOUNTKEY, ""));
            etPsw.setText(sharedPreferences.getString(PSWKEY, ""));
        }
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        etUserName = findViewById(R.id.et_user_name);
        etPsw = findViewById(R.id.et_psw);
        cbRemember = findViewById(R.id.cb_remember);
        //Listen the checkbox status. If it is checked, store the account
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAutoStore = true;
                } else {
                    isAutoStore = false;
                }
            }
        });
    }

    /**
     * Action when the login button pressed
     * */
    public void loginPressed(View view) {
        account = String.valueOf(etUserName.getText());
        password = String.valueOf(etPsw.getText());

        /*Search the db tables of each role: learner, instructor, supervisor*/
        learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP + "/drive/searchLearner/"
                + account + "&" + password);
        supervisor = OnlineDBHelper.searchSupervisorTable(LOCAL_IP + "/drive/searchSupervisor/"
                + account + "&" + password);
        instructor = OnlineDBHelper.searchInstructorTable(LOCAL_IP + "/drive/searchInstructor/"
                + account + "&" + password);

        if(learner != null){
            Intent intent = new Intent(this, LearnerHomeActivity.class);
            Toast.makeText(this, "Successfully login as a learner", Toast.LENGTH_LONG).show();
            /*pass the learner data to the next Activity*/
            intent.putExtra("learnerMail", learner.email);
            if(isAutoStore) {
                editor.putString(ACCOUNTKEY, account);
                editor.putString(PSWKEY, password);
            }
            else {
                editor.putString(ACCOUNTKEY, "");
                editor.putString(PSWKEY, "");
            }
            editor.putBoolean(CHECKKEY, isAutoStore);
            editor.commit();
            startActivity(intent);
        }
        //If the user is an instructor, jump to the instructor homepage
        else if(instructor != null){
            Intent intent = new Intent(this, InstructorHomePageActivityV1.class);
            Toast.makeText(this, "Successfully login as a instructor", Toast.LENGTH_LONG).show();
            intent.putExtra("instructorMail", instructor.email);
            if(isAutoStore) {
                editor.putString(ACCOUNTKEY, account);
                editor.putString(PSWKEY, password);
            }
            else {
                editor.putString(ACCOUNTKEY, "");
                editor.putString(PSWKEY, "");
            }
            editor.putBoolean(CHECKKEY, isAutoStore);
            editor.commit();
            startActivity(intent);
        }
        //If the user is a supervisor, jump to the supervisor homepage
        else if(supervisor != null){
            Intent intent = new Intent(this, SupervisorHomeActivity.class);
            Toast.makeText(this, "Successfully login as a supervisor", Toast.LENGTH_LONG).show();
            intent.putExtra("supervisorMail", supervisor.email);
            if(isAutoStore) {
                editor.putString(ACCOUNTKEY, account);
                editor.putString(PSWKEY, password);
            }
            else {
                editor.putString(ACCOUNTKEY, "");
                editor.putString(PSWKEY, "");
            }
            editor.putBoolean(CHECKKEY, isAutoStore);
            editor.commit();
            startActivity(intent);
        }
        /*If none of the three roles be found, pop the message show the users that there is no account */
        else{
            Toast.makeText(this, "Cannot find the account, please check it", Toast.LENGTH_LONG).show();
        }
    }

    public void registerPressed(View view) {
        Intent intent = new Intent(this, SelectRoleActivity.class);
        startActivity(intent);
    }

    public void forgetPSWPressed(View view) {
        Intent intent = new Intent(this, ForgetPswActivity.class);
        startActivity(intent);
    }


    /**
     *Initialize the data in the licence table in DB
     * */
    private void initialLicenceDB() {
        /*SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (6284816, 'full')");
        SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (6274816, 'full')");
        SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (9876543, 'full')");
        SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (8765432, 'full')");*/
        SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (1234567, 'learner')");
        SQLQueryHelper.insertDatabase(this , "INSERT into licence (driver_id, type)" +
                " VALUES (1995101, 'learner')");
    }

    /**
     *Initialize the data in the learner table in DB
     * */
    private void initialLearnerDB() {
        SQLQueryHelper.insertDatabase(this, "INSERT into learner (id, email, name, psw, date_of_birth)" +
                " VALUES (1234567, 'gongfurui0452424857@gmail.com', 'Furui Gong', 'gfr199510', '1995-10-17')");
    }

    /**
     *Initialize the data in the instructor table in DB
     * */
    private void initialInstructorDB() {
        SQLQueryHelper.insertDatabase(this, "INSERT into instructor (ADI, email, name, psw)" +
                " VALUES (123, 'u6284816@anu.edu.au', 'Frank Free', 'gfr199510')");
    }

    /**
     *Initialize the data in the supervisor table in DB
     * */
    private void initialSupervisorDB() {
        SQLQueryHelper.insertDatabase(this, "INSERT into supervisor (email, name, psw)" +
                " VALUES ('13810997948@163.com', 'Yanwen Gong', 'gfr199510')");
    }

    /*
    private void initialInstructor_LearnerDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into instructor_learner (ADI, learner_id)" +
                " VALUES (123, 1234567)");
    }*/

    /**
     *Initialize the data in the supervisor_learner table in DB
     * */
    private void initialSupervisor_LearnerDB() {
        SQLQueryHelper.insertDatabase(this, "INSERT into supervisor_learner (email, learner_id)" +
                " VALUES ('13810997948@163.com', 1234567)");
    }

    /**
     *Initialize the data in the ADIList table in DB
     * */
    private void initialADIDB() {
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (123)");
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (111)");
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (234)");
    }


}
