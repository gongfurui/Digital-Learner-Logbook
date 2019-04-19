package e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class ProgressFragment extends Fragment{

    private static final String ARG_PARAM1 = "learnerMail";
    private String learnerMail;
    private Learner learner;
    private ProgressBar timeBar;
    private ProgressBar courseBar;
    private TextView tvTimeProgress;
    private TextView tvCourseProgress;
    private TextView tvDistanceProgress;
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
            learnerMail = getArguments().getString(ARG_PARAM1);
            learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + learnerMail);
        }
        for (Boolean b: learner.courseProgressList) {
            if(b) finishedCourse++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_progress, container, false);
        initViews(view);
        timeBar.setProgress((int) (learner.time*100));
        tvTimeProgress.setText(learner.time + "/120 hours");
        courseBar.setProgress(finishedCourse);
        tvCourseProgress.setText(finishedCourse + "/23 competencies");
        return view;
    }


    /**
     * Initial the UI parameter involved in this fragment
     * @param view  the view from the information fragment
     * */
    public void initViews(View view) {
        timeBar = view.findViewById(R.id.timeBar);
        tvTimeProgress = view.findViewById(R.id.tv_timeProgress);
        courseBar = view.findViewById(R.id.courseBar);
        tvCourseProgress = view.findViewById(R.id.tv_courseProgress);
        tvDistanceProgress = view.findViewById(R.id.tv_distanceProgress);
        tvDistanceProgress.setText("Distance: " + learner.distance + " km");
    }



}
