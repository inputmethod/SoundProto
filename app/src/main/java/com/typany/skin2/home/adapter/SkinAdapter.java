package com.typany.skin2.home.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.home.view.SkinViewEntityCardView;
import com.typany.soundproto.R;

import java.util.List;

/**
 * Created by yangfeng on 2017/10/17.
 */

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {
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

    public SkinAdapter(DisplayImageOptions options) {
        this.displayImageOptions = options;
    }

//    @Override
//    public int getItemViewType(int position) {
//        SkinViewEntity item = itemList.get(position);
//        if (item instanceof SkinCategory) {
//            return ITEM_TYPE_CONTENT;
//        } else {
//            // todo: parse more type.
//            return ITEM_TYPE_TITLE;
//        }
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skin_view_entity_card_view, parent, false);
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
                    return TextUtils.equals(SkinAdapter.this.itemList.get(oldItemPosition).getBundleName(),
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
//        private final ImageView previewImageView;
//        private final LinearLayout indicatorView;

        public ViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView);
            SkinViewEntityCardView.attach(itemView, options);
//            previewImageView = (ImageView) itemView.findViewById(R.id.iv_skin);
//            indicatorView = (LinearLayout) itemView.findViewById(R.id.indicator_group);
        }

        public void bind(final SkinViewEntity viewEntity) {
            if (itemView instanceof SkinViewEntityCardView) {
                ((SkinViewEntityCardView) itemView).setItem(viewEntity);
            }
//            ImageLoader.getInstance().displayImage(viewEntity.getPreviewUrl(), previewImageView, imageOptions);
        }
    }

//    // todo: return layout resource id by view type.
//    private @LayoutRes int getItemLayoutRes(int viewType) {
//        return R.layout.item_skin_view_entity_card_view;
////        if (ITEM_TYPE_CONTENT == viewType) {
////            return R.layout.item_skin_card;
////        } else {
////            return R.layout.
////        }
//    }
}
