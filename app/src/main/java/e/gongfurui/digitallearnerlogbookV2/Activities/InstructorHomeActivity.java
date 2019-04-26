package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Instructor;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class InstructorHomeActivity extends AppCompatActivity {

    Instructor instructor;

    TextView tvInstructorName;
    TextView tvInstructorEmail;
    TextView tvInstructorADI;
    private AlertDialog alert = null;
    private EditText etMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home);
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

    /**
     * Establish the AlertDialog
     * */
    public void showDialog(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.request_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        alert = builder.setTitle("Request...")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", null)
                .create();
        alert.show();
    }

    public void viewLearnerPressed(View view) {
        showDialog(this);
        etMail = alert.getWindow().findViewById(R.id.et_mail);

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //check whether the user put the verify code
                if(etMail.getText().toString().isEmpty()) {
                    Toast.makeText(InstructorHomeActivity.this,
                            "The learner email is empty. Please check!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    String learnerMail = etMail.getText().toString();
                    String token;
                    token = OnlineDBHelper.searchLeanerTokenTable(LOCAL_IP +
                            "/drive/searchLearnerToken/" + learnerMail);
                    if(token == null){
                        Toast.makeText(InstructorHomeActivity.this,
                                "The learner currently unavailable for response your request",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        OnlineDBHelper.sendFCMRequest(LOCAL_IP +
                                "/drive/sendFCM/" + instructor.name + "&" + learnerMail);

                    }
                    /*Intent intent = new Intent(StudyActivity.this,
                            LearnerHomeActivity.class);
                    intent.putExtra("learnerMail", learner.email);
                    startActivity(intent);*/
                    alert.dismiss();
                }
            }
        });
    }
}