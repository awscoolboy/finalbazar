package ir.cafebazar.et.Models.collections;

import java.util.List;

public class CollDataModel {

    private String headerTitle;
    private List<Subcollection> appCollecation;

    public String getHeaderTitle() {
        return headerTitle;
    }

    public CollDataModel() {
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<Subcollection> getAppCollecation() {
        return appCollecation;
    }

    public void setAppCollecation(List<Subcollection> appCollecation) {
        this.appCollecation = appCollecation;
    }
}
