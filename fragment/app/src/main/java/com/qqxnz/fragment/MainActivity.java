package com.qqxnz.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    NotificationsUtils.goToSet(getApplicationContext());
//               Boolean enable = NotificationsUtils.isNotificationEnabled(getApplicationContext());
//                Log.d("notification","是否开启:"+ enable);
//                showToast(enable ? "开启" : "关闭");
            }
        });

        Button btn2 = findViewById(R.id.btn2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean enable = Notice.isNotificationEnabled(getApplicationContext());
                Log.d("notification","是否开启:"+ enable);
                showToast(enable ? "开启" : "关闭");
            }
        });



    }


    void showToast(String message){
        Toast.makeText(this,message,2).show();
    }

}