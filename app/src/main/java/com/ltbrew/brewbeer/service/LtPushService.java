package com.ltbrew.brewbeer.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ltbrew.brewbeer.R;
import com.ltbrew.brewbeer.api.ConfigApi;
import com.ltbrew.brewbeer.api.longconnection.TransmitCmdService;
import com.ltbrew.brewbeer.api.longconnection.interfaces.FileSocketReadyCallback;
import com.ltbrew.brewbeer.api.longconnection.process.ParsePackKits;
import com.ltbrew.brewbeer.api.longconnection.process.cmdconnection.CmdsConstant;
import com.ltbrew.brewbeer.uis.activity.BrewHomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qiusiping on 16/5/8.
 */
public class LtPushService extends IntentService implements FileSocketReadyCallback {
    private static final String TAG = "LtPushService";
    public static final String CMN_PRGS_PUSH_ACTION = "CMN_PRGS_PUSH";
    public static final String CMN_PRGS_CHECK_ACTION = "CMN_PRGS_CHECK_ACTION";
    public static final String PUSH_MSG_EXTRA = "pushMsg";
    public static final String CMD_RPT_ACTION = "cmd_rpt_action";
    public static final String UNBIND_ACTION = "unbind_action";
    public static final String FILE_SOCKET_IS_READY_ACTION = "fileSocketIsReadyAction";
    private int tryAgain = 3;
    private TransmitCmdService transmitCmdService;
    private final static int GRAY_SERVICE_ID = 1001;
    ServiceBinder serviceBinder = new ServiceBinder();
    private int state;
    private int starting = 0;
    private int started = 1;

