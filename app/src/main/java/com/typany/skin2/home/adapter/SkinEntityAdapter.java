package com.typany.skin2.home.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinBundle;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/15.
 *
 * 皮肤首页，分类列表和皮肤列表页的adapter基类及它们相关的View Holder类
 *
 */
abstract public class SkinEntityAdapter extends RecyclerView.Adapter<SkinEntityAdapter.ViewHolder> {
    private static final String TAG = SkinEntityAdapter.class.getSimpleName();

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

    // 抽象的view holder, 只保存所有派生类都需要的图片加载选项
    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        private final DisplayImageOptions imageOptions;

        public ViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView);
            imageOptions = options;
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

        protected void fillSubList(RecyclerView recyclerView, SkinViewEntity entity) {
            SkinEntityAdapterFactory.fillSubList(recyclerView, entity, imageOptions);
        }
    }

    // 类别和皮肤都含有预览图片，类别直接使用这个view holder，皮肤再派生出类来管理
    // 更多的子views.
    static class PreviewViewHolder extends ViewHolder {
        private final ImageView previewImageView;
        public PreviewViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);
            previewImageView = itemView.findViewById(R.id.iv_skin);
        }

        @CallSuper
        public void bind(final SkinViewEntity viewEntity) {
            bindPreviewImageView(previewImageView, viewEntity);
        }

        public static int layoutResourceId() {
            return R.layout.item_skin_card;
        }
    }

    // 皮肤列表的View Holder
    static class BundleViewHolder extends PreviewViewHolder {
        private final LinearLayout indicatorView;
        public BundleViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);
            indicatorView = itemView.findViewById(R.id.indicator_group);
        }

        public void bind(final SkinViewEntity viewEntity) {
            super.bind(viewEntity);

            if (viewEntity instanceof SkinBundle) {
                SkinBundle bundle = (SkinBundle) viewEntity;
                if (bundle.getIconList().isEmpty()) {
                    indicatorView.setVisibility(View.VISIBLE);
                    // todo: add icon into the
                }
            } else {
                SLog.e(TAG, "The item type should be SkinBundle: " + viewEntity.getClass().getCanonicalName());
            }
        }
    }

    // 含标题和子列表的View Holder
    static class RecyclerViewHolder extends ViewHolder {
        private RecyclerView recyclerView;
        private View titleLayout;
        private TextView titleView;
        private TextView moreView;

        public RecyclerViewHolder(View itemView, DisplayImageOptions options) {
            super(itemView, options);

            titleLayout = itemView.findViewById(R.id.theme_title);
            recyclerView = itemView.findViewById(R.id.theme_content);
            titleView = titleLayout.findViewById(R.id.tv_title);
            moreView = titleLayout.findViewById(R.id.tv_more);
        }

        public void bind(final SkinViewEntity viewEntity) {
            if (TextUtils.isEmpty(viewEntity.getBundleName())) {
                titleLayout.setVisibility(View.GONE);
            } else {
                titleLayout.setVisibility(View.VISIBLE);
                titleView.setText(viewEntity.getBundleName());

                if (viewEntity.isHasMore()) {
                    moreView.setVisibility(View.VISIBLE);
                    moreView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SkinEntityAdapterFactory.onMoreItemClicked(v.getContext(), viewEntity);
                        }
                    });
                } else {
                    moreView.setVisibility(View.GONE);
                    moreView.setOnClickListener(null);
                }
            }

            fillSubList(recyclerView, viewEntity);
        }

        public static int layoutResourceId() {
            return R.layout.item_skin_home_card_view;
        }
    }
}
