package com.qqxnz.webscokettest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    MZDWebSocketClient client;
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
                createClient();
                client.connect();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(editText.getText().toString());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.close();
            }
        });

    }


    void  createClient(){
        //"ws://172.16.5.151:3000"
        URI uri = URI.create("ws://172.16.5.151:3000");

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
                        messageTextView.setText("收到消息:"+message);
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

}