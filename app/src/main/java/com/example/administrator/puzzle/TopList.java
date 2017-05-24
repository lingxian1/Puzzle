package com.example.administrator.puzzle;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.puzzle.Utils.SQLiteHelper;

public class TopList extends AppCompatActivity {
    private TextView top_list;
    private SQLiteHelper sql;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        top_list=(TextView)findViewById(R.id.top_list);
        sql = new SQLiteHelper(this);
        //todo 显示前10的数据 分难度
        cursor=sql.query("3");
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            top_list.append("date"+cursor.getString(0)+"\n");
            top_list.append("tiem:"+cursor.getString(1)+"\n");
            top_list.append("--------------------------"+"\n");
        }
    }

    public void go_back(View view) {
        Intent intent=new Intent();
        setResult(0,intent);
        finish();
    }
}
