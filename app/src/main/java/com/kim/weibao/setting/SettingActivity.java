package com.kim.weibao.setting;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.kim.weibao.R;
import com.kim.weibao.utils.MySharedPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/11/15.
 */
public class SettingActivity extends AppCompatActivity {

    String TAG = "SettingActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.setting_ip0)
    EditText settingIp0;
    @Bind(R.id.setting_ip1)
    EditText settingIp1;
    @Bind(R.id.setting_ip2)
    EditText settingIp2;
    @Bind(R.id.setting_ip3)
    EditText settingIp3;
    @Bind(R.id.setting_port)
    EditText settingPort;
    @Bind(R.id.setting_submit)
    Button settingSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    public void init() {

        MySharedPreferences mySharedPreferences = new MySharedPreferences(SettingActivity.this);
        String ipAndPort = mySharedPreferences.readIPAndPort();
        if (!ipAndPort.equals(":") && ipAndPort != null && ipAndPort.length() > 1) {
            String[] IAP = ipAndPort.split(":");
            String[] ip = IAP[0].split("[.]");
            settingIp0.setText(ip[0]);
            settingIp1.setText(ip[1]);
            settingIp2.setText(ip[2]);
            settingIp3.setText(ip[3]);
            settingPort.setText(IAP[1]);
        } else {
            settingIp0.setText("192");
            settingIp1.setText("168");
            settingIp2.setText("174");
            settingIp3.setText("15");
            settingPort.setText("8888");
        }


        settingIp0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Integer.parseInt(s.toString()) > 254) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("不能大于254!");
                    builder.create().show();
                    settingIp0.setText("254");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        settingIp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Integer.parseInt(s.toString()) > 254) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("不能大于254!");
                    builder.create().show();
                    settingIp1.setText("254");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        settingIp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Integer.parseInt(s.toString()) > 254) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("不能大于254!");
                    builder.create().show();
                    settingIp2.setText("254");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        settingIp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && Integer.parseInt(s.toString()) > 254) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("不能大于254!");
                    builder.create().show();
                    settingIp3.setText("254");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        settingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip0 = settingIp0.getText().toString().trim();
                String ip1 = settingIp1.getText().toString().trim();
                String ip2 = settingIp2.getText().toString().trim();
                String ip3 = settingIp3.getText().toString().trim();
                String port = settingPort.getText().toString().trim();

                if (ip0.equals("") || ip1.equals("") || ip2.equals("") || ip3.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("请完整填写ip!");
                    builder.create().show();
                    settingIp0.requestFocus();
                } else if (port.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("请填写端口号!");
                    builder.create().show();
                    settingPort.requestFocus();
                } else {
                    String ip = ip0 + "." + ip1 + "." + ip2 + "." + ip3;
                    MySharedPreferences mySharedPreferences = new MySharedPreferences(SettingActivity.this);
                    mySharedPreferences.saveIPAndPort(ip, port);
                    Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
