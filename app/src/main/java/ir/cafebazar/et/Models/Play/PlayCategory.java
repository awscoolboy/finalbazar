package ir.cafebazar.et.Models.Play;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class PlayCategory {

    @ColumnInfo(name = "count")
    @SerializedName("count")
    private String count;

    @ColumnInfo(name = "count")
    @SerializedName("next")
    private String next;

    @ColumnInfo(name = "previous")
    @SerializedName("previous")
    private String previous;


    @ColumnInfo(name = "playCategories")
    @SerializedName("results")
    private List<PlayCat> playCategories;


    public List<PlayCat> getPlayCategories() {
        return playCategories;
    }


    public void setPlayCategories(List<PlayCat> playCategories) {
        this.playCategories = playCategories;
    }



    @Entity
    public static class PlayCat{


        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        private String id;

        @ColumnInfo(name = "catcode")
        @SerializedName("catcode")
        private String catcode;


        @ColumnInfo(name = "name")
        @SerializedName("name")
        private String name;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCatcode() {
            return catcode;
        }

        public void setCatcode(String catcode) {
            this.catcode = catcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }


}
