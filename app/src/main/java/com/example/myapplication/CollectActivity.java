package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.Detaile.DetailActivity;
import com.example.myapplication.androidclient.Client;
import com.example.myapplication.androidclient.Collect;
import com.example.myapplication.brief.AnimalAdapter;
import com.example.myapplication.brief.briefAnimalData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    Client client;
    Handler handler;
    ListView listView;
    CollectAdapter adapter;
    List<briefAnimalData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        listView=findViewById(android.R.id.list);
        handler = new Handler() { //主线程更新UI
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 33:
                        //...
                        //收到PROGRESS_CHANGED时刷新U

                        adapter=new CollectAdapter(getApplication(),R.layout.collect_animal_data_item,list,true);
                        listView.setAdapter(adapter);

                        break;

                }
                super.handleMessage(msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                {

                    try {
                        client=new Client();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    client.sendInfo("collect"+"&"+Login.personal.getP_useid());
                    list= client.readCollect();
                    handler.sendEmptyMessage(33);
                }
            }
        }).start();
        if (Login.personal.getP_useid()!=null){

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                briefAnimalData br=list.get(position);//通过position获取列表点击的信息
                Log.e("wwww", br.getDetailsUrl());
                Intent intent=new Intent();
                intent.putExtra("DetailURL",br.getDetailsUrl());//得到 动物详情的UPL发送给启动的Activity
                intent.putExtra("AniURL",br.getImg());//动物图片URl
                intent.putExtra("name",br.getName());//动物名
                intent.setClass(getApplication(), DetailActivity.class);//启动
                startActivity(intent);

            }
        });
    }
}
