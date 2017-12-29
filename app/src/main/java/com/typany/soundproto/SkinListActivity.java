package com.typany.soundproto;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.typany.api.skin.SkinItem;
import com.typany.model.SkinViewModel;
import com.typany.skin.SkinPagedListAdapter;


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
public class SkinListActivity extends AppCompatActivity {
    private SkinViewModel viewModel;
    
    private RecyclerView searchResultsRecyclerView;
//    private AppBarLayout appBarView;
    
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_list);
        
        viewModel = ViewModelProviders.of(this).get(SkinViewModel.class);
        
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
//        appBarView = findViewById(R.id.appBarView);

//        initSearchField();
        initPagedListAdapter();
    }

    private void initPagedListAdapter() {
        final SkinPagedListAdapter pagedListAdapter = new SkinPagedListAdapter();
        searchResultsRecyclerView.setAdapter(pagedListAdapter);
        viewModel.allSkinList().observe(this, new SkinPagedListObserver(pagedListAdapter));
    }

    private void initSearchField() {
//        searchStartView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                 initPagedListAdapter();
//            }
//        });
//        appBarView.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
//                searchContainerView.setTranslationY(verticalOffset);
//            }
//        });
    }

    private final static class SkinPagedListObserver implements Observer<PagedList<SkinItem>> {
        private SkinPagedListAdapter pagedListAdapter;
        public SkinPagedListObserver(SkinPagedListAdapter pagedListAdapter) {
            this.pagedListAdapter = pagedListAdapter;
        }

        @Override
        public void onChanged(@Nullable final PagedList<SkinItem> skinItems) {
            pagedListAdapter.setList(skinItems);
        }
    }

}
