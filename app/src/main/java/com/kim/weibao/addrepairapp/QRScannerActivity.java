package com.kim.weibao.addrepairapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.kim.weibao.R;
import com.kim.weibao.addrepairapp.zxing.camera.CameraManager;
import com.kim.weibao.addrepairapp.zxing.decode.DecodeThread;
import com.kim.weibao.addrepairapp.zxing.utils.BeepManager;
import com.kim.weibao.addrepairapp.zxing.utils.InactivityTimer;
import com.kim.weibao.addrepairapp.zxing.utils.QRScannerActivityHandler;
import com.kim.weibao.content.MyURL;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 伟阳 on 2015/10/29.
 */
public class QRScannerActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = QRScannerActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private QRScannerActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private Rect mCropRect = null;

    @Bind(R.id.qrscanner_preview)
    SurfaceView qrscannerPreview;
    @Bind(R.id.qrscanner_scan_line)
    ImageView qrscannerScanLine;
    @Bind(R.id.qrscanner_crop_view)
    RelativeLayout qrscannerCropView;
    @Bind(R.id.qrscanner_input_by_hand)
    FloatingActionButton qrscannerInputByHand;
    @Bind(R.id.qrscanner_container)
    RelativeLayout qrscannerContainer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ProgressDialog progressDialog = null;

    private boolean isHasSurface = false;

    Handler toAddRepairAppActivity = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String machineInfo = (String) msg.obj;
            if (!machineInfo.equals("null") && machineInfo != null) {
                // TODO: 2015/10/30 判断跳转
                Intent toAddRepairAppIntent = new Intent(QRScannerActivity.this, AddRepairAppActivity.class);
                toAddRepairAppIntent.putExtra("machineinfo", (String) msg.obj);
                startActivity(toAddRepairAppIntent);
                finish();
                progressDialog.cancel();
            } else {
                Snackbar snackbar = Snackbar.make(qrscannerPreview, "无结果！", Snackbar.LENGTH_SHORT);
                snackbar.show();
                restartPreviewAfterDelay(1000);
                progressDialog.cancel();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        qrscannerScanLine.startAnimation(animation);

        initFloatingActionButton();
    }

    public void initFloatingActionButton() {
        qrscannerInputByHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

    public void showInputDialog() {
        final EditText machineCodeEt = new EditText(QRScannerActivity.this);
        machineCodeEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(QRScannerActivity.this);
        builder.setTitle("请输入设备号");
        builder.setView(machineCodeEt);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String machineCode = machineCodeEt.getText().toString().trim();
                getMachineInfo(machineCode);
            }
        });
        machineCodeEt.requestFocus();
        builder.create().show();
    }

    public void getMachineInfo(final String machineCode) {
        progressDialog = new ProgressDialog(QRScannerActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("获取中...");
        progressDialog.show();
        Log.d(TAG, machineCode);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //  获取设备信息
                ClientResource client = new ClientResource(MyURL.GETMACHINEINFOBYMACHINECODE);
                Representation result = null;
                try {
                    result = client.post(machineCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.obj = "null";
                    toAddRepairAppActivity.sendMessage(message);
                    return;
                }
                try {
                    String machineInfo = result.getText().trim();
                    Message message = new Message();
                    message.obj = machineInfo;
                    toAddRepairAppActivity.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    /**
     * @param rawResult 扫码获取的结果
     * @param bundle
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        // TODO: 2015/10/30
        Toast.makeText(this, rawResult.getText().trim(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, rawResult.getText().trim());
        getMachineInfo(rawResult.getText().trim());
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new QRScannerActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        qrscannerCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = qrscannerCropView.getWidth();
        int cropHeight = qrscannerCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = qrscannerContainer.getWidth();
        int containerHeight = qrscannerContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        handler = null;

        if (isHasSurface) {
            initCamera(qrscannerPreview.getHolder());
        } else {
            qrscannerPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            qrscannerPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }
}
