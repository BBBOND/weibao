package com.kim.weibao.showarea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kim.weibao.R;
import com.kim.weibao.model.basicData.AreaInfo;

import java.util.List;

/**
 * Created by 伟阳 on 2015/11/8.
 */
public class AreaInfoAdapter extends ArrayAdapter<AreaInfo> {

    private int viewId;
    private LayoutInflater inflater;
    private List<AreaInfo> areaInfoList;

    public AreaInfoAdapter(Context context, int resource, List<AreaInfo> objects) {
        super(context, resource, objects);
        viewId = resource;
        inflater = LayoutInflater.from(context);
        areaInfoList = objects;
    }

    @Override
    public int getCount() {
        return areaInfoList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AreaInfo areaInfo = getItem(position);
        View view = inflater.inflate(viewId, null);
        TextView areaName = (TextView) view.findViewById(R.id.item_area_name);
        areaName.setText(areaInfo.getAreaName());
        return view;
    }
}
