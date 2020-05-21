package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
        import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import com.ashokvarma.bottomnavigation.BottomNavigationBar;
        import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.myapplication.androidclient.Client;
import com.example.myapplication.androidclient.Personal;
import com.example.myapplication.brief.ScanFragment;
import com.example.myapplication.personalcenter.MyFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private MyFragment mMyFragment;
    private ScanFragment mScanFragment;
    private HomeFragment mHomeFragment;
    private DynamicFragment mDynamicFragment;
    FragmentManager fm=this.getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /**
         * bottomNavigation 设置
         */

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        Login.personal=new Personal();
        /** 导航基础设置 包括按钮选中效果 导航栏背景色等 */

        bottomNavigationBar
                .setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                /**
                 *  setMode() 内的参数有三种模式类型：
                 *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
                 *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
                 *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
                 */

                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                /**
                 *  setBackgroundStyle() 内的参数有三种样式
                 *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
                 *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
                 *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
                 *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
                 */

                .setActiveColor("#FF107FFD") //选中颜色
                .setInActiveColor("#e9e6e6") //未选中颜色
                .setBarBackgroundColor("#1ccbae");//导航栏背景色

        /** 添加导航按钮 */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_search_black_24dp, "搜索"))
                .addItem(new BottomNavigationItem(R.drawable.ic_forum_black_24dp,"动态"))
                .addItem(new BottomNavigationItem(R.drawable.ic_my, "个人中心"))
                .setFirstSelectedPosition(lastSelectedPosition )
                .initialise(); //initialise 一定要放在 所有设置的最后一项

        setDefaultFragment();//设置默认导航栏

    }

    /**
     * 设置默认导航栏
     */
    private void setDefaultFragment() {
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction transaction = fm1.beginTransaction();
        mHomeFragment = HomeFragment.newInstance("首页");
        transaction.replace(R.id.tb, mHomeFragment);
        transaction.commit();
    }

    /**
     * 设置导航选中的事件
     */
    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        //fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance("首页");
                }
                transaction.replace(R.id.tb, mHomeFragment,"mHomeFragment");
                break;
            case 1:
                if (mScanFragment == null) {
                    mScanFragment = ScanFragment.newInstance("搜索");
                }
                transaction.replace(R.id.tb, mScanFragment,"mScanFragment");
                break;
            case 2:
                if (mDynamicFragment == null) {
                    mDynamicFragment = DynamicFragment.newInstance("动态");
                }
                transaction.replace(R.id.tb, mDynamicFragment,"mDynamicFragment");
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance("个人中心");
                }
                transaction.replace(R.id.tb, mMyFragment,"mMyFragment");
                break;

            default:
                break;
        }

        transaction.commit();// 事务提交
    }

    /**
     * 设置未选中Fragment 事务
     */
    @Override
    public void onTabUnselected(int position) {

    }

    /**
     * 设置释放Fragment 事务
     */
    @Override
    public void onTabReselected(int position) {

    }
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data!=null){
//            /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
//
//            Bundle data1=data.getExtras();
//            Personal personal1=new com.example.myapplication.androidclient.Personal(
//                    data1.getString("p_name"),
//                    data1.getString("p_useid"),
//                    data1.getString("p_password"),
//                    data1.getString("p_headphoto"));
//            System.out.println("af"+personal1.getP_useid());
//            Fragment f = fm.findFragmentByTag("mMyFragment");
//            /*然后在碎片中调用重写的onActivityResult方法*/
//            f.onActivityResult(requestCode, resultCode, data);
//        }


    }
}