    public LtPushService() {
        super("LtPushService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class ServiceBinder extends Binder {
        public LtPushService getService() {
            return LtPushService.this;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "======onStartCommand=====");
        startConnectionOnNewThread();
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void startConnectionOnNewThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                transmitCmdService = ConfigApi.startLongConnection(LtPushService.this);
                state = starting;

            }
        }).start();
    }


    public void sendBrewSessionCmd(Long package_id) {
        Log.e("", "===###################sendBrewSessionCmd####################====");

        if (transmitCmdService != null)
            transmitCmdService.sendBrewSessionCmd(package_id);
    }

    @Override
    public void onGetOauthTokenFailed() {

    }

    @Override
    public void onOAuthFailed() {
        Log.e("", "======================onOAuthFailed=========================");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (tryAgain > 0) {
                    tryAgain--;
                    transmitCmdService = ConfigApi.startLongConnection(LtPushService.this);
                    state = starting;
                }
            }
        }).start();

    }

    @Override
    public void onCmdSocketReady() {
        Log.e("", "======================onCmdSocketReady=========================");
        tryAgain = 3;

    }

    int trySendBrewSession = 3;

    @Override
    public void onFileSocketReady() {
        Log.e("", "======================onFileSocketReady=========================");
        if (state == starting && trySendBrewSession > 0) {
            Intent intent = new Intent();
            intent.setAction(FILE_SOCKET_IS_READY_ACTION);
            sendBroadcast(intent);
            trySendBrewSession--;
        }
        state = started;
    }

    @Override
    public void onInitializeLongConnFailed() {
        Log.e("", "======================onInitializeLongConnFailed=========================");

    }

    @Override
    public void onInitFileSocketFailed() {
        Log.e("", "======================onInitFileSocketFailed=========================");

    }

    @Override
    public void onFileSocketReconnect() {
        Log.e("", "======================onFileSocketReconnect=========================");
        state = starting;

    }

    @Override
    public void onCmdSocketReconnect() {
        Log.e("", "======================onCmdSocketReconnect=========================");

    }

    @Override
    public void onMaximumFileLength(int length) {
        Log.e("", "======================onMaximumFileLength=========================");

    }

    @Override
    public void onFileBegined() {
        Log.e("", "======================onFileBegined=========================");

    }

    @Override
    public void onFileUploadSuccess() {
        Log.e("", "======================onFileUploadSuccess=========================");

    }

    @Override
    public void onFileUploadFailed() {
        Log.e("", "======================onFileUploadFailed=========================");

    }

    @Override
    public void onFileUploadEnd() {
        Log.e("", "======================onFileUploadEnd=========================");

    }

    @Override
    public void onGetPidHeart(ArrayList<Integer> result, String r_hrr_endtime, String linkedIndex, String endindex) {

    }

    @Override
    public void onCmdHasPush(List<String> pushLists, String pok, long pushTime) {
        Log.e(TAG, "" + pushLists);
        int size = pushLists.size();
        String pushMsg = pushLists.get(0);
        PushMsg pushMessageObj = parsePushMsg(pushMsg);
        String cb = pushMessageObj.cb;
        if (cb.contains(":")) {
            String[] splittedStr = cb.split(":");
            cb = splittedStr[1];
        }
        Object obj = CmdsConstant.CMDSTR.lookup.get(cb);
        if (obj == null) {
            System.out.println("未识别的指令");
            return;
        }
        if(isBackground(this)){
            Intent intent = new Intent(this, BrewHomeActivity.class);
            showNotification(this, pushMessageObj.des, 11, intent);
            return;
        }
        Intent intent = new Intent();
        switch (PushCommand.valueOf(cb)) {
            case bind:
                break;
            case unbind:
                intent.setAction(UNBIND_ACTION);
                sendBroadcast(intent);
                break;
            case cmd_report:
                intent.setAction(CMD_RPT_ACTION);
                sendBroadcast(intent);
                break;
            case cmn_prgs:
                intent.setAction(CMN_PRGS_PUSH_ACTION);
                intent.putExtra(PUSH_MSG_EXTRA, pushMessageObj);
                sendBroadcast(intent);
                break;
            case cmn_msg:
                intent.setAction(CMN_PRGS_PUSH_ACTION);
                intent.putExtra(PUSH_MSG_EXTRA, pushMessageObj);
                sendBroadcast(intent);
                break;
            case brew_session:

                break;
        }

    }

    private PushMsg parsePushMsg(String pushMsg) {
        PushMsg pushMessage = new PushMsg();
        JSONObject jsonObject = JSON.parseObject(pushMsg);
        String cb = jsonObject.getString("cb");
        String description = jsonObject.getString("description");
        String id = jsonObject.getString("id");
        Integer f = jsonObject.getInteger("f");
        pushMessage.cb = cb;
        pushMessage.des = description;
        pushMessage.id = id;
        pushMessage.f = f;
        JSONObject pld = jsonObject.getJSONObject("_pld");
        if (pld != null || !pld.equals("null")) {
            String body = pld.getString("body");
            Integer si = pld.getInteger("si");
            Integer ratio = pld.getInteger("ratio");
            String st = pld.getString("st");

            pushMessage.body = body;
            pushMessage.si = si;
            pushMessage.ratio = ratio;
            pushMessage.st = st;
        }
        return pushMessage;
    }

    @Override
    public void onGetPidHeartHistory(String endindex, HashMap<String, ArrayList<Integer>> maps) {

    }

    @Override
    public void onGetCmdPrgs(String percent, String seq_index, String body) {
        PushMsg pushMessage = new PushMsg();
        pushMessage.ratio = Integer.valueOf(percent);
        pushMessage.si = Integer.valueOf(seq_index);
        byte[] bytes = ParsePackKits.charToByteArray(body.toCharArray()); //utf8中文串通过转字符串再转String会出现乱码， 所以在次将String转会byte数组， 再用系统的方法转为中文
        pushMessage.body = new String(bytes);
        Intent intent = new Intent();
        intent.setAction(CMN_PRGS_CHECK_ACTION);
        intent.putExtra(PUSH_MSG_EXTRA, pushMessage);
        sendBroadcast(intent);
    }


    public void showNotification(Context context, String txt, int notificationId, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 下面需兼容Android 2.x版本是的处理方式
        Notification.Builder nb = new Notification.Builder(context).setContentTitle("来自LinkBrew")
                .setContentText(txt).setNumber(1).setTicker(txt).setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent);
        Notification notify1 = null;
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 16) {
            notify1 = nb.getNotification();
        } else if (Build.VERSION.SDK_INT >= 16) {
            nb.build();
        }
        if (notify1 == null) {
            return;
        }
        notify1.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
        manager.notify(notificationId, notify1);
    }


    @Override
    public void onServerRespError() {

    }

    @Override
    public void onLongConnectionKickedOut() {

    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
