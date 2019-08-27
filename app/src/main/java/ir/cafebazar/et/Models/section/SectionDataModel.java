package ir.cafebazar.et.Models.section;

import java.util.List;
import java.util.List;

import ir.cafebazar.et.Models.BazaarApps;

public class SectionDataModel {

    private String headerTitle;
    private List<BazaarApps.CafeBazaarApp> cafeBazarApps;
    private String nextLink;

    public String getHeaderTitle() {
        return headerTitle;
    }

    public SectionDataModel() {

    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public String getNextLink() {
        return nextLink;
    }

    public SectionDataModel(String title, List<BazaarApps.CafeBazaarApp> cafeBazarApps) {
        this.headerTitle = title;
        this.cafeBazarApps = cafeBazarApps;
    }


    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<BazaarApps.CafeBazaarApp> getCafeBazarApps() {
        return cafeBazarApps;
    }

    public void setCafeBazarApps(List<BazaarApps.CafeBazaarApp> cafeBazarApps) {
        this.cafeBazarApps = cafeBazarApps;
    }
}
