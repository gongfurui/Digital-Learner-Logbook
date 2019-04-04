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

import e.gongfurui.digitallearnerlogbook.Adapters.SupervisorHomeFgPagerAdapter;
import e.gongfurui.digitallearnerlogbook.R;

public class SupervisorHomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txtTopbar;
    RadioGroup rgTabBar;
    private RadioButton rbChannel;
    private RadioButton rbMessage;
    private ViewPager vpager;

    private SupervisorHomeFgPagerAdapter mAdapter;

    //Parameters stands for the pages
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    //Parameters stands for the page title
    private final String PAGE_INFORMATION = "Information";
    private final String PAGE_LEARNERS = "Learners";

    public static String supervisorMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home);
        supervisorMail = getIntent().getStringExtra("supervisorMail");
        mAdapter = new SupervisorHomeFgPagerAdapter(getSupportFragmentManager());
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
        rgTabBar.setOnCheckedChangeListener(this);

        vpager = findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.addOnPageChangeListener(this);
        vpager.setCurrentItem(0);
        rbChannel.setChecked(true);
        txtTopbar.setText(PAGE_INFORMATION);

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
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) txtTopbar.setText(PAGE_INFORMATION);
        else if(position == 1) txtTopbar.setText(PAGE_LEARNERS);
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
            }
        }
    }
}
