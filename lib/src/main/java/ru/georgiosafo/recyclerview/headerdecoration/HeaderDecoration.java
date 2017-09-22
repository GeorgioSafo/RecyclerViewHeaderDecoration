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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class HeaderDecoration extends RecyclerView.ItemDecoration {

    private final HeaderStore headerStore;
    private final AdapterDataObserver adapterDataObserver;
    private boolean overlay;
    private DrawOrder drawOrder;

    public HeaderDecoration(HeaderStore headerStore) {
        this(headerStore, false);
    }

    public HeaderDecoration(HeaderStore headerStore, boolean overlay) {
        this(headerStore, overlay, DrawOrder.OverItems);
    }

    public HeaderDecoration(HeaderStore headerStore, boolean overlay, DrawOrder drawOrder) {
        this.overlay = overlay;
        this.drawOrder = drawOrder;
        this.headerStore = headerStore;
        this.adapterDataObserver = new AdapterDataObserver();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawOrder == DrawOrder.UnderItems) {
            drawHeaders(c, parent, state);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawOrder == DrawOrder.OverItems) {
            drawHeaders(c, parent, state);
        }
    }

    private void drawHeaders(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        Float lastY = null;

        for (int i = childCount - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(child);

            if (!lp.isItemRemoved() && !lp.isViewInvalid()) {

                float translationY = child.getTranslationY();

                if ((i == 0 && headerStore.isSticky()) || headerStore.isHeader(holder)) {

                    View header = headerStore.getHeaderViewByItem(holder);

                    if (header.getVisibility() == View.VISIBLE) {

                        int headerHeight = headerStore.getHeaderHeight(holder);
                        float y = getHeaderY(child, lm) + translationY;

                        if (headerStore.isSticky() && lastY != null && lastY < y + headerHeight) {
                            y = lastY - headerHeight;
                        }

                        c.save();
                        c.translate(0, y);
                        header.draw(c);
                        c.restore();

                        lastY = y;
                    }
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        boolean isHeader = lp.isItemRemoved() ? headerStore.wasHeader(holder) : headerStore.isHeader(holder);
        if (overlay || !isHeader) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, headerStore.getHeaderHeight(holder), 0, 0);
        }
    }

    public void registerAdapterDataObserver(RecyclerView.Adapter adapter) {
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    private float getHeaderY(View item, RecyclerView.LayoutManager lm) {
        return headerStore.isSticky() && lm.getDecoratedTop(item) < 0 ? 0 : lm.getDecoratedTop(item);
    }


    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {

        public AdapterDataObserver() {
        }

        @Override
        public void onChanged() {
            headerStore.clear();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            headerStore.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            headerStore.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            headerStore.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            headerStore.onItemRangeChanged(positionStart, itemCount);
        }
    }

}
