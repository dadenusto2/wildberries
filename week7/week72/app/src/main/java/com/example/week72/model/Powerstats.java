
package com.example.week72.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Powerstats implements Serializable {

    @SerializedName("intelligence")
    @Expose
    private Integer intelligence;
    @SerializedName("strength")
    @Expose
    private Integer strength;
    @SerializedName("speed")
    @Expose
    private Integer speed;
    @SerializedName("durability")
    @Expose
    private Integer durability;
    @SerializedName("power")
    @Expose
    private Integer power;
    @SerializedName("combat")
    @Expose
    private Integer combat;

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getCombat() {
        return combat;
    }

    public void setCombat(Integer combat) {
        this.combat = combat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Powerstats)) return false;
        Powerstats that = (Powerstats) o;
        return Objects.equals(getIntelligence(), that.getIntelligence()) && Objects.equals(getStrength(), that.getStrength()) && Objects.equals(getSpeed(), that.getSpeed()) && Objects.equals(getDurability(), that.getDurability()) && Objects.equals(getPower(), that.getPower()) && Objects.equals(getCombat(), that.getCombat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIntelligence(), getStrength(), getSpeed(), getDurability(), getPower(), getCombat());
    }
}
