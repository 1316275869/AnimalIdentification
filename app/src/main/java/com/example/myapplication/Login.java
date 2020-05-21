package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.androidclient.Client;
import com.example.myapplication.androidclient.Personal;
import com.example.myapplication.brief.AnimalAdapter;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private Button mCancleButton;                     //注销按钮
    private CheckBox mRememberCheck;
    Handler handler;
    boolean b=false;
    public static  Personal personal=null;
    Client client ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mCancleButton = (Button) findViewById(R.id.login_btn_cancle);




//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    client=new Client();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        handler = new Handler() { //主线程更新UI
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {

                switch(msg.what){

                    case  0:
                        //...
                        //收到PROGRESS_CHANGED时刷新UI


                            if (personal.getP_useid().equals("null")){
                                Toast.makeText(Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();


                            }else{
                                Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                i.putExtra("p_name",personal.getP_name());
                                i.putExtra("P_useid",personal.getP_useid());
                                i.putExtra("P_password",personal.getP_password());
                                i.putExtra("P_password",personal.getP_headphoto());
                                setResult(1,i);

                                finish();
                            }
                        }





                super.handleMessage(msg);
            }
        };
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();



                        try {
                            client=new Client();
                            if (mAccount.getText()!=null&&mPwd.getText()!=null){



                                String s=mAccount.getText().toString()+"&"+mPwd.getText().toString();

                                client.sendInfo(s);


                                personal=client.readInfo(handler,getApplication());
//                                while (true) {
//
//
//
//
//                                    try {
//                                        Thread.currentThread().sleep(3000);
//
//                                    } catch (InterruptedException e) {
//                                        // TODO: handle exception
//                                        e.printStackTrace();
//                                    }
//
//                                }


                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                            handler.sendEmptyMessage(1);
                        }


                    }
                }.start();
            }
        });


    }
}
