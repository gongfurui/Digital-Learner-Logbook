package e.gongfurui.digitallearnerlogbook.SupervisorHomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbook.R;

public class LearnerListFragment extends Fragment {

    public LearnerListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        TextView txt_content = view.findViewById(R.id.txt_content);
        txt_content.setText("Second Fragment");
        return view;
    }
}
