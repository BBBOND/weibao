package com.kim.weibao.showarea;

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
import com.kim.weibao.model.basicData.AreaInfo;
import com.kim.weibao.model.business.RepairApp;
import com.kim.weibao.planschedule.UpdatePlanScheduleActivity;

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
 * Created by 伟阳 on 2015/11/1.
 */
public class ShowAreaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private AreaInfoAdapter adapter;
    private List<AreaInfo> areaInfoList = new ArrayList<>();

    Handler getAreaInfoList = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String areaInfoListString = (String) msg.obj;
            if (!areaInfoListString.equals("null") && areaInfoListString != null) {
                areaInfoList.clear();
                areaInfoList.addAll(JSON.parseArray(areaInfoListString, AreaInfo.class));
                adapter.notifyDataSetChanged();
                swiperefreshlayout.setRefreshing(false);
                return true;
            } else {
                areaInfoList.clear();
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
        adapter = new AreaInfoAdapter(ShowAreaActivity.this, R.layout.item_area, areaInfoList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AreaInfo areaInfo = adapter.getItem(position);
                Intent intent = new Intent(ShowAreaActivity.this, DetailedAreaActivity.class);
                intent.putExtra("areaid", areaInfo.getId());
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
                ClientResource client = new ClientResource(MyURL.GETAREAINFOS);
                Representation result = null;
                Map<String, Object> map = new HashMap<String, Object>();
                String userRole = App.getROLE();
                if (userRole.equals(getResources().getStringArray(R.array.role_name)[0])) {
                    map.put("flag", "0");
                } else if (userRole.equals(getResources().getStringArray(R.array.role_name)[2])) {
                    map.put("flag", "1");
                } else if (userRole.equals(getResources().getStringArray(R.array.role_name)[1])) {
                    map.put("flag", "2");
                }
                map.put("userid", App.getUSERID());
                Log.d("ShowAreaActivity", JSON.toJSONString(map));
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ShowAreaActivity", "获取社区信息列表连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAreaInfoList.sendMessage(message);
                    return;
                }
                try {
                    String areaInfoListString = result.getText().trim();
                    Log.d("ShowAreaActivity", areaInfoListString);
                    Message message = Message.obtain();
                    message.obj = areaInfoListString;
                    getAreaInfoList.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ShowAreaActivity", "获取社区信息列表连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAreaInfoList.sendMessage(message);
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
