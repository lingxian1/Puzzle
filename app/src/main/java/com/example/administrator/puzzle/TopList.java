package com.example.administrator.puzzle;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.puzzle.Utils.SQLiteHelper;

public class TopList extends AppCompatActivity {
    private static final String TAG ="sqltest";
    private TextView top_list;
    private SQLiteHelper sql;
    private Cursor cursor;
    private RadioButton r3;
    private RadioButton r4;
    private RadioButton r5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);

        sql = new SQLiteHelper(this);
        init();
        //默认显示3*3的排行
        showTop10(""+3);

    }

    private void init() {
        top_list=(TextView)findViewById(R.id.top_list);
        r3=(RadioButton)findViewById(R.id.r3);
        r4=(RadioButton)findViewById(R.id.r4);
        r5=(RadioButton)findViewById(R.id.r5);
    }

    /**
     * 显示前10数据
     * @param type  类型
     */
    public void showTop10(String type){
        int line=1;
        int count=10;
        top_list.append("         date      ");
        top_list.append("        time"+"\n");
        top_list.append("-------------------------------------"+"\n");
        cursor=sql.query(type);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if(count==0) break;
            top_list.append(line+"    ");
            top_list.append(cursor.getString(0));
            top_list.append("    "+cursor.getInt(1)+"s"+"\n");
            //Log.d(TAG, "showTop10: "+cursor.getString(0)+"--------------"+cursor.getInt(1));
            line++;
            count--;
        }
    }

    /**
     * 退出当前activity
     * @param view
     */
    public void go_back(View view) {
        Intent intent=new Intent();
        setResult(0,intent);
        finish();
    }

    /**
     * 排行类型选择
     * @param view
     */
    public void choose(View view) {
        if(view.getId()==R.id.r3){
            top_list.setText("");
            showTop10(""+3);
        }else if(view.getId()==R.id.r4){
            top_list.setText("");
            showTop10(""+4);
        }else{
            top_list.setText("");
            showTop10(""+5);
        }
    }
}
