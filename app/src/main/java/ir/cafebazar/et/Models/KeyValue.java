package ir.cafebazar.et.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KeyValue {


    @ColumnInfo(name = "value")
    private String value;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "key")
    private String key;


    @NonNull
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
