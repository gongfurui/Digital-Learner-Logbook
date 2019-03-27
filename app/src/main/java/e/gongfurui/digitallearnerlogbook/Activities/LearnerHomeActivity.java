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

import e.gongfurui.digitallearnerlogbook.Adapters.MyFragmentPagerAdapter;
import e.gongfurui.digitallearnerlogbook.R;

public class LearnerHomeActivity extends AppCompatActivity   implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

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
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        initViews();
    }

    /**
     * Initial the UI parameter involved in this activity
     * */
    private void initViews() {
        txt_topbar = findViewById(R.id.txt_topbar);
        rg_tab_bar = findViewById(R.id.rg_tab_bar);
        rb_channel = findViewById(R.id.rb_channel);
        rb_message = findViewById(R.id.rb_message);
        rb_better = findViewById(R.id.rb_better);
        rb_setting = findViewById(R.id.rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.addOnPageChangeListener(this);
        vpager.setCurrentItem(0);
        rb_channel.setChecked(true);
        txt_topbar.setText("Information");

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
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_better:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_setting:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) txt_topbar.setText("Information");
        else if(position == 1) txt_topbar.setText("Competency");
        else if(position == 2) txt_topbar.setText("2");
        else if(position == 3) txt_topbar.setText("Progress");
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
                    rb_channel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_better.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rb_setting.setChecked(true);
                    break;
            }
        }
    }
}
