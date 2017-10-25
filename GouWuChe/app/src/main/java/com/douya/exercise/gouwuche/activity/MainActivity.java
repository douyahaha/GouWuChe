package com.douya.exercise.gouwuche.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.douya.exercise.gouwuche.R;
import com.douya.exercise.gouwuche.bean.Bean;
import com.douya.okhttplibrary.utils.GsonObjectCallback;
import com.douya.okhttplibrary.utils.OkHttp3Utils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListView elv;
    private TextView tv_Price;
    private Button bt_commit;
    private String url="http://result.eolinker.com/iYXEPGn4e9c6dafce6e5cdd23287d2bb136ee7e9194d3e9?uri=evaluation";
    private List<Bean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getServerData();
    }

    private void getServerData() {
        OkHttp3Utils.doGet(url, new GsonObjectCallback<Bean>() {
            @Override
            public void onUi(Bean bean) {
                data = bean.getData();

            }

            @Override
            public void onFailed(Call call, IOException e) {

            }
        });
    }

    private void initView() {
        elv = (ExpandableListView) findViewById(R.id.elv);
        tv_Price = (TextView) findViewById(R.id.tv_Price);
        bt_commit = (Button) findViewById(R.id.bt_commit);

        bt_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:

                break;
        }
    }
    //二级列表的适配器
    public class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return data.get(groupPosition).getDatas().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return data.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return data.get(groupPosition).getDatas().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, R.layout.group_item, null);
            TextView tv_group = (TextView) view.findViewById(R.id.tv_group);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
