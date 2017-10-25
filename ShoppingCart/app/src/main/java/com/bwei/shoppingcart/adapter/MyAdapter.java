package com.bwei.shoppingcart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwei.shoppingcart.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 类的用途
 * 2. @author forever
 * 3. @date 2017/9/6 16:06
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private JSONArray data;
    private Map<String, Boolean> map = new HashMap<>();

    public MyAdapter(Context context, JSONArray data, OnCheckListener listener) {
        this.context = context;
        this.data = data;
        setCheckData(false);
        this.listener = listener;

    }

    //全选控制适配器item
    private void setCheckData(boolean checkFlag) {
        map.clear();
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jsonObject = data.getJSONObject(i);
                String id = jsonObject.getString("id");
                map.put(id, checkFlag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //刷新适配器
    public void notifCheckData(boolean checkFlag) {
        //设置选择状态
        setCheckData(checkFlag);
        //刷新适配器
        notifyDataSetChanged();
    }

    //创建view设置给ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);

        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onClick(position,view);

                }
            }
        });
        return holder;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject jsonObject = data.getJSONObject(position);
            String image_url = jsonObject.optString("image_url");
            String title = jsonObject.getString("title");
            final String id = jsonObject.getString("id");


            holder.tv_title.setText(title);
            Picasso.with(context).load(image_url).placeholder(R.mipmap.ic_launcher).into(holder.iv);
            //根据id保存状态
            holder.cb.setChecked(map.get(id));

            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //根据点击选中状态
                    boolean checked = ((CheckBox) view).isChecked();
                    map.put(id, checked);
                    //定义一个全选的状态
                    boolean isChecked = true;
                    //遍历集合
                    for (String key : map.keySet()) {
                        Boolean value = map.get(key);
                        if (!value) {
                            isChecked = false;
                            if (listener != null) {
                                //取消全选
                                listener.onCheck(isChecked);
                            }
                            return;
                        }
                    }
                    if (isChecked) {
                        //全选
                        if (listener != null) {
                            listener.onCheck(isChecked);
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //数据长度
    @Override
    public int getItemCount() {
        return data.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tv_title;
        private CheckBox cb;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            cb = (CheckBox) itemView.findViewById(R.id.cb);

        }
    }
    //定义CheckBox点击的接口回调
    private OnCheckListener listener;

    public interface OnCheckListener {

        void onCheck(boolean isCheck);
    }

    public void setOnCheckListener(OnCheckListener listener) {
        this.listener = listener;
    }
  //定义条目点击的接口回调
    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener {

        void onClick( int position,View view);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
}
