package e.gongfurui.digitallearnerlogbook.LearnerHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import e.gongfurui.digitallearnerlogbook.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "learnerID";
    int learnerID;
    TextView tv_learnerName, tv_learnerID, tv_learnerDob, tv_learnerEmail;
    Learner learner;


    public static InformationFragment newInstance(int id) {
        InformationFragment fragment = new InformationFragment();
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
        View view = inflater.inflate(R.layout.fg_information, container, false);
        initView(view);
        tv_learnerName.setText(learner.name);
        tv_learnerDob.setText(learner.date_of_birth);
        tv_learnerID.setText(String.valueOf(learner.driver_id));
        tv_learnerEmail.setText(learner.email);
        return view;
    }

    /**
     * Initial the UI parameter involved in this fragment
     * */
    public void initView(View view){
        tv_learnerName = view.findViewById(R.id.tv_learnerName);
        tv_learnerID = view.findViewById(R.id.tv_learnerDriverID);
        tv_learnerDob = view.findViewById(R.id.tv_learnerDob);
        tv_learnerEmail = view.findViewById(R.id.tv_learnerEmail);
    }


}
