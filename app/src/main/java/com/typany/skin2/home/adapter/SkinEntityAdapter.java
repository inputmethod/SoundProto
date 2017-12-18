package com.typany.skin2.home.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/15.
 */
abstract public class SkinEntityAdapter extends RecyclerView.Adapter<SkinEntityAdapter.ViewHolder> {
    private final DisplayImageOptions displayImageOptions;
    private List<SkinViewEntity> itemList;

    private int getLimitedDataSize() {
        if (null == itemList) {
            return 0;
        } else {
            return itemList.size();
        }
    }

    abstract protected ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions); /*{
        return new ViewHolder(view, displayImageOptions);
    }*/

    public SkinEntityAdapter(DisplayImageOptions options) {
        this.displayImageOptions = options;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutResourceId(), parent, false);
        final ViewHolder holder = newViewHolderInstance(view, displayImageOptions);
        return holder;
    }

    abstract protected @LayoutRes int getItemLayoutResourceId();

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SkinViewEntity viewEntity = itemList.get(position);
        holder.bind(viewEntity);

        if (isEntityClickable()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClicked(v.getContext(), viewEntity);
                }
            });
        }
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
                    return SkinEntityAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    // crash while getAsLiveData().data is null
                    // to be evaluate such methods.
                    return TextUtils.equals(SkinEntityAdapter.this.itemList.get(oldItemPosition).getBundleName(),
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

    // 默认的列表元素不响应点击事件的，当一个具体派生类不响应点击事件，可重载此方法并返回true并
    // 重载onItemViewClicked()实现具体的响应逻辑
    protected boolean isEntityClickable () {
        return false;
    }

    // 可接收点击事件的列表元素被点击时的回调，派生类实现具体响应逻辑，这个方法只有在方法isEntityClickable
    // 被重载并返回true时，才会被调用到.
    protected final void onItemViewClicked(Context context, SkinViewEntity viewEntity) {
        SkinEntityAdapterFactory.onItemClicked(context, viewEntity);
    }

    public int calculateSpanSize(int position, int totalSpanSize) {
        return SkinEntityAdapterFactory.calculateSpanSize(itemList.get(position), totalSpanSize);
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        private final DisplayImageOptions imageOptions;
//        private final ImageView previewImageView;

        public ViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView);
            imageOptions = options;
//            previewImageView = (ImageView) itemView.findViewById(R.id.iv_skin);
        }

        abstract protected void bind(SkinViewEntity viewEntity);

        protected void bindPreviewImageView(ImageView previewImageView, SkinViewEntity viewEntity) {
            if (SkinEntityAdapterFactory.isAdStub(viewEntity.getBundleName())) {
                // todo: replace possible ad view in the item view.
                previewImageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                ImageLoader.getInstance().displayImage(viewEntity.getPreviewUrl(), previewImageView, imageOptions);
            }
        }
    }
}
