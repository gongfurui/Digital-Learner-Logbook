package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

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
        if(instructorMail == null) {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    if (key.equals("instructor_mail")) {
                        instructorMail = getIntent().getExtras().getString(key);
                    }
                }
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
     *Menu clicking event
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_add_item:
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            // Get new Instance ID token
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            OnlineDBHelper.insertTable(LOCAL_IP +
                                    "/drive/deleteLearnerToken/" + instructor.email + "&" + token);
                        });
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            default:
                break;
        }
        return true;
    }


    /**
     * Establish the AlertDialog
     * */
    public void showDialog(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.request_dialog, null);
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
        etMail = Objects.requireNonNull(alert.getWindow()).findViewById(R.id.et_mail);

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
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
                            "/drive/sendFCM/" + instructor.name + "&" + instructor.email + "&" +
                            learnerMail);

                }
                alert.dismiss();
            }
        });
    }
}
