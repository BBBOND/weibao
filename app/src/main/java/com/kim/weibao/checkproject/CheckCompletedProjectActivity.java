package com.kim.weibao.checkproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
public class CheckCompletedProjectActivity extends AppCompatActivity {

    @Bind(R.id.check_project_machine_name)
    TextView checkProjectMachineName;
    @Bind(R.id.check_project_error_type)
    TextView checkProjectErrorType;
    @Bind(R.id.check_project_app_code)
    TextView checkProjectAppCode;
    @Bind(R.id.check_project_plan_description)
    TextView checkProjectPlanDescription;
    @Bind(R.id.check_project_plan_money)
    TextView checkProjectPlanMoney;
    @Bind(R.id.check_project_plan_time)
    TextView checkProjectPlanTime;
    @Bind(R.id.check_project_plan_type)
    TextView checkProjectPlanType;
    @Bind(R.id.check_project_plan_exception_mess)
    TextView checkProjectPlanExceptionMess;
    @Bind(R.id.reject)
    Button reject;
    @Bind(R.id.accept)
    Button accept;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String appCode = "";
    private RepairApp repairApp = null;

    private ProgressDialog progressDialog;

    Handler getRepairAppHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppString = (String) msg.obj;
            if (!repairAppString.equals("null") && repairAppString != null) {
                repairApp = JSON.parseObject(repairAppString, RepairApp.class);
                checkProjectMachineName.setText(repairApp.getMachineName());
                checkProjectErrorType.setText(repairApp.getErrorType());
                checkProjectAppCode.setText(repairApp.getAppCode());
                getRepairPlan();
                return true;
            } else {
                progressDialog.cancel();
                Toast.makeText(CheckCompletedProjectActivity.this, "获取申请单信息失败", Toast.LENGTH_SHORT).show();
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
                RepairPlan repairPlan = JSON.parseObject(repairPlanString, RepairPlan.class);
                checkProjectPlanDescription.setText(repairPlan.getPlanDescription());
                checkProjectPlanMoney.setText("￥" + repairPlan.getPlanMoney());
                checkProjectPlanTime.setText(repairPlan.getPlanTime() + "");
                checkProjectPlanType.setText(repairPlan.getPlanType());
                checkProjectPlanExceptionMess.setText(repairPlan.getExceptionMess());
                progressDialog.cancel();
                return true;
            } else {
                progressDialog.cancel();
                Toast.makeText(CheckCompletedProjectActivity.this, "获取维保方案信息失败！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CheckCompletedProjectActivity.this, "验收通过", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CheckCompletedProjectActivity.this, "返工", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_checkproject);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        appCode = intent.getStringExtra("appcode");

        progressDialog = new ProgressDialog(CheckCompletedProjectActivity.this);
        progressDialog.setTitle("加载数据中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        getRepairApp();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckCompletedProjectActivity.this);
                builder.setTitle("确定通过?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("通过", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setTitle("提交中...");
                        progressDialog.show();
                        accept();
                    }
                });
                builder.create().show();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckCompletedProjectActivity.this);
                builder.setTitle("确定返工?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("返工", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setTitle("提交中...");
                        progressDialog.show();
                        reject();
                    }
                });
                builder.create().show();
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
                    Log.d("CheckCompletedProject", ">>>>>>>>获取维保方案信息连接失败！");
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
                    Log.d("CheckCompletedProject", ">>>>>>>>获取维保方案信息失败！");
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
                ClientResource client = new ClientResource(MyURL.CHECKPROJECTACCEPT);
                Representation result = null;
                try {
                    result = client.post(appCode);
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
                ClientResource client = new ClientResource(MyURL.CHECKPROJECTREJECT);
                Representation result = null;
                try {
                    result = client.post(appCode);
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
