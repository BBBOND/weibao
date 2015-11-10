package com.kim.weibao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kim.weibao.model.system.UserInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 用户名密码保存
 * Created by 伟阳 on 2015/10/20.
 */
public class UAPSP {
    private Context context;

    public UAPSP(Context context) {
        this.context = context;
    }

    public boolean save(String name, Map<String, Object> map) {
        boolean flag = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", String.valueOf(map.get("username")));
        editor.putString("password", String.valueOf(map.get("password")));
        flag = editor.commit();
        return flag;
    }

    public UserInfo read(String name) {
        UserInfo userInfo = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if (!username.equals("") && !password.equals("")) {
            userInfo = new UserInfo();
            userInfo.setUserName(username);
            userInfo.setPassword(password);
        }
        return userInfo;
    }
}
