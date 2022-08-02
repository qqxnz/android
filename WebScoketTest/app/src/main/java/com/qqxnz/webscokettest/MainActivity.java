package com.qqxnz.webscokettest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    TextView stateTextView;
    Button connectButton;
    EditText editText;
    Button sendButton;
    Button closeButton;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateTextView = findViewById(R.id.status);
        connectButton = findViewById(R.id.connect);
        editText = findViewById(R.id.edit);
        sendButton = findViewById(R.id.send);
        closeButton = findViewById(R.id.close);
        messageTextView = findViewById(R.id.message);



        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ws://172.16.5.158:3000
                //ws://106.75.24.200:7777/ws
                URI uri = URI.create("ws://172.16.5.158:3000");
                MZDWebSocketManager.getInstance().setInfo(uri,"111");
                MZDWebSocketUser user = new MZDWebSocketUser();
                user.setUid(111111);
                user.setToken("token11");
                user.setSecret("secret111");
                user.setType("xea");
                user.setOffsetTs(2);
                MZDWebSocketManager.getInstance().setUser(user);
                MZDWebSocketManager.getInstance().connect();
//                startListenNetwork();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Log.d("MainActivity","发送消息"+ text);
                MZDWebSocketManager.getInstance().sendMessage("/message",text);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MZDWebSocketManager.getInstance().close();
//                stopListenNetwork();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

//    public void startListenNetwork(){
//        MZDWebSocketManager.getInstance().startListenNetworkStateChange(this);
//    }
//
//    public void stopListenNetwork(){
//        MZDWebSocketManager.getInstance().stopListenNetworkStateChange();
//    }


}