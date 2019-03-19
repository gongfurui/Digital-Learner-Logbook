package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class SelectRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Jump to the register interface of learner
     * */
    public void registerLearnerPressed(View view) {
        Intent intent = new Intent(this, LearnerRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Jump to the register interface of instructor
     * */
    public void registerInstructorPressed(View view) {
        Intent intent = new Intent(this, InstructorRegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Jump to the register interface of supervisor
     * */
    public void registerSupervisorPressed(View view) {
        Intent intent = new Intent(this, SupervisorRegisterActivity.class);
        startActivity(intent);
    }
}
