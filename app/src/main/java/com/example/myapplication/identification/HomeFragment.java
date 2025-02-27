package com.example.myapplication.identification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.personalcenter.getPhotoFromPhotoAlbum;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.PotoTool;

import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import static android.app.Activity.RESULT_OK;
import static com.example.myapplication.utils.PotoTool.analyzeJSONArray;

/**
 *
 *
 */

public class HomeFragment extends Fragment {

    private final static int PROGRESS_CHANGED = 1000;
    private final static int PICK_PHOTO=1;
    private Button B_pz,B_xc,B_timeIden;
    private Button mStartSpeechButton;
    private TextView result_phothotx,result_tx;
    private ImageView imageView;
    private static TextView[] tx=new TextView[6];
    private static String photoPath;;

    private static  int TAKE_PHOTO=1;
    private static  int CROP_RESULT_CODE;
    private static String strResult;
    private Handler handler;
    private String analyResult;
    Activity activity;
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (Activity) context;
    }

    public HomeFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        B_xc=view.findViewById(R.id.id_xc);
        B_pz=view.findViewById(R.id.id_pz);
        B_timeIden=view.findViewById(R.id.id_time);
        tx[0]=view.findViewById(R.id.id_tx);
        imageView=view.findViewById(R.id.id_image);
        result_phothotx=view.findViewById(R.id.result_photo);
        result_tx=view.findViewById(R.id.result_tx);
        tx[1]=view.findViewById(R.id.id_tx1);
        tx[2]=view.findViewById(R.id.id_tx2);
        tx[3]=view.findViewById(R.id.id_tx3);
        tx[4]=view.findViewById(R.id.id_tx4);
        tx[5]=view.findViewById(R.id.id_tx5);

        tx[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tx[0].getText()!=null&&!tx[0].getText().toString().equals("")){

                    String[] strname=tx[0].getText().toString().trim().split(" ");

                    if(!strname[0].equals("非动物")){
                        Intent intent=new Intent();
                        intent.putExtra("name",strname[0]);
                        intent.setClass(getActivity(),BriefActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });
        tx[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tx[1].getText()!=null&&!tx[1].getText().toString().equals("")){
                    String[] strname=tx[1].getText().toString().trim().split(" ");
                    Intent intent=new Intent(getActivity(),BriefActivity.class);
                    intent.putExtra("name",strname[0]);
                    startActivity(intent);
                }
            }
        });
        tx[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tx[2].getText()!=null&&!tx[2].getText().toString().equals("")){
                    String[] strname=tx[2].getText().toString().trim().split(" ");

                    Intent intent=new Intent(getActivity(),BriefActivity.class);
                    intent.putExtra("name",strname[0]);
                    startActivity(intent);
                }
            }
        });
        tx[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tx[3].getText()!=null&&!tx[3].getText().toString().equals("")){
                    String[] strname=tx[3].getText().toString().trim().split(" ");

                    Intent intent=new Intent(getActivity(),BriefActivity.class);
                    intent.putExtra("name",strname[0]);
                    startActivity(intent);
                }
            }
        });
        tx[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tx[4].getText()!=null&&!tx[4].getText().toString().equals("")){
                    String[] strname=tx[4].getText().toString().trim().split(" ");

                    Intent intent=new Intent(getActivity(),BriefActivity.class);
                    intent.putExtra("name",strname[0]);
                    startActivity(intent);
                }
            }
        });
        tx[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tx[5].getText()!=null&&!tx[5].getText().toString().equals("")){
                    String[] strname=tx[5].getText().toString().trim().split(" ");

                    Intent intent=new Intent(getActivity(),BriefActivity.class);
                    intent.putExtra("name",strname[0]);
                    startActivity(intent);
                }
            }
        });

        handler = new Handler() { //主线程更新UI
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case PROGRESS_CHANGED:


                        if (analyResult!=null){
                            String[] strmsg=analyResult.trim().split("\n");
                            for (int i=0;i<strmsg.length;i++){
                                if(i<6){
                                    tx[i].setText(strmsg[i]);


                                }

                            }
                        }

                        break;

                }
                super.handleMessage(msg);
            }
        };

        B_timeIden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), RealIdentification.class);
                startActivity(intent);
            }
        });
        B_pz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //拍照

                for(int i=0;i<6;i++){
                    tx[i].setText("");
                }
                getPhoto();
            }
        });
        B_xc.setOnClickListener(new View.OnClickListener() {  //相册
            @Override
            public void onClick(View v) {

                for(int i=0;i<6;i++){
                    tx[i].setText("");
                }
                goPhotoAlbum();
            }
        });
        return view;
    }
    public   void getPhoto(){


        //调用摄像头进行拍照
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,0);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        Bitmap bitmap;

        if (requestCode==0&&resultCode==RESULT_OK&&data!=null){
            Bundle bundle=data.getExtras();
            bitmap=(Bitmap)bundle.get("data");

            byte[] imgData = PotoTool.getBitmapByte(bitmap);

            Glide.with(activity)
                    .load(imgData)
                    .override(600, 200)
                    .fitCenter()
                    .into(imageView);


            getIdentiResult(bitmap);

            result_phothotx.setText("识别图片");
            result_tx.setText("识别结果(动物名+相似度)");
            //String s=analyzeJSONArray(getIdentiResult(bitmap));
            // Log.d("TExt",s);
            // tx.setText(s);
            //得到解释的Json数据
            //getIdentiResult(bitmap)得到拍照后的识别结果

            //选择照片后的返回的结果
        }else if (requestCode==PICK_PHOTO&&resultCode==RESULT_OK&&data!=null){

            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(getContext(), data.getData());
            Log.d("Path",photoPath);
            bitmap=decodeBitmap(photoPath);



            Glide.with(activity)
                    .load(photoPath)
                    .override(600, 200)
                    .fitCenter()
                    .into(imageView);
            getIdentiResult(bitmap);
            //  imageView.setImageBitmap(decodeBitmap(photoPath));

            //handler.sendEmptyMessage(PROGRESS_CHANGED);



            result_phothotx.setText("识别图片");
            result_tx.setText("识别结果");
            //getIdentiResultPath(photoPath);

        }else {
            Toast.makeText(getActivity(), "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
        }

    }

    //调百度APi得到结果
    public void getIdentiResult(final Bitmap bitmap){

        new Thread(new Runnable(){
            @Override
            public void run() {
                Socket socket;
                String result;
                String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal";
                try {
                           /* socket = new Socket("192.168.0.113",9998);



                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
                            // imageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            //读取图片到ByteArrayOutputStream
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] bytes = baos.toByteArray();
                            out.write(bytes);

                            System.out.println("bytes--->"+bytes);
                            out.close();
                            socket.close();*/
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);


                    byte[] imgData = PotoTool.getBitmapByte(bitmap);
                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                    String param = "image=" + imgParam;

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = "24.c03324700820a5fc9d4c7e248b7074e0.2592000.1591266324.282335-18646339";

                    result = HttpUtil.post(url, accessToken, param);//得到返回的jSON数据

                    analyResult=analyzeJSONArray(result); //得到json解释后的数据
                    handler.sendEmptyMessage(PROGRESS_CHANGED);//发送信息 在主线程更新UI



                    //Log.d("XX",analyzeJSONArray(result));

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
    //通过相册图片的路径取得图片
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

    //
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



    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_PHOTO );
    }



}

