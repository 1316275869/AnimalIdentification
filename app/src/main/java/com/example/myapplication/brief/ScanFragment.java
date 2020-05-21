package com.example.myapplication.brief;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Detaile.DetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.androidclient.Client;

import com.example.myapplication.utils.SpeechRecognizerTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/8/16.
 */

public class ScanFragment extends Fragment implements SearchView.OnQueryTextListener, SpeechRecognizerTool.ResultsCallback{

    Handler handler;
    static final  int PROGRESS_CHANGED=1;
    String information=null;
    TextView tv;
    private SearchView sv;

    Button button;
    ListView listView=null;


    public static List<briefAnimalData> briefList=new ArrayList<>();

    private Context context;
    String s3=null;
    AnimalAdapter adapter;
    private String [] AnimalPotoURL=new String[100];

    public  SpeechRecognizerTool mSpeechRecognizerTool;



    public static ScanFragment newInstance(String param1) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;


    }

    public ScanFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        Bundle bundle = getArguments();
        mSpeechRecognizerTool=new SpeechRecognizerTool(context);



        initSearchView(view);

        button=view.findViewById(R.id.id_b);
        button.setOnTouchListener(new View.OnTouchListener() {  //语音识别
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizerTool.startASR(ScanFragment.this);

                        break;
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizerTool.stopASR();

                        break;
                    default:
                        return false;
                }

                return true;
            }
        });
///        mSpeechRecognizerTool.createTool();
        listView=view.findViewById(android.R.id.list);
        tv=view.findViewById(R.id.id_tv);
        handler = new Handler() { //主线程更新UI
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case PROGRESS_CHANGED:
                        //...
                        //收到PROGRESS_CHANGED时刷新UI

                       adapter=new AnimalAdapter(getActivity(),R.layout.brief_animal_data_item,briefList,false);
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
                intent.setClass(getActivity(), DetailActivity.class);//启动
                startActivity(intent);

            }
        });

        return view;
    }
    //初始化搜索框
    public void initSearchView(View view){
      //  lv =view.findViewById(android.R.id.list);
        //lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mStrings));
       // lv.setTextFilterEnabled(true);//设置lv可以被过虑
        sv = (SearchView) view.findViewById(R.id.id_sv);
        // 设置该SearchView默认是否自动缩小为图标
        sv.setIconifiedByDefault(false);
        sv.setBackgroundColor(0x22ff00ff);
        /**
         * 初始是否已经是展开的状态
         * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
         */
        //sv.onActionViewExpanded();
        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(this);
        //搜索框是否展开，false表示展开
       // sv.setIconified(false);
        sv.setFocusable(false);






        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("请输入要查找的动物名");


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

                    Log.e("fffff","zabdbahd");
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
    /**
     * @param urlpath String类型
     * @return byte[]类型
     * @throws IOException
     */
    public static byte[] GetUserHead(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // 设置请求方法为GET
        conn.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream(); // 通过输入流获得图片数据
        byte[] data = readInputStream(inputStream); // 获得图片的二进制数据
        return data;

    }

    private static byte[] readInputStream (InputStream inputStream) throws IOException{
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

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





                    Elements elements=doc.getElementsByClass("intro-inner-wrap");

                    String s0=doc.select("div.property>p").text();//界 门 纲


                    s3=s0.replace("<p>","").replace("</p>","").replace("<br>","\n");

                    String s1=elements.select("div.left-wrap").text();
                    Log.e("BBB",s0);
                    handler.sendEmptyMessage(PROGRESS_CHANGED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        briefList.clear();
        brief(query);
        tv.setText("符合关键字"+"\""+query+"\""+"的动物");

        return false;
    }



    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        //Toast.makeText(getActivity(), "textChange--->" + newText,Toast.LENGTH_LONG).show();
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            //lv.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
           // lv.setFilterText(newText);
        }
        return true;

    }
    @Override
    public void onStart() {
        super.onStart();

       mSpeechRecognizerTool.createTool();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSpeechRecognizerTool.destroyTool();
    }
    @Override
    public void onResults(String result) {

        final String finalResult = result;
        brief(result);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("SPEEK",finalResult);
               sv.setQueryHint(finalResult);


            }
        });
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }

    }
}
