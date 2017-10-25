package com.bwei.shoppingcart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bawei.swiperefreshlayoutlibrary.SwipyRefreshLayout;
import com.bawei.swiperefreshlayoutlibrary.SwipyRefreshLayoutDirection;
import com.bwei.shoppingcart.R;
import com.bwei.shoppingcart.adapter.MyAdapter;
import com.bwei.shoppingcart.utils.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String url = "http://result.eolinker.com/iYXEPGn4e9c6dafce6e5cdd23287d2bb136ee7e9194d3e9?uri=one";
    private RecyclerView rv;
    private SwipyRefreshLayout srl;
    private CheckBox checkbox;
    private MyAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String json = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray data = jsonObject.getJSONArray("data");
                        //设置适配器
                        adapter = new MyAdapter(MainActivity.this, data, new MyAdapter.OnCheckListener() {
                            @Override
                            public void onCheck(boolean isCheck) {
                                //当所有条目都选中时 全选将全中
                                checkbox.setChecked(isCheck);
                            }
                        });
                        rv.setAdapter(adapter);

                        //跳转页面
                        adapter.setOnRecyclerViewListener(new MyAdapter.OnRecyclerViewListener() {
                            @Override
                            public void onClick(int position, View view) {
                                startActivity(new Intent(MainActivity.this, OtherActivity.class));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找控件
        rv = (RecyclerView) findViewById(R.id.rv);
        srl = (SwipyRefreshLayout) findViewById(R.id.srl);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnClickListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        srl.setDirection(SwipyRefreshLayoutDirection.BOTH);
        srl.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        srl.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(int index) {
                checkbox.setChecked(false);
                getData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();

                        srl.setRefreshing(false);
                    }
                }, 2000);
            }

            @Override
            public void onLoad(int index) {

                checkbox.setChecked(false);
                getData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "上拉加载", Toast.LENGTH_SHORT).show();
                        srl.setRefreshing(false);
                    }
                }, 2000);

            }
        });


        getData();

    }

    private void getData() {
        OkHttp3Utils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Message message = handler.obtainMessage(0, json);
                message.sendToTarget();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkbox:
                if (((CheckBox) view).isChecked()) {
                    adapter.notifCheckData(true);


                } else {
                    adapter.notifCheckData(false);
                }
                break;
        }
    }
}
