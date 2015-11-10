package com.kim.weibao.examplan;

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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.business.RepairApp;
import com.kim.weibao.model.business.RepairPlan;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/31.
 */
public class ExamPlanActivity extends AppCompatActivity {

    @Bind(R.id.examplan_machine_name)
    TextView examplanMachineName;
    @Bind(R.id.examplan_error_type)
    TextView examplanErrorType;
    @Bind(R.id.examplan_app_code)
    TextView examplanAppCode;
    @Bind(R.id.examplan_plan_description)
    TextView examplanPlanDescription;
    @Bind(R.id.examplan_plan_money)
    TextView examplanPlanMoney;
    @Bind(R.id.examplan_plan_time)
    TextView examplanPlanTime;
    @Bind(R.id.examplan_plan_type)
    TextView examplanPlanType;
    @Bind(R.id.examplan_exception_mess)
    TextView examplanExceptionMess;
    @Bind(R.id.examplan_submit_time)
    TextView examplanSubmitTime;
    @Bind(R.id.reject)
    Button reject;
    @Bind(R.id.accept)
    Button accept;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String appCode = "";
    private RepairApp repairApp = null;
    private RepairPlan repairPlan = null;

    private ProgressDialog progressDialog;


    Handler getRepairAppHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppString = (String) msg.obj;
            if (!repairAppString.equals("null") && repairAppString != null) {
                repairApp = JSON.parseArray(repairAppString, RepairApp.class).get(0);
                examplanMachineName.setText(repairApp.getMachineName());
                examplanErrorType.setText(repairApp.getErrorType());
                examplanAppCode.setText(repairApp.getAppCode());
                getRepairPlan();
                return true;
            } else {
                progressDialog.cancel();
                Toast.makeText(ExamPlanActivity.this, "获取申请单信息失败", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    });

    Handler getRepairPlanHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairPlanString = (String) msg.obj;
            if (!repairPlanString.equals("null") && repairPlanString != null) {
                repairPlan = JSON.parseObject(repairPlanString, RepairPlan.class);
                examplanPlanDescription.setText(repairPlan.getPlanDescription());
                examplanPlanMoney.setText("￥" + repairPlan.getPlanMoney());
                examplanPlanTime.setText(repairPlan.getPlanTime() + "");
                examplanPlanType.setText(repairPlan.getPlanType());
                examplanExceptionMess.setText(repairPlan.getExceptionMess());
                examplanSubmitTime.setText(repairPlan.getSubmitTime() + "");
                progressDialog.cancel();
                return true;
            } else {
                progressDialog.cancel();
                Toast.makeText(ExamPlanActivity.this, "获取维保方案信息失败！", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    });

    Handler acceptHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String temp = (String) msg.obj;
            if (temp.equals("success")) {
                Toast.makeText(ExamPlanActivity.this, "已通过！", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                finish();
                return true;
            } else {
                progressDialog.cancel();
                Snackbar snackbar = Snackbar.make(toolbar, "操作失败,请重试!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    Handler rejectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String temp = (String) msg.obj;
            if (temp.equals("success")) {
                Toast.makeText(ExamPlanActivity.this, "已驳回！", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                finish();
                return true;
            } else {
                progressDialog.cancel();
                Snackbar snackbar = Snackbar.make(toolbar, "操作失败,请重试!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examplan);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ExamPlanActivity.this);
        progressDialog.setTitle("加载数据中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Intent intent = getIntent();
        appCode = intent.getStringExtra("appcode");

        getRepairApp();

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("提交中...");
                progressDialog.show();
                reject();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("提交中...");
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

    public void getRepairPlan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETREPAIRPLANBYAPPCODEANDPLANID);
                Representation result = null;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("appcode", appCode);
                map.put("planid", repairApp.getPlanId());
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ExamPlanActivity", ">>>>>>>>获取维保方案信息连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairPlanHandler.sendMessage(message);
                    return;
                }
                try {
                    String repairPlanString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = repairPlanString;
                    getRepairPlanHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ExamPlanActivity", ">>>>>>>>获取维保方案信息失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairPlanHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void accept() {
        // TODO: 2015/11/7
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.EXAMPLANACCEPT);
                Representation result = null;
                try {
                    result = client.post(repairPlan.getId());
                } catch (Exception e) {
                    e.printStackTrace();
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
                    Message message = Message.obtain();
                    message.obj = "failed";
                    acceptHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void reject() {
        // TODO: 2015/11/7
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.EXAMPLANREJECT);
                Representation result = null;
                try {
                    result = client.post(repairPlan.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "failed";
                    rejectHandler.sendMessage(message);
                    return;
                }
                try {
                    String temp = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = temp;
                    rejectHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "failed";
                    rejectHandler.sendMessage(message);
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
