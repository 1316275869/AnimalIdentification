package com.example.myapplication.androidclient;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.androidclient.Personal;
import com.example.myapplication.brief.briefAnimalData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {


    Context context;
    private final String HOST="192.168.43.114";
    private final int POST=6667;
    private Selector selector;
    private static SocketChannel socketChannel;
    private String username;

    public Client() throws IOException{

        selector =Selector.open();
        //连接fuwq
        this.socketChannel=socketChannel.open(new InetSocketAddress(HOST,POST));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username


    }

    //向服务器发送消息
    public void sendInfo(String info) {
        //info=username+"说"+info;

        try {
            //socketChannel.write(info);
            socketChannel.write(ByteBuffer.wrap(info.getBytes("GB2312")));
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
    //读取从服务器端回复的消息
    public Personal readInfo(Handler handler,Context context) {

        try {
            int readChannels=selector.select();

            if (readChannels>0) {
                Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();

                while (iterator.hasNext()) {//如果客户端有多个通道  考虑多个通道
                    SelectionKey key = (SelectionKey) iterator.next();
                    if (key.isReadable()) {
                        //得到相关的通道

                        SocketChannel sc=(SocketChannel) key.channel();
                        //得到一个buffer
                        ByteBuffer buffer=ByteBuffer.allocate(1024);
                        //d读取
                        sc.read(buffer);
                        //把读到的数据转成字符串
                        String msg=new String(buffer.array(),"GB2312");
                        System.out.println(msg.trim());//去掉头尾空格

                        Personal personal=Personal.splitMsg(msg);



                        Thread.sleep(100);

                        Log.d("Personal",msg);
                        receiveFile(sc,personal.getP_headphoto(),context);
                        handler.sendEmptyMessage(0);
                        return personal;
                    }

                }
                iterator.remove();//防止重复操作
            }else {
                //System.out.println("没有可以用 的通道。。。");
                return null;

            }



        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    public void sendFile(Context co) throws Exception {
        FileInputStream fis = null;
        ByteBuffer sendBuffer = ByteBuffer.allocate(1024*20);
        FileChannel channel = null;// 传输不同的数据选择对应的“通道”
        fis = new FileInputStream(String.valueOf(getUriFromDrawableRes(co,R.drawable.logo)));
        channel = fis.getChannel();
        int i = 1;
        int count = 0;
        while ((count = channel.read(sendBuffer)) != -1) {
            sendBuffer.flip();
            int send = socketChannel.write(sendBuffer);
            System.out.println("i===========" + (i++) + "   count:" + count + " send:" + send);
            while (send == 0) {// 传输失败（ 服务器端可能因为缓存区满，而导致数据传输失败，需要重新发送）
                Thread.sleep(10);
                send = socketChannel.write(sendBuffer);
                System.out.println("i重新传输====" + i + "   count:" + count + " send:" + send);
            }
            sendBuffer.clear();
        }
        channel.close();
        fis.close();
        socketChannel.close();
    }
    public List<briefAnimalData> readCollect(){
        try {
            int readChannels=selector.select();

            if (readChannels>0) {
                Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();

                while (iterator.hasNext()) {//如果客户端有多个通道  考虑多个通道
                    SelectionKey key = (SelectionKey) iterator.next();
                    if (key.isReadable()) {
                        //得到相关的通道

                        SocketChannel sc=(SocketChannel) key.channel();
                        //得到一个buffer
                        ByteBuffer buffer=ByteBuffer.allocate(1024*5);
                        //d读取
                        List<briefAnimalData> list=new ArrayList<>();

                            int count=sc.read(buffer);
                            //把读到的数据转成字符串

                        String[] cmsg=new String[100];
                            if (count>0){
                                String msg=new String(buffer.array(),"GB2312").trim();
                                cmsg=msg.split("&&");
                                System.out.println(msg.trim());//去掉头尾空格


                                for (int i=0;i<cmsg.length;i++){

                                    briefAnimalData c=briefAnimalData.splitMsg(cmsg[i]);
                                    System.out.println(cmsg[i]);
                                    list.add(c);

                                }

                            }

                        return list;

                    }

                }
                iterator.remove();//防止重复操作
            }else {
                //System.out.println("没有可以用 的通道。。。");

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    /**
     * 得到资源文件中图片的Uri
     * @param context 上下文对象
     * @param id 资源id
     * @return Uri
     */
    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }
    public static void receiveFile(SocketChannel socketChannel, String filename,Context context) throws IOException {
        FileOutputStream fos = null;
        FileChannel channel = null;



        try {
            fos = context.openFileOutput(filename, 0);
            channel = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024*100);

            buffer.clear();
            int count =socketChannel.read(buffer);
            int k=0;
            while (count>0) {
                System.out.println("k=" + (k++) + " 读取到数据量:" + count);
                buffer.flip();
                channel.write(buffer);
                fos.flush();
                buffer.clear();
                count = socketChannel.read(buffer);
            }
            if (count==-1) {
                socketChannel.close();

            }
        } finally {
            try {
                channel.close();
            } catch(Exception ex) {}
            try {
                fos.close();
            } catch(Exception ex) {}
        }
    }

}
