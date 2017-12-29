package com.typany.api.skin;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

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
public class SkinItem {
    @SerializedName("si")
    public String id;
    @SerializedName("sn")
    public String type;

    @SerializedName("ppu")
    public String ppu;

    public SkinItem(final String id, final String type, final String ppu) {
        this.id = id;
        this.type = type;
        this.ppu = ppu;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        final SkinItem skinItem = (SkinItem) o;
        
        if (!TextUtils.equals(id, skinItem.id)) return false;
        if (type != null ? !type.equals(skinItem.type) : skinItem.type != null) return false;
        return TextUtils.equals(((SkinItem) o).ppu, this.ppu);
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (ppu != null ? ppu.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "SkinItem{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", ppu=" + ppu +
                '}';
    }
}
