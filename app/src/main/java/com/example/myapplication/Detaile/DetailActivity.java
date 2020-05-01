package com.example.myapplication.Detaile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;


import com.example.myapplication.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class DetailActivity extends AppCompatActivity {

    Handler handler;
    private XRecyclerView recyclerView;
    private ArrayList<DetailData> list=new ArrayList<>();
    String s3,URL,AniPotoURL=null,name=null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context=this;
        Intent intent=getIntent();
        URL=intent.getStringExtra("DetailURL")+"/";

        AniPotoURL=intent.getStringExtra("AniURL");
        name=intent.getStringExtra("name");
        recyclerView=findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setPullRefreshEnabled(false);//上拉刷新
        recyclerView.setLoadingMoreEnabled(false);//下来加载

        //recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
       // Details(URL);
//        handler = new Handler() { //主线程更新UI
//
//            public void handleMessage(Message msg) {
//
//                DetailAdapter adapter=new DetailAdapter(list,context);
//                recyclerView.setAdapter(adapter);
//
//                recyclerView.setLimitNumberToCallLoadMore(list.size());
//                super.handleMessage(msg);
//            }
//        };

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Call<ResponseBody>  call=retrofit.create(GetDetail.class).getCall();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("TAG",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void initRV(){

    }

    public void Details(final String url){

        new Thread(){
            @Override
            public void run(){
                try {


                   /* Document document = Jsoup.parse(url);
                    document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
                    document.select("br").append("\\n");
                    document.select("p").prepend("\\n\\n");
                    String s = document.html().replaceAll("\\\\n", "\n");
                    String strUrl= Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));*/
                    String text = Jsoup.clean(url, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));

                    Document doc= Jsoup.connect(text).get();


                    Elements elements1=doc.select("div.clearfix>div>div>div");

                    Elements elements=doc.getElementsByClass("intro-inner-wrap");

                    String s0=doc.getElementsByClass("property").toString();//界 门 纲
                    s3=s0.replace("&nbsp; &nbsp; &nbsp; &nbsp;","").replace("<div class=\"property\">","")
                            .replace("<br>","").replace("<em>","#")
                            .replace("</em>","").replace("</div>","")
                            .replaceAll("\r|\n","*").replace("** * * *","\n").replace("* * *","\n")
                            .replace("* (","(").replace("* #"," ").replace("*","\n");
                    DetailData d=new DetailData(name,s3,AniPotoURL);

                    list.add(d);

                    int i=1;
                    String title=null,description=null;

                    for (Element elements2:elements1){


                        if (i%2!=0){//y'c
                            title=elements2.getElementsByClass("title").text();//标题
                        }else {

                            description=elements2.getElementsByClass("description").text();//描述
                            if(title!=null&&description!=null){
                                DetailData detailData=new DetailData(title,description);
                                list.add(detailData);
                            }

                        }

                       // Log.e("xxxxx",title);
                       // Log.e("BBB",description);
                        String src=elements2.select("div.description img").attr("src");//地理分布的图片
                        Log.e("srx",src);

                        i++;


                    }
                    //相关物种
                    Elements relat=doc.select("div.relation a");
                    Log.e("relat",relat.toString());
                   // String relatRep=relat.toString().replace("<a href=\"","#")







                    String s1=elements.select("div.left-wrap").text();
                    String  Summary=s1.replace("英文名","\n英文名").replace("学名","\n学名")
                            .replace("世界自然保护联盟","\n世界自然保护联盟").replace("标签","\n标签")
                            .replace("。 ","。\n");//物种概述
                    //身高  年龄
                    String  AShape=elements.select("div.right-wrap").text().replace("身高","\n身高").replace("体重","\n体重")
                            .replace("生命","\n生命").replace("食性","\n食性").replace("繁殖","\n繁殖")
                            .replace("习性","\n习性").replace("分布","\n分布");




                    //Log.e("BBB",s3.trim());
                    //Log.e("sss",AShape);

                   handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
