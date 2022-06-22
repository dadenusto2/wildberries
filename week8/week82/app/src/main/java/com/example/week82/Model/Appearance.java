
package com.example.week82.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appearance implements Serializable {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("race")
    @Expose
    private String race;
    @SerializedName("height")
    @Expose
    private List<String> height = null;
    @SerializedName("weight")
    @Expose
    private List<String> weight = null;
    @SerializedName("eyeColor")
    @Expose
    private String eyeColor;
    @SerializedName("hairColor")
    @Expose
    private String hairColor;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public List<String> getHeight() {
        return height;
    }

    public void setHeight(List<String> height) {
        this.height = height;
    }

    public List<String> getWeight() {
        return weight;
    }

    public void setWeight(List<String> weight) {
        this.weight = weight;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appearance)) return false;
        Appearance that = (Appearance) o;
        return Objects.equals(getGender(), that.getGender()) && Objects.equals(getRace(), that.getRace()) && Objects.equals(getHeight(), that.getHeight()) && Objects.equals(getWeight(), that.getWeight()) && Objects.equals(getEyeColor(), that.getEyeColor()) && Objects.equals(getHairColor(), that.getHairColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGender(), getRace(), getHeight(), getWeight(), getEyeColor(), getHairColor());
    }
}
