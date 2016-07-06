package com.sn.decks.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.ObjectValueFilter;
import com.avos.avoscloud.PaasClient;
import com.sn.decks.activity.R;
import com.sn.decks.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final int IS_IFRST = 1;

    private List<Map> datas_name;
    private List<Map> datas_context;

    public RecyclerViewAdapter() {
        datas_context = new ArrayList<>();
        datas_name = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = datas_name.get(position).get(position).toString();
        String context = datas_context.get(position).get(position).toString();
        if(!name.equals("") && !context.equals("")) {

            holder.textView.setText(name);
            holder.textView_context.setText(context);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), ""+datas.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(datas_name == null) {
            return 0;
        } else {
            return datas_name.size();
        }
    }

    class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView textView;
        private TextView textView_context;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = ((TextView) itemView.findViewById(R.id.textView_list_item));
            textView_context = ((TextView) itemView.findViewById(R.id.textView_addSharing_context));
        }
    }


    /**
     * 初始化数据
     */
    public void refresh() {
        getLeanCloudData();
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     */
    public void add(){
        getLeanCloudData();
        notifyDataSetChanged();
    }

    /**
     * 从云端获取数据并加入本地的listView
     */
    private void getLeanCloudData() {
        // leanCloud数据读取
        AVQuery.doCloudQueryInBackground("select * from SharingWords",
                new CloudQueryCallback<AVCloudQueryResult>() {

                    @SuppressWarnings("null")
                    @Override
                    public void done(AVCloudQueryResult result,
                                     AVException cqlException) {
                        if (cqlException == null) {
                            datas_name.clear();
                            datas_context.clear();
                            Map list_name = new IdentityHashMap();
                            Map list_context = new IdentityHashMap();
                            for (int i = 0; i < result.getResults().size(); i++) {

                                String name = result.getResults().get(i)
                                        .getString("uploadname");

                                String context = result.getResults().get(i)
                                        .getString("context");

                                list_name.put(i,"upload by " + name);
                                list_context.put(i, context);

                                datas_context.add(list_context);
                                datas_name.add(list_name);
                            }
                        }
                    }
                });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}