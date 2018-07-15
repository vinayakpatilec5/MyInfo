package com.mygamelogic.myinfo.Response;

/**
 * Created by admin on 15/07/18.
 */

public class MainResponse {
    private QueryResponse query;
    private Boolean batchcomplete;

    public QueryResponse getQuery() {
        return query;
    }

    public void setQuery(QueryResponse query) {
        this.query = query;
    }

    public Boolean getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(Boolean batchcomplete) {
        this.batchcomplete = batchcomplete;
    }
}
