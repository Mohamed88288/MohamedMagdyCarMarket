package com.mohamedmagdytest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Car implements Parcelable {

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
    private String brand;
    private String is_Used;
    private String construction_year;
    private String image;


    protected Car(Parcel in) {
        brand = in.readString();
        is_Used = in.readString();
        construction_year = in.readString();
        image = in.readString();

    }

    public Car() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(is_Used);
        dest.writeString(construction_year);
        dest.writeString(image);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String id) {
        this.brand = id;
    }

    public String getIs_Used() {
        return is_Used;
    }

    public void setIs_Used(String name) {
        this.is_Used = name;
    }

    public String getConstruction_year() {
        return construction_year;
    }

    public void setConstruction_year(String name_ar) {
        this.construction_year = name_ar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
