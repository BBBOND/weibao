package com.kim.weibao.utils;

import android.content.Context;
import android.util.Log;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

/**
 * Created by 伟阳 on 2015/11/21.
 */
public class URLUtil {

    public static String getRealURL(Context context, String url) {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        String ipAndPort = mySharedPreferences.readIPAndPort();
        String urlStr = null;
        if (ipAndPort != null && !ipAndPort.equals(":")) {
            urlStr = "http://" + ipAndPort + url;
        } else {
            urlStr = "http://" + "192.168.74.15:8888" + url;
        }
        return urlStr;
    }
}
