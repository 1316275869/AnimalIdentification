package com.example.myapplication.Detaile;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int HEAD_TV =0;
    private static final int DETAIL_TV =1;
    private Context context;
    private ArrayList<DetailData> list=new ArrayList<DetailData>();

    public DetailAdapter( ArrayList<DetailData> list,Context context) {
        this.list = list;
        this.context=context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=null;
        if (viewType== HEAD_TV){
           view=LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_head_adapter,parent,false);

           HeadViewHolder headViewHolder=new HeadViewHolder(view);
           return headViewHolder;

        }else if (viewType== DETAIL_TV){

            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_adapter,parent,false);
            ViewHolder headViewHolder=new ViewHolder(view);
            return headViewHolder;

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position)!=HEAD_TV){

            ViewHolder viewHolder=(ViewHolder) holder;
            DetailData detailData=list.get(position);
            viewHolder.detail.setText(detailData.getDetail());

            //字体加粗
            viewHolder.title.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            viewHolder.title.getPaint().setAntiAlias(true);
            viewHolder.title.setTextSize(20);
            viewHolder.title.setTextColor(0xffff00ff);
            viewHolder.title.setText(detailData.getTitle());
            ;
        }else if (getItemViewType(position)==HEAD_TV){

            HeadViewHolder viewHolder = (HeadViewHolder) holder;
            DetailData detailData = list.get(position);

            viewHolder.detail.setText(detailData.getDetail());

            Picasso.with(context).load(detailData.getUr()).into(viewHolder.iv_image);

            Log.e("wwao","我进来了");
            //字体加粗
            viewHolder.title.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            viewHolder.title.getPaint().setAntiAlias(true);
            viewHolder.title.setTextSize(30);
            viewHolder.title.setTextColor(0xffff00ff);
            viewHolder.title.setText(detailData.getTitle());
        }
    }


    @Override
    public int getItemViewType(int postion){

       if (postion==HEAD_TV){
           return HEAD_TV;
       }else {
           return DETAIL_TV;
       }

    }



    @Override
    public  int getItemCount() {
        return list.size();
    }
    static  class HeadViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_image;
        private TextView title;
        private TextView detail;


        public HeadViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.head_img);
            title = itemView.findViewById(R.id.head_text);
            detail= itemView.findViewById(R.id.head_tx);
            // price = itemView.findViewById(R.id.tv_price);
        }

    }
    static  class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_image;
        private TextView title;
        private TextView detail;
        private TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            //iv_image = itemView.findViewById(R.id);
            title = itemView.findViewById(R.id.id_tag);
            detail= itemView.findViewById(R.id.id_content);
            // price = itemView.findViewById(R.id.tv_price);
        }

    }
}
