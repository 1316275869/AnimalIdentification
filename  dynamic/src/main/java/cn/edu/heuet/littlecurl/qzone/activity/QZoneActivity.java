package cn.edu.heuet.littlecurl.qzone.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

import cn.edu.heuet.littlecurl.qzone.R;
import cn.edu.heuet.littlecurl.qzone.adapter.RecyclerVidewAdapter;
import cn.edu.heuet.littlecurl.qzone.bean.CommentItem;
import cn.edu.heuet.littlecurl.qzone.bean.Location;
import cn.edu.heuet.littlecurl.qzone.bean.MyMedia;
import cn.edu.heuet.littlecurl.qzone.bean.RecyclerViewItem;

/**
 * 从 activity_qzone.xml 布局文件中可以看出来
 * 一个下拉刷新组件SwipeRefreshLayout里面套一个RecyclerView
 * 所以此类的作用就是获取数据（我自己手写的）
 * 然后将数据给到RecyclerView的适配器
 */
public class QZoneActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    // Log打印的通用Tag
    private final String TAG = "QZoneActivity:";

    // 下拉刷新控件
    SwipeRefreshLayout swipeRefreshLayout;

    // 数据展示
    RecyclerView recyclerView;

    public RecyclerVidewAdapter recyclerViewAdapter;
    private ArrayList<RecyclerViewItem> recyclerViewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzone);
        // 初始化布局
        initView();
        // 自定义数据
        loadMyTestDate();
    }

    private void initView(){
        recyclerView = findViewById(R.id.recyclerView);
        // 布局管理器必须有，否则不显示布局
        // No layout manager attached; skipping layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // RecyclerView适配器
        recyclerViewAdapter = new RecyclerVidewAdapter(this, recyclerViewItemList);
        recyclerView.setAdapter(recyclerViewAdapter);

        // 下拉刷新控件
        // 因为该类 implements SwipeRefreshLayout.OnRefreshListener
        // 所以只需要在onCreate里注册一下监听器，具体的响应事件可以写到onCreate方法之外
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    // 自定义的测试数据（假装这是网络请求并解析后的数据）
    private void loadMyTestDate() {
        // 先构造MyMedia
        String imgUrl1 = "http://image.iltaw.com/20150503/86/70/d6yOlg4b6ILbz1VF.jpg";
        String imgUrl2 = "http://image.iltaw.com/20130313/120/112/n0skDWUAU6JXJgxp.jpg";
        String imgUrl3 = "http://image.iltaw.com/20130416/99/89/jqBdm3zSbS0XsjcY.jpg";
        // 视频内容：敲架子鼓
        String videoUrl1 = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
        // 视频内容：感受到鸭力
        String videoUrl2 = "http://gslb.miaopai.com/stream/w95S1LIlrb4Hi4zGbAtC4TYx0ta4BVKr-PXjuw__.mp4?vend=miaopai&ssig=8f20ca2d86ec365f0f777b769184f8aa&time_stamp=1574944581588&mpflag=32&unique_id=1574940981591448";
        // 视频内容：狗崽子
        String videoUrl4 = "http://gslb.miaopai.com/stream/7-5Q7kCzeec9tu~9XvZAxNizNAL1TJC7KtJCuw__.mp4?vend=miaopai&ssig=82b42debfc2a51569bafe6ac7a993d89&time_stamp=1574944868488&mpflag=32&unique_id=1574940981591448";
        String videoUrl3 = videoUrl4;

        MyMedia myMedia1 = new MyMedia(imgUrl1, videoUrl1);
        MyMedia myMedia2 = new MyMedia(imgUrl2);
        MyMedia myMedia3 = new MyMedia(imgUrl3, videoUrl2);
        MyMedia myMedia4 = new MyMedia(imgUrl1, videoUrl3);
        MyMedia myMedia5 = new MyMedia(imgUrl3, videoUrl4);
        // 再构造mediaList
        // 1张图片
        ArrayList<MyMedia> mediaList1 = new ArrayList<>();
        mediaList1.add(myMedia2);
        // 2张图片
        ArrayList<MyMedia> mediaList2 = new ArrayList<>();
        mediaList2.add(myMedia1);
        mediaList2.add(myMedia2);
        // 4张图片
        ArrayList<MyMedia> mediaList4 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mediaList4.add(myMedia1);
            mediaList4.add(myMedia2);
        }
        // 10张图片
        ArrayList<MyMedia> mediaList10 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mediaList10.add(myMedia1);
            mediaList10.add(myMedia2);
            mediaList10.add(myMedia3);
            mediaList10.add(myMedia4);
            mediaList10.add(myMedia5);
        }

        Location location = new Location();
        location.setAddress("Test Address");
        // 最后构造EvaluationItem
         RecyclerViewItem recyclerViewItem1 = new RecyclerViewItem(mediaList1, "东北虎，又称西伯利亚虎，分布于亚洲东北部，即俄罗斯西伯利亚地区、朝鲜和中国东北地区。东北虎是现存体重最大的肉食性猫科动物，其中雄性体长可达3米左右，尾长约1米，头大而圆，前额上的数条黑色横纹，中间常被串通，极似“王”字，故有“丛林之王”之美称,是中国国家一级保护动物。\n" +
                 "世界自然保护联盟红色名录列为：极危(CR)", "2019-11-02",
                "10080", "自强社", location, imgUrl1);
         RecyclerViewItem recyclerViewItem2 = new RecyclerViewItem(mediaList2, "远东豹，又名东北豹、朝鲜豹、阿穆尔豹，是豹的一个亚种，是北方寒带地区体型仅次于东北虎的大型猫科动物，也是世界上最稀少的猫科动物。曾经广泛分布于俄罗斯远东地区、中国东北黑龙江、吉林和朝鲜半岛北部的森林中。已被列入《华盛顿公约》附录Ⅰ，受到俄罗斯、朝鲜和中国政府的严格保护。", "2019-11-02",
                "10080", "信息技术学院", location, imgUrl2);
         RecyclerViewItem recyclerViewItem4 = new RecyclerViewItem(mediaList4, "虎纹伯劳，俗名花伯劳、虎伯劳。主要分布于亚洲的东部及南部。一般栖息于树林、分布自平原至丘陵、山地，喜栖于疏林边缘以及巢址选在带荆棘的灌木及洋槐等阔叶树。主要食物是昆虫，特别是蝗虫、蟋蟀、甲虫、臭虫、蝴蝶和飞蛾，也吃小鸟和蜥蜴。该物种的模式产地在印度尼西亚爪哇。\n" +
                 "世界自然保护联盟红色名录列为：无危（LC）", "2019-11-02",
                "10080", "信息技术学院", location, imgUrl2);
         RecyclerViewItem recyclerViewItem10 = new RecyclerViewItem(mediaList10, "沙虎鲨，又名戟齿砂鲛或戟齿锥齿鲨，是一种生活在海岸海域的大型鲨鱼。现正分布在大西洋、印度洋及太平洋不同的地方。沙虎鲨的外表凶猛，但它们往往很温顺，除非受到挑衅，否则它并不带有攻击性。食物主要有硬骨鱼（包括大海鲈）、其他鲨鱼及鱼、鱿鱼、蟹及龙虾。\n" +
                 "世界自然保护联盟红色名录列为：濒危（EN）", "2019-11-02",
                "10080", "雷雨话剧社", location, imgUrl3);

                //添加数据 ,Demo只添加5条评论
