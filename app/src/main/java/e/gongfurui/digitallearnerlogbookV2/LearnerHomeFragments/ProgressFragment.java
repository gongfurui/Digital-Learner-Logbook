package e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.CourseFeedback;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class ProgressFragment extends Fragment{

    private static final String ARG_PARAM1 = "learnerMail";
    private Learner learner;
    private ProgressBar timeBar;
    private ProgressBar courseBar;
    private TextView tvTimeProgress;
    private TextView tvCourseProgress;
    private String instructorName;
    private int instructorADI;
    private String lastDate;
    private HashMap<Integer, CourseFeedback> courseFeedbackMap;
    int finishedCourse;


    public static ProgressFragment newInstance(String mail) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String learnerMail;
            learnerMail = getArguments().getString(ARG_PARAM1);
            learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + learnerMail);
            courseFeedbackMap = OnlineDBHelper.searchCourseFeedbacksTable(LOCAL_IP +
                    "/drive/searchCourseFeedback/" + learnerMail);

        }
        for (Boolean b: learner.courseProgressList) {
            if(b) finishedCourse++;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_progress, container, false);
        initViews(view);
        timeBar.setProgress((int) (learner.time * 100));
        tvTimeProgress.setText(learner.time + "/120 hours");
        courseBar.setProgress(finishedCourse);
        tvCourseProgress.setText(finishedCourse + "/23 competencies");
        return view;
    }


    /**
     * Initial the UI parameter involved in this fragment
     * @param view  the view from the information fragment
     * */
    @SuppressLint("SetTextI18n")
    public void initViews(View view) {
        TextView tvDistanceProgress;
        timeBar = view.findViewById(R.id.timeBar);
        tvTimeProgress = view.findViewById(R.id.tv_timeProgress);
        courseBar = view.findViewById(R.id.courseBar);
        tvCourseProgress = view.findViewById(R.id.tv_courseProgress);
        Button btnApply = view.findViewById(R.id.btn_appoint);
        Button btnIssue = view.findViewById(R.id.btn_issue);

        tvDistanceProgress = view.findViewById(R.id.tv_distanceProgress);
        tvDistanceProgress.setText("Distance: " + learner.distance + " km");

        btnApply.setOnClickListener(v -> {
            if(finishedCourse != 0) {
                Toast.makeText(this.getContext(), "You already learnt the competency",
                        Toast.LENGTH_LONG).show();
            } else {
                if(learner.time < 120) {
                    Toast.makeText(this.getContext(), "You should at least fulfill the 120 " +
                            "driving hours requirements", Toast.LENGTH_LONG).show();
                } else {
                    boolean isInList = OnlineDBHelper.checkApplyList(LOCAL_IP +
                            "/drive/searchApplyExamList/");
                    if(isInList) {
                        Toast.makeText(this.getContext(), "You already apply for a exam, please" +
                                        " don't apply it again", Toast.LENGTH_LONG).show();
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        String date = year + "-" + month + "-" + day;
                        OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertApplyExamList/" +
                                learner.driver_id + "&" + learner.name + "&" + learner.time + "&" +
                                learner.date_of_birth + "&" + date);
                        Toast.makeText(this.getContext(), "Your application has sent to the " +
                                "transport authority", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnIssue.setOnClickListener(v -> {
            if(learner.time < 120 || finishedCourse < 23) {
                Toast.makeText(this.getContext(), "You should at least fulfill the 120 " +
                                "driving hours requirements and 23 competencies",
                        Toast.LENGTH_LONG).show();
            } else {
                boolean isInList = OnlineDBHelper.checkIssueList(LOCAL_IP +
                        "/drive/searchIssueLicenceList/");
                if(isInList) {
                    Toast.makeText(this.getContext(), "You already apply for issuing the " +
                            "driver licence please don't apply it again", Toast.LENGTH_LONG).show();
                } else {
                    instructorName = Objects.requireNonNull(courseFeedbackMap.get(23)).instructorName;
                    instructorADI = Objects.requireNonNull(courseFeedbackMap.get(23)).ADI;
                    lastDate = Objects.requireNonNull(courseFeedbackMap.get(23)).date;
                    OnlineDBHelper.insertTable(LOCAL_IP + "/drive/insertIssueLicenceList/" +
                            learner.driver_id + "&" + learner.name + "&" + instructorName + "&" +
                            instructorADI + "&" + learner.time + "&" + learner.date_of_birth + "&" +
                            lastDate);
                    Toast.makeText(this.getContext(), "Your application has sent to the " +
                            "transport authority", Toast.LENGTH_LONG).show();
                }
            }
        });
    }





}
