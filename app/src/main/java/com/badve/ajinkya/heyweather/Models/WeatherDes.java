package com.badve.ajinkya.heyweather.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajinkya on 03-06-2015.
 */
public class WeatherDes implements Parcelable {
    private int id;
    private String main;
    private String desciption;
    private String icon;

    public WeatherDes(int id, String main, String desciption, String icon) {
        this.id = id;
        this.main = main;
        this.desciption = desciption;
        this.icon = icon;
    }

    public WeatherDes() {
    }




    @Override
    public String toString() {
        return "WeatherDes{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", desciption='" + desciption + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(main);
        dest.writeString(desciption);
        dest.writeString(icon);
    }

    private WeatherDes(Parcel in) {
        id = in.readInt();
        main = in.readString();
        desciption = in.readString();
        icon = in.readString();
    }

    public static final Creator<WeatherDes> CREATOR = new Creator<WeatherDes>() {
        @Override
        public WeatherDes createFromParcel(Parcel in) {
            return new WeatherDes(in);
        }

        @Override
        public WeatherDes[] newArray(int size) {
            return new WeatherDes[size];
        }
    };

}
