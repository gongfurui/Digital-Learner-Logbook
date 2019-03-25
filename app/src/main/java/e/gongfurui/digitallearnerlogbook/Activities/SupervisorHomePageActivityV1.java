package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;

public class SupervisorHomePageActivityV1 extends AppCompatActivity {

    Supervisor supervisor;

    TextView tv_supervisorName, tv_supervisorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home_page_v1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String supervisorMail = getIntent().getStringExtra("supervisorMail");
        supervisor = SQLQueryHelper.searchSupervisorTable(this,
                "SELECT * FROM supervisor" +
                        " WHERE email = '"+ supervisorMail + "'");
        initView();
    }

    /**
     * Initial the parameter involved in this class
     * */
    public void initView(){
        tv_supervisorName = findViewById(R.id.tv_supervisorName);
        tv_supervisorEmail = findViewById(R.id.tv_supervisorEmail);
        tv_supervisorName.setText(supervisor.name);
        tv_supervisorEmail.setText(supervisor.email);
    }
}
