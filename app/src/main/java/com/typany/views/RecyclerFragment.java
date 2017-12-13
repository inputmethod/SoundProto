package com.typany.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.soundproto.R;
import com.typany.ui.skinui.LoadingFragment;
import com.typany.utilities.universalimageloader.ImageLoaderHelper;

/**
 * Created by yangfeng on 2017/12/13.
 */

abstract public class RecyclerFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DisplayImageOptions mOptions;
    private LoadingFragment mLoadingFragment;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.fragment_load_recycler, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mOptions = ImageLoaderHelper.newOptions(getContext(), true, Bitmap.Config.ARGB_8888);
        mLoadingFragment = new LoadingFragment();

        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(instanceAdapter(mOptions));

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fl_container, mLoadingFragment);
        ft.commit();
    }

    protected void showRefreshingViews() {
        if (mLoadingFragment != null && mLoadingFragment.isHidden()) {
            mLoadingFragment.show(true);
        }
    }

    protected void showRecyclerView() {
        if (mLoadingFragment != null) {
            mLoadingFragment.hide();
        }
    }

    public void drawLoading() {
        // TODO
    }

    protected int getColumnCount() {
        return 3;
    }

    abstract protected RecyclerView.Adapter instanceAdapter(DisplayImageOptions options);
}
