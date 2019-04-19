package e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "learnerMail";
    String learnerMail;
    TextView tv_learnerName, tv_learnerID, tv_learnerDob, tv_learnerEmail;
    Learner learner;


    public static InformationFragment newInstance(String mail) {
        InformationFragment fragment = new InformationFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_l_information, container, false);
        initView(view);
        tv_learnerName.setText(learner.name);
        tv_learnerDob.setText(learner.date_of_birth);
        tv_learnerID.setText(String.valueOf(learner.driver_id));
        tv_learnerEmail.setText(learner.email);
        return view;
    }

    /**
     * Initial the UI parameter involved in this fragment
     * @param view  the view from the information fragment
     * */
    public void initView(View view) {
        tv_learnerName = view.findViewById(R.id.tv_learnerName);
        tv_learnerID = view.findViewById(R.id.tv_learnerDriverID);
        tv_learnerDob = view.findViewById(R.id.tv_learnerDob);
        tv_learnerEmail = view.findViewById(R.id.tv_learnerEmail);
    }


}
