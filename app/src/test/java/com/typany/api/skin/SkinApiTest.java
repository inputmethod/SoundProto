package com.typany.api.skin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by yangfeng on 2017/12/5.
 */

public class SkinApiTest {
    private SkinRepositoryImplRetrofit skinRepositoryImplRetrofit;

    @Before
    public void initialize() {
        skinRepositoryImplRetrofit = new SkinRepositoryImplRetrofit();
    }


    @Test
    public void testGetAllBooks() throws Exception {
        List<Skin> sounds = skinRepositoryImplRetrofit.getAllSkins();
        Assert.assertFalse(sounds.isEmpty());
    }
}
