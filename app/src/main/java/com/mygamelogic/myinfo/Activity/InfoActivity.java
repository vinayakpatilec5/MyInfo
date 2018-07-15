package com.mygamelogic.myinfo.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygamelogic.myinfo.R;
import com.mygamelogic.myinfo.Response.InfoResponse;
import com.mygamelogic.myinfo.Response.MainResponse;
import com.mygamelogic.myinfo.Response.PageData;
import com.mygamelogic.myinfo.Response.PageInfoData;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 15/07/18.
 */

public class InfoActivity extends AppCompatActivity {
    private WebView webview_info;
    private SwipeRefreshLayout refreshlayout_infoScreen;
    private PageData pageData;
    private OkHttpClient client = new OkHttpClient();
    private InfoResponse urlResponse;
    private boolean isActivityLive=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityLive=true;
        setContentView(R.layout.activity_info);
        Intent callingIntent = getIntent();
        String pageDatsStr = callingIntent.getStringExtra("pageData");
        Gson gson = new Gson();
        pageData = gson.fromJson(pageDatsStr, PageData.class);
        setToolbar();
        intiViews();
        showLoader();
        getPageUrlByPageId();
    }

    private void intiViews() {
        webview_info = (WebView) findViewById(R.id.webview_info);
        refreshlayout_infoScreen = (SwipeRefreshLayout) findViewById(R.id.refreshlayout_infoScreen);
    }

    @Override
    public void onBackPressed() {
        isActivityLive=false;
        super.onBackPressed();
    }

    //---------------------------------------
    //set top toolbar
    private void setToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar_infolayout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //show loader as it ll take time to load data from server
    private void showLoader() {
        try {
            refreshlayout_infoScreen.bringToFront();
            refreshlayout_infoScreen.setVisibility(View.VISIBLE);
            refreshlayout_infoScreen.setColorSchemeColors(getColorBack(R.color.Orange), getColorBack(R.color.LiteGreen), getColorBack(R.color.Orange), getColorBack(R.color.LiteGreen));
            refreshlayout_infoScreen.post(new Runnable() {
                @Override
                public void run() {
                    refreshlayout_infoScreen.setRefreshing(true);
                }
            });
            refreshlayout_infoScreen.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshlayout_infoScreen.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getColorBack(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(id);
        } else {
            return getResources().getColor(id);
        }
    }

    private void hideLoader() {
        refreshlayout_infoScreen.setRefreshing(false);
        refreshlayout_infoScreen.setVisibility(View.GONE);
    }
    private void showToast(String message){
        Toast toast1 = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast1.show();
    }

    //-------------------------------------------------------------------
    private void getPageUrlByPageId() {
        if ((pageData != null) && (pageData.getPageid() > 0)) {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute("");
        } else {
            showToast("Something went wrong");
            this.finish();
        }
    }

    public class OkHttpHandler extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            Request.Builder builder = new Request.Builder();
            String mainUrlStr="https://en.wikipedia.org/w/api.php?action=query&format=json&" +
                    "prop=info&pageids={pageId}&inprop=url";
            mainUrlStr=mainUrlStr.replace("{pageId}",(""+pageData.getPageid()));
            builder.url(mainUrlStr);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            hideLoader();
            Gson gson=new Gson();
            if(o!=null) {
                urlResponse = gson.fromJson(((String) o), InfoResponse.class);
                openWebView();
            }else {
                if(isActivityLive) {
                    isActivityLive = false;
                    showToast("NO INTERNET CONNECTION");
                    InfoActivity.this.finish();
                }
            }
        }
    }

    private void openWebView(){
            boolean webViewOpened=false;
            if ((urlResponse != null) && (urlResponse.getQuery() != null) && (urlResponse.getQuery().getPages() != null) &&
                    (urlResponse.getQuery().getPages().size() > 0)) {
                for (String key : urlResponse.getQuery().getPages().keySet()) {
                    PageInfoData pageInfoData = urlResponse.getQuery().getPages().get(key);
                    if ((pageInfoData != null) && (pageInfoData.getFullurl() != null)) {
                        webview_info.clearCache(true);
                        webview_info.getSettings().setUseWideViewPort(true);
                        webview_info.setInitialScale(1);
                        webview_info.getSettings().setBuiltInZoomControls(true);
                        webview_info.clearHistory();
                        webViewOpened=true;
                        webview_info.getSettings().setAllowFileAccess(true);
                        webview_info.getSettings().setDomStorageEnabled(true);
                        webview_info.getSettings().setJavaScriptEnabled(true);
                        webview_info.getSettings().setPluginState(WebSettings.PluginState.ON);
                        webview_info.getSettings().setLoadWithOverviewMode(true);
                        webview_info.getSettings().setUseWideViewPort(true);
                        webview_info.getSettings().setPluginState(WebSettings.PluginState.ON);
                        webview_info.loadUrl(pageInfoData.getFullurl());
                        webview_info.setWebChromeClient(new WebChromeClient() {
                            public void onProgressChanged(WebView view, int progress) {
                                if (progress > 90) {
                                    hideLoader();
                                }
                            }
                        });
                        webview_info.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                view.loadUrl(request.toString());
                                return false;
                            }
                        });
                    }
                }
                if(!webViewOpened){
                   showError();
                }
            } else {
                showError();
            }
    }
    private void showError(){
        if(isActivityLive) {
            isActivityLive=false;
            showToast("Something went wrong");
            this.finish();
        }
    }


}