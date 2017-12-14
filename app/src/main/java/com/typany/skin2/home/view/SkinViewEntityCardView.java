package com.typany.skin2.home.view;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.typany.debug.SLog;
import com.typany.skin2.home.model.SkinBundle;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.soundproto.R;

import java.util.List;


/**
 * Created by yangfeng on 2017/9/23.
 */

public class SkinViewEntityCardView extends RelativeLayout {
    private static final String TAG = SkinViewEntityCardView.class.getSimpleName();

    private SkinViewEntity itemData = null;

    public SkinViewEntityCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    public void setItem(SkinViewEntity entity) {
        // todo: render entity data to views.
        if (entity != itemData) {
            checkAndChangeLayout(entity);
            itemData = entity;
        }
    }

    private void checkAndChangeLayout(SkinViewEntity entity) {
        if (TextUtils.isEmpty(entity.getBundleName()) && !entity.isHasMore()) {
            titleLayout.setVisibility(GONE);
        } else {
            titleLayout.setVisibility(VISIBLE);
            titleView.setText(entity.getBundleName());
            moreView.setVisibility(entity.isHasMore() ? VISIBLE : GONE);
        }

        final List<SkinViewEntity> entityList = entity.getBundleList();
        if (entityList.isEmpty()) {
            recyclerView.setVisibility(GONE);
            if (isAdStub(entity.getBundleName())) {
                // todo: setup for AD stub here.
                // 广告点位，通过固定的名字
            }
        } else {
            recyclerView.setVisibility(VISIBLE);
            int displayColumn = entity.getDisplayColumn();
            if (displayColumn > 0) {
                ContentItemAdapter adapter = new ContentItemAdapter(imageOptions);
                recyclerView.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), entity.getDisplayColumn());
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (isAdStub(entityList.get(position).getBundleName())) {
                            return 2;
                        } else {
                            return 1;
                        }
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                adapter.setSkinItemList(entityList);
            } else if (displayColumn == 0){
                // todo: 显示单排小圆风格的图标
                SLog.e(TAG, "todo: display small circle icon, e.g. for collection");
            } else {
                // todo: 异常？不识别的数据集合
                SLog.e(TAG, "Unknown entity: " + entity.getBundleName() + ", " + displayColumn);
            }
        }
    }

    private static boolean isAdStub(String bundleName) {
        return TextUtils.equals(bundleName, "ad_top_name") || TextUtils.equals(bundleName, "ad_name");
    }

    private DisplayImageOptions imageOptions;
//    private ImageView previewImageView;
    private RecyclerView recyclerView;
    private View titleLayout;
    private TextView titleView;
    private TextView moreView;

    private void attach(DisplayImageOptions options) {
        imageOptions = options;
    }

    public static void attach(View itemView, DisplayImageOptions options) {
        if (itemView instanceof SkinViewEntityCardView) {
            SkinViewEntityCardView view = (SkinViewEntityCardView) itemView;
            view.attach(options);
        } else {
            throw new IllegalArgumentException("Item view need to be instance of SkinViewEntityCardView, " + itemView.getClass().getSimpleName());
        }
    }

    protected void parseHolderViews() {
//        previewImageView = (ImageView) findViewById(R.id.iv_skin);
        titleLayout = findViewById(R.id.theme_title);
        recyclerView = (RecyclerView) findViewById(R.id.theme_content);
        titleView = (TextView) titleLayout.findViewById(R.id.tv_title);
        moreView = (TextView) titleLayout.findViewById(R.id.tv_more);
    }

    private static class ContentItemAdapter extends RecyclerView.Adapter<ContentItemAdapter.ViewHolder> {
        private final DisplayImageOptions displayImageOptions;

        private List<SkinViewEntity> itemList;

        private int getLimitedDataSize() {
            if (null == itemList || itemList.isEmpty()) {
                return 0;
            } else {
                return itemList.size();
            }
        }

        private ContentItemAdapter.ViewHolder newViewHolderInstance(View view, DisplayImageOptions displayImageOptions) {
            return new ContentItemAdapter.ViewHolder(view, displayImageOptions);
        }

        public ContentItemAdapter(DisplayImageOptions options) {
            this.displayImageOptions = options;
        }

        @Override
        public ContentItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skin_card, parent, false);
            final ContentItemAdapter.ViewHolder holder = newViewHolderInstance(view, displayImageOptions);
            return holder;
        }

        @Override
        public void onBindViewHolder(ContentItemAdapter.ViewHolder holder, int position) {
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
                        return ContentItemAdapter.this.itemList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return itemList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        // crash while getAsLiveData().data is null
                        // to be evaluate such methods.
                        return TextUtils.equals(ContentItemAdapter.this.itemList.get(oldItemPosition).getBundleName(),
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
            private final ImageView previewImageView;
            private final LinearLayout indicatorView;

            public ViewHolder(View itemView, DisplayImageOptions options) {
                super(itemView);
                imageOptions = options;
                previewImageView = (ImageView) itemView.findViewById(R.id.iv_skin);
                indicatorView = (LinearLayout) itemView.findViewById(R.id.indicator_group);
            }

            public void bind(final SkinViewEntity viewEntity) {
                if (isAdStub(viewEntity.getBundleName())) {
                    previewImageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    ImageLoader.getInstance().displayImage(viewEntity.getPreviewUrl(), previewImageView, imageOptions);
                }

                if (viewEntity instanceof SkinBundle) {
                    SkinBundle bundle = (SkinBundle) viewEntity;
                    if (bundle.getIconList().isEmpty()) {
                        indicatorView.setVisibility(VISIBLE);
                        // todo: add icon into the
                    }
                }
            }
        }
    }
}
