package com.example.demo_baidumap.db;

public class DatabaseBean {
    private int _id;
    private String district_geocode;
    private String city;

    public DatabaseBean(int _id, String district_geocode, String city) {
        this._id = _id;
        this.district_geocode = district_geocode;
        this.city = city;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDistrict_geocode() {
        return district_geocode;
    }

    public void setDistrict_geocode(String district_geocode) {
        this.district_geocode = district_geocode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
