package e.gongfurui.digitallearnerlogbookV2.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbookV2.Activities.InstructorLearnersActivity;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.ProgressFragment;

public class InstructorLearnerFgPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public InstructorLearnerFgPagerAdapter(FragmentManager fm) {
        super(fm);
        CompetencyFragment competencyFg;
        ProgressFragment progressFg;

        competencyFg = CompetencyFragment.newInstanceI(InstructorLearnersActivity.learnerMail,
                InstructorLearnersActivity.instructorMail);
        progressFg = ProgressFragment.newInstance(InstructorLearnersActivity.learnerMail);

        fragments.add(competencyFg) ;
        fragments.add(progressFg);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
