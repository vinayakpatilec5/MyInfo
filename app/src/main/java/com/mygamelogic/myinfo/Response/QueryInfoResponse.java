package com.mygamelogic.myinfo.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 15/07/18.
 */

public class QueryInfoResponse {
    private Map<String,PageInfoData> pages;

    public Map<String, PageInfoData> getPages() {
        return pages;
    }

    public void setPages(Map<String, PageInfoData> pages) {
        this.pages = pages;
    }
}
