package com.example.demo_baidumap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    LocationClient locationClient;
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    Boolean isFirstLocate = true;
    RadioGroup radioGroup;
    Button getWeather;
    double lat,lng;
    String mID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

//        在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //SDKInitializer.setCoordType(CoordType.BD09LL);

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.locationInfo);
        mMapView = findViewById(R.id.maoview);
        radioGroup = findViewById(R.id.radioGroup);
        getWeather = findViewById(R.id.weather);
//        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,false, BitmapDescriptorFactory.fromResource(R.drawable.point));
//        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
//        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,false, BitmapDescriptorFactory.fromResource(R.drawable.point)));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio1:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.radio2:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.radio3:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                        break;
                }
            }
        });

        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setMyLocationEnabled(true);



        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

//      相应权限申请
        List<String> premissionList = new ArrayList<String>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            premissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            premissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            premissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!premissionList.isEmpty()){
            String[] premissions = premissionList.toArray(new String[premissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,premissions,1);
        }else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有的权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode。Hight_Accuracy 高精度
        //LocationMode.Battery_Saving 低功耗
        //LocationMode.Device_Sensors 仅适用设备 GPS
        option.setCoorType("bd09ll"); //经纬度坐标类型，默认GCJ02:国测局坐标  BD09LL 百度
        option.setScanSpan(1000); //设置发起定位请求的时间间隔
        option.setOpenGps(true); //设置是否使用GPS 默认为false
        option.setIgnoreKillProcess(false); // 定位 SDK内部是一个service，并放到了独立进程
        option.setLocationNotify(true); //设置是否当GPS有时效时按照1s/1次频率输出CPS结果，默认为false
        option.SetIgnoreCacheException(false); //设置是否收集Crash信息，默认收集，false
        option.setWifiCacheTimeOut(5*60*1000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }
//    创建一个监听器
    private  class MyLocationListener extends BDAbstractLocationListener{

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        StringBuilder currentPosition = new StringBuilder();
        mID = bdLocation.getAddress().adcode;
        //        定位自己的位置
        navigateTo(bdLocation);
        currentPosition.append("您现在所在的位置是：").append("\n");
        currentPosition
//                .append(bdLocation.getCountry()).append(bdLocation.getProvince())
//                .append(bdLocation.getCity()).append(bdLocation.getDistrict())
//                .append(bdLocation.getTown()).append(bdLocation.getStreet())
                .append(bdLocation.getAddress().address);
        textView.setText(currentPosition);
        lat = bdLocation.getLatitude();
        lng = bdLocation.getLongitude();


        //以下是显示
//            StringBuilder currentPosition = new StringBuilder();
//        currentPosition.append("维度：").append(bdLocation.getLatitude()).append("\n");
//        currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
//        currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
//        currentPosition.append("省份：").append(bdLocation.getProvince()).append("\n");
//        currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
//        currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
//        currentPosition.append("村镇：").append(bdLocation.getTown()).append("\n");
//        currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
//        currentPosition.append("地址：").append(bdLocation.getAddress()).append("\n");
//        currentPosition.append("定位方式：");
//        if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
//            currentPosition.append("GPS");
//        }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//            currentPosition.append("网络");
//        }
//        textView.setText(currentPosition);
    }
}

public void navigateTo(BDLocation bdLocation){
        //判断是不是第一次识别
    if(isFirstLocate){
        LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        mBaiduMap.animateMapStatus(update);
        isFirstLocate = false;
    }
    MyLocationData.Builder builder = new MyLocationData.Builder();
    builder.longitude(bdLocation.getLongitude()).accuracy(bdLocation.getRadius());
    builder.latitude(bdLocation.getLatitude()).direction(bdLocation.getDirection());
    MyLocationData locationData = builder.build();
    mBaiduMap.setMyLocationData(locationData);



}

protected void onResume() {
    super.onResume();
//    当activity执行onResume时执行mMapView的onResume方法
    mMapView.onResume();
}
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }
public void moveToWeather(View view){
        Intent intent = new Intent(MainActivity.this,Weather_Demo.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lat);
        bundle.putDouble("lng",lng);
        bundle.putString("ID",mID);
        intent.putExtras(bundle);
        startActivity(intent);
}


}