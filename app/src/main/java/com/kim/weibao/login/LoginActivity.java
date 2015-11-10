package com.kim.weibao.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kim.weibao.R;
import com.kim.weibao.content.App;
import com.kim.weibao.content.MyURL;
import com.kim.weibao.index.IndexActivity;
import com.kim.weibao.model.system.UserInfo;
import com.kim.weibao.utils.UAPSP;


import org.restlet.Context;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String USERINFO = "userinfo";

    // UI references.
    @Bind(R.id.sign_in_button)
    Button signInButton;
    @Bind(R.id.login_form)
    ScrollView loginForm;
    @Bind(R.id.sign_in_checkBox)
    CheckBox signInCheckBox;
    @Bind(R.id.username)
    TextInputLayout usernameTil;
    @Bind(R.id.password)
    TextInputLayout passwordTil;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private int backKeyPressedTime = 0;

    ProgressDialog loginDialog;

    Handler loginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String id = (String) msg.obj;
            if (!id.equals("null") && id != null) {
                App.setUSERID(id);
                Log.d("LoginActivity", ">>>>>>>>>>>>>>>>>>登陆成功！！");
                Log.d("LoginActivity", "id:" + id);
                getSystemMenu(id); //获取系统菜单
                return true;
            } else {
                loginDialog.cancel();
                Snackbar snackbar = Snackbar.make(loginForm, "用户名或密码错误！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                Log.d("LoginActivity", ">>>>>>>>>>>>>>>>>>>>>登陆失败！！");
                return false;
            }
        }
    });

    Handler getSystemMenuHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String roleName = (String) msg.obj;
            List<String> list = Arrays.asList(getResources().getStringArray(R.array.role_name));
            Log.d("LoginActivity", "roleName:" + roleName);
            if (!roleName.equals("null") && roleName != null && list.contains(roleName)) {
                App.setROLE(roleName);
                Intent indexIntent = new Intent(LoginActivity.this, IndexActivity.class);
                indexIntent.putExtra("rolename", roleName);
                loginDialog.cancel();
                startActivity(indexIntent);
                finish();
                return true;
            } else {
                loginDialog.cancel();
                Snackbar snackbar = Snackbar.make(loginForm, "登陆出错!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        passwordTil.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        UAPSP uapsp = new UAPSP(LoginActivity.this);
        UserInfo userInfo = uapsp.read(USERINFO);
        if (userInfo != null) {
            String username = userInfo.getUserName().trim();
            String password = userInfo.getPassword().trim();
            usernameTil.getEditText().setText(username);
            passwordTil.getEditText().setText(password);
            signInCheckBox.setChecked(true);
        }
    }

    private void attemptLogin() {
        usernameTil.setError(null);
        passwordTil.setError(null);

        final String username = usernameTil.getEditText().getText().toString();
        final String password = passwordTil.getEditText().getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            usernameTil.setError(getString(R.string.error_username_should_not_empty));
            focusView = usernameTil.getEditText();
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordTil.setError(getString(R.string.error_password_should_not_empty));
            focusView = passwordTil.getEditText();
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(username, password);
        }
    }

    private void login(final String username, final String password) {
        loginDialog = new ProgressDialog(LoginActivity.this);
        loginDialog.setTitle("登陆中");
        loginDialog.setCancelable(true);
        loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginDialog.show();
        //登陆检测操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", username);
                map.put("password", password);

                ClientResource client = new ClientResource(new Context(), MyURL.LOGIN);
//                client.getContext().getParameters().add("socketTimeout", String.valueOf(8000));
                // 上传用户名密码
                Representation result = null;
                try {
                    result = client.post(JSON.toJSONString(map));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("LoginActivity", "获取登陆连接失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    loginHandler.sendMessage(message);
                    return;
                }
                try {
                    String s = result.getText().trim();
                    if (!s.equals("null")) {
                        if (signInCheckBox.isChecked()) {
                            UAPSP uapsp = new UAPSP(LoginActivity.this);
                            uapsp.save(USERINFO, map);
                        }
                        Message message = Message.obtain();
                        message.obj = s;
                        loginHandler.sendMessage(message);
                    } else {
                        loginDialog.cancel();
                        Log.d("LoginActivity", "获取登录ID为空");
                        Snackbar snackbar = Snackbar.make(loginForm, "用户名或密码错误!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("LoginActivity", "获取登陆失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    loginHandler.sendMessage(message);
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
//                client.getContext().getParameters().add("socketTimeout", String.valueOf(8000));
                Representation result = null;
                try {
                    result = client.post(userId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("LoginActivity", "获取用户角色连接失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getSystemMenuHandler.sendMessage(message);
                    return;
                }
                try {
                    String s = result.getText().trim();
                    Log.d("LoginActivity", "s:" + s);
                    Message message = Message.obtain();
                    message.obj = s;
                    getSystemMenuHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("LoginActivity", "获取用户角色失败");
                    Message message = Message.obtain();
                    message.obj = "null";
                    getSystemMenuHandler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backKeyPressedTime == 0) {
            Snackbar snackbar = Snackbar.make(loginForm, "再按一次返回键退出程序", Snackbar.LENGTH_SHORT);
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

