package e.gongfurui.digitallearnerlogbook.LearnerHomeFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import e.gongfurui.digitallearnerlogbook.Activities.CompetencyActivity;
import e.gongfurui.digitallearnerlogbook.Adapters.CompetencyAdapter;
import e.gongfurui.digitallearnerlogbook.Adapters.PracticeAdapter;
import e.gongfurui.digitallearnerlogbook.GPSActivities.MainActivity;
import e.gongfurui.digitallearnerlogbook.GPSActivities.MapsActivity;
import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;
import e.gongfurui.digitallearnerlogbook.Roles.Route;


public class PracticeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "learnerID";
    int learnerID;
    Learner learner;
    private List<Route> mData = null;
    private Context mContext;
    PracticeAdapter mAdapter = null;
    ListView list_route;
    private HashMap<Integer, Route> routeMap = new HashMap<>();

    public static PracticeFragment newInstance(int id) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*Retrieve the required data from the SQLite database*/
            learnerID = getArguments().getInt(ARG_PARAM1);
            learner = SQLQueryHelper.searchLearnerTable(this.getContext(),
                    "SELECT * FROM learner" +
                            " WHERE id = "+ learnerID);
            routeMap = SQLQueryHelper.getRouteMapFromRouteTable(this.getContext(),
                    "SELECT * FROM route " +
                            "WHERE learnerID = " + learnerID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_practice, container, false);
        mContext = PracticeFragment.this.getContext();
        list_route = view.findViewById(R.id.list_practice);



        final LayoutInflater inflater1 = LayoutInflater.from(this.getContext());
        View headView = inflater1.inflate(R.layout.list_practice_header, null, false);
        View footView = inflater1.inflate(R.layout.list_practice_footer, null, false);
        mData = new LinkedList<>();
        addToList();
        mAdapter = new PracticeAdapter((LinkedList<Route>) mData, mContext);
        list_route.addHeaderView(headView);
        list_route.addFooterView(footView);
        list_route.setAdapter(mAdapter);
        list_route.setOnItemClickListener(this);

        Button btn_practicing = view.findViewById(R.id.btn_practicing);

        btn_practicing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("learnerID", learnerID);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Add the route details into the route list
     * */
    public void addToList() {
        if(routeMap != null) {
            for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                mData.add(entry.getValue());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            Intent intent = new Intent(mContext, MapsActivity.class);
            intent.putExtra("route", new Gson().toJson(mData.get(position - 1)));
            startActivity(intent);

        }
    }
}
