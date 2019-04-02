package e.gongfurui.digitallearnerlogbook.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import e.gongfurui.digitallearnerlogbook.Adapters.LearnerHomeFgPagerAdapter;
import e.gongfurui.digitallearnerlogbook.R;

public class LearnerHomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txtTopbar;
    RadioGroup rgTabBar;
    private RadioButton rbChannel;
    private RadioButton rbMessage;
    private RadioButton rbBetter;
    private RadioButton rbSetting;
    private ViewPager vPager;

    private LearnerHomeFgPagerAdapter mAdapter;

    //Parameters stands for the pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    public static int learnerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_home);
        learnerID=getIntent().getIntExtra("learnerID", 0);
        mAdapter = new LearnerHomeFgPagerAdapter(getSupportFragmentManager());
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        txtTopbar = findViewById(R.id.txt_topbar);
        rgTabBar = findViewById(R.id.rg_tab_bar);
        rbChannel = findViewById(R.id.rb_channel);
        rbMessage = findViewById(R.id.rb_message);
        rbBetter = findViewById(R.id.rb_better);
        rbSetting = findViewById(R.id.rb_setting);
        rgTabBar.setOnCheckedChangeListener(this);

        vPager = findViewById(R.id.vpager);
        vPager.setAdapter(mAdapter);
        vPager.addOnPageChangeListener(this);
        vPager.setCurrentItem(0);
        rbChannel.setChecked(true);
        txtTopbar.setText("Information");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
     *Menu clicking event
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_add_item:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            default:
                break;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_better:
                vPager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_setting:
                vPager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) txtTopbar.setText("Information");
        else if(position == 1) txtTopbar.setText("Competency");
        else if(position == 2) txtTopbar.setText("2");
        else if(position == 3) txtTopbar.setText("Progress");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //3 state，
        // 0 stands for nothing，
        // 1 stands for sliding，
        // 2 stands for slided.
        if (state == 2) {
            switch (vPager.getCurrentItem()) {
                case PAGE_ONE:
                    rbChannel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rbMessage.setChecked(true);
                    break;
                case PAGE_THREE:
                    rbBetter.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rbSetting.setChecked(true);
                    break;
            }
        }
    }
}
