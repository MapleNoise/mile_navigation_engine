package com.mile.mile_navigation_engine.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Karl on 27/02/2017.
 */

public class Sponsor implements Parcelable {
    private String key;
    private String name;
    private String LogoNameURL;
    private String illustrationURL;
    private String urlWeb;
    private String description;
    private ArrayList<Integer> idAnnotation;
    private Boolean dataFetched;
    private String vocal; //vocal is sponsor arrayName orthograph for TTS. If no vocal field is found, then it must be equal to arrayName.
    private String imageURL;
    private String logoURL;

    public Sponsor() {
        this.key = "";
        name = "";
        LogoNameURL = "";
        illustrationURL = "";
        urlWeb = "";
        description = "";
        vocal = "";
        imageURL = null;
        logoURL = null;
    }

    public Sponsor(String key) {
        this.key = key;
        name = "";
        LogoNameURL = "";
        illustrationURL = "";
        urlWeb = "";
        description = "";
        vocal = "";
        imageURL = null;
        logoURL = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setLogoNameURL(String logoNameURL) {
        this.LogoNameURL = logoNameURL;
    }

    public String getLogoNameURL() {
        return LogoNameURL;
    }



    public String getIllustrationURL() {
        return illustrationURL;
    }

    public void setIllustrationURL(String illustrationURL) {
        this.illustrationURL = illustrationURL;
    }

    public ArrayList<Integer> getIdAnnotation() { return idAnnotation; }

    public void setIdAnnotation (ArrayList<Integer> annotation) { this.idAnnotation = annotation; }

    public Boolean getDataFetched() { return dataFetched; }

    public String getVocal() {
        return vocal;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    //********************************//
    //*********  PARCEL  *************//
    //********************************//

    @Override
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(LogoNameURL);
        dest.writeString(vocal);
        dest.writeString(logoURL);
        dest.writeString(imageURL);

    }

    public Sponsor(Parcel source){
            /*
             * Reconstruct from the Parcel
             */
        key = source.readString();
        name = source.readString();
        LogoNameURL = source.readString();
        vocal = source.readString();
        logoURL = source.readString();
        imageURL = source.readString();
    }

    public static final Creator<Sponsor> CREATOR = new Creator<Sponsor>()
    {
        @Override
        public Sponsor createFromParcel(Parcel source)
        {
            return new Sponsor(source);
        }

        @Override
        public Sponsor[] newArray(int size)
        {
            return new Sponsor[size];
        }
    };

    @Override
    public String toString() {
        return "Sponsor{" +
                "key='" + key + '\'' +
                ", arrayName='" + name + '\'' +
                ", LogoNameURL='" + LogoNameURL + '\'' +
                ", illustrationURL='" + illustrationURL + '\'' +
                ", urlWeb='" + urlWeb + '\'' +
                ", arrayDescription='" + description + '\'' +
                ", idAnnotation=" + idAnnotation +
                ", dataFetched=" + dataFetched +
                ", vocal='" + vocal + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", logoURL='" + logoURL + '\'' +
                '}';
    }
}
