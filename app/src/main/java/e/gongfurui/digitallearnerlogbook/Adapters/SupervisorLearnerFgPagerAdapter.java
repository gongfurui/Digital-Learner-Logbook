package e.gongfurui.digitallearnerlogbook.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbook.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.PracticeFragment;

public class SupervisorLearnerFgPagerAdapter extends FragmentPagerAdapter {
    //Maximum pages in the PagerAdapter
    private final int PAGER_COUNT = 2;
    public CompetencyFragment competencyFg;
    public PracticeFragment practiceFg;

    public ArrayList<Fragment> fragments = new ArrayList<>();


    public SupervisorLearnerFgPagerAdapter(FragmentManager fm) {
        super(fm);
        competencyFg = CompetencyFragment.newInstanceS(SupervisorLearnersActivity.learnerID,
                SupervisorLearnersActivity.supervisorMail);
        practiceFg = PracticeFragment.newInstanceS(SupervisorLearnersActivity.learnerID,
                SupervisorLearnersActivity.supervisorMail);
        fragments.add(competencyFg) ;
        fragments.add(practiceFg);
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
