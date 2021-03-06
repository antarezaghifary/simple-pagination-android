package id.azerid.pagination;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import id.azerid.pagination.adapter.AdapterItem;
import id.azerid.pagination.entity.Item;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterItem.OnLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener {


    private AdapterItem mAdapter;
    private ArrayList<Item> itemList;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();
        swipeRefresh = findViewById(R.id.swipeRefresh);
        RecyclerView mRecyclerView = findViewById(R.id.rvList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterItem(this);
        mRecyclerView.setAdapter(mAdapter);
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
        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(Void... voids) {
                ///////////////////////////////////////////
                int start = mAdapter.getItemCount() - 1;
                int end = start + 15;
                List<Item> list = new ArrayList<>();
                if (end < 200) {
                    for (int i = start + 1; i <= end; i++) {
                        list.add(new Item("Item " + i));
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
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                mAdapter.dismissLoading();
                mAdapter.addItemMore(items);
                mAdapter.setMore(true);
            }
        }.execute();

    }

    private void loadData() {
        itemList.clear();
        for (int i = 1; i <= 20; i++) {
            itemList.add(new Item("Item " + i));
        }
        mAdapter.addAll(itemList);
    }

}
