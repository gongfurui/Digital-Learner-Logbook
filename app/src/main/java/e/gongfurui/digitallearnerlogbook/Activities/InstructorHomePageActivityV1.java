package e.gongfurui.digitallearnerlogbook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

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
        String instructorJson=getIntent().getStringExtra("instructor");
        instructor=new Gson().fromJson(instructorJson, Instructor.class);
        init();
    }

    /**
     * Initial the parameter involved in this class
     * */
    public void init(){
        tv_instructorName = findViewById(R.id.tv_instructorName);
        tv_instructorEmail = findViewById(R.id.tv_instructorEmail);
        tv_instructorADI = findViewById(R.id.tv_instructorADI);
        tv_instructorName.setText(instructor.name);
        tv_instructorEmail.setText(instructor.email);
        tv_instructorADI.setText(String.valueOf(instructor.ADI));
    }

}
