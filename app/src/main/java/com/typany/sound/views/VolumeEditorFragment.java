package com.typany.sound.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.typany.debug.SLog;
import com.typany.settings.RunningStatus;
import com.typany.soundproto.R;
import com.typany.utilities.CommonUtils;
import com.typany.utilities.CompatibilityUtils;

/**
 * Created by sunhang on 16-1-11.
 */
public class VolumeEditorFragment extends Fragment {
    private View container;
    private View controller;
    private boolean mIsShow;
    private String activityName;
    private View mActivityView;
    private EditorActionListener mEditorActionListener;

    public void setActivityName(String name) {
        activityName = name;
    }


    public void setActivityView(View view) {
        mActivityView = view;
    }

    public void setEditorActionListener(EditorActionListener l) {
        mEditorActionListener = l;
    }

    private SoundVolumeWrapper volumeWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = View.inflate(getActivity(), R.layout.sound_volume_edit_frgment, null);

        CompatibilityUtils.callViewSetBackground(root.findViewById(R.id.et), new EditBkgDrawable(getContext()));


        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        styledAttributes.recycle();

        this.container = root.findViewById(R.id.container);
        this.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RunningStatus.getInstance().ismChangeLanguageDialog()) {
                    hide();
                }
            }
        });

        controller = root.findViewById(R.id.rl);

        final DetectorView detectorView = new DetectorView(getContext());
        detectorView.setLayoutListener(new LayoutListener() {
            @Override
            public void onKeyboardHided() {
                if (mIsShow) {
                    if (!RunningStatus.getInstance().ismChangeLanguageDialog()) {
                        hide();
                    }
                }
            }
        });
        ((ViewGroup) root.findViewById(R.id.rl_detect)).addView(detectorView, -1, -1);

        EditText et = (EditText) root.findViewById(R.id.et);
        et.setTextIsSelectable(false);
        et.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        et.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        et.setFocusable(false);

        setRetainInstance(false);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (container.getVisibility() == View.VISIBLE) {
            hide();
        }
    }

    public boolean isShow() {
        return mIsShow;
    }

    public void show() {
        mIsShow = true;

        if (mEditorActionListener != null) {
            mEditorActionListener.onShow();
        }

        if (getView() == null) return;
        final EditText et = (EditText) getView().findViewById(R.id.et);
        et.setText("");
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);

        if (null == volumeWrapper) {
            volumeWrapper = new SoundVolumeWrapper(controller);
        }

        volumeWrapper.refreshUi();

        et.requestFocus();

        if (mIsShow) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(getView().findViewById(R.id.et), InputMethodManager.SHOW_FORCED);
        }

        container.setVisibility(View.VISIBLE);
        controller.setVisibility(View.VISIBLE);
    }


    public void hide() {
        mIsShow = false;

        container.setVisibility(View.INVISIBLE);
        controller.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getView().findViewById(R.id.et).getApplicationWindowToken(), 0);
        }


        if (getView() != null) {
            getView().findViewById(R.id.et).clearFocus();
            getView().findViewById(R.id.et).setFocusable(false);
        }

        if (mEditorActionListener != null) {
            mEditorActionListener.onHide();
        }

//        if (!SkinConstants.isBuildInTheme && SkinConstants.getCurrentSkinName() != null && !SkinConstants.getCurrentSkinName().contains("custom")) {
//            RateContext.getInstance().setThemeUsingTimes(1);
//        }

        RunningStatus.getInstance().setEditorShown(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final EditText et = (EditText) getView().findViewById(R.id.et);
        et.setCustomSelectionActionModeCallback(null);
    }

    public void refreshUi() {
        if (null != volumeWrapper) {
            volumeWrapper.refreshUi();
        }
    }

    private class DetectorView extends View {
        private LayoutListener listener;
        private int lastHeight;

        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        public DetectorView(Context context) {
            super(context);

            // init
            lastHeight = context.getResources().getDisplayMetrics().heightPixels - getStatusBarHeight();
        }

        void setLayoutListener(LayoutListener l) {
            listener = l;
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);

            if (!changed) {
                return;
            }

            if (mActivityView != null && mActivityView.getFitsSystemWindows() == false) {

                if (mActivityView.getHeight() - lastHeight > 200 * getResources().getDisplayMetrics().density) {
                    if (listener != null) {
                        listener.onKeyboardHided();
                    }
                }

                lastHeight = mActivityView.getHeight();
            } else {
                int height = bottom - top;
                if (height - lastHeight > 200 * getResources().getDisplayMetrics().density) {
                    if (listener != null) {
                        listener.onKeyboardHided();
                    }
                }

                SLog.i(CommonUtils.TAG_SUN, "EditorFragment onLayout 2 >> " + height + " " + lastHeight);

                lastHeight = height;
            }
        }

    }

    interface LayoutListener {
        void onKeyboardHided();
    }

    public interface EditorActionListener {
        void onShow();

        void onHide();
    }

    private static class EditBkgDrawable extends Drawable {
        private Paint paint = new Paint();
        private RectF rectF = new RectF();
        private float radius;

        public EditBkgDrawable(Context context) {
            radius = context.getResources().getDisplayMetrics().density * 4;
            paint.setColor(0x33000000);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect bounds = getBounds();
            rectF.left = rectF.top = 0;
            rectF.right = bounds.right;
            rectF.bottom = bounds.bottom;
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }

        @Override
        public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {}

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {}

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }
}
