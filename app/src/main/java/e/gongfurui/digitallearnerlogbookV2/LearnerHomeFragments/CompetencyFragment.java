package e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments;

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
import java.util.Map;

import e.gongfurui.digitallearnerlogbookV2.Activities.CompetencyActivity;
import e.gongfurui.digitallearnerlogbookV2.Adapters.CompetencyAdapter;
import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Competency;
import e.gongfurui.digitallearnerlogbookV2.Roles.CourseFeedback;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class CompetencyFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "learnerMail";
    private static final String ARG_PARAM2 = "supervisorMail";

    private String learnerMail;
    private String supervisorMail;

    Learner learner;
    private List<Competency> mData = null;
    private Context mContext;
    CompetencyAdapter mAdapter = null;
    ListView list_competency;
    private HashMap<Integer, String> nameMap = new HashMap<>();
    private HashMap<Integer, String> commentMap = new HashMap<>();


    public static CompetencyFragment newInstance(String mail) {
        CompetencyFragment fragment = new CompetencyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mail);
        fragment.setArguments(args);
        return fragment;
    }

    public static CompetencyFragment newInstanceS(String lMail, String mail) {
        CompetencyFragment fragment = new CompetencyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, lMail);
        args.putString(ARG_PARAM2, mail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Retrieve the required data from the SQLite database
            learnerMail = getArguments().getString(ARG_PARAM1);
            supervisorMail = getArguments().getString(ARG_PARAM2);

            learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + learnerMail);
            HashMap<Integer, CourseFeedback> courseFeedbackMap;
            courseFeedbackMap = OnlineDBHelper.searchCourseFeedbacksTable(LOCAL_IP +
                    "/drive/searchCourseFeedback/" + learnerMail);
            for (Map.Entry<Integer, CourseFeedback> entry : courseFeedbackMap.entrySet()){
                nameMap.put(entry.getKey(), entry.getValue().instructorName);
                commentMap.put(entry.getKey(), entry.getValue().feedback);
            }
            /*nameMap = SQLQueryHelper.getNameListFromCourseFeedbackTable(this.getContext(),
                    "SELECT course_id, instructor_name FROM courseFeedback" +
                            " WHERE learner_id = "+ learner.driver_id);
            commentMap = SQLQueryHelper.getCommentListFromCourseFeedbackTable(this.getContext(),
                    "SELECT course_id, feedback FROM courseFeedback" +
                            " WHERE learner_id = "+ learner.driver_id);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_competency, container, false);
        mContext = CompetencyFragment.this.getContext();
        list_competency = view.findViewById(R.id.list_competency);
        final LayoutInflater inflater1 = LayoutInflater.from(this.getContext());
        View headView = inflater1.inflate(R.layout.list_competency_header, null, false);
        mData = new LinkedList<>();
        addToList();
        mAdapter = new CompetencyAdapter((LinkedList<Competency>) mData, mContext);
        list_competency.addHeaderView(headView);
        list_competency.setAdapter(mAdapter);
        list_competency.setOnItemClickListener(this);
        return view;
    }

    /**
     * Add the competency details into the competency list
     * */
    public void addToList() {
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
                commentMap.get(1), learner.courseProgressList.get(0), nameMap.get(1)));
        mData.add(new Competency(2, "2 CABIN DRILL" ,
                "Adjustments required to ensure safe, effective and comfortable operation" +
                        "of the vehicle",
                "- Seat and mirrors to be incorrectly adjusted prior to assessment.\n" +
                        "- Parked and secured.\n" +
                        "- Engine not running.\n" +
                        "Student in driver's seat.",
                "The student will on at least (2) occasions during a single lesson," +
                        " demonstrate the following checks and adjustments unaided by the instructor:\n" +
                        "The student will, when asked,'Are you ready to drive?:'\n" +
                        "- Check the vehicle is parked and secured (park brake applied).\n" +
                        "- Check the seat and head restraints are correctly adjusted.\n" +
                        "- Check the internal and external mirrors are properly adjusted for appropriate rear visibility.\n" +
                        "- Check sear belts are secured for all occupants.\n- Check the steering wheel is adjusted correctly.\n" +
                        "- Check all doors are secured.",
                commentMap.get(2), learner.courseProgressList.get(1), nameMap.get(2)));
        mData.add(new Competency(3, "3 STARTING UP PROCEDURE" ,
                "Demonstrate engine start procedure",
                "Typical driving situations",
                "The student will, on at least four occasions during a single lesson," +
                        " demonstrate engine start procedure unaided by the instructor.\n" +
                        "The student will, when instructed to start the vehicle:\n" +
                        "- Ensure park brake is applied.\n- Ensure gear lever is in neutral (park for automatic).\n" +
                        "- Demonstrate use of the Inhibitor Switch (for automatic transmission vehicles only).\n" +
                        "Switch on ignition.\n- Set accelerator and choke to recommended positions (where applicable).\n" +
                        "- Monitor all gauges.\n- Monitor warning lights and buzzers.\n- Start the vehicle.\n" +
                        "- Re-check gauges.\n- Re-check warning lights.",
                commentMap.get(3), learner.courseProgressList.get(2), nameMap.get(3)));
        mData.add(new Competency(4, "4 MOVING OFF PROCEDURE" ,
                "Demonstrate moving off from a stationary position safety, efficiently" +
                        " and while maintaining full control of vehicle.",
                "- Vehicle parked adjacent to kerb in a low level traffic area, preferably" +
                        "free of parked cars, with engine running and park brake on.\n" +
                        "- Stationary, from an intersection hold line.",
                "The student will on at least (8) occasions during a single lesson, demonstrate moving off" +
                        "correctly unaided by the instructor - 4 times from a parked position and 4 times from an" +
                        "intersection hold line.\nThe student will, when instructed to move off:\n- Depress cluth (manual).\n" +
                        "- Select correct gear.\n- Check braking is applied (auto or manual).\n- Check Mirrors\n" +
                        "- Indicate.\n- Apply appropriate engine revs (acceleration)\n- Release clutch to friction point (manual).\n" +
                        "- Check all mirrors.\n- Check blind spot (head check where applicable).\n" +
                        "- Release park brake (where necessary).\n- Gently release clutch (where applicable).\n" +
                        "- Headcheck as vehicle begins to move.\n- Not roll backward.\n - Accelerate smoothly.\n" +
                        "- Cancel indicator.\n- Recheck mirrors.\n- Not unnecessarily obstruct traffic.",
                commentMap.get(4), learner.courseProgressList.get(3), nameMap.get(4)));
        mData.add(new Competency(5, "5 GEAR CHANGING" ,
                "",
                "",
                "",
                commentMap.get(5), learner.courseProgressList.get(4), nameMap.get(5)));
        mData.add(new Competency(6, "6 STEERING CONTROL" ,
                "",
                "",
                "",
                commentMap.get(6), learner.courseProgressList.get(5), nameMap.get(6)));
        mData.add(new Competency(7, "7 TURNS, LEFT AND RIGHT" ,
                "",
                "",
                "",
                commentMap.get(7), learner.courseProgressList.get(6), nameMap.get(7)));
        mData.add(new Competency(8, "8 SPEED CONTROL" ,
                "",
                "",
                "",
                commentMap.get(8), learner.courseProgressList.get(7), nameMap.get(8)));
        mData.add(new Competency(9, "9 SLOWING PROCEDURE" ,
                "",
                "",
                "",
                commentMap.get(9), learner.courseProgressList.get(8), nameMap.get(9)));
        mData.add(new Competency(10, "10 STOPPING PROCEDURE" ,
                "",
                "",
                "",
                commentMap.get(10), learner.courseProgressList.get(9), nameMap.get(10)));
        mData.add(new Competency(11, "11 HILL STARTS" ,
                "",
                "",
                "",
                commentMap.get(11), learner.courseProgressList.get(10), nameMap.get(11)));
        mData.add(new Competency(12, "12 GIVE WAT RULES" ,
                "",
                "",
                "",
                commentMap.get(12), learner.courseProgressList.get(11), nameMap.get(12)));
        mData.add(new Competency(13, "13 STEERING CONTROL" ,
                "",
                "",
                "",
                commentMap.get(13), learner.courseProgressList.get(12), nameMap.get(13)));
        mData.add(new Competency(14, "14 TURNS, LEFT AND RIGHT" ,
                "",
                "",
                "",
                commentMap.get(14), learner.courseProgressList.get(13), nameMap.get(14)));
        mData.add(new Competency(15, "15 REVERSE PARALLEL PARKING" ,
                "",
                "",
                "",
                commentMap.get(15), learner.courseProgressList.get(14), nameMap.get(15)));
        mData.add(new Competency(16, "16 U TURNS" ,
                "",
                "",
                "",
                commentMap.get(16), learner.courseProgressList.get(15), nameMap.get(16)));
        mData.add(new Competency(17, "17 TURNING AROUND IN THE ROAD, EG THREE POINT TURN" ,
                "",
                "",
                "",
                commentMap.get(17), learner.courseProgressList.get(16), nameMap.get(17)));
        mData.add(new Competency(24, "REVIEW AND CHECK SKILLS (C1-C17)" ,
                "",
                "",
                "",
                "", false, ""));
        mData.add(new Competency(18, "18 LANE CHANGING, MERGING< ENTERING FREEWAYS< FORM ONE LANE" ,
                "",
                "",
                "",
                commentMap.get(18), learner.courseProgressList.get(17), nameMap.get(18)));
        mData.add(new Competency(19, "19 OVERTAKING" ,
                "",
                "",
                "",
                commentMap.get(19), learner.courseProgressList.get(18), nameMap.get(19)));
        mData.add(new Competency(20, "20 OBSERVATION SKILL, VISUAL SEARCHING AND SCANNING, HAZARD RECOGNITION" ,
                "",
                "",
                "",
                commentMap.get(20), learner.courseProgressList.get(19), nameMap.get(20)));
        mData.add(new Competency(21, "21 COMPLIANCE WITH THE SYSTEM OF VEHICLE CONTROL" ,
                "",
                "",
                "",
                commentMap.get(21), learner.courseProgressList.get(20), nameMap.get(21)));
        mData.add(new Competency(22, "22 VULNERABLE ROAD USERS" ,
                "",
                "",
                "",
                commentMap.get(22), learner.courseProgressList.get(21), nameMap.get(22)));
        mData.add(new Competency(25, "REVIEW AND CHECK SKILLS (C1-22)",
                "",
                "",
                "",
                "", false, ""));
        mData.add(new Competency(23, "23 FINAL DRIVE ON BUSY ROADS AND UNFAMILIAR ROADS",
                "",
                "",
                "",
                commentMap.get(23), learner.courseProgressList.get(22), nameMap.get(23)));
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            if(position == 18 || position == 24 || position == 25){

            }
            else {
                Intent intent = new Intent(this.mContext, CompetencyActivity.class);
                intent.putExtra("competency", new Gson().toJson(mData.get(position - 1)));
                intent.putExtra("learner", new Gson().toJson(learner));
                intent.putExtra("supervisorMail", supervisorMail);
                startActivity(intent);
            }
        }
    }
}
