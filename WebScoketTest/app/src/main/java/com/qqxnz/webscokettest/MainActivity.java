package com.qqxnz.webscokettest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements NetStateChangeObserver {
    TextView stateTextView;
    Button connectButton;
    EditText editText;
    Button sendButton;
    Button closeButton;
    TextView messageTextView;
    MZDWebSocketClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        NetworkChangeReceiver.registerReceiver(this);
        // 注册
        NetworkChangeReceiver.registerObserver(this);

        stateTextView = findViewById(R.id.status);
        connectButton = findViewById(R.id.connect);
        editText = findViewById(R.id.edit);
        sendButton = findViewById(R.id.send);
        closeButton = findViewById(R.id.close);
        messageTextView = findViewById(R.id.message);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClient();
                client.connect();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i = 0; i <50 ; i++) {
                    Log.e("MZDWebSocketClient", "sendMessage" );
                    client.send(editText.getText().toString());
//                }
//                client.sendPing();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.close();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//解除注册
        NetworkChangeReceiver.unRegisterReceiver(this);
    }

    void  createClient(){
        //"ws://172.16.5.151:3000"
        URI uri = URI.create("ws://106.75.24.200:7777/ws");

        client = new MZDWebSocketClient(uri, new MZDWebSocketClientListener() {
            @Override
            public void onOpen() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        stateTextView.setText("已连接");
                    }
                });

            }

            @Override
            public void onMessage(String message) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        messageTextView.setText(messageTextView.getText().toString() + "\n"+ message);
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        stateTextView.setText("已断开");
                    }
                });
            }

            @Override
            public void onError(Exception ex) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        stateTextView.setText("错误信息："+ex.getMessage());
                    }
                });
            }
        });

    }


    @Override
    public void onDisconnect() {
        Log.d("NetworkChange","onDisconnect");
    }

    @Override
    public void onMobileConnect() {
        Log.d("NetworkChange","onMobileConnect");
    }

    @Override
    public void onWifiConnect() {
        Log.d("NetworkChange","onWifiConnect");
    }
}