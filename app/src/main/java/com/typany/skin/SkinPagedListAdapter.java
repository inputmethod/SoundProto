package com.typany.skin;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.view.ViewGroup;

import com.typany.api.skin.SkinItem;
import com.typany.soundproto.SkinViewHolder;


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
public class SkinPagedListAdapter extends PagedListAdapter<SkinItem, SkinViewHolder> {
    private static final DiffCallback<SkinItem> diffCallback = new DiffCallback<SkinItem>() {
        public boolean areItemsTheSame(@NonNull SkinItem oldItem, @NonNull SkinItem newItem) {
            return oldItem.id == newItem.id;
        }
        
        public boolean areContentsTheSame(@NonNull SkinItem oldItem, @NonNull SkinItem newItem) {
            return oldItem.equals(newItem);
        }
    };
    
    public SkinPagedListAdapter() {
        super(diffCallback);
    }
    
    public void onBindViewHolder(SkinViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }
    
    public SkinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkinViewHolder(parent);
    }
}
