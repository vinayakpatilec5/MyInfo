package com.mygamelogic.myinfo.Response;

/**
 * Created by admin on 15/07/18.
 */

public class PageData {
    private String title;
    private int pageid;
    private ThumbnailResponse thumbnail;
    private TermsResponse terms;
private String fullurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public ThumbnailResponse getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailResponse thumbnail) {
        this.thumbnail = thumbnail;
    }

    public TermsResponse getTerms() {
        return terms;
    }

    public void setTerms(TermsResponse terms) {
        this.terms = terms;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }
}
