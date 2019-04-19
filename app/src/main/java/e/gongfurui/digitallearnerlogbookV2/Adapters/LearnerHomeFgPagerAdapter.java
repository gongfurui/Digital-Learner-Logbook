package e.gongfurui.digitallearnerlogbookV2.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbookV2.Activities.LearnerHomeActivity;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.InformationFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.PracticeFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.ProgressFragment;

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
        informationFg = InformationFragment.newInstance(LearnerHomeActivity.learnerMail);
        competencyFg = CompetencyFragment.newInstance(LearnerHomeActivity.learnerMail);
        practiceFg = PracticeFragment.newInstance(LearnerHomeActivity.learnerMail);
        progressFg = ProgressFragment.newInstance(LearnerHomeActivity.learnerMail);
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
