package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;

public class InstructorHomePageActivityV1 extends AppCompatActivity {

    Instructor instructor;

    TextView tv_instructorName, tv_instructorEmail, tv_instructorADI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home_page_v1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int instructorADI=getIntent().getIntExtra("instructorADI", 0);
        instructor = SQLQueryHelper.searchInstructorTable(this,
                "SELECT * FROM instructor" +
                        " WHERE ADI = "+ instructorADI + "");
        initView();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    public void initView(){
        tv_instructorName = findViewById(R.id.tv_instructorName);
        tv_instructorEmail = findViewById(R.id.tv_instructorEmail);
        tv_instructorADI = findViewById(R.id.tv_instructorADI);
        tv_instructorName.setText(instructor.name);
        tv_instructorEmail.setText(instructor.email);
        tv_instructorADI.setText(String.valueOf(instructor.ADI));
    }

}
