package com.kim.weibao.index;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;

import com.kim.weibao.R;
import com.kim.weibao.about.AboutActivity;
import com.kim.weibao.addrepairapp.QRScannerActivity;
import com.kim.weibao.addrepairplan.ShowRepairAppActivity;
import com.kim.weibao.checkproject.ShowCompletedProjectActivity;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.examplan.ShowRepairPlanActivity;
import com.kim.weibao.login.LoginActivity;
import com.kim.weibao.planschedule.ShowPlanScheduleActivity;
import com.kim.weibao.showarea.ShowAreaActivity;
import com.kim.weibao.utils.URLUtil;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/19.
 */
public class IndexActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.index_gridlayout)
    GridLayout indexGridlayout;

    private int backKeyPressedTime = 0;

    Handler titleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String username = (String) msg.obj;
            if (!username.equals("null") && username != null) {
                setTitle("你好，" + username);
                return true;
            } else {
                setTitle("你好");
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String roleName = intent.getStringExtra("rolename");
        getUserName();
        if (roleName.equals(getResources().getStringArray(R.array.role_name)[0])) {
            Log.d("IndexActivity", "roleName:" + roleName.equals(getResources().getStringArray(R.array.role_name)[0]));
            //创建普通用户菜单
            indexGridlayout.addView(newAddRepairAppButton());
            indexGridlayout.addView(newShowAreaButton());
            indexGridlayout.addView(newAboutButton());
        } else if (roleName.equals(getResources().getStringArray(R.array.role_name)[1])) {
            Log.d("IndexActivity", "roleName:" + roleName.equals(getResources().getStringArray(R.array.role_name)[1]));
            //创建监管用户菜单
            indexGridlayout.addView(newExamPlanButton());
            indexGridlayout.addView(newCheckProjectButton());
            indexGridlayout.addView(newShowAreaButton());
            indexGridlayout.addView(newAboutButton());
        } else if (roleName.equals(getResources().getStringArray(R.array.role_name)[2])) {
            Log.d("IndexActivity", "roleName:" + roleName.equals(getResources().getStringArray(R.array.role_name)[2]));
            //创建维保用户菜单
            indexGridlayout.addView(newAddRepairAppButton());
            indexGridlayout.addView(newAddRepairPlanButton());
            indexGridlayout.addView(newPlanScheduleButton());
            indexGridlayout.addView(newShowAreaButton());
            indexGridlayout.addView(newAboutButton());
        }
    }

    protected void getUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(URLUtil.getRealURL(getApplicationContext(),MyURL.GETUSERINFO));
                Representation result = null;
                try {
                    result = client.post(App.getUSERID());
                } catch (ResourceException e) {
                    e.printStackTrace();
                    Log.d("IndexActivity", "获取用户名连接失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    titleHandler.sendMessage(message);
                    return;
                }
                try {
                    String username = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = username;
                    titleHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("IndexActivity", "获取用户名失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    titleHandler.sendMessage(message);
                }

            }
        }).start();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                App.setUSERID("");
                App.setROLE("");
                startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }

    public Button newAddRepairAppButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[0]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, QRScannerActivity.class));
            }
        });
        return button;
    }

    public Button newAddRepairPlanButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[1]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, ShowRepairAppActivity.class));
            }
        });
        return button;
    }

    public Button newExamPlanButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[2]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, ShowRepairPlanActivity.class));
            }
        });
        return button;
    }

    public Button newPlanScheduleButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[3]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, ShowPlanScheduleActivity.class));
            }
        });
        return button;
    }

    public Button newCheckProjectButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[4]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, ShowCompletedProjectActivity.class));
            }
        });
        return button;
    }

    public Button newShowAreaButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[5]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, ShowAreaActivity.class));
            }
        });
        return button;
    }

    public Button newAboutButton() {
        Button button = new Button(IndexActivity.this);
        button.setText(getResources().getStringArray(R.array.system_menus)[6]);
        button.setWidth((getScreenWidth() - 50) / 3);
        button.setHeight((getScreenWidth() - 50) / 3);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackground(getResources().getDrawable(R.color.colorPrimaryDark));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, AboutActivity.class));
            }
        });
        return button;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backKeyPressedTime == 0) {
            Snackbar snackbar = Snackbar.make(indexGridlayout, "再按一次返回键退出程序", Snackbar.LENGTH_SHORT);
            snackbar.show();
            backKeyPressedTime = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        backKeyPressedTime = 0;
                    }
                }
            }.start();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backKeyPressedTime == 1) {
            this.finish();
            System.exit(0);
        }
        return true;
    }
}
