package com.typany.skin2.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * Created by yangfeng on 2017/9/23.
 *
 * 皮肤首页的各组视图，如collection, categories, feature, trending, all theme, 每组是一个SkinHomeCardView,
 * 可显示一条标题栏加More按钮和一个skin和skin类组元素，当列表某个元素的名称是特定的字符时，可表示该元素为
 * 广告占位符，在相应视图插入显示广告。
 */

public class SkinHomeCardView extends RelativeLayout {
    private static final String TAG = SkinHomeCardView.class.getSimpleName();

    public SkinHomeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int width = MeasureSpec.getSize(widthMeasureSpec);
//        final int height = width; // Math.round(width * 0.7128514056224899f);
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!changed) {
            return;
        }
    }
}
