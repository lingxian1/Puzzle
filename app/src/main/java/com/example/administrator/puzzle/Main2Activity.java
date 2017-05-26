package com.example.administrator.puzzle;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.puzzle.Adapt.GridPicListAdapter;
import com.example.administrator.puzzle.Utils.SQLiteHelper;
import com.example.administrator.puzzle.Utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main2Activity";
    private PopupWindow popupWindow;
    private View popupView;
    private TextView ps3;
    private TextView ps4;
    private TextView ps5;
    private TextView type_selected;
    private LayoutInflater layoutInflater;
    private GridView gv_Pic_List;
    private List<Bitmap> picList;
    private Button search_top_info;
    private int type = 3;
    private int[] resPicId;
    private SQLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        picList = new ArrayList<Bitmap>();
        init();
        gv_Pic_List.setAdapter(new GridPicListAdapter(Main2Activity.this, picList));
        gv_Pic_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Main2Activity.this, "done"+type+"---"+resPicId[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main2Activity.this, PuzzleMain.class);
                intent.putExtra("picSelectedID", resPicId[position]);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        type_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });
    }

    public void init(){
        gv_Pic_List = (GridView) findViewById(R.id.pic_list);
        //行间距
        gv_Pic_List.setVerticalSpacing(10);
        //初始化button
        search_top_info=(Button)findViewById(R.id.button);
        // 初始化Bitmap数据 默认图片添加
        resPicId = new int[] {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,
                R.drawable.a6,R.drawable.a7,R.drawable.a8,R.drawable.a9,R.drawable.a10};
        Bitmap[] bitmaps = new Bitmap[resPicId.length];
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), resPicId[i]);
            picList.add(bitmaps[i]);
        }
        // 显示type
        type_selected = (TextView) findViewById(R.id.textView);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // type view
        popupView = layoutInflater.inflate(R.layout.popui, null);

        ps3 = (TextView) popupView.findViewById(R.id.p3);
        ps4 = (TextView) popupView.findViewById(R.id.p4);
        ps5 = (TextView) popupView.findViewById(R.id.p5);

        // 监听事件
        ps3.setOnClickListener(this);
        ps4.setOnClickListener(this);
        ps5.setOnClickListener(this);
    }

    /**
     * 难度选择菜单
     * @param view
     */
    public void ShowPopup(View view){
        int density=(int) ScreenUtil.getDeviceDensity(this);
        popupWindow = new PopupWindow(popupView, 200*density,200*density);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 透明背景
        Drawable transpent = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(transpent);
        // 获取位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0]-20*density, location[1]);
    }

    /**
     * 难度选择菜单动作
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()) {
            // Type
            case R.id.p3:
                type = 3;
                type_selected.setText("3 X 3");
                break;
            case R.id.p4:
                type = 4;
                type_selected.setText("4 X 4");
                break;
            case R.id.p5:
                type = 5;
                type_selected.setText("5 X 5");
                break;
            default:
                break;
        }
        popupWindow.dismiss();
    }

    /**
     * 查看排行榜
     * @param view
     */
    public void searchTop(View view) {
        Intent intent = new Intent(Main2Activity.this, TopList.class);
        startActivity(intent);
    }
}
