/*
 * Copyright (c) 2017 Gevork Safaryan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.georgiosafo.recyclerview.headerdecoration;

import android.support.v7.widget.RecyclerView;

public class HeaderBuilder {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private IHeaderAdapter headersAdapter;
    private OnHeaderClickListener headerClickListener;
    private boolean overlay;
    private boolean isSticky;
    private DrawOrder drawOrder;


    public HeaderBuilder() {
        this.isSticky = true;
        this.drawOrder = DrawOrder.OverItems;
    }

    public HeaderBuilder setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public HeaderBuilder setStickyHeadersAdapter(IHeaderAdapter adapter) {
        return setStickyHeadersAdapter(adapter, false);
    }

    public HeaderBuilder setStickyHeadersAdapter(IHeaderAdapter adapter, boolean overlay) {
        this.headersAdapter = adapter;
        this.overlay = overlay;
        return this;
    }

    public HeaderBuilder setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public HeaderBuilder setOnHeaderClickListener(OnHeaderClickListener headerClickListener) {
        this.headerClickListener = headerClickListener;
        return this;
    }

    public HeaderBuilder setSticky(boolean isSticky) {
        this.isSticky = isSticky;
        return this;
    }

    public HeaderBuilder setDrawOrder(DrawOrder drawOrder) {
        this.drawOrder = drawOrder;
        return this;
    }

    public HeaderDecoration build() {

        HeaderStore store = new HeaderStore(recyclerView, headersAdapter, isSticky);

        HeaderDecoration decoration = new HeaderDecoration(store, overlay, drawOrder);

        decoration.registerAdapterDataObserver(adapter);

        if (headerClickListener != null) {
            HeadersTouchListener touchListener = new HeadersTouchListener(recyclerView, store);

            touchListener.setListener(headerClickListener);

            recyclerView.addOnItemTouchListener(touchListener);
        }

        return decoration;
    }
}
