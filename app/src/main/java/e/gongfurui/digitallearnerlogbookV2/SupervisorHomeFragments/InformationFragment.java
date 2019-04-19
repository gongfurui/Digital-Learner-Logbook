package e.gongfurui.digitallearnerlogbookV2.SupervisorHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbookV2.Helpers.SQLQueryHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Supervisor;

public class InformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "supervisorMail";
    String supervisorMail;
    TextView tvSupervisorName;
    TextView tvSupervisorEmail;
    Supervisor supervisor;


    public static InformationFragment newInstance(String email) {
        InformationFragment fragment = new InformationFragment();
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
            supervisor = SQLQueryHelper.searchSupervisorTable(this.getContext(),
                    "SELECT * FROM supervisor" +
                            " WHERE email = '" + supervisorMail + "'");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_s_information, container, false);
        initView(view);
        tvSupervisorName.setText(supervisor.name);
        tvSupervisorEmail.setText(supervisor.email);
        return view;
    }

    /**
     * Initial the UI parameter involved in this fragment
     * @param view  the view from the information fragment
     * */
    public void initView(View view) {
        tvSupervisorName = view.findViewById(R.id.tv_supervisorName);
        tvSupervisorEmail = view.findViewById(R.id.tv_supervisorEmail);
    }


}
