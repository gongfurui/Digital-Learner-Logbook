package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class SupervisorHomePageActivityV1 extends AppCompatActivity {

    Supervisor supervisor;

    TextView tv_supervisorName, tv_supervisorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home_page_v1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String supervisorJson = getIntent().getStringExtra("supervisor");
        supervisor = new Gson().fromJson(supervisorJson, Supervisor.class);
        init();
    }

    /**
     * Initial the parameter involved in this class
     * */
    public void init(){
        tv_supervisorName = findViewById(R.id.tv_supervisorName);
        tv_supervisorEmail = findViewById(R.id.tv_supervisorEmail);
        tv_supervisorName.setText(supervisor.name);
        tv_supervisorEmail.setText(supervisor.email);
    }
}
