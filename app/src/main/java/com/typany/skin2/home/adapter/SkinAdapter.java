package com.typany.skin2.home.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.keyboard.views.settingPanel.sound.SoundItemView;
import com.typany.sound.service.SoundBoundItem;
import com.typany.soundproto.R;

import java.util.List;

/**
 * Created by yangfeng on 2017/10/17.
 */

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {
    private final boolean limit;
    private final @LayoutRes int layoutId;

    private final DisplayImageOptions displayImageOptions;

    private List<SoundBoundItem> itemList;

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

    private ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new ViewHolder(view, displayImageOptions);
    }

    public SkinAdapter(DisplayImageOptions options, boolean limit) {
        this.displayImageOptions = options;
        this.limit = limit;
        layoutId = limit ? R.layout.item_sound_card_limit : R.layout.item_sound_card_full;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        final ViewHolder holder = newViewHolderInstance(view, displayImageOptions);
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
                    return SkinAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    // crash while getAsLiveData().data is null
                    // to be evaluate such methods.
                    return TextUtils.equals(SkinAdapter.this.itemList.get(oldItemPosition).getAsLiveData().getValue().data.bundleName(),
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView);

            SoundItemView.attach(itemView, options);
        }

        public void bind(final SoundBoundItem soundBoundItem) {
            if (itemView instanceof SoundItemView) {
                ((SoundItemView) itemView).setItem(soundBoundItem);
            }
        }
    }
}
