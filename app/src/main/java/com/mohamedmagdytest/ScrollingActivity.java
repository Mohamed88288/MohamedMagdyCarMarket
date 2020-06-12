package com.mohamedmagdytest;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView noDataFound;
    public ArrayList<Car> Cars;
    private CarsAdapter adapter;
    private int loading = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mProgressView;
    private int start = 1;
    int visibleItemCount, totalItemCount = 1;
    int firstVisiblesItems = 0;
    int current_page = 0;
    boolean networkConnected;

    boolean canLoadMoreData = true; // make this variable false while your web service call is going on.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        noDataFound = (TextView) findViewById(R.id.no_data_found);
        mProgressView = findViewById(R.id.progress);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            Cars=new ArrayList<>();
        adapter = new CarsAdapter(this, R.layout.adapter_cars_item, Cars);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                networkConnected = CheckConnection.isConnected(ScrollingActivity.this);
                if (networkConnected)
                {
                    mSwipeRefreshLayout.setRefreshing(true);

                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (canLoadMoreData) {
                        if ((visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                                canLoadMoreData  = false;
                                start++;
                            networkConnected = CheckConnection.isConnected(ScrollingActivity.this);
                            if (networkConnected)
                            {
                                refreshContent(2);
                            }
                            else
                            {
                                Snackbar.make(recyclerView, "No Internet Connection  ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                mSwipeRefreshLayout.setRefreshing(false);

                            }

                        }
                    }

                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loading == 0) {
                    networkConnected = CheckConnection.isConnected(ScrollingActivity.this);
                    if (networkConnected)
                    {
                        refreshContent(1);
                    }
                    else
                    {
                        Snackbar.make(mRecyclerView, "No Internet Connection  ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        noDataFound.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
            if (loading == 0) {
                networkConnected = CheckConnection.isConnected(ScrollingActivity.this);
                if (networkConnected)
                {
                    refreshContent(1);
                }
                else
                {
                    Snackbar.make(mRecyclerView, "No Internet Connection  ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    noDataFound.setVisibility(View.VISIBLE);

                }

            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshContent(final int loading) {
        this.loading = loading;
        noDataFound.setVisibility(View.GONE);
        if (loading == 2) {
          //  Cars.add(new Car());
            adapter.notifyDataSetChanged();
        } else {
            //
            start = 1;
            Cars.clear();
        }
        mSwipeRefreshLayout.setRefreshing(true);
        //
        // Instantiate the RequestQueue.
        MyApplication.getInstance().getRequestQueue().getCache().clear();

        String url = MyApplication.URL+String.valueOf(start);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        get_data(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        // Add the request to the RequestQueue.
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public void get_data(String response)
    {
        try {
            JSONObject jsonobject = new JSONObject(response);
            if (jsonobject.getInt("status")==1)
            {
                JSONArray jsonArray=jsonobject.getJSONArray("data");
                if (jsonArray.length() > 0 && loading == 1) {
                    Cars.clear();
                } else if (loading == 2) {
                    Cars.remove(Cars.size() - 1);
                }
                //
                for (int i = 0; i < jsonArray.length(); i++) {
                    Car car = new Car();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    car.setBrand(jsonObject.getString("brand"));
                    car.setConstruction_year(jsonObject.getString("constructionYear"));
                    car.setImage(jsonObject.getString("imageUrl"));
                    car.setIs_Used(String.valueOf(jsonObject.getBoolean("isUsed")));
                    Cars.add(car);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                loading = 0;
                canLoadMoreData=true;
                adapter.notifyDataSetChanged();
            }else
            {
                noDataFound.setVisibility(View.GONE);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        //
        mSwipeRefreshLayout.setRefreshing(false);
        loading = 0;
        adapter.notifyDataSetChanged();
    }
}