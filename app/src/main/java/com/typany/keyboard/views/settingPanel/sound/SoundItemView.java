package com.typany.keyboard.views.settingPanel.sound;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typany.base.lifecycle.ProcessScopeViewModelProviders;
import com.typany.ime.IMEApplicationContext;
import com.typany.network.StatefulResource;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.service.SoundBundle;
import com.typany.sound.viewmodel.SoundViewModel;
import com.typany.soundproto.MainActivity;
import com.typany.soundproto.R;


/**
 * Created by yangfeng on 2017/9/23.
 */

public class SoundItemView extends RelativeLayout {
    private SoundBoundItem item;

    private Drawable mSeledImage4_4 = null;
    private Drawable mSeledImageCheck4_4 = null;

    private SoundViewModel soundViewModel;

    private Observer<StatefulResource<SoundBundle>> observer = new Observer<StatefulResource<SoundBundle>>() {
        @Override
        public void onChanged(@Nullable StatefulResource<SoundBundle> soundBundleStatefulResource) {
            switch (soundBundleStatefulResource.status) {
                case LOADING:
                    updateDownloadingProgress(soundBundleStatefulResource.progress);
                    break;
                case SUCCESS:
                    onViewDataChanged(soundBundleStatefulResource.data);
                    break;
                case ERROR:
                    // TODO
                    break;
            }
        }
    };

    private void onViewDataChanged(SoundBundle data) {
        drawSoundBundle(data);

        if (data.getSelect()) {
            boolean lastClickedItem = soundViewModel.updateSelectedItem(item);
            if (lastClickedItem) {
                playSoundBundle(data);
            }
        }

        setSelected(data.getSelect());
    }

    protected void drawSoundBundle(SoundBundle item) {
        final String url;
        if (null == item) {
            url = moreUrl;
        } else {
            setSelected(item.getSelect());
            url = item.previewUrl();
        }
        drawSoundBundleIcon(url);
    }

    protected final void drawSoundBundleIcon(String url) {
        ImageLoader.getInstance().displayImage(url, previewImageView, imageOptions);
    }

    private void playSoundBundle(SoundBundle item) {
        soundViewModel.clearLastClickedItem();
        Toast.makeText(getContext(), "playSoundBundle, " + item.bundleName(), Toast.LENGTH_SHORT).show();
    }

    // remove observer getViewModel any existing item
    // check to construct fake data for 'More' item
    // apply real sound item

    // todo: apply icon drawable with skin.
    private String moreUrl = "drawable://" + R.drawable.sound_icon_more_white;

    public void setItem(final SoundBoundItem item) {
        if (null != this.item) {
            this.item.getAsLiveData().removeObserver(observer);
        }

        this.item = item;
        if (null == item) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMoreSoundListActivity();
                }
            });
        } else {
            this.item.getAsLiveData().observeForever(observer);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击之后，model数据更新，然后会触发observer的重绘，播放预览音
                    soundViewModel.clickSoundItem(item);
                }
            });
        }
    }

    private void startMoreSoundListActivity() {
        Context context = getContext();
        context.startActivity(new Intent(context, MainActivity.class)
                .putExtra("page_index", 3)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public SoundItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        mSeledImage4_4 = ContextCompat.getDrawable(getContext(), R.drawable.sound_item_selected_white);
//        mSeledImageCheck4_4 = ContextCompat.getDrawable(getContext(), R.drawable.sound_item_selected_white);

        soundViewModel = ProcessScopeViewModelProviders.of(IMEApplicationContext.application).get(SoundViewModel.class);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        parseHolderViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = width; // Math.round(width * 0.7128514056224899f);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!changed) {
            return;
        }
    }


    public void clearColorFilter() {
        if (null != mSeledImageCheck4_4) {
            mSeledImageCheck4_4.clearColorFilter();
        }
    }

    public void setColorFilter(int color, PorterDuff.Mode srcIn) {
        if (null != mSeledImageCheck4_4) {
            mSeledImageCheck4_4.setColorFilter(color, srcIn);
        }
    }

    public void setRoundRectImageResource(int id) {
        if (id > 0) {
            mSeledImage4_4 = ContextCompat.getDrawable(getContext(), id);
        } else {
            mSeledImage4_4 = null;
        }
    }
    public void setIndicatorImageResource(int id) {
        if (id > 0) {
            mSeledImageCheck4_4 = ContextCompat.getDrawable(getContext(), id);
        } else {
            mSeledImageCheck4_4 = null;
        }
    }


    private DisplayImageOptions imageOptions;
    private ImageView previewImageView;
    private void attach(DisplayImageOptions options) {
        imageOptions = options;
    }

    public static void attach(View itemView, DisplayImageOptions options) {
        if (itemView instanceof SoundItemView) {
            SoundItemView view = (SoundItemView) itemView;
            view.attach(options);
        } else {
            throw new IllegalArgumentException("Item view need to be instance of SoundItemView, " + itemView.getClass().getSimpleName());
        }
    }

    protected void parseHolderViews() {
        previewImageView = (ImageView) findViewById(R.id.iv_skin);
    }

    // draw downloading or update progress
    protected void updateDownloadingProgress(int progress) {
    }
}
