package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class LoginActivity extends AppCompatActivity {

    EditText et_user_name, et_psw;
    Learner learner;
    Instructor instructor;
    Supervisor supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    /**
     * Initial the parameter involved in this class
     * */
    private void init(){
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
    }

    /**
     * Action when the login button pressed
     * */
    public void loginPressed(View view) {
        String account = String.valueOf(et_user_name.getText());
        String password = String.valueOf(et_psw.getText());
        /*Search the db tables of each role: learner, instructor, supervisor*/
        learner = SQLQueryHelper.searchLearnerTable(this,
                "SELECT * FROM learner" +
                        " WHERE email = '"+ account + "' and psw = '" + password +"'");
        instructor = SQLQueryHelper.searchInstructorTable(this,
                "SELECT * FROM instructor" +
                        " WHERE email = '"+ account + "' and psw = '" + password +"'");
        supervisor = SQLQueryHelper.searchSupervisorTable(this,
                "SELECT * FROM supervisor" +
                        " WHERE email = '"+ account + "' and psw = '" + password +"'");
        if(learner != null){
            Intent intent = new Intent(this, LearnerHomeActivity.class);
            Toast.makeText(this, "Successfully login as a learner", Toast.LENGTH_LONG).show();
            /*pass the learner data to the next Activity*/
            intent.putExtra("learner", new Gson().toJson(learner));
            startActivity(intent);
        }
        //If the user is an instructor, jump to the instructor homepage
        else if(instructor != null){
            Intent intent = new Intent(this, InstructorHomePageActivityV1.class);
            Toast.makeText(this, "Successfully login as a instructor", Toast.LENGTH_LONG).show();
            intent.putExtra("instructor", new Gson().toJson(instructor));
            startActivity(intent);
        }
        //If the user is a supervisor, jump to the supervisor homepage
        else if(supervisor != null){
            Intent intent = new Intent(this, SupervisorHomePageActivityV1.class);
            Toast.makeText(this, "Successfully login as a supervisor", Toast.LENGTH_LONG).show();
            intent.putExtra("supervisor", new Gson().toJson(supervisor));
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
     * Because we don't have the manage system to store the online database,
     * we use the SQLite to initial the database we expected to interact with through the app.
     * This function is to initial the database. delete previous one and add the mock data into it
     * */
    public void InitialDBPressed(View view) {
        SQLQueryHelper.deleteDatabase(this, "test_carson");
        initialLicenceDB();
        initialLearnerDB();
        initialInstructorDB();
        initialSupervisorDB();
        initialADIDB();
        /*initialInstructor_LearnerDB();*/
        initialSupervisor_LearnerDB();
        Toast.makeText(this, "Database has been initialized", Toast.LENGTH_LONG).show();
    }

    /**
     *Initialize the data in the licence table in DB
     * */
    private void initialLicenceDB(){
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
    private void initialLearnerDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into learner (id, email, name, psw, date_of_birth)" +
                " VALUES (1234567, 'gongfurui0452424857@gmail.com', 'Furui Gong', 'gfr199510', '1995-10-17')");
    }

    /**
     *Initialize the data in the instructor table in DB
     * */
    private void initialInstructorDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into instructor (ADI, email, name, psw)" +
                " VALUES (123, 'u6284816@anu.edu.au', 'Frank Free', 'gfr199510')");
    }

    /**
     *Initialize the data in the supervisor table in DB
     * */
    private void initialSupervisorDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into supervisor (email, name, psw)" +
                " VALUES ('13810997948@163.com', 'Yanwen Gong', 'gfr199510')");
    }

    /**
     *Initialize the data in the instructor_learner table in DB
     * */
    /*private void initialInstructor_LearnerDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into instructor_learner (ADI, learner_id)" +
                " VALUES (123, 1234567)");
    }*/

    /**
     *Initialize the data in the supervisor_learner table in DB
     * */
    private void initialSupervisor_LearnerDB(){
        SQLQueryHelper.insertDatabase(this, "INSERT into supervisor_learner (email, learner_id)" +
                " VALUES ('13810997948@163.com', 1234567)");
    }

    /**
     *Initialize the data in the ADIList table in DB
     * */
    private void initialADIDB(){
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (123)");
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (111)");
        SQLQueryHelper.insertDatabase(this , "INSERT into ADIList (adi)" +
                " VALUES (234)");
    }
}
