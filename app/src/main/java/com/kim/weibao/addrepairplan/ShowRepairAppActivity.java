package com.kim.weibao.addrepairplan;

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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.business.RepairApp;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/31.
 */
public class ShowRepairAppActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private int page = 0;
    private RepairAppAdapter adapter;
    private List<RepairApp> repairAppList = new ArrayList<>();

    Handler getRepairAppList = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppListString = (String) msg.obj;
            Log.d("ShowRepairAppActivity", repairAppListString);
            if (!repairAppListString.equals("null") && repairAppListString != null) {
                repairAppList.clear();
                repairAppList.addAll(JSON.parseArray(repairAppListString, RepairApp.class));
                Log.d("ShowRepairAppActivity", "repairAppList:" + repairAppList);
                adapter.notifyDataSetChanged();
                swiperefreshlayout.setRefreshing(false);
                return true;
            } else {
                repairAppList.clear();
                adapter.notifyDataSetChanged();
                Snackbar snackbar = Snackbar.make(toolbar, "无数据,下拉刷新！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                swiperefreshlayout.setRefreshing(false);
                return false;
            }
        }
    });

    Handler loadMoreHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
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
        adapter = new RepairAppAdapter(ShowRepairAppActivity.this, R.layout.item_repair, repairAppList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairApp repairApp = adapter.getItem(position);
                Log.d("ShowRepairAppActivity", repairApp.getAppCode());
                Intent intent = new Intent(ShowRepairAppActivity.this, AddRepairPlanActivity.class);
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
                ClientResource client = null;
                if (App.getROLE().equals(getResources().getStringArray(R.array.role_name)[2])) {
                    client = new ClientResource(MyURL.GETREPAIRAPP1);
                } else if (App.getROLE().equals(getResources().getStringArray(R.array.role_name)[1])) {
                    client = new ClientResource(MyURL.GETREPAIRAPP3);
                }

                Representation result = null;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userid", App.getUSERID());
                map.put("step", 0);
//                map.put("page", 0);
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ShowRepairAppActivity", "获取申请单列表失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppList.sendMessage(message);
                }
                try {
                    String repairAppListString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = repairAppListString;
                    getRepairAppList.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ShowRepairAppActivity", "获取申请单列表失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppList.sendMessage(message);
                }
            }
        }).start();
    }

    public void loadMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {

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
