package com.example.liuwangshu.moonmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    public static final String TAG = "MoonMessenger";
    public static final int MSG_FROMCLIENT=1000;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROMCLIENT:
                    Log.i(TAG,"服务端收到的信息-------"+msg.getData().get("msg"));
                    //得到客户端传来的Messenger对象
                    Messenger mMessenger=msg.replyTo;
                    Message mMessage=Message.obtain(null,MessengerService.MSG_FROMCLIENT);
                    Bundle mBundle=new Bundle();
                    mBundle.putString("rep","这里是服务端，我们收到信息了");
                    mMessage.setData(mBundle);
                    try {
                        mMessenger.send(mMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Messenger mMessenger=new Messenger(mHandler);
        return mMessenger.getBinder();
    }
}
