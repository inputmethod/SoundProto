package com.typany.sound.adapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typany.network.StatefulResource;
import com.typany.sound.callback.SoundItemClickCallback;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.service.SoundBundle;
import com.typany.sound.views.ProgressBarView;
import com.typany.soundproto.MainActivity;
import com.typany.soundproto.R;
import com.typany.ui.sticker.DictWidgets;

import java.util.List;

/**
 * Created by yangfeng on 2017/10/17.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {
    private final boolean limit;

    private final DisplayImageOptions displayImageOptions;
    private SoundItemClickCallback soundItemClickCallback;

    private List<SoundBoundItem> itemList;

    private @LayoutRes int getItemLayoutId() {
        return limit ? R.layout.item_sound_card_limit : R.layout.item_sound_card_full;
    }

    private int getLimitedDataSize() {
        if (null == itemList || itemList.isEmpty()) {
            return 0;
        } else {
            if (limit) {
                return Math.min(itemList.size() + 1, 8);
            } else {
                return itemList.size();
            }
        }
    }

    private boolean isLastMoreItem(int position) {
        if (limit && position == getItemCount() - 1) {
            return true;
        } else {
            return false;
        }
    }

    private ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions, SoundItemClickCallback soundItemClickCallback) {
        if (limit) {
            return new ViewHolderLimit(view, displayImageOptions, soundItemClickCallback);
        } else {
            return new ViewHolderFull(view, displayImageOptions, soundItemClickCallback);
        }
    }

    public SoundAdapter(DisplayImageOptions options, SoundItemClickCallback soundItemClickCallback, boolean limit) {
        this.displayImageOptions = options;
        this.soundItemClickCallback = soundItemClickCallback;
        this.limit = limit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false);
        final ViewHolder holder = newViewHolderInstance(view, displayImageOptions, soundItemClickCallback);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isLastMoreItem(position)) {
            holder.bind(null);
        } else {
            holder.bind(itemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return getLimitedDataSize();
    }

    public void setSoundItemList(final List<SoundBoundItem> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return SoundAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    // crash while getAsLiveData().data is null
                    // to be evaluate such methods.
                    return TextUtils.equals(SoundAdapter.this.itemList.get(oldItemPosition).getAsLiveData().getValue().data.bundleName(),
                            itemList.get(newItemPosition).getAsLiveData().getValue().data.bundleName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    // TODO
                    return false;
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private final DisplayImageOptions imageOptions;
        private final SoundItemClickCallback soundItemClickCallback;
        private final ImageView previewImageView;
        private final LifecycleRegistry lifecycleRegistry;

        public ViewHolder(View itemView, DisplayImageOptions options, SoundItemClickCallback clickCallback) {
            super(itemView);
            previewImageView = (ImageView) itemView.findViewById(R.id.iv_skin);
            this.imageOptions = options;
            this.soundItemClickCallback = clickCallback;
            this.lifecycleRegistry = new LifecycleRegistry(this);
        }

        @Override
        public LifecycleRegistry getLifecycle() {
            return lifecycleRegistry;
        }

        public void bind(final SoundBoundItem soundBoundItem) {
            if (null == soundBoundItem) {
                throw new IllegalArgumentException("unexpected data binding, null");
            }


            itemView.setTag(R.id.skin_item_tag_key0, this);
            itemView.setTag(soundBoundItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SoundBoundItem item = (SoundBoundItem)view.getTag();
                    soundItemClickCallback.onClick(item);
                }
            });

            lifecycleRegistry.markState(Lifecycle.State.STARTED);
            soundBoundItem.getAsLiveData().observe(this, new Observer<StatefulResource<SoundBundle>>() {
                @Override
                public void onChanged(@Nullable StatefulResource<SoundBundle> soundBundleStatefulResource) {
                    SoundBundle item = soundBundleStatefulResource.data;
                    switch (soundBundleStatefulResource.status) {
                        case LOADING:
                            // draw downloading or update progress
                            int progress = soundBundleStatefulResource.progress;
                            // updateDownloadingProgress(progress);
                            break;
                        case SUCCESS:
                            switch (item.getStatus()) {
                                case INFO_LOADED:
                                    // TODO draw preview url
                                    drawSoundBundle(item);
                                    break;
                                case DATA_LOAED:
                                    // TODO download finished
                                    // playSoundBundle(item);
                                    break;
                            }
                            break;
                        case ERROR:
                            // TODO
                            break;
                    }
                }
            });

            SoundBundle item = soundBoundItem.getAsLiveData().getValue().data;
            drawSoundBundle(item);
        }

        protected void drawSoundBundle(SoundBundle item) {
            if (null == item) {
                return;
            }

            itemView.setSelected(item.getSelect());
            drawSoundBundleIcon(item.previewUrl());
        }

        protected final void drawSoundBundleIcon(String url) {
            ImageLoader.getInstance().displayImage(url, previewImageView, imageOptions);
        }
    }

    static class ViewHolderLimit extends ViewHolder {
        public ViewHolderLimit(View itemView, DisplayImageOptions options, SoundItemClickCallback clickCallback) {
            super(itemView, options, clickCallback);
        }

        @Override
        public void bind(final SoundBoundItem soundBoundItem) {
            if (null == soundBoundItem) {
                bindMoreOption();
            } else {
                super.bind(soundBoundItem);
            }
        }

        private void bindMoreOption() {
            // todo: apply icon drawable with skin.
            drawSoundBundleIcon("drawable://" + R.drawable.sound_icon_more_white);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, MainActivity.class)
                            .putExtra("page_index", 3)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }

    static class ViewHolderFull extends ViewHolder {
        private View downloadingMask;
        private View remoteIndicator;
        private ProgressBarView progressBarView;

        public ViewHolderFull(View itemView, DisplayImageOptions options, SoundItemClickCallback clickCallback) {
            super(itemView, options, clickCallback);
            downloadingMask = itemView.findViewById(R.id.downloading_layout);
            progressBarView = (ProgressBarView) itemView.findViewById(R.id.view);
            remoteIndicator = itemView.findViewById(R.id.sound_remote_indicator);
        }

        @Override
        protected void drawSoundBundle(SoundBundle item) {
            super.drawSoundBundle(item);

            remoteIndicator.setVisibility(isRemotePreviewUrl(item.previewUrl()) ? View.VISIBLE : View.GONE);
        }

        private boolean isRemotePreviewUrl(String url) {
            return null != url && url.startsWith("http://");
        }

        public void startLoading(SoundBundle model) {
            downloadingMask.setVisibility(View.VISIBLE);
            remoteIndicator.setVisibility(View.GONE);
        }

        public void updateProgress(int schedule) {
            progressBarView.setCurrentProgress(schedule);
        }

        public void onDownloadCompleted() {
            downloadingMask.setVisibility(View.GONE);
        }

        public void updateProgressState(int state) {
            if (state == DictWidgets.STATUS_DOWNLOADING) {
                downloadingMask.setVisibility(View.VISIBLE);
            } else {
                downloadingMask.setVisibility(View.GONE);
            }
        }

        public void onDownloadFailed() {
            downloadingMask.setVisibility(View.GONE);
            remoteIndicator.setVisibility(View.VISIBLE);
        }
    }
}
