package com.camiss.supportstudent.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.camiss.supportstudent.ActivityRecordContent;
import com.camiss.supportstudent.Model.Record;
import com.camiss.supportstudent.R;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.CustomViewHolder> {

    List<Record> contents;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private Context mContext;

    public TestRecyclerViewAdapter(List<Record> contents, Context context) {
        Log.i("AnhHT11", "Size = "+ contents.size());
        this.contents = contents;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
//            case TYPE_HEADER: {
//                view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item_card_big, parent, false);
//                return new CustomViewHolder(view) {
//                };
//            }
            case TYPE_HEADER:
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new CustomViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
            case TYPE_CELL:
                holder.tvRecord.setText(Html.fromHtml(contents.get(position).getContent()));
                holder.tvRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityRecordContent.sRecord = contents.get(position);
                        mContext.startActivity(new Intent(mContext,ActivityRecordContent.class));
                    }
                });
                break;
        }
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvRecord;

        public CustomViewHolder(View view) {
            super(view);
            this.tvRecord = (TextView) view.findViewById(R.id.tvRecord);
        }
    }
}