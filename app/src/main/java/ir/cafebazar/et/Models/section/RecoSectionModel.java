package ir.cafebazar.et.Models.section;


import java.util.List;

import ir.cafebazar.et.Models.Recommended;

public class RecoSectionModel {

    private String headerTitle;
    private List<Recommended> cafeBazarApps;

    public RecoSectionModel() {
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<Recommended> getCafeBazarApps() {
        return cafeBazarApps;
    }

    public void setCafeBazarApps(List<Recommended> cafeBazarApps) {
        this.cafeBazarApps = cafeBazarApps;
    }
}
