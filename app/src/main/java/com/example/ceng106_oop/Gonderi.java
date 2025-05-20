package com.example.ceng106_oop;

public class Gonderi {
    private String id;
    private String category;
    private String sender_id;
    private String item;
    private String province;
    private String district;
    private String neighborhood;
    private String street;
    private String building_info;
    private String status;

    // Getter ve setter'lar


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUser_id() { return sender_id; }
    public void setUser_id(String sender_id) { this.sender_id = sender_id; }


    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

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

