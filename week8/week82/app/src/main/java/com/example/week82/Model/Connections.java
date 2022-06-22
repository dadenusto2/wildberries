
package com.example.week82.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connections)) return false;
        Connections that = (Connections) o;
        return Objects.equals(getGroupAffiliation(), that.getGroupAffiliation()) && Objects.equals(getRelatives(), that.getRelatives());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupAffiliation(), getRelatives());
    }
}
