package com.example.myapplication.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.collect.CollectActivity;
import com.example.myapplication.Login;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;

/**
 * Created by on 2017/8/16.
 */

public class MyFragment extends Fragment {

    private final static int PICK_PHOTO=1;
    private final static int LOGIN=0;

    static String photoPath;
    private List<Personal> personalList=new ArrayList<>();
    private Context context;
    private ListView listView;
    private TextView user_name,user_val;
    private ImageView blurImageView,avatarImageView;
    private  PersonalAdapter adapter;
    private  com.example.myapplication.androidclient.Personal personal1=null;
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
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

    public MyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);

        blurImageView= view.findViewById(R.id.iv_blur);
        avatarImageView = view.findViewById(R.id.iv_avatar);
        listView=view.findViewById(R.id.my_listview);

        user_name=view.findViewById(R.id.user_name);
        user_val=view.findViewById(R.id.user_val);
//
//        Glide.with(context).load(context.getFilesDir().getPath()+"//"+"123456.jpg").bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
//        Glide.with(context).load(context.getFilesDir().getPath()+"//"+"123456.jpg").bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);
        if (photoPath==null&&Login.personal==null){
            Glide.with(context).load(R.drawable.www).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
           Glide.with(context).load(R.drawable.www).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);

        }else if (photoPath!=null){
            Glide.with(context).load(photoPath).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
            Glide.with(context).load(photoPath).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);

        }else if (Login.personal.getP_headphoto()!=null){
            user_name.setText(Login.personal.getP_name());
            user_val.setText(Login.personal.getP_useid());
            Glide.with(context).load(context.getFilesDir().getPath()+"//"+Login.personal.getP_headphoto()).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
            Glide.with(context).load(context.getFilesDir().getPath()+"//"+Login.personal.getP_headphoto()).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);

        }

        personalInit();


        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhotoAlbum();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Personal p=personalList.get(position);
                if (p.getImg()==R.drawable.ic_my){

                    Intent intent=new Intent(getActivity(),Login.class);

                    startActivityForResult(intent,LOGIN);
                }
                if(p.getImg()==R.drawable.ic_star_border_black_24dp){
                    if (Login.personal.getP_useid()!=null){
                        Intent intent=new Intent(getActivity(), CollectActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), "未登录", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return view;

    }

    public void personalInit(){

        personalList.clear();

        Personal personal=new Personal(R.drawable.ic_visibility_black_24dp,"历史记录");
        personalList.add(personal);
        Personal personal1=new Personal(R.drawable.ic_star_border_black_24dp,"收藏");
        personalList.add(personal1);

        Personal personal2=new Personal(R.drawable.ic_my,"登陆");
        personalList.add(personal2);
        adapter=new PersonalAdapter(context,R.layout.personal_adapter,personalList);
        listView.setAdapter(adapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_PHOTO&&resultCode==RESULT_OK&&data!=null){
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(context, data.getData());

            Glide.with(context).load(photoPath).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
            Glide.with(context).load(photoPath).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);

        }
        if(requestCode==0){
            if(resultCode==1){

                 if (Login.personal.getP_headphoto()!=null){
                     System.out.println(context.getFilesDir().getPath()+"//"+Login.personal.getP_headphoto());
                     Glide.with(context).load(context.getFilesDir().getPath()+"//"+Login.personal.getP_headphoto()).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(blurImageView);
                     Glide.with(context).load(context.getFilesDir().getPath()+"//"+Login.personal.getP_headphoto()).bitmapTransform(new CropCircleTransformation(getActivity())).into(avatarImageView);
                 }
                 if (Login.personal.getP_useid()!=null){
                     user_name.setText(Login.personal.getP_name());
                     user_val.setText(Login.personal.getP_useid());

                 }
            }
        }

    }

    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_PHOTO );
    }
    public Bitmap decodeBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        String filePath = path;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null) {
            Log.e("ERRO","decodeBitmap() first decode bitmap is null");
        }

        // 计算缩放比
        options.inSampleSize = computeSampleSize(options,800,600);
        // 需要把options.inJustDecodeBounds 设为 false,否则没有图片
        options.inJustDecodeBounds = false;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            //用于存储Pixel的内存空间在系统内存不足时可以被回收
            //在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）会自动重新解码
            // 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组
            // 这选项会影响性能 可能会造成卡顿  KITKAT时被过时  不推荐使用
            // 当然不管4.4以上还是以下只要内存捉急还是可以使用的 两害相比取其轻 接着可以尝试下优化内存  降下来以后在去掉
            //options.inPurgeable = true;
            //和inPurgeable结合使用在它为true的时候 在bitmap被回收后再次使用时重新解码的时候不用做深拷贝 而是共享编码数据
            //options.inInputShareable = true;
        }else {
            //4.4以后 jni层设置密度 doDecode放开了对密度的支持 willScale能为true
            DisplayMetrics dm = getResources().getDisplayMetrics();
            //设计图720p下设计的 图片分辨率没超过720p 想压很点 该值加大
            options.inDensity = DisplayMetrics.DENSITY_XHIGH;
            options.inTargetDensity = dm.densityDpi;
        }

        bitmap = BitmapFactory.decodeFile(filePath, options);

        if (bitmap != null) {

        }
        return bitmap;
    }
    private int computeSampleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {
        int simpleSize = 1;
        if(options == null){
            return simpleSize;
        }

        int realWidth = options.outWidth;
        int realHeight = options.outHeight;

        //下面的算法图片质量会比较高  只有请求宽高中大或等的一方 且比实际大小大时才会做缩小处理
        //可以先求出宽和高的缩小比例 在返回高的或低的 求比例的时候 被除数可以根据实际情况选择相对应的 或宽高中小的或大的
        //也可以直接使用官方源码中的computeSampleSize
        if(requestWidth>realWidth && requestWidth>=requestHeight){
            simpleSize = Math.round((float)requestWidth / realWidth);
        }else if(requestHeight>realHeight && requestHeight>=requestWidth){
            simpleSize = Math.round((float)requestHeight/realHeight);
        }

        return simpleSize;

    }
}
