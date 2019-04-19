package e.gongfurui.digitallearnerlogbookV2.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbookV2.Activities.SupervisorLearnersActivity;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.CompetencyFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.PracticeFragment;
import e.gongfurui.digitallearnerlogbookV2.LearnerHomeFragments.ProgressFragment;

public class SupervisorLearnerFgPagerAdapter extends FragmentPagerAdapter {



    private ArrayList<Fragment> fragments = new ArrayList<>();


    public SupervisorLearnerFgPagerAdapter(FragmentManager fm) {
        super(fm);
        CompetencyFragment competencyFg;
        PracticeFragment practiceFg;
        ProgressFragment progressFg;

        competencyFg = CompetencyFragment.newInstanceS(SupervisorLearnersActivity.learnerMail,
                SupervisorLearnersActivity.supervisorMail);
        practiceFg = PracticeFragment.newInstanceS(SupervisorLearnersActivity.learnerMail,
                SupervisorLearnersActivity.supervisorMail);
        progressFg = ProgressFragment.newInstance(SupervisorLearnersActivity.learnerMail);

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
