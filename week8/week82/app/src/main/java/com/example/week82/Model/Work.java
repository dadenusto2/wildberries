
package com.example.week82.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Work  implements Serializable {

    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("base")
    @Expose
    private String base;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Work)) return false;
        Work work = (Work) o;
        return Objects.equals(getOccupation(), work.getOccupation()) && Objects.equals(getBase(), work.getBase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOccupation(), getBase());
    }
}
