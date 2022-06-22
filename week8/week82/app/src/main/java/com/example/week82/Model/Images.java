
package com.example.week82.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Images)) return false;
        Images images = (Images) o;
        return Objects.equals(getXs(), images.getXs()) && Objects.equals(sm, images.sm) && Objects.equals(md, images.md) && Objects.equals(getLg(), images.getLg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getXs(), sm, md, getLg());
    }
}
