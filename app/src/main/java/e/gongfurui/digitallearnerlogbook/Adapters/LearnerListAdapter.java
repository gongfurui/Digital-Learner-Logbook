package e.gongfurui.digitallearnerlogbook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Learner;

public class LearnerListAdapter extends BaseAdapter {

    private LinkedList<Learner> mData;
    private Context mContext;

    public LearnerListAdapter(LinkedList<Learner> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int finishedCourse = 0;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_learner,parent,
                false);
        TextView tv_lName = convertView.findViewById(R.id.tv_lName);
        TextView tv_lCompetencyProgress = convertView.findViewById(R.id.tv_lCompetencyProgress);
        TextView tv_lHourProgress = convertView.findViewById(R.id.tv_lHourProgress);
        tv_lName.setText(mData.get(position).name);
        for (Boolean b: mData.get(position).courseProgressList) {
            if(b) finishedCourse++;
        }
        tv_lCompetencyProgress.setText(finishedCourse + "/23");
        tv_lHourProgress.setText(mData.get(position).time +"/120 h");
        return convertView;
    }
}
