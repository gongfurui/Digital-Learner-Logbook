package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Instructor;

public class InstructorHomePageActivityV1 extends AppCompatActivity {

    Instructor instructor;

    TextView tvInstructorName;
    TextView tvInstructorEmail;
    TextView tvInstructorADI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home_page_v1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int instructorADI = getIntent().getIntExtra("instructorADI", 0);
        instructor = SQLQueryHelper.searchInstructorTable(this,
                "SELECT * FROM instructor" +
                        " WHERE ADI = "+ instructorADI + "");
        initView();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    public void initView() {
        tvInstructorName = findViewById(R.id.tv_instructorName);
        tvInstructorEmail = findViewById(R.id.tv_instructorEmail);
        tvInstructorADI = findViewById(R.id.tv_instructorADI);
        tvInstructorName.setText(instructor.name);
        tvInstructorEmail.setText(instructor.email);
        tvInstructorADI.setText(String.valueOf(instructor.ADI));
    }

}
