package com.example.administrator.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TopList extends AppCompatActivity {
    TextView top_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        top_list=(TextView)findViewById(R.id.top_list);
        //todo 显示前10的数据 分难度 
    }

    public void go_back(View view) {
        Intent intent=new Intent();
        intent.putExtra("Result","doing");
        setResult(0,intent);
        finish();
    }
}
