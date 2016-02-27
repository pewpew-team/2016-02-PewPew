package com.pewpew.pewpew.additional;

import com.google.gson.annotations.SerializedName;

public class TokenJson {

    @SerializedName("tokenValue")
    private String tokenValue;

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
