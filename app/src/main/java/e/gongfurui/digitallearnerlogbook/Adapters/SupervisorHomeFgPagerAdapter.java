package e.gongfurui.digitallearnerlogbook.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import e.gongfurui.digitallearnerlogbook.Activities.SupervisorHomeActivity;
import e.gongfurui.digitallearnerlogbook.Roles.Supervisor;
import e.gongfurui.digitallearnerlogbook.SupervisorHomeFragments.InformationFragment;
import e.gongfurui.digitallearnerlogbook.SupervisorHomeFragments.LearnerListFragment;


public class SupervisorHomeFgPagerAdapter extends FragmentPagerAdapter {
    //Maximum pages in the PagerAdapter
    private final int PAGER_COUNT = 2;
    public InformationFragment informationFg;
    public LearnerListFragment learnerListFragment;

    public ArrayList<Fragment> fragments = new ArrayList<>();


    public SupervisorHomeFgPagerAdapter(FragmentManager fm) {
        super(fm);
        informationFg = InformationFragment.newInstance(SupervisorHomeActivity.supervisorMail);
        learnerListFragment = LearnerListFragment.newInstance(SupervisorHomeActivity.supervisorMail);
        fragments.add(informationFg) ;
        fragments.add(learnerListFragment);
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
