package com.pewpew.pewpew.common;

import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

public class MongoId {
    @SerializedName("_id")
    private String _id;

    public ObjectId get_id() {
        return new ObjectId(_id);
    }

    public void set_id(String tokenValue) {
        this._id = _id;
    }
}
