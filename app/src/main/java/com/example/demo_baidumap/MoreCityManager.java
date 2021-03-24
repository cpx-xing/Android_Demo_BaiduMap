package com.example.demo_baidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.weather.OnGetWeatherResultListener;
import com.baidu.mapapi.search.weather.WeatherDataType;
import com.baidu.mapapi.search.weather.WeatherResult;
import com.baidu.mapapi.search.weather.WeatherSearch;
import com.baidu.mapapi.search.weather.WeatherSearchOption;
import com.example.demo_baidumap.db.DBManaget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoreCityManager extends AppCompatActivity implements View.OnClickListener {

    TextView moreCityName;
    TextView moreCityTemp;
    BaseAdapter mBaseAdapter;
    private  List<CityTemp> mData;
    ListView mListView;
    Intent intent;
    List<String> idList;
    ImageView back;
    ImageView yes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_city_manager);
        mListView = findViewById(R.id.more_city_list);


        back = findViewById(R.id.more_city_back);
        yes = findViewById(R.id.more_city_yes);

        mData = new ArrayList<CityTemp>();

        intent = getIntent();
        Bundle bd = intent.getExtras();
        idList = new ArrayList<String>();
        idList = bd.getStringArrayList("idlist");

        back.setOnClickListener(this);
        yes.setOnClickListener(this);

//        Toast.makeText(getApplicationContext(),idList.toString(),Toast.LENGTH_SHORT).show();

        for(int i = 0;i<idList.size();i++){
//            mData.add(new CityTemp("上海","100℃"));
            String districtID = idList.get(i);
            WeatherSearchOption weatherSearchOption = new WeatherSearchOption()
                    .weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
                    .districtID(districtID);
            WeatherSearch mWeatherSearch = WeatherSearch.newInstance();
            mWeatherSearch.setWeatherSearchResultListener(new OnGetWeatherResultListener() {
                @Override
                public void onGetWeatherResultListener(final WeatherResult weatherResult) {
                    mData.add(new CityTemp(weatherResult.getLocation().getCity(),weatherResult.getRealTimeWeather().getSensoryTemp()+"℃"));
//                    Toast.makeText(getApplicationContext(),mData.size()+"",Toast.LENGTH_SHORT).show();
                }
            });
            mWeatherSearch.request(weatherSearchOption);
        }
        Toast.makeText(this,mData.size()+"",Toast.LENGTH_SHORT).show();
        mData.add(new CityTemp("上海","10000"));
        mBaseAdapter = new MyMoreCityAdapter(mData,this);
        mListView.setAdapter(mBaseAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_city_back:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("警告").setMessage("是否确认退出");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
                break;
            case R.id.more_city_yes:

                break;
        }
    }
}