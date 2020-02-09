package com.mile.mile_navigation_engine.utils.Language;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Karl on 12/05/2017.
 */

public class LanguageUtils implements Parcelable {

    private final String languageCode;
    private final String value;

    public LanguageUtils(String languageCode, String value) {
        this.languageCode = languageCode;
        this.value = value;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getValue() {
        return value;
    }

    //PARCEL METHOD
    @Override
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(languageCode);
        dest.writeString(value);
    }

    public LanguageUtils(Parcel source){
        languageCode = source.readString();
        value = source.readString();
    }

    public static final Creator<LanguageUtils> CREATOR = new Creator<LanguageUtils>()
    {
        @Override
        public LanguageUtils createFromParcel(Parcel source)
        {
            return new LanguageUtils(source);
        }

        @Override
        public LanguageUtils[] newArray(int size)
        {
            return new LanguageUtils[size];
        }
    };


}