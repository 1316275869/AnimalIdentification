package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
    private Personal personal=null;
    Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mCancleButton = (Button) findViewById(R.id.login_btn_cancle);

        handler = new Handler() { //主线程更新UI
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void handleMessage(Message msg) {
                personal=client.personal;
                System.out.println(personal.getP_useid()+"wwwwww");
                Log.d("personal",personal.toString());
                switch(msg.what){

                    case  0:       //...
                        //收到PROGRESS_CHANGED时刷新UI
                    if (personal.getP_useid().equals("null")){
                        Toast.makeText(Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();

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
                            client.initClient("192.168.0.106", 8989);
                            if (mAccount.getText()!=null&&mPwd.getText()!=null){
                                client.listen(mAccount.getText().toString(),mPwd.getText().toString());
                                client.listen(mAccount.getText().toString(),mPwd.getText().toString());
                                //client.listen(mAccount.getText().toString(),mPwd.getText().toString());
                                // personal=client.listen(mAccount.getText().toString(),mPwd.getText().toString());
                                handler.sendEmptyMessage(0);
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
