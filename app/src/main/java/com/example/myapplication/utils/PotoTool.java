package com.example.myapplication.utils;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PotoTool {


    //将Bitmap转换为二进制数组
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    //解析Json数据
    public  static String analyzeJSONArray(String res){
        String s="";
        try{

            JSONObject jsonObject=new JSONObject(res);
            JSONArray jsonArray=jsonObject.getJSONArray("result");

            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                s =s+"\n"+jsonObject1.optString("name",null)+"   "+jsonObject1.optString("score",null);


            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
