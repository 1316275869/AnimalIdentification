package com.example.myapplication.identification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Detaile.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.brief.AnimalAdapter;
import com.example.myapplication.brief.briefAnimalData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BriefActivity extends AppCompatActivity {

    String name;
    private TextView tx;
    ListView listView=null;
    Handler handler;
    String information=null;
    static final  int PROGRESS_CHANGED=1;

    AnimalAdapter adapter;
    private String [] AnimalPotoURL=new String[100];

    public  List<briefAnimalData> briefList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        tx=findViewById(R.id.id_tv);
        listView=findViewById(android.R.id.list);

        tx.setText("符合关键字"+"\""+name+"\""+"的动物");
        handler = new Handler() { //主线程更新UI
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case PROGRESS_CHANGED:
                        //...
                        //收到PROGRESS_CHANGED时刷新UI

                        if(briefList.size()==0){
                            tx.setText( "没有符合关键字"+"\""+name+"\""+"的动物");
                        }

                        adapter=new AnimalAdapter(getApplication(),R.layout.brief_animal_data_item,briefList,false);
                        listView.setAdapter(adapter);
                        break;

                }
                super.handleMessage(msg);
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                briefAnimalData br=briefList.get(position);//通过position获取列表点击的信息
                Log.e("wwww", br.getDetailsUrl());
                Intent intent=new Intent();
                intent.putExtra("DetailURL",br.getDetailsUrl());//得到 动物详情的UPL发送给启动的Activity
                intent.putExtra("AniURL",AnimalPotoURL[position]);//动物图片URl
                intent.putExtra("name",br.getName());//动物名
                intent.setClass(BriefActivity.this,DetailActivity.class);//启动
                startActivity(intent);

            }
        });
        briefList.clear();
        brief(name);

    }
    public void brief(final String url){
        new Thread(){
            @Override
            public void run(){
                super.run();
                byte[] data;
                Bitmap b;
                try {
                    //1.根据id查询
                    //标签
                    //class


                    //属性  getElementByAttribute
                    Log.e("fffff","zhes ss");
                    Document doc= Jsoup.connect("http://www.iltaw.com/search?q="+URLjiema(url)).get();

                    Log.e("fffff","http://www.iltaw.com/search?q="+URLjiema(url));
                    Elements element=doc.select("ul.info-list>li");
                    Elements elementPager=doc.select("div.pager-v1 a");
                    Log.d("Page","wwwwwwww");



                    int i=0;
                    for (Element element1 : element){
                        //Log.e("EEE",element1.text());
                        String e1=element1.select("div.image-wrap img").attr("data-url");//动物的图片链接
                        String e3=element1.select("div.image-wrap a").attr("href");//动物的详情链接
                        String e2=element1.select("div.text-wrap a").text();//动物英文名
                        String e4=element1.select("div.text-wrap>p").text();//动物的简略情况 东北虎（学名：Panthera tigris altaica），又称西伯利亚虎。。。
                        information=element1.select("div.image-wrap img").attr("alt");//动物名

                        AnimalPotoURL[i++]=e1;
                        //data=GetUserHead(e1);
                        // b= BitmapFactory.decodeByteArray(data,0,data.length);
                        briefAnimalData aa=new briefAnimalData(e1,information,e4,e3);
                        briefList.add(aa);

                        Log.e("EEE",information);


                    }
//                    briefAnimalData aa=new briefAnimalData(null,"","","");
//                    briefList.add(aa);

                    handler.sendEmptyMessage(PROGRESS_CHANGED);

                    //String href = e.attr("href");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    //进行URLEncoder转码
    public String URLjiema (String str){

        String sURL=null;
        try {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(URLEncoder.encode(str,"utf-8"));
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
