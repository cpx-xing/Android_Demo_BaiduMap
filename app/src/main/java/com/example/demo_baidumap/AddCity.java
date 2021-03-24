package com.example.demo_baidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_baidumap.db.DBManaget;

import java.util.ArrayList;
import java.util.List;

public class AddCity extends AppCompatActivity {

    EditText editText;
    GridView gridView;
    Button button;
    DBManaget dbManager;
    List<CityMsg> cityMsgList;
    TextView infoMassage;
    BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        editText = findViewById(R.id.add_city_edittext);
        gridView = findViewById(R.id.add_city_msg);
        button = findViewById(R.id.add_city_btn);
        infoMassage = findViewById(R.id.testtext);

        //    如果city这个表是空的就添加数据，如果不是空的就不添加，防止多次添加
        Cursor cursor_city = dbManager.sqLiteDatabase.query("city", null, null, null, null, null, null);
        String s = cursor_city.toString();
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
//        if(cursor_city.moveToNext()){
            ContentValues values2 = new ContentValues();
            cityMsgList = new ArrayList<CityMsg>();
            cityMsgList.add(new CityMsg("北京","110100"));
            cityMsgList.add(new CityMsg("天津","120100"));
            cityMsgList.add(new CityMsg("上海","310100"));
            cityMsgList.add(new CityMsg("广州","440100"));
            cityMsgList.add(new CityMsg("深圳","440300"));
            cityMsgList.add(new CityMsg("杭州","330100"));
            cityMsgList.add(new CityMsg("东莞","441900"));
            cityMsgList.add(new CityMsg("宁波","330200"));
            cityMsgList.add(new CityMsg("西安","610100"));
            cityMsgList.add(new CityMsg("成都","510100"));
            cityMsgList.add(new CityMsg("重庆","500100"));
            cityMsgList.add(new CityMsg("南京","320100"));
            cityMsgList.add(new CityMsg("苏州","320500"));
            cityMsgList.add(new CityMsg("武汉","420100"));
            cityMsgList.add(new CityMsg("厦门","350200"));

            mAdapter = new MyAddCityAdapter(cityMsgList,getApplicationContext());
            gridView.setAdapter(mAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ContentValues valuesAdd = new ContentValues();
                    valuesAdd.put("district_geocode",cityMsgList.get(position).getCityId());
                    valuesAdd.put("city",cityMsgList.get(position).getCityName());
                    dbManager.sqLiteDatabase.insert("info",null,valuesAdd);
                }
            });

//            for(int i = 0;i<cityMsgList.size();i++){
//                values2.put(cityMsgList.get(i).getCityName(),cityMsgList.get(i).getCityId());
//                dbManager.sqLiteDatabase.insert("city",null,values2);
//            }
//            Toast.makeText(getApplicationContext(),"已经创建了数据库",Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(getApplicationContext(),"已经创建了数据库",Toast.LENGTH_SHORT).show();
//        展示一些常用的城市
//        for(int i = 0;i<cityMsgList.size();i++){
////            View itemView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_main_center,null);
////            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
////            gridLayout.addView(itemView);
////
//            TextView textView = findViewById(R.id.add_city_name);
//            textView.setText("cityMsgList.get(i).getCityName()");
//        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.sqLiteDatabase.execSQL("DELETE FROM info WHERE city = ?",new String[]{"天津"});
                Toast.makeText(getApplicationContext(),dbManager.sqLiteDatabase.query("info",null,null,null,null,null,null).getCount()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

}