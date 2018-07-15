package com.mygamelogic.myinfo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygamelogic.myinfo.Adapter.InfoArrayAdapter;
import com.mygamelogic.myinfo.Database.DatabaseHelper;
import com.mygamelogic.myinfo.Interface.InfoCallback;
import com.mygamelogic.myinfo.R;
import com.mygamelogic.myinfo.Response.MainResponse;
import com.mygamelogic.myinfo.Response.PageData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 15/07/18.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,InfoCallback {
    private EditText editext_search;
    private SwipeRefreshLayout swiperefreshparent_layout;
    private OkHttpClient client = new OkHttpClient();
    private RecyclerView recyclerView;
    private Boolean doubleBackToExitPressedOnce=false;
    private List<PageData> pageDataList=new ArrayList<>();
    private InfoArrayAdapter infoArrayAdapter;
    private DatabaseHelper databaseHelper;
    private String currentSearchText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setClickListener();
        setEditextValueChangeListener();
        databaseHelper=new DatabaseHelper(this);
    }
    private void initViews(){
        editext_search=(EditText) findViewById(R.id.editext_search);
        swiperefreshparent_layout=(SwipeRefreshLayout) findViewById(R.id.swiperefreshparent_layout);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
    }
    private void setClickListener(){
        findViewById(R.id.imageview_gosearch).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_gosearch:
                searchButtonTapped();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finish();
            System.exit(0);
            return;
        }
        doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.backbtn_text));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    private void showToast(String message){
        Toast toast1 = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast1.show();
    }
//---------------------------------------------------------------------
//loader related methode
private void showLoader() {
    try {
        if(swiperefreshparent_layout.getVisibility()==View.GONE) {
            swiperefreshparent_layout.setVisibility(View.VISIBLE);
            swiperefreshparent_layout.setColorSchemeColors(getColorBack(R.color.Orange), getColorBack(R.color.LiteGreen), getColorBack(R.color.Orange), getColorBack(R.color.LiteGreen));
            swiperefreshparent_layout.setRefreshing(true);
//            swiperefreshparent_layout.post(new Runnable() {
//                @Override
//                public void run() {
//                    swiperefreshparent_layout.setRefreshing(true);
//                }
//            });
            swiperefreshparent_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swiperefreshparent_layout.setRefreshing(false);
                }
            });
        }else {
            swiperefreshparent_layout.setRefreshing(true);
        }
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
        swiperefreshparent_layout.setRefreshing(false);
    }
//-------------------------------------------------------
private void setRecyclerView(){
    if((pageDataList!=null)) {
        if (pageDataList.size() > 0) {
            swiperefreshparent_layout.setVisibility(View.VISIBLE);
            if (infoArrayAdapter == null) {
                infoArrayAdapter = new InfoArrayAdapter(getApplicationContext(),pageDataList);
                infoArrayAdapter.setInfoCallback(this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(infoArrayAdapter);
            } else {
                infoArrayAdapter.refreshList(pageDataList);
            }
        } else {
            swiperefreshparent_layout.setVisibility(View.GONE);
            TextView textView=(TextView) findViewById(R.id.textview_nodata);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.emptysearch_text));
        }
    }else{
        swiperefreshparent_layout.setVisibility(View.GONE);
        TextView textView=(TextView) findViewById(R.id.textview_nodata);
        textView.setVisibility(View.VISIBLE);
        textView.setText(getString(R.string.nointernet_text));
    }
}

    @Override
    public void clickOnRow(int position) {
        if((pageDataList!=null)&&(pageDataList.size()>position)) {
            Intent intent = new Intent(this, InfoActivity.class);
            Gson gson = new Gson();
            intent.putExtra("pageData",gson.toJson(pageDataList.get(position)));
            startActivity(intent);
        }else {
            showToast("Something went wrong!");
        }
    }

    //-----------------------------------------------------------------------------------
private void setEditextValueChangeListener(){
    editext_search.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //if search text is not empty show go button
            if(editext_search.getText().toString().isEmpty()){
                findViewById(R.id.imageview_gosearch).setVisibility(View.GONE);
            }else{
                findViewById(R.id.imageview_gosearch).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    });
    editext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchButtonTapped();
                return true;
            }
            return false;
        }
    });
}
    private void searchButtonTapped(){
        if(!(editext_search.getText().toString().isEmpty())) {
            findViewById(R.id.imageview_gosearch).setVisibility(View.GONE);
            if (infoArrayAdapter != null) {
                infoArrayAdapter.refreshList(new ArrayList<PageData>());
            }
            findViewById(R.id.textview_nodata).setVisibility(View.GONE);
            hideKeyboard(editext_search);

            String gsonData = databaseHelper.getGsonData(editext_search.getText().toString().trim());
            if ((gsonData != null) && (!gsonData.isEmpty())) {
                Gson gson = new Gson();
                MainResponse mainResponse = gson.fromJson(gsonData, MainResponse.class);
                if (mainResponse != null) {
                    if ((mainResponse.getQuery() != null) && (mainResponse.getQuery().getPages() != null)) {
                        pageDataList = mainResponse.getQuery().getPages();
                    } else {
                        pageDataList = new ArrayList<>();
                    }
                } else {
                    pageDataList = null;
                }
                setRecyclerView();
            } else {
                showLoader();
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(editext_search.getText().toString().trim());
            }
        }else {
            showToast(getString(R.string.nosearch_text));
        }
    }
    public void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public class OkHttpHandler extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            Request.Builder builder = new Request.Builder();
            String mainUrlStr="http://en.wikipedia.org//w/api.php?action=query" +
                    "&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch" +
                    "&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50" +
                    "&pilimit=10&wbptterms=description&gpssearch={searchText}&gpslimit=10";
            mainUrlStr=mainUrlStr.replace("{searchText}",(String)objects[0]);
            currentSearchText=(String)objects[0];
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
                MainResponse mainResponse = gson.fromJson(((String) o), MainResponse.class);
                if (mainResponse != null) {
                    databaseHelper.checkForDataAndAdd(currentSearchText,((String)o));
                    if ((mainResponse.getQuery() != null) && (mainResponse.getQuery().getPages() != null)) {
                        pageDataList = mainResponse.getQuery().getPages();
                    }else {
                        pageDataList=new ArrayList<>();
                    }
                }else {
                    pageDataList=null;
                }
            }else {
                pageDataList=null;
            }
            setRecyclerView();
        }
    }

}