//        name.add("白雪公主");
//        toName.add("小矮人");
//        content1.add("你们好啊~");
//
//        name.add("小矮人");
//        toName.add("白雪公主");
//        content1.add("白雪公主，早上好啊~");
//
//        name.add("王子");
//        toName.add("");
//        content1.add("这条说说很有道理的样子啊~");
//
//        name.add("国王");
//        toName.add("");
//        content1.add("我很喜欢这条说说~");
//
//        name.add("白雪公主");
//        toName.add("王子");
//        content1.add("你也是XX的朋友啊？");
        ArrayList<CommentItem> commentItems=new ArrayList<>();
        ArrayList<CommentItem> commentItems1=new ArrayList<>();
        ArrayList<CommentItem> commentItems2=new ArrayList<>();
        ArrayList<CommentItem> commentItems3=new ArrayList<>();
        CommentItem commentItem1=new CommentItem("白雪公主","小矮人","你们好啊~");
        commentItems.add(commentItem1);
        commentItems1.add(commentItem1);
        CommentItem commentItem2=new CommentItem("小矮人","白雪公主","白雪公主，早上好啊~~");
        commentItems.add(commentItem2);
        CommentItem commentItem3=new CommentItem("王子","","我很喜欢这条说说~");
        commentItems.add(commentItem3);
        commentItems1.add(commentItem3);
        CommentItem commentItem4=new CommentItem("小矮人","白雪公主","白雪公主，早上好啊~~");
        commentItems.add(commentItem4);
        //每个recyclerViewItem都必须有setCommentItemArrayList
        recyclerViewItem1.setCommentItemArrayList(commentItems);
        recyclerViewItem2.setCommentItemArrayList(commentItems1);

        recyclerViewItem4.setCommentItemArrayList(commentItems2);
        recyclerViewItem10.setCommentItemArrayList(commentItems3);


        recyclerViewItemList.add(recyclerViewItem1);
        recyclerViewItemList.add(recyclerViewItem2);
        recyclerViewItemList.add(recyclerViewItem4);
        recyclerViewItemList.add(recyclerViewItem10);
    }

    @Override
    public void onRefresh() {
        // 加载数据（先清空原来的数据）
//        recyclerViewItemList.clear();
//        // loadBackendData(url);
//        loadMyTestDate();
//        // 打乱顺序（为了确认确实是刷新了）
//        Collections.shuffle(recyclerViewItemList);
//        // 通知适配器数据已经改变
//        recyclerViewAdapter.notifyDataSetChanged();
//        // 下拉刷新完成
//        if (swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//        }
    }
}
