package com.typany.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.soundproto.R;
import com.typany.ui.skinui.LoadingFragment;
import com.typany.utilities.ViewModelFactory;
import com.typany.utilities.universalimageloader.ImageLoaderHelper;

/**
 * Created by yangfeng on 2017/12/13.
 *
 * The base class for data collection display with recycler view, per android
 * architecture component: live data, lifecycle, room, paging library
 *
 * Image loader should be initialized by activity or application before any instance of
 * any sub class of this RecyclerFragment class
 *
 * A loading Fragment is included to present data loading progress.
 *
 * onCreateView inflates and initialize a uniform layout for all sub classes and get common view, fragment and
 * image option ready.
 * onActivityCreated get adapter and preferred grid column of sub classes to setup recycler view, and plugin the
 * loading fragment into view layout.
 *
 * startObserver is helper for sub classes to begin load its live data and start with according observer,
 * every sub classes MUST implement getLiveData() to load live data from its view model, and invoke this method
 * with its observer instance.
 */

abstract public class RecyclerFragment extends Fragment {
    private final static String TAG = RecyclerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private DisplayImageOptions mOptions;
    private LoadingFragment mLoadingFragment;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.theme_layout, null);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mOptions = ImageLoaderHelper.newOptions(getContext(), true, Bitmap.Config.ARGB_8888);
        mLoadingFragment = new LoadingFragment();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = instanceLayoutManager();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(instanceAdapter(mOptions));

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.container, mLoadingFragment);
        ft.commit();
    }

    protected RecyclerView.LayoutManager instanceLayoutManager() {
        return new StaggeredGridLayoutManager(getColumnCount(), GridLayoutManager.VERTICAL);
    }

    protected void startObserver(Observer observer) {
        try {
            LiveData liveData = getLiveData();
            liveData.observe(this, observer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            SLog.e(TAG, "unexpected exception: " + e.getMessage());
        }
    }

    protected final String updateActivityTitle() {
        Bundle bundle = getArguments();
        String title = bundle.getString("bundleName");
        if (!TextUtils.isEmpty(title)) {
            getActivity().setTitle(title);
        }
        return title;
    }

    protected abstract LiveData getLiveData();

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

    protected <T extends ViewModel> T getViewModel(Class<T> cls) throws IllegalArgumentException {
        return ViewModelFactory.from(this, cls);
    }
}
