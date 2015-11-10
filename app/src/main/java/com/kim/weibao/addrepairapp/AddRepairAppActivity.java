package com.kim.weibao.addrepairapp;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.basicData.AreaInfo;
import com.kim.weibao.model.basicData.MachineInfo;
import com.kim.weibao.model.business.RepairApp;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/29.
 */
public class AddRepairAppActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.addrepairapp_machine_name)
    TextView addrepairappMachineName;
    @Bind(R.id.addrepairapp_machine_code)
    TextView addrepairappMachineCode;
    @Bind(R.id.addrepairapp_area_name)
    TextView addrepairappAreaName;
    @Bind(R.id.addrepairapp_area_address)
    TextView addrepairappAreaAddress;
    @Bind(R.id.addrepairapp_app_code)
    EditText addrepairappAppCode;
    @Bind(R.id.addrepairapp_app_type)
    Spinner addrepairappAppType;
    @Bind(R.id.addrepairapp_error_type)
    Spinner addrepairappErrorType;
    @Bind(R.id.addrepairapp_app_description)
    EditText addrepairappAppDescription;
    @Bind(R.id.addrepairapp_is_rapad_repair)
    RadioGroup addrepairappIsRapadRepair;
    @Bind(R.id.addrepairapp_submit)
    Button addrepairappSubmit;

    private ProgressDialog loadingProgressDialog;
    private int tempProgress = 1;
    private final int MAXPROGRESS = 4;
    private AreaInfo areaInfo;

    Handler getAreaInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String areaInfoString = (String) msg.obj;
            if (!areaInfoString.equals("null") && areaInfoString != null) {
                areaInfo = JSON.parseObject(areaInfoString, AreaInfo.class);
                addrepairappAreaName.setText(areaInfo.getAreaName());
                addrepairappAreaAddress.setText(areaInfo.getAreaAddress());
                if (tempProgress >= MAXPROGRESS) {
                    loadingProgressDialog.cancel();
                } else {
                    loadingProgressDialog.setProgress(tempProgress++);
                }
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "获取社区信息失败！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                finish();
                return false;
            }
        }
    });

    Handler getAppTypeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String appTypeListString = (String) msg.obj;
            if (!appTypeListString.equals("null") && appTypeListString != null) {
                List<String> appTypeList = JSON.parseArray(appTypeListString, String.class);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRepairAppActivity.this, android.R.layout.simple_list_item_1, appTypeList);
                addrepairappAppType.setAdapter(adapter);
                if (tempProgress >= MAXPROGRESS) {
                    loadingProgressDialog.cancel();
                } else {
                    loadingProgressDialog.setProgress(tempProgress++);
                }
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "获取申请类型信息失败！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                finish();
                return false;
            }
        }
    });

    Handler getErrorTypeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String errorTypeListString = (String) msg.obj;
            if (!errorTypeListString.equals("null") && errorTypeListString != null) {
                List<String> errorTypeList = JSON.parseArray(errorTypeListString, String.class);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRepairAppActivity.this, android.R.layout.simple_list_item_1, errorTypeList);
                addrepairappErrorType.setAdapter(adapter);
                if (tempProgress >= MAXPROGRESS) {
                    loadingProgressDialog.cancel();
                } else {
                    loadingProgressDialog.setProgress(tempProgress++);
                }
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "获取申请类型信息失败！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                finish();
                return false;
            }
        }
    });

    Handler submitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (!result.equals("null") && result.equals("success") && result != null) {
                Toast.makeText(AddRepairAppActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            } else {
                Snackbar snackbar = Snackbar.make(toolbar, "提交失败，请重试或退出！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrepairapp);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("维修申请");

        Intent intent = getIntent();
        String machineInfoString = intent.getStringExtra("machineinfo");

        loadingProgressDialog = new ProgressDialog(AddRepairAppActivity.this);
        loadingProgressDialog.setTitle("加载信息中...");
        loadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loadingProgressDialog.setMax(MAXPROGRESS);
        loadingProgressDialog.show();

        final MachineInfo machineInfo = JSON.parseObject(machineInfoString, MachineInfo.class);
        addrepairappMachineName.setText(machineInfo.getMachineName());
        addrepairappMachineCode.setText(machineInfo.getMachineCode());
        if (tempProgress >= MAXPROGRESS) {
            loadingProgressDialog.cancel();
        } else {
            loadingProgressDialog.setProgress(tempProgress++);
        }
        getAreaInfo(machineInfo.getAreaId());
        getAppType();
        getErrorType();

        addrepairappSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appCode = addrepairappAppCode.getText().toString().trim();
                String appType = addrepairappAppType.getSelectedItem().toString().trim();
                String errorType = addrepairappErrorType.getSelectedItem().toString().trim();
                String appDescription = addrepairappAppDescription.getText().toString().trim();
                boolean isRapadRepair = true;
                switch (addrepairappIsRapadRepair.getCheckedRadioButtonId()) {
                    case R.id.addrepairapp_is_rapad_repair_yes:
                        isRapadRepair = true;
                        break;
                    case R.id.addrepairapp_is_rapad_repair_no:
                        isRapadRepair = false;
                        break;
                    default:
                        break;
                }
                if (appCode.equals("")) {
                    addrepairappAppCode.setError(getResources().getString(R.string.should_not_empty));
                    addrepairappAppCode.requestFocus();
                } else if (appDescription.equals("")) {
                    addrepairappAppDescription.setError(getResources().getString(R.string.should_not_empty));
                    addrepairappAppDescription.requestFocus();
                } else {
                    RepairApp repairApp = new RepairApp();
                    repairApp.setAppCode(appCode);
                    repairApp.setAppType(appType);
                    Log.d("AddRepairAppActivity", ">>>>>>" + machineInfo.getId().trim());
                    repairApp.setMachineId(machineInfo.getId().trim());
                    repairApp.setMachineName(machineInfo.getMachineName());
                    repairApp.setErrorType(errorType);
                    repairApp.setAppDescription(appDescription);
                    repairApp.setIsRapadRepair(isRapadRepair);
                    Log.d("AddRepairAppActivity", ">>>>>>" + App.getUSERID());
                    repairApp.setAppUserId(App.getUSERID());
                    repairApp.setAppTime(new Date());
                    repairApp.setAreaId(machineInfo.getAreaId());
                    repairApp.setStep(0);
                    repairApp.setRepairDepartmentId(areaInfo.getRepairDepartmentId());
                    repairApp.setMachineType("");
                    repairApp.setInfoSource("");
                    repairApp.setDepartment("");
                    repairApp.setReceiveTime(null);
                    repairApp.setArriveTime(null);
                    repairApp.setAppStatus("申请中");
                    // TODO: 2015/11/1
                    submitRepairApp(repairApp);
                }
            }
        });
    }

    public void getAreaInfo(final String areaId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETAREAINFOBYID);
                Representation result = null;
                try {
                    result = client.post(areaId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "无法获取社区信息");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAreaInfoHandler.sendMessage(message);
                    return;
                }
                try {
                    String areaInfoString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = areaInfoString;
                    getAreaInfoHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "无法获取社区信息");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAreaInfoHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void getAppType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETDETAILNAMEBYTYPENAME);
                Representation result = null;
                try {
                    result = client.post(URLEncoder.encode(getResources().getStringArray(R.array.type_name)[0], "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "获取申请类型连接失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAppTypeHandler.sendMessage(message);
                    return;
                }

                try {
                    String appTypeListString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = appTypeListString;
                    getAppTypeHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "获取申请类型失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAppTypeHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void getErrorType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETDETAILNAMEBYTYPENAME);
                Representation result = null;
                try {
                    result = client.post(URLEncoder.encode(getResources().getStringArray(R.array.type_name)[1], "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "获取故障信息连接失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getErrorTypeHandler.sendMessage(message);
                    return;
                }

                try {
                    String appTypeListString = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = appTypeListString;
                    getErrorTypeHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "获取故障信息失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getErrorTypeHandler.sendMessage(message);
                }
            }
        }).start();
    }

    public void submitRepairApp(final RepairApp repairApp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.POSTREPAIRAPP);
                Representation result = null;
                String repairAppString = JSON.toJSONString(repairApp);
                Log.d("AddRepairAppActivity", "repairAppString" + repairAppString);
                try {
                    String string = URLEncoder.encode(repairAppString, "utf-8");
                    Log.d("AddRepairAppActivity", "string::::::" + string);
                    result = client.post(string);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "上传失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    submitHandler.sendMessage(message);
                    return;
                }
                try {
                    String temp = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = temp;
                    submitHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("AddRepairAppActivity", "上传失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    submitHandler.sendMessage(message);
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
