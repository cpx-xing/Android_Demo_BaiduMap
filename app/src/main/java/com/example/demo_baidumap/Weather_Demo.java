package com.example.demo_baidumap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo_baidumap.db.DBManaget;
import com.example.demo_baidumap.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class Weather_Demo extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    List<String> cityList;
    List<Fragment> fragmentList;
    double mLat,mLng;
    Intent intent;
    String cityId;
    List<String> DBCityList;
    List<String> DBIdList;
    DBManaget dbManager;

    ImageView imageViewAdd;
    ImageView imageViewMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager.initDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather__demo);

//        获取传入的location信息
        intent = getIntent();
        Bundle bd = intent.getExtras();
        mLat = bd.getDouble("lat");
        mLng = bd.getDouble("lng");
        cityId = bd.getString("ID");

//        cityMsgList = new ArrayList<CityMsg>();
//        cityMsgList.add(new CityMsg("北京","110100"));
//        cityMsgList.add(new CityMsg("天津","120100"));
//        cityMsgList.add(new CityMsg("上海","310100"));
//        cityMsgList.add(new CityMsg("广州","440100"));
//        cityMsgList.add(new CityMsg("深圳","440300"));
//        cityMsgList.add(new CityMsg("杭州","330100"));
//        cityMsgList.add(new CityMsg("东莞","441900"));
//        cityMsgList.add(new CityMsg("宁波","330200"));
//        cityMsgList.add(new CityMsg("西安","610100"));
//        cityMsgList.add(new CityMsg("成都","510100"));
//        cityMsgList.add(new CityMsg("重庆","500100"));
//        cityMsgList.add(new CityMsg("南京","320100"));
//        cityMsgList.add(new CityMsg("苏州","320500"));
//        cityMsgList.add(new CityMsg("武汉","420100"));
//        cityMsgList.add(new CityMsg("厦门","350200"));
//  初始化控件
        init();

//        测试city表格
//        dbManager.sqLiteDatabase.delete("info",null,null);
//        ContentValues values2 = new ContentValues();
//        values2.put("city_name","上海");
//        values2.put("city_id","110100");
//        dbManager.sqLiteDatabase.insert("city",null,values2);

//      在数据库中添加location的信息
        ContentValues values1 = new ContentValues();
        values1.put("district_geocode",cityId);
        values1.put("city","0");
        dbManager.sqLiteDatabase.insert("info",null,values1);

//      在数据库中添加更多的城市
        ContentValues values = new ContentValues();
        values.put("district_geocode","310100");
        values.put("city","上海");
        dbManager.sqLiteDatabase.insert("info",null,values);
        DBCityList = new ArrayList<>();
        DBIdList = new ArrayList<>();

//        查询数据库中的所有city的信息
        Cursor cursor = dbManager.sqLiteDatabase.query("info", null, null, null, null, null, null);

        while (cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String district_geocode = cursor.getString(cursor.getColumnIndex("district_geocode"));
            DBCityList.add(city);
            DBIdList.add(district_geocode);
        }

//        将查询到的信息发给list初始化Fragment中的viewpaper
        cityList = DBCityList;
        initPaper();
        CityFragmentPageAdapter adapter = new CityFragmentPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);

    }
    private void init(){
        viewPager =findViewById(R.id.main_vp);
        cityList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        imageViewAdd = findViewById(R.id.main_iv_add);
        imageViewMore = findViewById(R.id.main_iv_more);
        imageViewAdd.setOnClickListener(this);
        imageViewMore.setOnClickListener(this);
    }
    private void initPaper(){
        for(int i =0;i<cityList.size();i++){
            City_Weather_Fragment cityWeatherFragment = new City_Weather_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("ID",cityId);
            bundle.putString("city",cityList.get(i));
            bundle.putString("district_geocode",DBIdList.get(i));
            cityWeatherFragment.setArguments(bundle);
            fragmentList.add(cityWeatherFragment);
        }
    }



    @Override
    public void onClick(View v) {
    switch (v.getId()){
        //    增加城市天气
        case R.id.main_iv_add:
            Intent intent = new Intent(this,AddCity.class);
            startActivity(intent);
            break;
        //    管理城市
        case R.id.main_iv_more:
                Intent intent1 = new Intent(getApplicationContext(),MoreCityManager.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("idlist", (ArrayList<String>) DBIdList);
//            Toast.makeText(this,bundle.toString(),Toast.LENGTH_SHORT).show();
                intent1.putExtras(bundle);
                startActivity(intent1);
            break;
    }



    }
}