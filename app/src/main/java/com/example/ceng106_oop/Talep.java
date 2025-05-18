package com.example.ceng106_oop;

import com.google.gson.annotations.SerializedName;

public class Talep {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("category")
    private String category;
    @SerializedName("item")
    private String item;
    @SerializedName("province")
    private String province;
    @SerializedName("district")
    private String district;
    @SerializedName("neighborhood")
    private String neighborhood;
    @SerializedName("street")
    private String street;
    @SerializedName("building_info")
    private String building_info;
    @SerializedName("status")
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getBuilding_info() { return building_info; }
    public void setBuilding_info(String building_info) { this.building_info = building_info; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}





