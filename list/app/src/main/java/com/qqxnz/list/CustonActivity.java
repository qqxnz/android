package com.qqxnz.list;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CustonActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custon);
        listView = findViewById(R.id.customListView);
        Log.d("Custom","onCreate");
        List<ItemData> list = new ArrayList<>();

        for (int i = 0 ; i < 100 ;i++){
            ItemData data = new ItemData("标题"+i);
            list.add(data);
            Log.d("Data",data.getTitle());
        }

        listView.setAdapter(new MyAdapter(list,this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onclick","点击位置:"+position);
            }
        });

    }
}