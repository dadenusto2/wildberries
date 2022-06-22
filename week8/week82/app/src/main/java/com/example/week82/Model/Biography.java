
package com.example.week82.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Biography  implements Serializable {

    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("alterEgos")
    @Expose
    private String alterEgos;
    @SerializedName("aliases")
    @Expose
    private List<String> aliases = null;
    @SerializedName("placeOfBirth")
    @Expose
    private String placeOfBirth;
    @SerializedName("firstAppearance")
    @Expose
    private String firstAppearance;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("alignment")
    @Expose
    private String alignment;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlterEgos() {
        return alterEgos;
    }

    public void setAlterEgos(String alterEgos) {
        this.alterEgos = alterEgos;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getFirstAppearance() {
        return firstAppearance;
    }

    public void setFirstAppearance(String firstAppearance) {
        this.firstAppearance = firstAppearance;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Biography)) return false;
        Biography biography = (Biography) o;
        return Objects.equals(getFullName(), biography.getFullName()) && Objects.equals(getAlterEgos(), biography.getAlterEgos()) && Objects.equals(getAliases(), biography.getAliases()) && Objects.equals(getPlaceOfBirth(), biography.getPlaceOfBirth()) && Objects.equals(getFirstAppearance(), biography.getFirstAppearance()) && Objects.equals(getPublisher(), biography.getPublisher()) && Objects.equals(getAlignment(), biography.getAlignment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullName(), getAlterEgos(), getAliases(), getPlaceOfBirth(), getFirstAppearance(), getPublisher(), getAlignment());
    }
}
