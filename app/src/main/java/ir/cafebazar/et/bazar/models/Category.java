package ir.cafebazar.et.bazar.models;

import com.google.gson.annotations.SerializedName;

public class Category {


    @SerializedName("id")
    private String id;

    @SerializedName("game")
    private boolean game;

    @SerializedName("slug")
    private String slug;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public Category(String id, boolean game, String slug, String name, String image) {
        this.id = id;
        this.game = game;
        this.slug = slug;
        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isGame() {
        return game;
    }

    public void setGame(boolean game) {
        this.game = game;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
