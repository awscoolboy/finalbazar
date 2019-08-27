package ir.cafebazar.et.Models.collections;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Collections {


    @SerializedName("count")
    private String count;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private List<Collection> results;


    public static class Collection{

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;


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
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Collection> getResults() {
        return results;
    }

    public void setResults(List<Collection> results) {
        this.results = results;
    }
}
