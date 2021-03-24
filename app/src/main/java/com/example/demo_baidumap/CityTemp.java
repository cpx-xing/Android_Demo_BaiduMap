package com.example.demo_baidumap;

public class CityTemp {
    private String cityName;
    private String cityTemp;

    public CityTemp(String cityName, String cityTemp) {
        this.cityName = cityName;
        this.cityTemp = cityTemp;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(String cityTemp) {
        this.cityTemp = cityTemp;
    }
}
