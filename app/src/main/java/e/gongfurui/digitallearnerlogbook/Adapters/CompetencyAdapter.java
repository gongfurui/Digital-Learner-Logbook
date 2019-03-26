package e.gongfurui.digitallearnerlogbook.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import e.gongfurui.digitallearnerlogbook.R;
import e.gongfurui.digitallearnerlogbook.Roles.Competency;

public class CompetencyAdapter extends BaseAdapter {

    private LinkedList<Competency> mData;
    private Context mContext;

    public CompetencyAdapter(LinkedList<Competency> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_competency,parent,false);
        TextView tv_cTitle = convertView.findViewById(R.id.tv_cTitle);
        tv_cTitle.setText(mData.get(position).title);
        if(position == 17 || position == 23 || position == 24){
            tv_cTitle.setTextColor(Color.RED);
        }
        ImageView iv_cProgress = convertView.findViewById(R.id.iv_cProgress);
        if(!mData.get(position).isFinished) iv_cProgress.setVisibility(View.INVISIBLE);
        TextView tv_cCertify = convertView.findViewById(R.id.tv_cCertify);
        tv_cCertify.setText(mData.get(position).instructorName);
        return convertView;
    }
}
