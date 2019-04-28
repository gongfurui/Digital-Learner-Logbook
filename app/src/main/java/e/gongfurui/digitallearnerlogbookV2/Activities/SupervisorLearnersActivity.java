package e.gongfurui.digitallearnerlogbookV2.Activities;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import e.gongfurui.digitallearnerlogbookV2.Adapters.SupervisorLearnerFgPagerAdapter;
import e.gongfurui.digitallearnerlogbookV2.Helpers.OnlineDBHelper;
import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Learner;

import static e.gongfurui.digitallearnerlogbookV2.Helpers.ValuesHelper.LOCAL_IP;

public class SupervisorLearnersActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    //UI Objects
    private TextView txtTopbar;
    RadioGroup rgTabBar;
    private RadioButton rbChannel;
    private RadioButton rbMessage;
    private RadioButton rbSetting;
    private ViewPager vpager;

    private Learner learner;

    private SupervisorLearnerFgPagerAdapter mAdapter;

    //Parameters stands for the pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    //Parameters stands for the page title
    private final String PAGE_FINISHED_COMPETENCY = "'s Competency";

    public static String learnerMail;
    public static String supervisorMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_learners);
        learnerMail = Objects.requireNonNull(getIntent().getExtras()).getString("learnerMail");
        learner = OnlineDBHelper.searchLearnerTable(LOCAL_IP +
                "/drive/searchLearnerByMail/" + learnerMail);
        supervisorMail = getIntent().getExtras().getString("supervisorMail");
        mAdapter = new SupervisorLearnerFgPagerAdapter(getSupportFragmentManager());
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
        rbMessage = findViewById(R.id.rb_message);
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
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_setting:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        String PAGE_PRACTICE_TRACE = "'s Practice";
        String PAGE_PROGRESS = "'s Progress";
        if(position == 0) txtTopbar.setText(learner.name + PAGE_FINISHED_COMPETENCY);
        else if(position == 1) txtTopbar.setText(learner.name + PAGE_PRACTICE_TRACE);
        else if(position == 2) txtTopbar.setText(learner.name + PAGE_PROGRESS);
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
                    rbMessage.setChecked(true);
                    break;
                case PAGE_THREE:
                    rbSetting.setChecked(true);
                    break;
            }
        }
    }
}
