package com.wyc.androidn;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText editText;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private int i=0;

    private MessageReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MY_BROADCAST");
        registerReceiver(receiver, filter);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d("wyc","onCreate");

        btn=(Button)findViewById(R.id.button);
        btn3=(Button)findViewById(R.id.button3);
        editText=(EditText)findViewById(R.id.editText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,SecondActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!TextUtils.isEmpty(editText.getText())) {
                        ClipData.Item item = new ClipData.Item(editText.getText());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData dragData = new ClipData("test", mimeTypes, item);
                        View.DragShadowBuilder shadow = new View.DragShadowBuilder(editText);
                        view.startDragAndDrop(dragData, shadow, null, View.DRAG_FLAG_GLOBAL);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        btn2=(Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalNotification();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageNotification(j+"");
            }
        });

        btn4=(Button)findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewNotification();
            }
        });
    }
    public void CustomViewNotification(){
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification_customview);
        contentView.setImageViewResource(R.id.imgview,R.drawable.bg);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.chat)
                .setLargeIcon( BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.head))
                .setCustomContentView(contentView)
                .setStyle(new Notification.DecoratedCustomViewStyle())
                .setRemoteInputHistory(new String[]{"good","boy"})
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i, notification);
    }
    public static String KEY_TEXT_REPLY = "key_text_reply";
    public void NormalNotification(){
        i++;
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(MainActivity.this, MessageReceiver.class), 0);
        // Key for the string that's delivered in the action's intent.
        String KEY_TEXT_REPLY = "key_text_reply";
        String replyLabel ="reply";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        // Create the reply action and add the remote input.
        Notification.Action action =
                new Notification.Action.Builder(R.drawable.send,
                        "send", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification and add the action.
        Notification newMessageNotification =
                new Notification.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.chat)
                        .setContentTitle("ContentTitle")
                        .setContentText("ContentText")
                        .setTicker("TickerText:您有新短消息，请注意查收")
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true)
                        .setRemoteInputHistory(new String[]{"good","boy"})
                        .setLargeIcon( BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.head))
                        .setNumber(i).setSubText("subtext")
                        .addAction(action).build();

// Issue the notification.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i, newMessageNotification);

    }
    int j=0;
    Handler handle=new Handler();
    public void MessageNotification(String msg){

        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, 0);


        String replyLabel ="reply";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        // Create the reply action and add the remote input.
        Notification.Action action =
                new Notification.Action.Builder(R.drawable.send,
                        "send", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        Notification notification = new Notification.Builder(this).
                setStyle(new Notification.MessagingStyle("Me")
                        .setConversationTitle("Team lunch")
                        .addMessage(msg+" Hi", System.currentTimeMillis(), null) // Pass in null for user.
                        .addMessage(msg+" What's up?",  System.currentTimeMillis(), "Coworker")
                        .addMessage(msg+" Not much",  System.currentTimeMillis(), null)
                        .addMessage(msg+" How about lunch?",  System.currentTimeMillis(), "Coworker"))
                .setSmallIcon(R.drawable.chat)
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setTicker("TickerText:您有新短消息，请注意查收")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setLargeIcon( BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.head))
                .setNumber(i).setSubText("subtext")
                .addAction(action).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(i, notification);

        handle.postDelayed(r,1000);
    }
    Runnable r= new Runnable() {
        @Override
        public void run() {
            MessageNotification(""+(j++));
        }
    };
    public DragAndDropPermissions requestDragAndDropPermissions(DragEvent event) {
        Log.d("wyc","requestDragAndDropPermissions  ");
        switch (event.getAction()) {

            case DragEvent.ACTION_DROP:
                Log.d("wyc","Main requestDragAndDropPermissions  DragEvent.ACTION_DROP");
                break;

            default:
                break;
        }
        return super.requestDragAndDropPermissions(event);
    }
    @Override
    public boolean isInMultiWindowMode() {
        return super.isInMultiWindowMode();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("wyc","onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("wyc","onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("wyc","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wyc","onPause");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("wyc","onConfigurationChanged");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("wyc","onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wyc","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d("wyc","onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handle.removeCallbacks(r);
            Log.d("wyc","receive msg");
            String msg=(String) getMessageText(intent);
            if(msg!=null) {
                Toast.makeText(context, msg, 0).show();
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(i);

        }
        private CharSequence getMessageText(Intent intent) {
            Bundle remoteInput = android.support.v4.app.RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                return remoteInput.getCharSequence(MainActivity.KEY_TEXT_REPLY);
            }
            return null;
        }
    }

}
