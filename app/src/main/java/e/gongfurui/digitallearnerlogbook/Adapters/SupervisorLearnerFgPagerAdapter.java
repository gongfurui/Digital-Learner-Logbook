package e.gongfurui.digitallearnerlogbook.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbook.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.PracticeFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.ProgressFragment;

public class SupervisorLearnerFgPagerAdapter extends FragmentPagerAdapter {



    private ArrayList<Fragment> fragments = new ArrayList<>();


    public SupervisorLearnerFgPagerAdapter(FragmentManager fm) {
        super(fm);
        CompetencyFragment competencyFg;
        PracticeFragment practiceFg;
        ProgressFragment progressFg;

        competencyFg = CompetencyFragment.newInstanceS(SupervisorLearnersActivity.learnerID,
                SupervisorLearnersActivity.supervisorMail);
        practiceFg = PracticeFragment.newInstanceS(SupervisorLearnersActivity.learnerID,
                SupervisorLearnersActivity.supervisorMail);
        progressFg = ProgressFragment.newInstance(SupervisorLearnersActivity.learnerID);

        fragments.add(competencyFg) ;
        fragments.add(practiceFg);
        fragments.add(progressFg);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
