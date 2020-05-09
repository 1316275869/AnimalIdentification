package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.personalcenter.Personal;
import com.example.myapplication.personalcenter.PersonalAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.heuet.littlecurl.qzone.LauncherActivity;
import cn.edu.heuet.littlecurl.qzone.activity.QZoneActivity;

public class DynamicFragment extends Fragment {

    private List<Personal> personalList=new ArrayList<>();

    private ListView listView;
    private ImageView blurImageView,avatarImageView;
    private  PersonalAdapter adapter;
    private  Activity activity;
    public static DynamicFragment newInstance(String param1) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    public DynamicFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        activity= (Activity) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dynamic_fragment, container, false);

        listView=view.findViewById(R.id.dynamic_listview);


        personalInit();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Personal p=personalList.get(position);
                if (p.getImg()==R.drawable.ic_forum_black_24dp){

                    startActivity(new Intent(activity, QZoneActivity.class));

                }
            }
        });

        return view;
    }
    public void personalInit(){

        personalList.clear();

        Personal personal=new Personal(R.drawable.ic_forum_black_24dp,"动物空间");
        personalList.add(personal);
        Personal personal1=new Personal(R.drawable.ic_forum_black_24dp,"动物新闻");
        personalList.add(personal1);

        adapter=new PersonalAdapter(activity,R.layout.personal_adapter,personalList);
        listView.setAdapter(adapter);


    }

}
