package com.example.myapplication.personalcenter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.brief.briefAnimalData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonalAdapter extends ArrayAdapter<Personal> {
    private int resouceId;
    //将上下文、ListView子项布局的id、数据 传递进来
    public PersonalAdapter(@NonNull Context context, int resource, @NonNull List<Personal> objects) {
        super(context, resource, objects);
        resouceId=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Personal brief=getItem(position);//获取当前项的实例
        //LayoutInflater的inflate()方法接收3个参数：需要实例化布局资源的id、ViewGroup类型视图组对象、false
        //false表示只让父布局中声明的layout属性生效，但不会为这个view添加父布局
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(parent.getContext()).inflate(resouceId,parent,false);
            viewHolder= new ViewHolder();
            //通过ViewHolder获取实例
            viewHolder.Image=view.findViewById(R.id.personal_img);
            viewHolder.name=view.findViewById(R.id.personal_txt);
            //将ViewHolder存储在view中
            view.setTag(viewHolder);
        }else {
            //否则，重用convertView
            view = convertView;
            //重新获取ViewHolder（利用View的getTag()方法，把ViewHolder重新取出）
            viewHolder = (ViewHolder)view.getTag();
        }
        //设置图片和文字
        viewHolder.Image.setImageResource(brief.getImg());
        viewHolder.name.setText(brief.getText());
        //字体加粗
//        viewHolder.name.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
//        viewHolder.name.getPaint().setAntiAlias(true);
//        viewHolder.name.setTextSize(20);
//        viewHolder.name.setTextColor(0xffff00ff);

        return  view;



    }
    class ViewHolder{
        ImageView Image;
        TextView name;

    }
}
