
package com.example.week72.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

//класс для данных о герое
public class HeroData implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("powerstats")
    @Expose
    private Powerstats powerstats;
    @SerializedName("appearance")
    @Expose
    private Appearance appearance;
    @SerializedName("biography")
    @Expose
    private Biography biography;
    @SerializedName("work")
    @Expose
    private Work work;
    @SerializedName("connections")
    @Expose
    private Connections connections;
    @SerializedName("images")
    @Expose
    private Images images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Powerstats getPowerstats() {
        return powerstats;
    }

    public void setPowerstats(Powerstats powerstats) {
        this.powerstats = powerstats;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public Biography getBiography() {
        return biography;
    }

    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Connections getConnections() {
        return connections;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "HeroData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", powerstats=" + powerstats +
                ", appearance=" + appearance +
                ", biography=" + biography +
                ", work=" + work +
                ", connections=" + connections +
                ", images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeroData)) return false;
        HeroData heroData = (HeroData) o;
        return Objects.equals(getId(), heroData.getId()) && Objects.equals(getName(), heroData.getName()) && Objects.equals(getSlug(), heroData.getSlug()) && Objects.equals(getPowerstats(), heroData.getPowerstats()) && Objects.equals(getAppearance(), heroData.getAppearance()) && Objects.equals(getBiography(), heroData.getBiography()) && Objects.equals(getWork(), heroData.getWork()) && Objects.equals(getConnections(), heroData.getConnections()) && Objects.equals(getImages(), heroData.getImages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSlug(), getPowerstats(), getAppearance(), getBiography(), getWork(), getConnections(), getImages());
    }
}
