package ir.cafebazar.et.bazar.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gener {


    @SerializedName("genre")
    private Genere genere;

    @SerializedName("games")
    private ArrayList<Gamee> gameeArrayList;


    public Gener() {
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public ArrayList<Gamee> getGameeArrayList() {
        return gameeArrayList;
    }

    public void setGameeArrayList(ArrayList<Gamee> gameeArrayList) {
        this.gameeArrayList = gameeArrayList;
    }

    public static class Genere{

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("description")
        private String description;

        @SerializedName("totalGamesCount")
        private String  totalGamesCount;

        public Genere() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTotalGamesCount() {
            return totalGamesCount;
        }

        public void setTotalGamesCount(String totalGamesCount) {
            this.totalGamesCount = totalGamesCount;
        }
    }



    public static class Gamee{

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("code")
        private String code;

        @SerializedName("image")
        private String  image;

        @SerializedName("thumb")
        private String thumb;

        @SerializedName("likes")
        private String likes;

        @SerializedName("gamePlays")
        private String gamePlays;

        @SerializedName("socialsImage")
        private String socialsImage;

        @SerializedName("imageUrl")
        private String priceToOpen;

        @SerializedName("tag")
        private String tag;

        @SerializedName("genresNames")
        private ArrayList<String> genresNames;

        @SerializedName("url")
        private String  url;

        public Gamee() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getGamePlays() {
            return gamePlays;
        }

        public void setGamePlays(String gamePlays) {
            this.gamePlays = gamePlays;
        }

        public String getSocialsImage() {
            return socialsImage;
        }

        public void setSocialsImage(String socialsImage) {
            this.socialsImage = socialsImage;
        }

        public String getPriceToOpen() {
            return priceToOpen;
        }

        public void setPriceToOpen(String priceToOpen) {
            this.priceToOpen = priceToOpen;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public ArrayList<String> getGenresNames() {
            return genresNames;
        }

        public void setGenresNames(ArrayList<String> genresNames) {
            this.genresNames = genresNames;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
