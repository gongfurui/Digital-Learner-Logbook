package e.gongfurui.digitallearnerlogbookV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import e.gongfurui.digitallearnerlogbookV2.R;
import e.gongfurui.digitallearnerlogbookV2.Roles.Route;

public class PracticeAdapter extends BaseAdapter {

    private LinkedList<Route> mData;
    private Context mContext;

    public PracticeAdapter(LinkedList<Route> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_practice, parent,
                false);
        TextView tv_routeID = convertView.findViewById(R.id.tv_routeID);
        TextView tv_distance = convertView.findViewById(R.id.tv_routeDistance);
        TextView tv_time = convertView.findViewById(R.id.tv_routeTime);

        tv_routeID.setText(String.valueOf(mData.get(position).routeID));
        tv_distance.setText(String.valueOf(mData.get(position).distance + " km"));
        tv_time.setText(String.valueOf(mData.get(position).time + " h"));
        return convertView;
    }


}
