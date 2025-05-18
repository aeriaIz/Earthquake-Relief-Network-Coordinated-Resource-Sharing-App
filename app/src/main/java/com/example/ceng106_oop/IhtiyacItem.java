package com.example.ceng106_oop;
public class IhtiyacItem {
    public String item;
    public String province;
    public String district;
    public String neighborhood;
    public String street;
    public String building_info;

    // Boş constructor (Gson gibi kütüphaneler için gerekli)
    public IhtiyacItem() {}

    // Getter ve Setter (isteğe bağlı)
    public String getItem() {
        return item;
    }

    public String getProvince() {
        return province;
    }
    public String getDistrict() {
        return district;
    }
    public String getNeighborhood() {
        return neighborhood;
    }
    public String getStreet() {
        return street;
    }
    public String getBuilding_info() {
        return building_info;
    }

    @Override
    public String toString() {
        return neighborhood + " mahallesi " +
                street + " caddesi No:" +
                building_info + " " +
                province + "/" +
                district;
    }

}
