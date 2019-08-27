package ir.cafebazar.et.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Developer implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("developerID")
    private String developerID;

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID(String developerID) {
        this.developerID = developerID;
    }
}
