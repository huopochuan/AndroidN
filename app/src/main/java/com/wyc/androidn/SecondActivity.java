package com.wyc.androidn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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
        textView =(TextView)findViewById(R.id.textView);
        editText=(EditText)findViewById(R.id.editText2);

        editText.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP:
                        editText.setText(dragEvent.getClipData().getItemAt(0).getText());
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public DragAndDropPermissions requestDragAndDropPermissions(DragEvent event) {
        Log.d("wyc","requestDragAndDropPermissions  ");
        switch (event.getAction()) {

            case DragEvent.ACTION_DROP:
                Log.d("wyc","requestDragAndDropPermissions  DragEvent.ACTION_DROP");
                /** 3.在这里显示接收到的结果 */
                textView.setText(event.getClipData().getItemAt(0).getText());
                break;

            default:
                break;
        }
        return super.requestDragAndDropPermissions(event);
    }
}
