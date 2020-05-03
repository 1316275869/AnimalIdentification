package cn.edu.heuet.littlecurl.qzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.heuet.littlecurl.ninegridview.base.NineGridViewAdapter;
import cn.edu.heuet.littlecurl.ninegridview.bean.NineGridItem;
import cn.edu.heuet.littlecurl.ninegridview.preview.NineGridViewGroup;
import cn.edu.heuet.littlecurl.qzone.R;
import cn.edu.heuet.littlecurl.qzone.bean.CommentItem;
import cn.edu.heuet.littlecurl.qzone.bean.MyMedia;
import cn.edu.heuet.littlecurl.qzone.bean.RecyclerViewItem;
import cn.edu.heuet.littlecurl.qzone.praise.GoodView;
import cn.edu.heuet.littlecurl.qzone.ui.NoScrollListView;

public class RecyclerVidewAdapter extends RecyclerView.Adapter<RecyclerVidewAdapter.ViewHolder> {

    private Context context;
    private List<RecyclerViewItem> recyclerViewItemList;

    private ArrayList<String> name;
    //记录被回复说说用户的集合
    private ArrayList<String> toName;
    //记录评论内容的集合
    private ArrayList<String> content1;
    private CommentAdapter adapter;
    //点赞
    GoodView mGoodView;
    boolean praise=false;

    public RecyclerVidewAdapter() {
    }

    /**
     * 接受外部传来的数据
     */
    public RecyclerVidewAdapter(Context context, List<RecyclerViewItem> recyclerViewItemList) {
        this.context = context;
        this.recyclerViewItemList = recyclerViewItemList;
    }

