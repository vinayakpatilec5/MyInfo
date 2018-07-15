package com.mygamelogic.myinfo.Response;

import java.util.List;

/**
 * Created by admin on 15/07/18.
 */

public class QueryResponse {
    private List<PageData> pages;

    public List<PageData> getPages() {
        return pages;
    }

    public void setPages(List<PageData> pages) {
        this.pages = pages;
    }
}
//https://en.wikipedia.org//w/api.php?action=query&format=json&
// prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&
// formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=vinayak&gpslimit=1

//https://en.wikipedia.org/w/api.php?action=query&format=json&prop=info&pageids=1710617&inprop=url