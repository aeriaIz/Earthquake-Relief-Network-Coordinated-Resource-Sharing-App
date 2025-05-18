package com.example.ceng106_oop;
import androidx.annotation.NonNull;

public class modelIhtiyacItem {
    public String item;
    public String street;
    public String neighborhood;
    public String province;
    public String building_info;
    public String district;
    public String status;
    public String id;
    public String user_id;

    public modelIhtiyacItem(String user_id, String id, String status, String item, String neighborhood, String street, String building_info, String province, String district) {
        this.item = item;
        this.user_id=user_id;
        this.id = id;
        this.status = status;
        this.neighborhood = neighborhood;
        this.street = street;
        this.building_info = building_info;
        this.province = province;
        this.district = district;
    }


    @NonNull
    @Override
    public String toString() {
        return item + " - " + neighborhood + " mah., " + street + " cad., No:" + building_info + ", " + province + "/" + district;
    }
}