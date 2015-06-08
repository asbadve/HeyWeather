package com.badve.ajinkya.heyweather.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ajinkya on 03-06-2015.
 */
public class Weather implements Parcelable {
    private Date date;
    private ArrayList<Tempreture> mArrayListTemp;
    private Double pressure;
    private int humidity;
    private ArrayList<WeatherDes> mArrayListWeatherInfo;
    private Double WindSpeed;
    private int windDirection;
    private int cloud;//cloudiness




    public Weather() {


            // initialization
           /* mArrayListWeatherInfo = new ArrayList<WeatherDes>();
            mArrayListTemp = new ArrayList<Tempreture>();*/

    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "date=" + date +
                ", mArrayListTemp=" + mArrayListTemp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", mArrayListWeatherInfo=" + mArrayListWeatherInfo +
                ", WindSpeed=" + WindSpeed +
                ", windDirection=" + windDirection +
                ", cloud=" + cloud +
                '}';
    }

    public ArrayList<Tempreture> getmArrayListTemp() {
        return mArrayListTemp;
    }

    public void setmArrayListTemp(ArrayList<Tempreture> mArrayListTemp) {
        this.mArrayListTemp = mArrayListTemp;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public ArrayList<WeatherDes> getmArrayListWeatherInfo() {
        return mArrayListWeatherInfo;
    }

    public void setmArrayListWeatherInfo(ArrayList<WeatherDes> mArrayListWeatherInfo) {
        this.mArrayListWeatherInfo = mArrayListWeatherInfo;
    }

    public Double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        WindSpeed = windSpeed;
    }



    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }


    public Weather(Date date, ArrayList<Tempreture> mArrayListTemp, Double pressure, int humidity, ArrayList<WeatherDes> mArrayListWeatherInfo, Double windSpeed, Double rain, int windDirection, int cloud) {
        this.date = date;
        this.mArrayListTemp = mArrayListTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.mArrayListWeatherInfo = mArrayListWeatherInfo;
        this.WindSpeed = windSpeed;
        this.windDirection = windDirection;
        this.cloud = cloud;
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
        dest.writeSerializable(date);
        dest.writeTypedList(mArrayListTemp);
        dest.writeDouble(pressure);
        dest.writeTypedList(mArrayListWeatherInfo);
        dest.writeDouble(WindSpeed);
        dest.writeInt(humidity);
        dest.writeInt(windDirection);
        dest.writeInt(cloud);
    }

        private Weather(Parcel input) {
        this.date = (java.util.Date) input.readSerializable();
//        this.mArrayListTemp =input.readArrayList(Tempreture.class.getClassLoader());
        this.pressure = input.readDouble();
  //      this.mArrayListWeatherInfo = input.readArrayList(WeatherDes.class.getClassLoader());
        this.WindSpeed = input.readDouble();
        this.humidity = input.readInt();
        this.windDirection = input.readInt();
        this.cloud = input.readInt();
            mArrayListTemp = new ArrayList<Tempreture>();
        input.readTypedList(mArrayListTemp, Tempreture.CREATOR);
            mArrayListWeatherInfo = new ArrayList<WeatherDes>();
        input.readTypedList(mArrayListWeatherInfo, WeatherDes.CREATOR);
    }
}
