package com.kim.weibao.addrepairplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kim.weibao.R;
import com.kim.weibao.model.business.RepairApp;

import java.util.List;

/**
 * Created by 伟阳 on 2015/11/4.
 */
public class RepairAppAdapter extends ArrayAdapter<RepairApp> {

    private int viewId;
    private LayoutInflater inflater;
    private List<RepairApp> repairAppList;

    public RepairAppAdapter(Context context, int resource, List<RepairApp> objects) {
        super(context, resource, objects);
        viewId = resource;
        inflater = LayoutInflater.from(context);
        repairAppList = objects;
    }

    @Override
    public int getCount() {
        return repairAppList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RepairApp repairApp = getItem(position);
        View view = inflater.inflate(viewId, null);
        TextView appCode = (TextView) view.findViewById(R.id.repair_app_code);
        TextView machineName = (TextView) view.findViewById(R.id.repair_machine_name);
        TextView errorType = (TextView) view.findViewById(R.id.repair_error_type);
        TextView appStatus = (TextView) view.findViewById(R.id.repair_app_status);

        appCode.setText(repairApp.getAppCode());
        machineName.setText(repairApp.getMachineName());
        errorType.setText(repairApp.getErrorType());
        appStatus.setText(repairApp.getAppStatus());

        return view;
    }
}
