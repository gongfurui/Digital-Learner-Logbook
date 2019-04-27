package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import e.gongfurui.digitallearnerlogbookV2.Adapters.InstructorLearnerFgPagerAdapter;
import e.gongfurui.digitallearnerlogbookV2.R;

public class InstructorLearnersActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txtTopbar;
    RadioGroup rgTabBar;
    private RadioButton rbChannel;
    private RadioButton rbSetting;
    private ViewPager vpager;

    private InstructorLearnerFgPagerAdapter mAdapter;

    //Parameters stands for the pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    //Parameters stands for the page title
    private final String PAGE_FINISHED_COMPETENCY = "Finished Competency";
    private final String PAGE_PROGRESS = "Progress";

    public static String learnerMail;
    public static String instructorMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_learners);
        learnerMail = Objects.requireNonNull(getIntent().getExtras()).getString("learner_mail");
        instructorMail = getIntent().getExtras().getString("instructor_mail");
        mAdapter = new InstructorLearnerFgPagerAdapter(getSupportFragmentManager());
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        txtTopbar = findViewById(R.id.txt_topbar);
        rgTabBar = findViewById(R.id.rg_tab_bar);
        rbChannel = findViewById(R.id.rb_channel);
        rbSetting = findViewById(R.id.rb_setting);

        rgTabBar.setOnCheckedChangeListener(this);

        vpager = findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.addOnPageChangeListener(this);
        vpager.setCurrentItem(0);
        rbChannel.setChecked(true);
        txtTopbar.setText(PAGE_FINISHED_COMPETENCY);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) txtTopbar.setText(PAGE_FINISHED_COMPETENCY);
        else if(position == 1) txtTopbar.setText(PAGE_PROGRESS);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //3 state，
        // 0 stands for nothing，
        // 1 stands for sliding，
        // 2 stands for slided.
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rbChannel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbSetting.setChecked(true);
                    break;
            }
        }
    }
}
