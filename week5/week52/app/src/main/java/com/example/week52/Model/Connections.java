
package com.example.week52.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Connections implements Serializable {

    @SerializedName("groupAffiliation")
    @Expose
    private String groupAffiliation;
    @SerializedName("relatives")
    @Expose
    private String relatives;

    public String getGroupAffiliation() {
        return groupAffiliation;
    }

    public void setGroupAffiliation(String groupAffiliation) {
        this.groupAffiliation = groupAffiliation;
    }

    public String getRelatives() {
        return relatives;
    }

    public void setRelatives(String relatives) {
        this.relatives = relatives;
    }

}
