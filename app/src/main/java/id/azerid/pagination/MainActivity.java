package id.azerid.pagination;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.azerid.pagination.adapter.AdapterItem;
import id.azerid.pagination.api.ApiInterfaces;
import id.azerid.pagination.api.RetrofitApiCiient;
import id.azerid.pagination.api.model.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterItem.OnLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener {


    ApiInterfaces apiInterfaces;
    private AdapterItem mAdapter;
    private ArrayList<ApiResponse> itemList;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView mRecyclerView;
    private List<ApiResponse> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterfaces = RetrofitApiCiient.getClient().create(ApiInterfaces.class);

        itemList = new ArrayList<>();
        swipeRefresh = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.rvList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterItem(this);
        //mRecyclerView.setAdapter(mAdapter);
        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 2)) {
                    mAdapter.showLoading();
                }
            }
        });
        surah();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity_", "onStart");
        loadData();
    }

    @Override
    public void onRefresh() {
        Log.d("MainActivity_", "onRefresh");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                loadData();

            }
        }, 2000);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onLoadMore() {
        Log.d("MainActivity_", "onLoadMore");
        new AsyncTask<Void, Void, List<ApiResponse>>() {
            @Override
            protected List<ApiResponse> doInBackground(Void... voids) {
                ///////////////////////////////////////////
                int start = mAdapter.getItemCount() - 1;
                int end = start + 8;
                List<ApiResponse> list = new ArrayList<>();
                if (end < 200) {
                    for (int i = start + 1; i <= end; i++) {
                        list.add(new ApiResponse());
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /////////////////////////////////////////////////
                return list;

            }

            @Override
            protected void onPostExecute(List<ApiResponse> items) {
                super.onPostExecute(list);
                mAdapter.dismissLoading();
                mAdapter.addItemMore(list);
                mAdapter.setMore(true);
            }
        }.execute();

    }

    private void loadData() {
        itemList.clear();
        for (int i = 1; i <= 10; i++) {
            itemList.add(new ApiResponse());
        }
        mAdapter.addAll(itemList);

    }
    private void surah() {
        Call<List<ApiResponse>> call = apiInterfaces.getData();
        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200){
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            Log.d("Respon", "data surah:  " + jsonArray);
                            list = response.body();
                            AdapterItem adapterItem = new AdapterItem(MainActivity.this::onLoadMore);
                            adapterItem.addItemMore(list);
                            mRecyclerView.setAdapter(adapterItem);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {

            }
        });
    }
}
