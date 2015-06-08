package com.badve.ajinkya.heyweather.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajinkya on 03-06-2015.
 */
public class Tempreture implements Parcelable {
    private Double dayTmpreture;
    private Double minDailyTmpreture;
    private Double maxDailyTmpreture;
    private Double nightTmpreture;
    private Double eveTmpreture;
    private Double morningTmpreture;

    public Tempreture(Double dayTmpreture, Double minDailyTmpreture, Double maxDailyTmpreture, Double nightTmpreture, Double eveTmpreture, Double morningTmpreture) {
        this.dayTmpreture = dayTmpreture;
        this.minDailyTmpreture = minDailyTmpreture;
        this.maxDailyTmpreture = maxDailyTmpreture;
        this.nightTmpreture = nightTmpreture;
        this.eveTmpreture = eveTmpreture;
        this.morningTmpreture = morningTmpreture;
    }

    public Tempreture() {
    }


    public Double getDayTmpreture() {
        return dayTmpreture;
    }

    public void setDayTmpreture(Double dayTmpreture) {
        this.dayTmpreture = dayTmpreture;
    }

    public Double getMinDailyTmpreture() {
        return minDailyTmpreture;
    }

    public void setMinDailyTmpreture(Double minDailyTmpreture) {
        this.minDailyTmpreture = minDailyTmpreture;
    }

    public Double getMaxDailyTmpreture() {
        return maxDailyTmpreture;
    }

    public void setMaxDailyTmpreture(Double maxDailyTmpreture) {
        this.maxDailyTmpreture = maxDailyTmpreture;
    }

    @Override
    public String toString() {
        return "Tempreture{" +
                "dayTmpreture=" + dayTmpreture +
                ", minDailyTmpreture=" + minDailyTmpreture +
                ", maxDailyTmpreture=" + maxDailyTmpreture +
                ", nightTmpreture=" + nightTmpreture +
                ", eveTmpreture=" + eveTmpreture +
                ", morningTmpreture=" + morningTmpreture +
                '}';
    }

    public Double getNightTmpreture() {
        return nightTmpreture;
    }

    public void setNightTmpreture(Double nightTmpreture) {
        this.nightTmpreture = nightTmpreture;
    }

    public Double getEveTmpreture() {
        return eveTmpreture;
    }

    public void setEveTmpreture(Double eveTmpreture) {
        this.eveTmpreture = eveTmpreture;
    }

    public Double getMorningTmpreture() {
        return morningTmpreture;
    }

    public void setMorningTmpreture(Double morningTmpreture) {
        this.morningTmpreture = morningTmpreture;
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
        dest.writeDouble(dayTmpreture);
        dest.writeDouble(minDailyTmpreture);
        dest.writeDouble(maxDailyTmpreture);
        dest.writeDouble(nightTmpreture);
        dest.writeDouble(eveTmpreture);
        dest.writeDouble(morningTmpreture);

    }

    private Tempreture(Parcel input) {

        this.dayTmpreture = input.readDouble();
        this.minDailyTmpreture = input.readDouble();
        this.maxDailyTmpreture = input.readDouble();
        this.nightTmpreture = input.readDouble();
        this.eveTmpreture = input.readDouble();
        this.morningTmpreture = input.readDouble();
    }

    public static final Creator<Tempreture> CREATOR = new Creator<Tempreture>() {
        @Override
        public Tempreture createFromParcel(Parcel in) {
            return new Tempreture(in);
        }

        @Override
        public Tempreture[] newArray(int size) {
            return new Tempreture[size];
        }
    };


}
