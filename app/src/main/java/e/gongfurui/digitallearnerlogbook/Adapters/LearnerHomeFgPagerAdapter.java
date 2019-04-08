package e.gongfurui.digitallearnerlogbook.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbook.Activities.LearnerHomeActivity;
import e.gongfurui.digitallearnerlogbook.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.InformationFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.PracticeFragment;
import e.gongfurui.digitallearnerlogbook.LearnerHomeFragments.ProgressFragment;

public class LearnerHomeFgPagerAdapter extends FragmentPagerAdapter {

    //Maximum pages in the PagerAdapter
    private final int PAGER_COUNT = 4;
    public InformationFragment informationFg;
    public CompetencyFragment competencyFg;
    public PracticeFragment practiceFg;
    public ProgressFragment progressFg;

    public ArrayList<Fragment> fragments = new ArrayList<>();


    public LearnerHomeFgPagerAdapter(FragmentManager fm) {
        super(fm);
        informationFg = InformationFragment.newInstance(LearnerHomeActivity.learnerID);
        competencyFg = CompetencyFragment.newInstance(LearnerHomeActivity.learnerID);
        practiceFg = PracticeFragment.newInstance(LearnerHomeActivity.learnerID);
        progressFg = ProgressFragment.newInstance(LearnerHomeActivity.learnerID);
        fragments.add(informationFg) ;
        fragments.add(competencyFg);
        fragments.add(practiceFg);
        fragments.add(progressFg);
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
