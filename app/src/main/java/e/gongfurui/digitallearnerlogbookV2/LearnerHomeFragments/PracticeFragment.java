package e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import e.gongfurui.digitallearnerlogbookV2.Adapters.PracticeAdapter;
import e.gongfurui.digitallearnerlogbookV2.GPSActivities.MainActivity;
import e.gongfurui.digitallearnerlogbookV2.GPSActivities.MapsActivity;
import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;
import e.gongfurui.digitallearnerlogbookV2.Roles.Route;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;


public class PracticeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "learnerMail";
    private static final String ARG_PARAM2 = "supervisorMail";

    String learnerMail;
    String supervisorMail;
    Learner learner;

    private List<Route> mData = null;
    private Context mContext;
    PracticeAdapter mAdapter = null;
    ListView list_route;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Route> routeMap = new HashMap<>();

    public static PracticeFragment newInstance(String mail) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mail);
        fragment.setArguments(args);
        return fragment;
    }

    public static PracticeFragment newInstanceS(String mail, String supervisorMail) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mail);
        args.putString(ARG_PARAM2, supervisorMail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*Retrieve the required data from the SQLite database*/
            learnerMail = getArguments().getString(ARG_PARAM1);
            supervisorMail =getArguments().getString(ARG_PARAM2);

            learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                    "/drive/searchLearnerByMail/" + learnerMail);
            routeMap = OnlineDBHelper.searchRoutesTable(LOCAL_IP +
                    "/drive/searchRoute/" + learnerMail);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_practice, container, false);
        mContext = PracticeFragment.this.getContext();
        list_route = view.findViewById(R.id.list_practice);



        final LayoutInflater inflater1 = LayoutInflater.from(this.getContext());
        @SuppressLint("InflateParams") View headView = inflater1.inflate(R.layout.list_practice_header, null, false);
        @SuppressLint("InflateParams") View footView = inflater1.inflate(R.layout.list_practice_footer, null, false);
        mData = new LinkedList<>();
        addToList();
        mAdapter = new PracticeAdapter((LinkedList<Route>) mData, mContext);
        list_route.addHeaderView(headView);
        if(supervisorMail == null) list_route.addFooterView(footView);
        list_route.setAdapter(mAdapter);
        list_route.setOnItemClickListener(this);
        if(supervisorMail == null) {
            Button btn_practicing = view.findViewById(R.id.btn_practicing);

            btn_practicing.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("learnerMail", learnerMail);
                if(routeMap != null) intent.putExtra("newRouteID", routeMap.size() + 1);
                startActivity(intent);
            });
        }
        return view;
    }

    /**
     * Add the route details into the route list
     * */
    public void addToList() {
        if(routeMap != null) {
            for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                if(!entry.getValue().isApproved) mData.add(entry.getValue());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            Intent intent = new Intent(mContext, MapsActivity.class);
            intent.putExtra("route", new Gson().toJson(mData.get(position - 1)));
            intent.putExtra("supervisorMail", supervisorMail);
            intent.putExtra("learnerMail", learnerMail);
            startActivity(intent);

        }
    }
}
