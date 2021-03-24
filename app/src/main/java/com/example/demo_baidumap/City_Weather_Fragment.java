package com.example.demo_baidumap;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.weather.OnGetWeatherResultListener;
import com.baidu.mapapi.search.weather.WeatherDataType;
import com.baidu.mapapi.search.weather.WeatherResult;
import com.baidu.mapapi.search.weather.WeatherSearch;
import com.baidu.mapapi.search.weather.WeatherSearchForecasts;
import com.baidu.mapapi.search.weather.WeatherSearchOption;
import com.example.demo_baidumap.db.DBManaget;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class City_Weather_Fragment extends Fragment implements View.OnClickListener {
    TextView tempTv, cityTv, conditionTv, windTv, tempRangeTv, dateTv, clothIndexTv, carIndexTv, coldIndexTv, sportIndexTv, raysIndexTv, textWeatherMsg;
    ImageView dayIv;
    LinearLayout futureLayout;
    String cityID;
    String cityName;
//    未来若干天的天气
    List<WeatherSearchForecasts> weatherSearchForecastsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city__weather_, container, false);
        initView(view);
        Bundle bd1 = this.getArguments();
        cityID = bd1.getString("district_geocode");
        cityName = bd1.getString("city");

        String districtID = cityID;

//        Toast.makeText(getContext(),MSG,Toast.LENGTH_SHORT).show();
//        districtID = cityID; // LOCATION区域ID
//        if(cityName.equals("0")){
//             districtID = cityID; // LOCATION区域ID
//        }else if(cityName.equals("上海")){
//            districtID = "310100";
//            Toast.makeText(getContext(),cityName,Toast.LENGTH_SHORT).show();
//        }else {
//            districtID = "130200";
//        }

        WeatherSearchOption weatherSearchOption = new WeatherSearchOption()
                .weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
                .districtID(districtID);

        WeatherSearch mWeatherSearch = WeatherSearch.newInstance();
        mWeatherSearch.setWeatherSearchResultListener(new OnGetWeatherResultListener() {
            @Override
            public void onGetWeatherResultListener(final WeatherResult weatherResult) {
                int realTemp = weatherResult.getRealTimeWeather().getTemperature();
                tempTv.setText(realTemp + "℃");
                cityTv.setText(weatherResult.getLocation().getCity());
                conditionTv.setText(weatherResult.getRealTimeWeather().getPhenomenon());

                String nowTime = weatherResult.getRealTimeWeather().getUpdateTime();
                String morth = nowTime.substring(4, 6);
                String day = nowTime.substring(6,8);

                dateTv.setText(Integer.valueOf(morth)+"月"+Integer.valueOf(day)+"日");

                windTv.setText(weatherResult.getRealTimeWeather().getWindDirection() + weatherResult.getRealTimeWeather().getWindPower()+"");
                tempRangeTv.setText("体感温度："+ weatherResult.getRealTimeWeather().getSensoryTemp()+ "℃");
                weatherSearchForecastsList = weatherResult.getForecasts();
                textWeatherMsg.setText(cityID +"\n" +
//                        weatherResult.getRealTimeWeather().getPhenomenon() +"\n" +
                        weatherResult.getLocation().getCity() +"\n" +nowTime+"\n"+
                        weatherSearchForecastsList+"\n"+weatherResult.getLifeIndexes()+"\n"+
                        cityName
                        );

                //        填充未来几天的天气情况
                for(int i = 1 ;i<weatherSearchForecastsList.size();i++){
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
                    itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    futureLayout.addView(itemView);

                    TextView idateTv = itemView.findViewById(R.id.item_center_tv_date);
                    idateTv.setText(weatherSearchForecastsList.get(i).getWeek());

                    TextView weather1 = itemView.findViewById(R.id.item_center_tv_con);
                    weather1.setText(weatherSearchForecastsList.get(i).getPhenomenonDay());
                    TextView temp = itemView.findViewById(R.id.item_center_tv_temp);
                    temp.setText(weatherSearchForecastsList.get(i).getLowestTemp() +"~"+weatherSearchForecastsList.get(i).getHighestTemp());
                    ImageView iimageView = itemView.findViewById(R.id.item_center_iv_temp);

                    switch (weatherSearchForecastsList.get(i).getPhenomenonDay()){
                        case "多云":
                            iimageView.setImageResource(R.mipmap.duoyun);
                            break;
                        case "小雨":
                            iimageView.setImageResource(R.mipmap.xioayu1);
                            break;
                        case "晴":
                            iimageView.setImageResource(R.mipmap.defult0);
                            break;
                        case "阴":
                            iimageView.setImageResource(R.mipmap.yin);
                            break;
                        case "中雨":
                            iimageView.setImageResource(R.mipmap.xiaoyu);
                            break;
                    }


//                    idateTv.setText(weatherSearchForecastsList.get(i).getWeek());
//                    weatherTv.setText(weatherSearchForecastsList.get(i).getPhenomenonDay());
//                    itemp.setText(weatherSearchForecastsList.get(i).getLowestTemp() +"~" + weatherSearchForecastsList.get(i).getHighestTemp());
////                    iimageView.setImageResource(R.mipmap.defult0);
                    Log.e("LAG_E_WEATHER","week");
                }

            }
        });
        mWeatherSearch.request(weatherSearchOption);
//
//        textWeatherMsg.setText(weatherSearchForecastsList.size()+"");
        return view;
    }

    private void initView(View view) {
        //初始化控件
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_weather);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.farg_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        coldIndexTv = view.findViewById(R.id.frag_index_tv_cold);
        sportIndexTv = view.findViewById(R.id.farg_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rasy);
        dayIv = view.findViewById(R.id.frag_iv_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);
        textWeatherMsg = view.findViewById(R.id.getWeatherMsg);

//        设置监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg = "";
        switch (v.getId()) {
            case R.id.farg_index_tv_dress:
//                builder.setTitle("穿衣指数");
//                Weatherbean.ResultsBean.IndexBean indexBean = indexList.get(0);
//                String msg = indexBean.getZs() +"\n"+indexBean.getDes();
//                builder.setMessage(msg);
                builder.setTitle("穿衣指数");
                builder.setMessage("今天天气不热");
                builder.setPositiveButton("确定", null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.farg_index_tv_sport:
                builder.setTitle("运动指数");
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
            case R.id.frag_index_tv_rasy:
                builder.setTitle("紫外线指数");
                builder.setMessage(msg);
                builder.setPositiveButton("确定", null);
                break;
        }
        builder.create().show();
    }


}