package e.gongfurui.digitallearnerlogbook.SupervisorHomeFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import e.gongfurui.digitallearnerlogbook.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbook.Adapters.LearnerListAdapter;
import e.gongfurui.digitallearnerlogbook.Adapters.PracticeAdapter;
import e.gongfurui.digitallearnerlogbook.GPSActivities.MapsActivity;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.PracticeFragment;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Route;

public class LearnerListFragment extends Fragment implements AdapterView.OnItemClickListener  {

    private static final String ARG_PARAM1 = "supervisorMail";

    String supervisorMail;

    private List<Learner> mData = null;
    private Context mContext;
    private HashMap<Integer, Learner> learnerMap = new HashMap<>();
    LearnerListAdapter mAdapter = null;
    private Learner learner;
    private ArrayList <Integer> learnerIDList = new ArrayList<>();
    private ListView list_learner;


    public static LearnerListFragment newInstance(String email) {
        LearnerListFragment fragment = new LearnerListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            supervisorMail = getArguments().getString(ARG_PARAM1);
            //Retrieve the required data from the SQLite database
            learnerIDList = SQLQueryHelper.searchIDsFromSupervisorLearnerTable(this.getContext(),
                    "SELECT learner_id FROM supervisor_learner " +
                            "WHERE email = '" + supervisorMail + "'");
            for (Integer learnerID : learnerIDList) {
                learner = SQLQueryHelper.searchLearnerTable(this.getContext(),
                        "SELECT * FROM learner" +
                                " WHERE id = "+ learnerID);
                learnerMap.put(learner.driver_id, learner);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_learner_list, container, false);
        mContext = LearnerListFragment.this.getContext();
        list_learner = view.findViewById(R.id.list_learner);
        final LayoutInflater inflater1 = LayoutInflater.from(this.getContext());
        View headView = inflater1.inflate(R.layout.list_learner_header, null, false);
        mData = new LinkedList<>();
        addToList();
        mAdapter = new LearnerListAdapter((LinkedList<Learner>) mData, mContext);
        list_learner.addHeaderView(headView);
        list_learner.setAdapter(mAdapter);
        list_learner.setOnItemClickListener(this);
        return view;
    }

    /**
     * Add the learner details into the learner list
     * */
    public void addToList() {
        if(learnerMap != null) {
            for (Map.Entry<Integer, Learner> entry : learnerMap.entrySet()) {
                mData.add(entry.getValue());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            Intent intent = new Intent(mContext, SupervisorLearnersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("learnerID", mData.get(position - 1).driver_id);
            bundle.putString("supervisorMail", supervisorMail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
