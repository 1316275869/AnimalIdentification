package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cn.edu.heuet.littlecurl.qzone.LauncherActivity;
import cn.edu.heuet.littlecurl.qzone.activity.QZoneActivity;

public class DynamicFragment extends Fragment {

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

        TextView tv = (TextView)view.findViewById(R.id.dynamic_txt);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, QZoneActivity.class));
            }
        });

        return view;
    }

}
