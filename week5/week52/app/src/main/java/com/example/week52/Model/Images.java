
package com.example.week52.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Images implements Serializable {

    @SerializedName("xs")
    @Expose
    private String xs;
    @SerializedName("sm")
    @Expose
    private String sm;
    @SerializedName("md")
    @Expose
    private String md;
    @SerializedName("lg")
    @Expose
    private String lg;

    public String getXs() {
        return xs;
    }

    public String getLg() {
        return lg;
    }

}
