package com.example.liuwangshu.moonmessenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MessengerActivity extends AppCompatActivity {
    private Messenger mMessenger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent=new Intent(MessengerActivity.this,MessengerService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.MSG_FROMCLIENT:
                    Log.i(MessengerService.TAG, "客户端收到的信息-------" + msg.getData().get("rep"));
                    break;
            }
        }
    };


private ServiceConnection mServiceConnection=new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMessenger=new Messenger(service);
        Message mMessage=Message.obtain(null,MessengerService.MSG_FROMCLIENT);
        Bundle mBundle=new Bundle();
        mBundle.putString("msg", "这里是客户端，收到了嘛服务端");
        mMessage.setData(mBundle);
        //将Messenger传递给服务端
        mMessage.replyTo=new Messenger(mHandler);
        try {
            mMessenger.send(mMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
};
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);

    }
}
