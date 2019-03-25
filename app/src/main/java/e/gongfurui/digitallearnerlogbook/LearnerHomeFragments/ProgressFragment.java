package e.gongfurui.digitallearnerlogbook.LearnerHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class ProgressFragment extends Fragment{

    private static final String ARG_PARAM1 = "learnerID";
    int learnerID;
    Learner learner;
    ProgressBar timeBar, courseBar;
    TextView tv_timeProgress, tv_courseProgress;
    int finishedCourse;


    public static ProgressFragment newInstance(int id) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            learnerID = getArguments().getInt(ARG_PARAM1);
            learner = SQLQueryHelper.searchLearnerTable(this.getContext(),
                    "SELECT * FROM learner" +
                            " WHERE id = "+ learnerID);
        }
        for (Boolean b: learner.courseProgressList) {
            if(b) finishedCourse++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_progress, container, false);
        timeBar = view.findViewById(R.id.timeBar);
        tv_timeProgress = view.findViewById(R.id.tv_timeProgress);
        courseBar = view.findViewById(R.id.courseBar);
        tv_courseProgress = view.findViewById(R.id.tv_courseProgress);
        timeBar.setProgress((int) (learner.time*100));
        tv_timeProgress.setText(learner.time + "/120 hours");
        courseBar.setProgress(finishedCourse);
        tv_courseProgress.setText(finishedCourse + "/23 competencies");
        return view;
    }



}
