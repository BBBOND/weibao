package com.kim.weibao.showarea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.model.basicData.AreaInfo;
import com.kim.weibao.utils.URLUtil;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/11/7.
 */
public class DetailedAreaActivity extends AppCompatActivity {


    @Bind(R.id.detail_area_code)
    TextView detailAreaCode;
    @Bind(R.id.detail_area_name)
    TextView detailAreaName;
    @Bind(R.id.detail_area_address)
    TextView detailAreaAddress;
    @Bind(R.id.detail_area_type)
    TextView detailAreaType;
    @Bind(R.id.detail_area_contects_name)
    TextView detailAreaContectsName;
    @Bind(R.id.detail_area_tel)
    TextView detailAreaTel;
    @Bind(R.id.detail_area_mail)
    TextView detailAreaMail;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String areaId = "";
    private ProgressDialog loadingProgressDialog;


    Handler getAreaInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String areaInfoString = (String) msg.obj;
            if (!areaInfoString.equals("null") && areaInfoString != null) {
                AreaInfo areaInfo = JSON.parseObject(areaInfoString, AreaInfo.class);
                detailAreaCode.setText(areaInfo.getAreaCode());
                detailAreaName.setText(areaInfo.getAreaName());
                detailAreaAddress.setText(areaInfo.getAreaAddress());
                detailAreaType.setText(areaInfo.getAreaType());
                detailAreaContectsName.setText(areaInfo.getAreaContactsName());
                detailAreaTel.setText(areaInfo.getAreaTel());
                detailAreaTel.setMovementMethod(LinkMovementMethod.getInstance());
                detailAreaMail.setText(areaInfo.getAreaMail());
                detailAreaMail.setMovementMethod(LinkMovementMethod.getInstance());
                loadingProgressDialog.cancel();
                return true;
            } else {
                loadingProgressDialog.cancel();
                Toast.makeText(DetailedAreaActivity.this, "获取社区信息失败", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailarea);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        areaId = intent.getStringExtra("areaid");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingProgressDialog = new ProgressDialog(DetailedAreaActivity.this);
        loadingProgressDialog.setTitle("加载中...");
        loadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(URLUtil.getRealURL(getApplicationContext(),MyURL.GETAREAINFOBYID));
                Representation result = null;
                try {
                    result = client.post(areaId);
                } catch (ResourceException e) {
                    e.printStackTrace();
                    Log.d("DetailedAreaActivity", "获取社区信息连接失败");
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
                    Log.d("DetailedAreaActivity", "获取社区信息失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getAreaInfoHandler.sendMessage(message);
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
