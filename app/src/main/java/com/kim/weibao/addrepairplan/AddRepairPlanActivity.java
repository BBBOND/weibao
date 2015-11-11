package com.kim.weibao.addrepairplan;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 提交维保方案
 * Created by 伟阳 on 2015/10/31.
 */
public class AddRepairPlanActivity extends AppCompatActivity {

    @Bind(R.id.addrepairplan_machine_name)
    TextView addrepairplanMachineName;
    @Bind(R.id.addrepairplan_error_type)
    TextView addrepairplanErrorType;
    @Bind(R.id.addrepairplan_appcode)
    TextView addrepairplanAppcode;
    @Bind(R.id.addrepairplan_plan_description)
    EditText addrepairplanPlanDescription;
    @Bind(R.id.addrepairplan_plan_money)
    EditText addrepairplanPlanMoney;
    @Bind(R.id.addrepairplan_plan_type)
    Spinner addrepairplanPlanType;
    @Bind(R.id.addrepairplan_exception_mess)
    EditText addrepairplanExceptionMess;
    @Bind(R.id.addrepairplan_plan_time)
    Button addrepairplanPlanTime;
    @Bind(R.id.addrepairplan_submit)
    Button addrepairplanSubmit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String appCode = "";
    private RepairApp repairApp = null;

    private ProgressDialog progressDialog;

    Handler getPlanTypeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String planTypeListString = (String) msg.obj;
            if (!planTypeListString.equals("null") && planTypeListString != null) {
                Log.d("AddRepairPlanActivity", "获取方案类型成功！");
                List<String> planTypeList = JSON.parseArray(planTypeListString, String.class);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddRepairPlanActivity.this, android.R.layout.simple_list_item_1, planTypeList);
                addrepairplanPlanType.setAdapter(adapter);
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "加载信息失败！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                finish();
                return false;
            }
        }
    });

    Handler getRepairAppHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String repairAppListString = (String) msg.obj;
            Log.d("AddRepairPlanActivity", repairAppListString);
            if (!repairAppListString.equals("null") && repairAppListString != null) {
                repairApp = JSON.parseArray(repairAppListString, RepairApp.class).get(0);
                addrepairplanMachineName.setText(repairApp.getMachineName());
                addrepairplanErrorType.setText(repairApp.getErrorType());
                addrepairplanAppcode.setText(repairApp.getAppCode());
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                return true;
            } else {
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                Toast.makeText(AddRepairPlanActivity.this, "获取申请单信息失败", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    });

    Handler postRepairPlanHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String temp = (String) msg.obj;
            if (temp.equals("success") && temp != null) {
                Toast.makeText(AddRepairPlanActivity.this, "提交成功，请等待审核！", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "提交失败，请重试！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                addrepairplanSubmit.setClickable(true);
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrepairplan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(AddRepairPlanActivity.this);
        progressDialog.setTitle("加载数据中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Intent intent = getIntent();
        appCode = intent.getStringExtra("appcode");
        Log.d("AddRepairPlanActivity", appCode);

        getRepairApp();

        addrepairplanPlanTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddRepairPlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Log.d("AddRepairPlanActivity", date);
                        addrepairplanPlanTime.setText(date);
                        addrepairplanExceptionMess.requestFocus();
                    }
                }, year, monthOfYear, dayOfMonth);
                dialog.show();
            }
        });

        addrepairplanSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String planDescription = addrepairplanPlanDescription.getText().toString().trim();
                String planMoney = addrepairplanPlanMoney.getText().toString().trim();
                String planTime = addrepairplanPlanTime.getText().toString().trim();
                String planType = addrepairplanPlanType.getSelectedItem().toString().trim();
                String exceptionMess = addrepairplanExceptionMess.getText().toString().trim();
                if (planDescription.equals("")) {
                    addrepairplanPlanDescription.setError(getResources().getString(R.string.should_not_empty));
                    addrepairplanPlanDescription.requestFocus();
                } else if (planMoney.equals("")) {
                    addrepairplanPlanMoney.setError(getResources().getString(R.string.should_not_empty));
                    addrepairplanPlanMoney.requestFocus();
                } else if (planTime.equals("未选择")) {
                    Snackbar snackbar = Snackbar.make(toolbar, "请选择预计的完工时间", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (exceptionMess.equals("")) {
                    addrepairplanExceptionMess.setError(getResources().getString(R.string.should_not_empty));
                    addrepairplanExceptionMess.requestFocus();
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("yy-mm-dd", Locale.CHINA);
                    RepairPlan repairPlan = new RepairPlan();
                    repairPlan.setAppCode(repairApp.getAppCode());
                    repairPlan.setPlanDescription(planDescription);
                    repairPlan.setPlanMoney(Long.parseLong(planMoney));
                    try {
                        repairPlan.setPlanTime(format.parse(planTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    repairPlan.setSubmitTime(new Date());
                    repairPlan.setAppId(repairApp.getId());
                    repairPlan.setPlanUserId(App.getUSERID());
                    repairPlan.setPlanType(planType);
                    repairPlan.setExceptionMess(exceptionMess);

                    postRepairPlan(repairPlan);
                    addrepairplanSubmit.setClickable(false);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETDETAILNAMEBYTYPENAME);
                Representation result;
                try {
                    result = client.post(URLEncoder.encode(getResources().getStringArray(R.array.type_name)[2], "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", "获取申请类别失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getPlanTypeHandler.sendMessage(message);
                    return;
                }
                try {
                    String planType = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = planType;
                    getPlanTypeHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", "获取申请类别失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getPlanTypeHandler.sendMessage(message);
                }
            }
        }).start();
        super.onResume();
    }

    public void getRepairApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETREPAIRAPP2);
                Representation result;
                try {
                    result = client.post(appCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", ">>>>>>>>获取申请信息连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppHandler.sendMessage(message);
                    return;
                }
                try {
                    String repairAppListString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = repairAppListString;
                    getRepairAppHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", ">>>>>>>>获取申请信息失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getRepairAppHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void postRepairPlan(final RepairPlan repairPlan) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.POSTREPAIRPLAN);
                Representation result;
                try {
                    String str = URLEncoder.encode(JSON.toJSONString(repairPlan), "utf-8");
                    result = client.post(str);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", ">>>>>>>>上传维保方案连接失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    postRepairPlanHandler.sendMessage(message);
                    return;
                }

                try {
                    String temp = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = temp;
                    postRepairPlanHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairPlanActivity", ">>>>>>>>上传维保方案失败！");
                    Message message = Message.obtain();
                    message.obj = "null";
                    postRepairPlanHandler.sendMessage(message);
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
