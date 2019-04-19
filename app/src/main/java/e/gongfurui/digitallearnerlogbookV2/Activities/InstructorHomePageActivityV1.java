package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

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
        String instructorMail = getIntent().getStringExtra("instructorMail");
        instructor = OnlineDBHelper.searchInstructorTable(LOCAL_IP +
                "/drive/searchInstructorByMail/" + instructorMail);
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
