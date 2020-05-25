package com.example.myapplication.identification;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;


import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.utils.Base64Util;
import com.example.myapplication.utils.FileUtil;
import com.example.myapplication.utils.HttpUtil;
import com.example.myapplication.utils.PotoTool;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import static com.example.myapplication.utils.PotoTool.analyzeJSONArray;


public class RealIdentification extends AppCompatActivity {

    private TextureView viewFinder;

    Handler handler;
    TextView txView;

    private String analyResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_identification);

        txView=findViewById(R.id.text_view);

        viewFinder = findViewById(R.id.view_finder);

        viewFinder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                updateTransform();
            }
        });

        viewFinder.post(new Runnable() {
            @Override
            public void run() {

                startCamera();
            }
        });
    }

    private void startCamera() {
        // 1. preview
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(new Rational(1, 1))
                .setTargetResolution(new Size(640,640))
                .build();

        Preview preview = new Preview(previewConfig);
        handler = new Handler() { //主线程更新UI
            public void handleMessage(Message msg) {



                txView.setText(analyResult);
                super.handleMessage(msg);
            }
        };
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) viewFinder.getParent();
                parent.removeView(viewFinder);
                parent.addView(viewFinder, 0);

                viewFinder.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });



        // 2. capture
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setTargetAspectRatio(new Rational(1,1))
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .build();
        final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);

        viewFinder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                File photo = new File(getExternalCacheDir() + "/" + "rphoto" + ".jpg");
                imageCapture.takePicture(photo, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {

                        analyResult="开始识别";
                        getIdentiResult(file.getAbsolutePath());

                        txView.setText(analyResult);

                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        showToast("error " + message);
                        cause.printStackTrace();
                    }
                });

                return true;
            }
        });



        CameraX.bindToLifecycle(this, preview, imageCapture);

    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();
        // Compute the center of the view finder
        float centerX = viewFinder.getWidth() / 2f;
        float centerY = viewFinder.getHeight() / 2f;

        float[] rotations = {0,90,180,270};
        // Correct preview output to account for display rotation
        float rotationDegrees = rotations[viewFinder.getDisplay().getRotation()];

        matrix.postRotate(-rotationDegrees, centerX, centerY);

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix);
    }


    //调百度APi得到结果
    public void getIdentiResult(String path){


        new Thread(new Runnable() {





            @Override
            public void run() {
                String result;
                String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal";
                try {
                    // 本地文件路径
                    String filePath = path;
                    byte[] imgData = FileUtil.readFileByBytes(filePath);



                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");


                    String param = "image=" + imgParam;

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = "24.c03324700820a5fc9d4c7e248b7074e0.2592000.1591266324.282335-18646339";

                    result = HttpUtil.post(url, accessToken, param);//得到返回的jSON数据

                    analyResult=analyzeJSONArray(result); //得到json解释后的数据
                    handler.sendEmptyMessage(9);//发送信息 在主线程更新UI



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


    @Override
    protected void onDestroy() {
        CameraX.unbindAll();
        super.onDestroy();
    }

}

