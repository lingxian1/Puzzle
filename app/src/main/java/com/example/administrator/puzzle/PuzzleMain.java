package com.example.administrator.puzzle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.puzzle.Adapt.GridItemsAdapter;
import com.example.administrator.puzzle.Bean.ItemBean;
import com.example.administrator.puzzle.Utils.DateUtils;
import com.example.administrator.puzzle.Utils.GameUtil;
import com.example.administrator.puzzle.Utils.ImagesUtil;
import com.example.administrator.puzzle.Utils.SQLiteHelper;
import com.example.administrator.puzzle.Utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PuzzleMain extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="test" ;
    // 选择的图片
    private Bitmap picSelected;
    // 拼图完成时显示的最后一个图片
    public static Bitmap lastBitmap;
    private int resId;
    public static int type = 2;
    // Flag 是否已显示原图
    private boolean isShowImg;

    public static int timerIndex = 0;
    private Timer timer;
    private List<Bitmap> bitmapItemLists = new ArrayList<Bitmap>();
    private GridItemsAdapter adapter;
    private GridView puzzle_main_detail;
    private Button showImage;
    private Button Restart;
    private TextView time;
    private ImageView imageView;
    private SQLiteHelper sql;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 更新计时器
                    timerIndex++;
                    time.setText("" + timerIndex);
                    break;
                default:
                    break;
            }
        }
    };
    private TimerTask timerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_main);
        // 获取选择的图片
        Bitmap picSelectedTemp;
        // 选择默认图片
        resId = getIntent().getExtras().getInt("picSelectedID");
        type = getIntent().getExtras().getInt("type", 2);
        sql=new SQLiteHelper(this);

        picSelectedTemp = BitmapFactory.decodeResource(getResources(), resId);
        handlerImage(picSelectedTemp);
        initViews();
        generateGame();
        puzzle_main_detail.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GameUtil.isMoveable(position)) {
                    // 交换点击Item与空格的位置
                    GameUtil.swapItems(GameUtil.itemBeans.get(position), GameUtil.blankItemBean);
                    // 重新获取图片
                    recreateData();
                    // 通知GridView更改UI
                    adapter.notifyDataSetChanged();

                    // 判断是否成功
                    if (GameUtil.isSuccess()) {
                        // 将最后一张图显示完整
                        recreateData();
                        bitmapItemLists.remove(type * type - 1);
                        bitmapItemLists.add(lastBitmap);
                        // 通知GridView更改UI
                        adapter.notifyDataSetChanged();
                        if(newRecord(""+type,timerIndex)){
                            Toast.makeText(PuzzleMain.this, "新记录!时间:"+timerIndex+"s", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(PuzzleMain.this, "拼图成功!时间:"+timerIndex+"s", Toast.LENGTH_LONG).show();
                        }
                        //写入本地数据库
                        sql.insert(DateUtils.getDate(),timerIndex,""+type);
                        puzzle_main_detail.setEnabled(false);
                        timer.cancel();
                        timerTask.cancel();
                    }
                }
            }
        });
        showImage.setOnClickListener(this);
        // 显示原图按钮点击事件
        Restart.setOnClickListener(this);
    }

    private void generateGame() {
        // 切图 获取初始拼图数据 正常顺序
        new ImagesUtil().createInitBitmaps(type, picSelected, PuzzleMain.this);
        // 生成随机数据
        GameUtil.getPuzzleGenerator();
        // 获取Bitmap集合
        for (ItemBean temp : GameUtil.itemBeans) {
            bitmapItemLists.add(temp.getBitmap());
        }

        // 数据适配器
        adapter = new GridItemsAdapter(this, bitmapItemLists);
        puzzle_main_detail.setAdapter(adapter);

        // 启用计时器
        timer = new Timer(true);
        // 计时器线程
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        // 每1000ms执行 延迟0s
        timer.schedule(timerTask, 0, 1000);
    }

    private void initViews() {
        // Button
        showImage = (Button) findViewById(R.id.origin_pic);
        Restart = (Button) findViewById(R.id.replay);

        // Flag 是否已显示原图
        isShowImg = false;


        puzzle_main_detail = (GridView) findViewById(R.id.puzzle_main_detail);
        // 设置为N*N显示
        puzzle_main_detail.setNumColumns(type);
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(picSelected.getWidth(), picSelected.getHeight());
        // 水平居中
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // 其他格式属性
        gridParams.addRule(RelativeLayout.BELOW, R.id.ll_puzzle_main_spinner);
        // Grid显示
        puzzle_main_detail.setLayoutParams(gridParams);
        puzzle_main_detail.setHorizontalSpacing(0);
        puzzle_main_detail.setVerticalSpacing(0);

        // TV计时器
        time = (TextView) findViewById(R.id.time);
        time.setText("0秒");

        // 添加显示原图的View
        addImgView();
    }

    private void addImgView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_puzzle_main);
        imageView = new ImageView(PuzzleMain.this);
        imageView.setImageBitmap(picSelected);
        int x = (int) (picSelected.getWidth() * 0.9F);
        int y = (int) (picSelected.getHeight() * 0.9F);
        LayoutParams params = new LayoutParams(x, y);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);
        relativeLayout.addView(imageView);
        imageView.setVisibility(View.GONE);
    }


    private void handlerImage(Bitmap bitmap) {
        // 将图片放大到固定尺寸
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeigt = ScreenUtil.getScreenSize(this).heightPixels;
        picSelected = new ImagesUtil().resizeBitmap(screenWidth * 0.8f, screenHeigt * 0.6f, bitmap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 清空相关参数设置
        cleanConfig();
        this.finish();
    }

    /**
     * 清空相关参数设置
     */
    private void cleanConfig() {
        // 清空相关参数设置
        GameUtil.itemBeans.clear();
        // 停止计时器
        timer.cancel();
        timerTask.cancel();
        timerIndex = 0;
    }

    /**
     * 重新获取图片
     */
    private void recreateData() {
        bitmapItemLists.clear();
        for (ItemBean temp : GameUtil.itemBeans) {
            bitmapItemLists.add(temp.getBitmap());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 显示原图按钮点击事件
            case R.id.origin_pic:
                Animation animShow = AnimationUtils.loadAnimation(PuzzleMain.this, R.anim.image_show_anim);
                Animation animHide = AnimationUtils.loadAnimation(PuzzleMain.this, R.anim.image_hide_anim);
                if (isShowImg) {
                    imageView.startAnimation(animHide);
                    imageView.setVisibility(View.GONE);
                    isShowImg = false;
                } else {
                    imageView.startAnimation(animShow);
                    imageView.setVisibility(View.VISIBLE);
                    isShowImg = true;
                }
                break;
            // 重置按钮点击事件
            case R.id.replay:
                cleanConfig();
                generateGame();
                recreateData();
                // 通知GridView更改UI
                adapter.notifyDataSetChanged();
                puzzle_main_detail.setEnabled(true);
                break;
            default:
                break;
        }
    }

    /**
     * 新记录
     * @param type
     * @param timerIndex
     * @return
     */
    public boolean newRecord(String type,int timerIndex){
        Cursor cursor=sql.getTop(type);
        if(cursor==null) return true;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            int temp=cursor.getInt(1);
            Log.d(TAG, "newRecord: "+temp);
            if (timerIndex<temp){
                Log.d(TAG, "true");
                return true;
            }
        }
        Log.d(TAG, "false");
        return false;
    }
    /**
     * 返回
     */
    public void back(View view) {
        Intent intent=new Intent();
        setResult(0,intent);
        finish();
    }
}
