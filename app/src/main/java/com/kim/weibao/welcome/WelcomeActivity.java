package com.kim.weibao.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.examplan.ExamPlanActivity;
import com.kim.weibao.index.IndexActivity;
import com.kim.weibao.login.LoginActivity;
import com.kim.weibao.model.basicData.AreaInfo;
import com.kim.weibao.model.system.UserInfo;
import com.kim.weibao.utils.UAPSP;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 伟阳 on 2015/10/29.
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String USERINFO = "userinfo";


    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String id = (String) msg.obj;
            if (!id.equals("null") && id != null) {
                App.setUSERID(id);
                Log.d("WelcomeActivity", ">>>>>>>>>>>>>>>>>>登陆成功！！");
                getSystemMenu(id);
                return true;
            } else {
                Toast.makeText(WelcomeActivity.this, "自动登陆失败,请手动登录!", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                Log.d("WelcomeActivity", ">>>>>>>>>>>>>>>>>>>>>登陆失败！！");
                finish();
                return false;
            }
        }
    });

    Handler getSystemMenuHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String roleName = (String) msg.obj;
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.role_name));
            if (!roleName.equals("null") && roleName != null && list.contains(roleName)) {
                App.setROLE(roleName);
                Intent indexIntent = new Intent(WelcomeActivity.this, IndexActivity.class);
                indexIntent.putExtra("rolename", roleName);
                startActivity(indexIntent);
                finish();
                return true;
            } else {
                Toast.makeText(WelcomeActivity.this, "自动登陆失败,请手动登录!", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    public void init() {
        UAPSP uapsp = new UAPSP(WelcomeActivity.this);
        UserInfo userInfo = uapsp.read(USERINFO);
        if (userInfo != null) {
            login(userInfo.getUserName(), userInfo.getPassword());
        } else {
            new Handler().postAtTime(new toLoginActivity(), SystemClock.uptimeMillis() + 1500);
        }
    }

    private void login(final String username, final String password) {
        //登陆检测操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.LOGIN);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", username);
                map.put("password", password);
                Representation result = null;
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "null";
                    loginHandler.sendMessage(message);
                    return;
                }
                try {
                    Thread.sleep(1000);
                    String s = result.getText().trim();
                    Message message = Message.obtain();
                    message.obj = s;
                    loginHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getSystemMenu(final String userId) {
        Log.d("LoginActivity", "userId:" + userId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientResource client = new ClientResource(MyURL.GETSYSTEMMENU);
                Representation result = null;
                try {
                    result = client.post(userId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.obj = "null";
                    getSystemMenuHandler.sendMessage(message);
                }
                try {
                    String s = result.getText().trim();
                    Log.d("WelcomeActivity", "s:" + s);
                    Message message = Message.obtain();
                    message.obj = s;
                    getSystemMenuHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class toLoginActivity implements Runnable {
        @Override
        public void run() {
            Intent indexIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(indexIntent);
            finish();
        }
    }
}
