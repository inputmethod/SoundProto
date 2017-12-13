package com.typany.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.typany.soundproto.R;

/**
 * Created by yangfeng on 2017/12/13.
 */

abstract public class RecyclerFragment extends Fragment {
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.fragment_local_skin, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(instanceAdapter());
    }

    protected int getColumnCount() {
        return 3;
    }

    abstract protected RecyclerView.Adapter instanceAdapter();
}
