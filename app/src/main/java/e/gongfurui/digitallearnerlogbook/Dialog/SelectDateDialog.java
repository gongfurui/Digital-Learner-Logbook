package e.gongfurui.digitallearnerlogbook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.wheelviewlibrary.WheelView;
import e.gongfurui.wheelviewlibrary.adapter.NumericWheelAdapter;
import e.gongfurui.wheelviewlibrary.listener.OnWheelChangedListener;
import e.gongfurui.wheelviewlibrary.listener.SelectInterface;


/*Modified by Furui Gong 2019-3-19*/
public class SelectDateDialog implements OnWheelChangedListener {


    public static final int START_YEAR = 1900;
    public static final int END_YEAR = 2100;


    private WheelView year;
    private WheelView month;
    private WheelView day;


    private Activity context;
    private SelectInterface selectAdd;


    public SelectDateDialog(SelectInterface selectAdd) {
        this.selectAdd = selectAdd;
    }


    public void showDateDialog(final Context context) {
        this.context = (Activity) context;
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.dialog_lhp)
                .create();
        dialog.show();

        Window window = dialog.getWindow();
        // set layout
        window.setContentView(R.layout.dialog_select_address);
        // Set height and width
        window.getDecorView().setPadding(0, 0, 0, 0);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set popup animation
        window.setWindowAnimations(R.style.mystyle);
        window.setGravity(Gravity.BOTTOM);

        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//Throgh Calendar counts the month requires to +1
        int curDate = c.get(Calendar.DATE);
        year = (WheelView) window.findViewById(R.id.id_province);
        initYear(context);
        month = (WheelView) window.findViewById(R.id.id_city);
        initMonth(context);
        day = (WheelView) window.findViewById(R.id.id_district);
        initDay(curYear, curMonth, context);

        year.setCurrentItem(curYear - START_YEAR);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        year.setVisibleItems(9);
        month.setVisibleItems(9);
        day.setVisibleItems(9);

        //set scroll listener
        setDateUpListener();

        Button ok = window.findViewById(R.id.btn_confirm);
        Button cancel = window.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.format(Locale.ENGLISH,
                        "%4d-%2d-%2d", year.getCurrentItem() + START_YEAR,//1900
                        month.getCurrentItem() + 1, day.getCurrentItem() + 1);
                str = str.replace(' ', '0');
                selectAdd.selectedResult(str);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    final Date date = dateFormat.parse(str);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return false;
            }
        });



    }

    private void setDateUpListener() {
        year.addChangingListener(this);
        month.addChangingListener(this);
        year.addChangingListener(this);
    }


    /**
     * Initial year
     */
    private void initYear(Context context) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context,
                START_YEAR, END_YEAR);
        numericWheelAdapter.setLabel(" ");
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }

    /**
     * Initial month
     */
    private void initMonth(Context context) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, 12, "%02d");
        numericWheelAdapter.setLabel(" ");
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }

    /**
     * Initial days
     */
    private void initDay(int arg1, int arg2, Context context) {
        //Set adapter
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(arg1, arg2), "%02d");
        //Establish label
        numericWheelAdapter.setLabel(" ");
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }

    private int getDay(int year, int month) {
        int day;
        boolean flag;
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            flag = true;
        } else {
            flag = false;
        }

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == year || wheel == month) {
            int yearNum = year.getCurrentItem() + START_YEAR;
            int monthNum = month.getCurrentItem() + 1;
            if (wheel == year && monthNum != 2) {
                return;
            }
            initDay(yearNum, monthNum, context);
        }
    }
}
