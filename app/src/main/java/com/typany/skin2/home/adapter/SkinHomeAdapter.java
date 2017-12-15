package com.typany.skin2.home.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.home.view.SkinHomeCardView;
import com.typany.soundproto.R;

import java.util.List;

/**
 * Created by yangfeng on 2017/10/17.
 */

public class SkinHomeAdapter extends RecyclerView.Adapter<SkinHomeAdapter.ViewHolder> {
    private static final String TAG = SkinHomeAdapter.class.getSimpleName();

    private final DisplayImageOptions displayImageOptions;

    private List<SkinViewEntity> itemList;

    private int getLimitedDataSize() {
        if (null == itemList || itemList.isEmpty()) {
            return 0;
        } else {
            return itemList.size();
        }
    }

    private ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
        return new ViewHolder(view, displayImageOptions);
    }

    public SkinHomeAdapter(DisplayImageOptions options) {
        this.displayImageOptions = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skin_home_card_view, parent, false);
        final ViewHolder holder = newViewHolderInstance(view, displayImageOptions);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return getLimitedDataSize();
    }

    public void setSkinItemList(final List<SkinViewEntity> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return SkinHomeAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TextUtils.equals(SkinHomeAdapter.this.itemList.get(oldItemPosition).getBundleName(),
                            itemList.get(newItemPosition).getBundleName());
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
        private final DisplayImageOptions imageOptions;
        public ViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView);
            this.imageOptions = options;
        }

        public void bind(final SkinViewEntity viewEntity) {
            if (itemView instanceof SkinHomeCardView) {
                ((SkinHomeCardView) itemView).setItem(viewEntity, imageOptions);
            } else {
                SLog.e(TAG, "ViewHolder bind item with unexpected type: " + itemView.getClass().getCanonicalName());
            }
        }
    }
}