    /**
     * 填充视图
     */
    @NonNull
    @Override
    public RecyclerVidewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 获取控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {


        private final ImageView avatar;
        private final TextView tv_username;
        private final TextView tv_createTime;
        private final TextView tv_content;
        private final NineGridViewGroup nineGridViewGroup;
        private final TextView tv_location;
        private final ImageView iv_detail_triangle;
        private final ImageView iv_eye;
        private final ImageView iv_share;
        private NoScrollListView noScrollListView;
        private EditText editText;
        private Button send;
        int[] po=new int[100];

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 头像
            avatar = itemView.findViewById(R.id.avatar);
            // 用户名
            tv_username = itemView.findViewById(R.id.tv_username);
            // 创建时间
            tv_createTime = itemView.findViewById(R.id.tv_createTime);
            // 内容
            tv_content = itemView.findViewById(R.id.tv_content);
            // 图片九宫格控件
            nineGridViewGroup = itemView.findViewById(R.id.nineGrid);
            // 位置
            tv_location = itemView.findViewById(R.id.tv_location);
            // 位置详情三角小图标
            iv_detail_triangle = itemView.findViewById(R.id.iv_detail_triangle);
            // 围观眼睛小图标
            iv_eye = itemView.findViewById(R.id.iv_eye);
            // 分享小图标
            iv_share = itemView.findViewById(R.id.iv_share);


            editText=itemView.findViewById(R.id.edit);
            send=itemView.findViewById(R.id.button4);
            noScrollListView=itemView.findViewById(R.id.lustview);
        }
    }

    /**
     * 绑定控件
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerVidewAdapter.ViewHolder holder, int position) {

        name = new ArrayList<>();
        toName = new ArrayList<>();
        content1 = new ArrayList<>();

//        //添加数据 ,Demo只添加5条评论
//        name.add("白雪公主");
//        toName.add("小矮人");
//        content1.add("你们好啊~");
//
//        name.add("小矮人");
//        toName.add("白雪公主");
//        content1.add("白雪公主，早上好啊~");
//
//        name.add("王子");
//        toName.add("");
//        content1.add("这条说说很有道理的样子啊~");
//
//        name.add("国王");
//        toName.add("");
//        content1.add("我很喜欢这条说说~");
//
//        name.add("白雪公主");
//        toName.add("王子");
//        content1.add("你也是XX的朋友啊？");



        // 获取对应的数据
        RecyclerViewItem recyclerViewItem = recyclerViewItemList.get(position);


        // 往控件上绑定数据
        NineGridViewGroup.getImageLoader().onDisplayImage(context,holder.avatar,recyclerViewItem.getHeadImageUrl());

        for (CommentItem commentItem:recyclerViewItem.getCommentItemArrayList()) {
            name.add(commentItem.getName());
            toName.add(commentItem.getToName());
            content1.add(commentItem.getContent());
        }
        adapter = new CommentAdapter(name,toName,content1,context,holder.editText);
        holder.noScrollListView.setAdapter(adapter);

        holder.tv_username.setText(recyclerViewItem.getNickName());
        holder.tv_createTime.setText(recyclerViewItem.getCreateTime());
        holder.tv_content.setText(recyclerViewItem.getContent());
        holder.tv_location.setText(recyclerViewItem.getLocation().getAddress());
        mGoodView = new GoodView(context);
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String str=holder.editText.getText().toString();


                if(str!=null&&!str.equals("")){
                    if(str.substring(0,1).equals("@")){
                        String toName1=str.substring(1, str.indexOf(":"));
                        String contenttxt=str.substring(str.indexOf(":")+1);
                        name.add("张三");
                        toName.add(toName1);
                        content1.add(contenttxt);
//                        CommentItem commentItem=new CommentItem("张三",toName1,contenttxt);
//                        recyclerViewItemList.get(position).getCommentItemArrayList().add(commentItem);
                    }else{
                        name.add("张三");
                        toName.add("");
                        content1.add(str);
//                        CommentItem commentItem=new CommentItem("张三","",str);
//                        recyclerViewItemList.get(position).getCommentItemArrayList().add(commentItem);
                    }

                    holder.editText.setText("");
                    adapter = new CommentAdapter(name,toName,content1,context,holder.editText);
                    holder.noScrollListView.setAdapter(adapter);
                    Log.d("HHH",position+recyclerViewItem.getCommentItemArrayList().get(0).getName());

                }
            }
        });
        holder.iv_detail_triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "位置详情图标点击事件还未开发", Toast.LENGTH_SHORT).show();
            }
        });
        holder.iv_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "围观图标点击事件还未开发", Toast.LENGTH_SHORT).show();
              if (praise){
                  reset(holder.iv_eye);
              }else {
                  good(holder.iv_eye);
              }
            }
        });
        holder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "分享图标点击事件还未开发", Toast.LENGTH_SHORT).show();
                allShare(recyclerViewItem.getContent());
            }
        });

        // 为满足九宫格适配器数据要求，需要构造对应的List
        ArrayList<MyMedia> mediaList = recyclerViewItem.getMediaList();
        // 没有数据就没有九宫格
        if (mediaList != null && mediaList.size() > 0) {
            ArrayList<NineGridItem> nineGridItemList = new ArrayList<>();
            for (MyMedia myMedia : mediaList) {
                String thumbnailUrl = myMedia.getImageUrl();
                String bigImageUrl = thumbnailUrl;
                String videoUrl = myMedia.getVideoUrl();
                nineGridItemList.add(new NineGridItem(thumbnailUrl, bigImageUrl, videoUrl));
            }
            NineGridViewAdapter nineGridViewAdapter = new NineGridViewAdapter(nineGridItemList);
            holder.nineGridViewGroup.setAdapter(nineGridViewAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItemList.size();
    }

    //点赞开发
    public void good(View view){
        ((ImageView)view).setImageResource(R.mipmap.good_checked);
        mGoodView.setText("+1");
        mGoodView.show(view);
        praise=true;
    }
    public void reset(View view){

        ((ImageView)view).setImageResource(R.mipmap.good);
        mGoodView.reset();
        praise=false;
    }
    /**
     * Android原生分享功能
     * 默认选取手机所有可以分享的APP
     */
    public void allShare(String s){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "动物动态");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, s);//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        context.startActivity(share_intent);
    }


}
