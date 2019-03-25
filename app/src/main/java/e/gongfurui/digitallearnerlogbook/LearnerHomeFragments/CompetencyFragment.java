package e.gongfurui.digitallearnerlogbook.LearnerHomeFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import e.gongfurui.digitallearnerlogbook.Activities.CompetencyActivity;
import e.gongfurui.digitallearnerlogbook.Adapters.CompetencyAdapter;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class CompetencyFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "learnerID";
    int learnerID;
    Learner learner;
    private List<Competency> mData = null;
    private Context mContext;
    private CompetencyAdapter mAdapter = null;
    private ListView list_competency;
    private HashMap<Integer, String> nameMap = new HashMap<>();


    public static CompetencyFragment newInstance(int id) {
        CompetencyFragment fragment = new CompetencyFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_competency, container, false);
        mContext = CompetencyFragment.this.getContext();
        list_competency = view.findViewById(R.id.list_competency);
        final LayoutInflater inflater1 = LayoutInflater.from(this.getContext());
        View headView = inflater1.inflate(R.layout.list_header, null, false);
        mData = new LinkedList<>();
        addToList();
        mAdapter = new CompetencyAdapter((LinkedList<Competency>) mData, mContext);
        list_competency.addHeaderView(headView);
        list_competency.setAdapter(mAdapter);
        list_competency.setOnItemClickListener(this);
        return view;
    }

    public void addToList(){
        nameMap = SQLQueryHelper.getNameListFromCourseFeedbackTable(this.getContext(),
                "SELECT course_id, instructor_name FROM courseFeedback" +
                        " WHERE learner_id = "+ learner.driver_id);
        mData.add(new Competency(1, "1 VEHICLE CONTROLS" ,
                "Locate, identify and describe the function and operation of all" +
                        " controls, gauges and warning lights.",
                "- Vehicle parked and secured in low level traffic area.\n" +
                        "- Engine not running.",
                "The student will, on at least two (2) occasions during a single lesson, " +
                        "locate identify and describe the function and operation of the following controls" +
                        " unaided by the instructor:\n" +
                        "- Ignition switch.\n- Steering lock.\n- Indicators.\n- Hazard lights.\n" +
                        "- Washer/wipers.\n- Warning lights/gauges/buzzers.\n- Accelerator.\n- Clutch(if applicable).\n" +
                        "- Footbrake.\n- Footbrace(if fitted) or bracing point.\n- Park brake.\n- Gear position.\n" +
                        "- Steering wheel, including adjustments.\n- Horn.\n- Air conditioning system.\n" +
                        "- Heating system.\n- Demisting system(front and rear).\n- All lighting controls(including high/low beam control).\n" +
                        "- All door controls.\n- Other controls as applicable to the assessment vehicle.Eg cruise control, " +
                        "bonnet release, central locking, electric mirror operation and/or special modifications.",
                "", learner.courseProgressList.get(0), nameMap.get(1)));
        mData.add(new Competency(2, "2 CABIN DRILL" ,
                "Adjustments required to ensure safe, effective and comfortable operation" +
                        "of the vehicle",
                "- Seat and mirrors to be incorrectly adjusted prior to assessment.\n" +
                        "- Parked and secured.\n" +
                        "- Engine not running.\n" +
                        "Student in driver's seat.",
                "The student will on at least (2) occasions during a single lesson," +
                        " demonstrate the following checks and adjustments unaided by the instructor:\n" +
                        "The student will, when asked,'Are you ready to drive?:'",
                "", learner.courseProgressList.get(1), nameMap.get(2)));
        mData.add(new Competency(3, "3 STARTING UP PROCEDURE" ,
                "Demonstrate engine start procedure",
                "Typical driving situations",
                "The student will, on at least four occasions during a single lesson," +
                        " demonstrate engine start procedure unaided by the instructor.\n" +
                        "The student will, when instructed to start the vehicle",
                "", learner.courseProgressList.get(2), nameMap.get(3)));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            Intent intent = new Intent(this.mContext, CompetencyActivity.class);
            intent.putExtra("competency", new Gson().toJson(mData.get(position-1)));
            intent.putExtra("learner", new Gson().toJson(learner));
            startActivity(intent);
        }
    }
}
