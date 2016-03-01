package com.camiss.supportstudent.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camiss.supportstudent.Model.Record;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.camiss.supportstudent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    public RecyclerViewFragment() {

    }
    private static int ITEM_COUNT = 0;

    private List<Record> mContentItems = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public RecyclerViewFragment(List<Record> list) {
        mContentItems = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        Log.i("AnhHT11", "size in 2 = " + mContentItems.size());
        mAdapter = new RecyclerViewMaterialAdapter(new TestRecyclerViewAdapter(mContentItems,getContext()));
        mRecyclerView.setAdapter(mAdapter);
//
//        {
//            for (int i = 0; i < ITEM_COUNT; ++i)
//                mContentItems.add(new Object());
//            mAdapter.notifyDataSetChanged();
//        }
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }
}
