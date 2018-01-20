package com.infinite.rzzkan.uny.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rzzkan on 20/01/2018.
 */

public class SliderModel implements Parcelable {
    private String slider = "";

    public String getSlider() {
        return slider;
    }

    public void setSlider(String slider) {
        this.slider = slider;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(slider);
    }

    public SliderModel() {
        super();
    }


    protected SliderModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.slider = in.readString();

    }

    public static final Parcelable.Creator<SliderModel> CREATOR = new Parcelable.Creator<SliderModel>() {
        @Override
        public SliderModel createFromParcel(Parcel in) {
            return new SliderModel(in);
        }

        @Override
        public SliderModel[] newArray(int size) {
            return new SliderModel[size];
        }
    };
}
