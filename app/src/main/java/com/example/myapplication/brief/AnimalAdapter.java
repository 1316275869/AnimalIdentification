package com.example.myapplication.brief;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.example.myapplication.androidclient.Client;
import com.example.myapplication.praise.GoodView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


public class AnimalAdapter extends ArrayAdapter<briefAnimalData> {
    private int resouceId;
    //将上下文、ListView子项布局的id、数据 传递进来
    GoodView mGoodView;

    private Client client;
    boolean praise;
    public AnimalAdapter(@NonNull Context context, int resource, @NonNull List<briefAnimalData> objects,boolean praise) {
        super(context, resource, objects);
        resouceId=resource;

        this.praise=praise;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        briefAnimalData brief=getItem(position);//获取当前项的实例
        //LayoutInflater的inflate()方法接收3个参数：需要实例化布局资源的id、ViewGroup类型视图组对象、false
        //false表示只让父布局中声明的layout属性生效，但不会为这个view添加父布局
        View view;

        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(parent.getContext()).inflate(resouceId,parent,false);
            viewHolder= new ViewHolder();
            //通过ViewHolder获取实例
            viewHolder.Image=view.findViewById(R.id.animal_img);
            viewHolder.name=view.findViewById(R.id.id_name);
            viewHolder.brief=view.findViewById(R.id.id_brief);
            viewHolder.collect=view.findViewById(R.id.collect);

            //将ViewHolder存储在view中
            view.setTag(viewHolder);
        }else {
            //否则，重用convertView
            view = convertView;
            //重新获取ViewHolder（利用View的getTag()方法，把ViewHolder重新取出）
            viewHolder = (ViewHolder)view.getTag();
        }
        mGoodView = new GoodView(getContext());

        if (Login.personal.getP_useid()!=null){
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
                        client.readCollect();
                    }
                }
            }).start();
        }

        //设置图片和文字
        Picasso.with(getContext()).load(brief.getImg()).into(viewHolder.Image);
        //viewHolder.Image.setImageBitmap(brief.getImg());
        viewHolder.name.setText(brief.getName());
        //字体加粗
        viewHolder.name.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        viewHolder.name.getPaint().setAntiAlias(true);
        viewHolder.name.setTextSize(20);
        viewHolder.name.setTextColor(0xffff00ff);
        viewHolder.brief.setText(brief.getBriefData());
        if (praise){
            good(viewHolder.collect);
        }
        viewHolder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.personal.getP_useid()!=null){
                    if (praise){
                        reset(viewHolder.collect);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                client.sendInfo("collectdelete"+"&"+ Login.personal.getP_useid() +"&"+brief.getName());

                            }
                        }).start();

                    }else {
                        good(viewHolder.collect);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                client.sendInfo("collectadd"+"&"+ Login.personal.getP_useid() +"&"+brief.getImg()+"&"+brief.getName()+"&"+brief.getBriefData()+"&"+brief.getDetailsUrl());
                            }
                        }).start();

                    }
                }else{
                    Toast.makeText(getContext(), "未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }
    public void good(View view){
        ((ImageView)view).setImageResource(R.mipmap.collection_checked);

        mGoodView.setText("+1");
        mGoodView.show(view);
        praise=true;
    }
    public void reset(View view){

        ((ImageView)view).setImageResource(R.mipmap.collection);
        mGoodView.reset();
        praise=false;
    }
    class ViewHolder{
        ImageView Image;
        TextView name;
        TextView brief;
        ImageView collect;
    }
}
