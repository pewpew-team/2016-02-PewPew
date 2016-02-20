package com.pewpew.pewpew.additional;

import com.google.gson.annotations.SerializedName;

public class IdFromJson {


    public String get_id() {
        return _id;
    }

    @SerializedName("_id")
    public String _id;
}
