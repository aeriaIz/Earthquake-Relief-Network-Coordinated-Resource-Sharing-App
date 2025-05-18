package com.example.ceng106_oop;
public class UserModel {
    private String id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private int birthday;
    private int birthmonth;
    private int birthyear;

    public UserModel() {
        // Boş constructor gerekli (Retrofit için)
    }

    // Giriş için (login işlemi)
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Tüm kullanıcı bilgileri için (kayıt/güncelleme)
    public UserModel(String id,String username, String password, String name, String surname, String gender, int birthday, int birthmonth, int birthyear) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.birthyear = birthyear;
    }

    // Getter ve Setter'lar
    public String getId(){
        return id;
    }

    public void setId (String id){
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // Zeynep UserModel

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getBirthday() {
        return birthday;
    }
    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }
    public int getBirthmonth() {
        return birthmonth;
    }
    public void setBirthmonth(int birthmonth) {
        this.birthmonth = birthmonth;
    }
    public int getBirthyear() {
        return birthyear;
    }
    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }
    //
     //
}

