package com.kim.weibao.planschedule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.addrepairplan.RepairAppAdapter;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.business.RepairApp;
import com.kim.weibao.utils.URLUtil;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/31.
 */
public class ShowPlanScheduleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private RepairAppAdapter adapter;
    private List<RepairApp> repairAppList = new ArrayList<>();

    Handler getRepairAppList = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppListString = (String) msg.obj;
            if (!repairAppListString.equals("null") && repairAppListString != null) {
                repairAppList.clear();
                repairAppList.addAll(JSON.parseArray(repairAppListString, RepairApp.class));
                adapter.notifyDataSetChanged();
                swiperefreshlayout.setRefreshing(false);
                return true;
            } else {
                repairAppList.clear();
                adapter.notifyDataSetChanged();
                swiperefreshlayout.setRefreshing(false);
                Snackbar snackbar = Snackbar.make(toolbar, "无数据,下拉刷新！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swiperefreshlayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swiperefreshlayout.setOnRefreshListener(this);
        adapter = new RepairAppAdapter(ShowPlanScheduleActivity.this, R.layout.item_repair, repairAppList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairApp repairApp = adapter.getItem(position);
                Intent intent = new Intent(ShowPlanScheduleActivity.this, UpdatePlanScheduleActivity.class);
                intent.putExtra("appcode", repairApp.getAppCode());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(URLUtil.getRealURL(getApplicationContext(), MyURL.GETREPAIRAPP3));
                Representation result = null;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userid", App.getUSERID());
                map.put("step", "4");
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ShowPlanSchedule", "获取申请单列表失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppList.sendMessage(message);
                    return;
                }
                try {
                    String repairAppListString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = repairAppListString;
                    getRepairAppList.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ShowPlanSchedule", "获取申请单列表失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppList.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
