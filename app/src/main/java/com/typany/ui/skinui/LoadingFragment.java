package com.typany.ui.skinui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.typany.debug.SLog;
import com.typany.soundproto.R;

/**
 * Created by sunhang on 16-1-11.
 */
public class LoadingFragment extends Fragment {
    private ImageView mImageView;
//    private TextView mTextView;
    private boolean mShow;
    private boolean mShowMask = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = View.inflate(getActivity(), R.layout.skin_loading, null);
        ImageView iv = (ImageView) root.findViewById(R.id.iv);
//        TextView tv = (TextView) root.findViewById(R.id.tv);
        Glide.with(getActivity())
                .load(R.mipmap.loading_animation)
                .priority(Priority.HIGH)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Integer, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception arg0, Integer arg1,
                                               Target<GlideDrawable> arg2, boolean arg3) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   Integer model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(iv);

        mImageView = iv;
//        mTextView = tv;
//        mTextView.setText(R.string.wait_changing_skin);
//        mTextView.setVisibility(View.GONE);

        root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                return;
            }
        });

//        if (getActivity() instanceof OnLineSkinActivity) {
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
//            lp.setMargins(0,
//                    - getResources().getDimensionPixelSize(R.dimen.toolbar_elevation),
//                    0, 0);
//        }
        setRetainInstance(false);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, -1);
        root.setLayoutParams(lp);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean show = bundle.getBoolean("show");
            if (show) {
                mShow = true;
            }
        }

        if (mShow) {
            if (mShowMask) {
                show(true);
            } else {
                show();
            }
        } else {
            hide();
        }
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * fix : http://10.134.74.226:880/browse/GIME-1894
     * @param top
     */
    public void patch(int top) {
        View v = getView();
        if (v == null) {
            return;
        }

        v.setPadding(0, top, 0, 0);
    }

    public void show() {
        show(false);
    }

    public void show(String title, boolean mask) {
//        mTextView.setText(title);
        show(mask);
    }

    public void show(boolean showMask) {
        mShow = true;
        mShowMask = showMask;

        SLog.d("Loading", "show showMask " + showMask);
        // Sometimes the method "isAdded" return true, but "getView" return null.
        // So we need use both of theme to judge.
        if (isAdded() == false || getView() == null) {
            return;
        }

        if (getView().getParent() != null) {
            getView().getParent().bringChildToFront(getView());
        }
        Drawable drawable = ((ImageView) mImageView).getDrawable();
        if (drawable instanceof GifDrawable) {
            GifDrawable animatable = (GifDrawable) drawable;
            if (animatable.isRunning()) {
//                animatable.stop();
            } else {
                animatable.start();
            }
        }
        Glide.with(getActivity())
                .load(R.mipmap.loading_animation)
                .asGif()
                .placeholder(mImageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mImageView);
        mImageView.setVisibility(View.VISIBLE);
        getView().setVisibility(View.VISIBLE);

//        ((AnimationDrawable) mImageView.getDrawable()).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ((AnimationDrawable) mImageView.getDrawable()).start();
//            }
//        });


        if (showMask) {
            getView().findViewById(R.id.rl).setBackgroundResource(R.drawable.skin_loading_bkg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getView().findViewById(R.id.rl).setElevation(getResources().getDisplayMetrics().density * 24 + 0.5f);
            }
            getView().setBackgroundColor(Color.parseColor("#44000000"));
        } else {
            getView().findViewById(R.id.rl).setBackgroundColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getView().findViewById(R.id.rl).setElevation(0);
            }
            getView().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void hide() {
        mShow = false;
        try {
//            mTextView.setText(R.string.wait_changing_skin);
            SLog.i("Loading", "show hide ");
            if (isAdded() == false || getView() == null) {
                return;
            }
            Drawable drawable = ((ImageView) mImageView).getDrawable();
            if (drawable instanceof GifDrawable) {
                GifDrawable animatable = (GifDrawable) drawable;
                if (animatable.isRunning()) {
                    animatable.stop();
//                } else {
//                    animatable.start();
                }
            }
//            ((AnimationDrawable) mImageView.getDrawable()).stop();
            mImageView.setVisibility(View.INVISIBLE);
            getView().setVisibility(View.INVISIBLE);
        }catch (Exception e){
        }
    }

    public static class NullLoadingFragment extends LoadingFragment {
        @Override
        public void show() {
        }

        @Override
        public void hide() {
        }
    }
}
