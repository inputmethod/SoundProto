package com.typany.soundproto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.typany.api.skin.SkinItem;

/**
 * This file is part of Everboxing modules. <br/><br/>
 * <p>
 * The MIT License (MIT) <br/><br/>
 * <p>
 * Copyright (c) 2017 Malyshev Yegor aka brainail at org.brainail.everboxing@gmail.com <br/><br/>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy <br/>
 * of this software and associated documentation files (the "Software"), to deal <br/>
 * in the Software without restriction, including without limitation the rights <br/>
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell <br/>
 * copies of the Software, and to permit persons to whom the Software is <br/>
 * furnished to do so, subject to the following conditions: <br/><br/>
 * <p>
 * The above copyright notice and this permission notice shall be included in <br/>
 * all copies or substantial portions of the Software. <br/><br/>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR <br/>
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, <br/>
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE <br/>
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER <br/>
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, <br/>
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN <br/>
 * THE SOFTWARE.
 */
public class SkinViewHolder extends RecyclerView.ViewHolder {
    private TextView itemTypeView;
    private TextView itemNameView;
    private ImageView itemCoverView;
    
    public SkinViewHolder(final ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.skin_item, parent, false));
    }
    
    private SkinViewHolder(final View itemView) {
        super(itemView);
        itemTypeView = itemView.findViewById(R.id.itemTypeView);
        itemNameView = itemView.findViewById(R.id.itemNameView);
        itemCoverView = itemView.findViewById(R.id.itemCoverView);
    }
    
    private SkinItem item;
    
    public void bindTo(SkinItem item) {
        this.item = item;
        if (null != item) {
            itemTypeView.setText(item.type);
        } else {
            itemTypeView.setText("Ouhh...");
        }

        if (null != item) {
            itemNameView.setText(item.id);
        } else {
            itemNameView.setText("Ouhhhhhhhh...");
        }

        if (null != item && null != item.ppu) {
            itemCoverView.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext())
                    .load(item.ppu)
                    .apply(new RequestOptions().placeholder(R.drawable.empty_placeholder))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemCoverView);
        } else {
            Glide.with(itemView.getContext()).clear(itemCoverView);
            itemCoverView.setImageResource(R.drawable.empty_placeholder);
        }
    }
}
