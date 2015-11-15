package com.kim.weibao.planschedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.business.PlanSchedule;
import com.kim.weibao.model.business.RepairApp;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/31.
 */
public class UpdatePlanScheduleActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.update_plan_schedule_machine_name)
    TextView updatePlanScheduleMachineName;
    @Bind(R.id.update_plan_schedule_error_type)
    TextView updatePlanScheduleErrorType;
    @Bind(R.id.update_plan_schedule_app_code)
    TextView updatePlanScheduleAppCode;
    @Bind(R.id.update_plan_schedule_description)
    EditText updatePlanScheduleDescription;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.accept)
    Button accept;


    private String appCode = "";
    private RepairApp repairApp = null;

    private ProgressDialog progressDialog;

    Handler getRepairAppHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppString = (String) msg.obj;
            if (!repairAppString.equals("null") && repairAppString != null) {
                Log.d("UpdatePlanSchedule", ">>>>>获取申请单信息成功");
                repairApp = JSON.parseArray(repairAppString, RepairApp.class).get(0);
                updatePlanScheduleMachineName.setText(repairApp.getMachineName());
                updatePlanScheduleErrorType.setText(repairApp.getErrorType());
                updatePlanScheduleAppCode.setText(repairApp.getAppCode());
                progressDialog.cancel();
                return true;
            } else {
                Log.d("UpdatePlanSchedule", ">>>>>获取申请单信息失败");
                progressDialog.cancel();
                Toast.makeText(UpdatePlanScheduleActivity.this, "获取申请单信息失败", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    });

    Handler postPlanScheduleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String temp = (String) msg.obj;
            if (temp.equals("success")) {
                Log.d("UpdatePlanSchedule", ">>>>>>提交成功");
                progressDialog.cancel();
                Toast.makeText(UpdatePlanScheduleActivity.this, "更新进度成功", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                Log.d("UpdatePlanSchedule", ">>>>>>提交失败");
                progressDialog.cancel();
                Snackbar snackbar = Snackbar.make(toolbar, "提交失败,请重试", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    Handler acceptHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String temp = (String) msg.obj;
            if (temp.equals("success") && temp != null) {
                Log.d("UpdatePlanSchedule", ">>>>>>提交成功");
                progressDialog.cancel();
                Toast.makeText(UpdatePlanScheduleActivity.this, "设置完工成功", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                Log.d("UpdatePlanSchedule", ">>>>>>提交失败");
                progressDialog.cancel();
                Snackbar snackbar = Snackbar.make(toolbar, "提交失败,请重试", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateplanschedule);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(UpdatePlanScheduleActivity.this);
        progressDialog.setTitle("加载数据中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Intent intent = getIntent();
        appCode = intent.getStringExtra("appcode");

        getRepairApp();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = updatePlanScheduleDescription.getText().toString().trim();
                if (description.equals("")) {
                    updatePlanScheduleDescription.setError(getResources().getString(R.string.should_not_empty));
                    updatePlanScheduleDescription.requestFocus();
                } else {
                    PlanSchedule planSchedule = new PlanSchedule();
                    planSchedule.setAppId(repairApp.getId());
                    planSchedule.setAppCode(repairApp.getAppCode());
                    planSchedule.setPlanId(repairApp.getPlanId());
                    planSchedule.setScheduleDescription(description);
                    planSchedule.setRecordUserId(App.getUSERID());
                    planSchedule.setRecordTime(new Date());
                    progressDialog.setTitle("加载数据中...");
                    progressDialog.show();
                    postPlanSchedule(planSchedule);
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("加载数据中...");
                progressDialog.show();
                accept();
            }
        });
    }

    public void getRepairApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETREPAIRAPP2);
                Representation result = null;
                try {
                    result = client.post(appCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ExamPlanActivity", ">>>>>>>>获取申请信息连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppHandler.sendMessage(message);
                    return;
                }
                try {
                    String repairAppString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = repairAppString;
                    getRepairAppHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ExamPlanActivity", ">>>>>>>>获取申请信息失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void postPlanSchedule(final PlanSchedule planSchedule) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.POSTPLANSCHEDULE);
                Representation result = null;
                try {
                    String planScheduleString = URLEncoder.encode(JSON.toJSONString(planSchedule), "utf-8");
                    result = client.post(planScheduleString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("UpdatePlanSchedule", ">>>>>>>>>提交进度连接失败！");
                    Message message = Message.obtain();
                    message.obj = "failed";
                    postPlanScheduleHandler.sendMessage(message);
                    return;
                }

                try {
                    String temp = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = temp;
                    postPlanScheduleHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("UpdatePlanSchedule", ">>>>>>>>>提交进度失败！");
                    Message message = Message.obtain();
                    message.obj = "failed";
                    postPlanScheduleHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void accept() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.FINASHPLANSCHEDULE);
                Representation result = null;
                PlanSchedule planSchedule = new PlanSchedule();
                planSchedule.setAppCode(appCode);
                planSchedule.setRecordUserId(App.getUSERID());
                planSchedule.setPlanId(repairApp.getPlanId());
                planSchedule.setAppId(repairApp.getId());
                planSchedule.setScheduleDescription("完工");
                try {
                    result = client.post(URLEncoder.encode(JSON.toJSONString(planSchedule), "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("UpdatePlanSchedule", ">>>>>>>>>设置完工连接失败！");
                    Message message = Message.obtain();
                    message.obj = "failed";
                    acceptHandler.sendMessage(message);
                    return;
                }

                try {
                    String temp = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = temp;
                    acceptHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("UpdatePlanSchedule", ">>>>>>>>>设置完工失败！");
                    Message message = Message.obtain();
                    message.obj = "failed";
                    acceptHandler.sendMessage(message);
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
