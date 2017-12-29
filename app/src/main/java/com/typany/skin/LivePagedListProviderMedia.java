package com.typany.skin;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.typany.api.skin.SkinItem;
import com.typany.api.skin.SkinRepositoryImpl;

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
public class LivePagedListProviderMedia {
    private static final int PAGED_LIST_PAGE_SIZE = 20;
    private static final boolean PAGED_LIST_ENABLE_PLACEHOLDERS = false;
    private static final PagedList.Config configure = new PagedList.Config.Builder()
            .setPageSize(PAGED_LIST_PAGE_SIZE)
            .setInitialLoadSizeHint(PAGED_LIST_PAGE_SIZE)
            .setEnablePlaceholders(PAGED_LIST_ENABLE_PLACEHOLDERS)
            .build();


    private static class NetworkDataSource extends SkinPageOffsetNetworkDataSource {
        public NetworkDataSource(SkinRepositoryImpl instance) {
            super(instance);
        }

//        @Override
//        protected List<SkinItem> convertToItems(final SkinListResponse response, final int size) {
//            final List<SkinItem> items = new ArrayList<>(size);
//            final String baseUrl = response.getBaseResUrl();
//            for (int index = 0; index < size; ++ index) {
//                if (index >= response.themes.size()) {
//                    items.add(new SkinItem("0", null, null));
//                } else {
//                    SkinItem item = response.themes.get(index);
//                    item.ppu = baseUrl + item.ppu;
//                    items.add(item);
//                }
//            }
//            return items;
//        }
    }

    private static class SubSkinDataSourceFactory implements DataSource.Factory<Integer, SkinItem> {
        @Override
        public DataSource<Integer, SkinItem> create() {
            SkinRepositoryImpl repository = SkinRepositoryImpl.getInstance();
            NetworkDataSource dataSource = new NetworkDataSource(repository);
            return dataSource;
        }
    }

    // 为了可读性，把final实例串连生成对象放到函数里逐行依次生成依赖对象。
//    private final LivePagedListProvider livePagedListProvider =
//            new LivePagedListProviderImp(new NetworkDataSource(SkinRepositoryImpl.getInstance()));
    public LiveData<PagedList<SkinItem>> livePageListProvider() {
        SubSkinDataSourceFactory sourceFactory = new SubSkinDataSourceFactory();
//        return new LivePagedListBuilder(sourceFactory, PAGED_LIST_PAGE_SIZE).build();
        return new LivePagedListBuilder(sourceFactory, configure).build();
    }
    
    public static LivePagedListProviderMedia getInstance() {
        return InstanceHolder.INSTASNCE;
    }

    private static final class InstanceHolder {
        private static final LivePagedListProviderMedia INSTASNCE = new LivePagedListProviderMedia();
    }
}
