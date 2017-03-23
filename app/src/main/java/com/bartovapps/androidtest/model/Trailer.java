package com.bartovapps.androidtest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BartovMoti on 01/27/17.
 */
public class Trailer {

    @SerializedName("name")
    public String name;
    @SerializedName("key")
    public String key;

    @Override
    public String toString() {
        return "Trailer{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
