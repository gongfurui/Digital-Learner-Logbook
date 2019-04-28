package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import e.gongfurui.digitallearnerlogbookV2.Adapters.InstructorLearnerFgPagerAdapter;
import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class InstructorLearnersActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txtTopbar;
    RadioGroup rgTabBar;
    private RadioButton rbChannel;
    private RadioButton rbSetting;
    private ViewPager vpager;

    private InstructorLearnerFgPagerAdapter mAdapter;

    private Learner learner;

    //Parameters stands for the pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    //Parameters stands for the page title
    private final String PAGE_FINISHED_COMPETENCY = "'s Competency";

    public static String learnerMail;
    public static String instructorMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_learners);
        learnerMail = Objects.requireNonNull(getIntent().getExtras()).getString("learner_mail");
        learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                "/drive/searchLearnerByMail/" + learnerMail);
        instructorMail = getIntent().getExtras().getString("instructor_mail");
        mAdapter = new InstructorLearnerFgPagerAdapter(getSupportFragmentManager());
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    @SuppressLint("SetTextI18n")
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
        txtTopbar.setText(learner.name + PAGE_FINISHED_COMPETENCY);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_setting:
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        String PAGE_PROGRESS = "'s Progress";
        if(position == 0) txtTopbar.setText(learner.name + PAGE_FINISHED_COMPETENCY);
        else if(position == 1) txtTopbar.setText(learner.name + PAGE_PROGRESS);
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
