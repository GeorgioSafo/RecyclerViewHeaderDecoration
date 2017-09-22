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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class HeadersTouchListener implements RecyclerView.OnItemTouchListener {

    private final HeaderStore headerStore;
    private final GestureDetector gestureDetector;
    private OnHeaderClickListener listener;

    public HeadersTouchListener(RecyclerView parent, HeaderStore headerStore) {
        this.headerStore = headerStore;
        this.gestureDetector = new GestureDetector(parent.getContext(), new SingleTapDetector(parent));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView parent, MotionEvent e) {
        return listener != null && gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView parent, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void setListener(OnHeaderClickListener listener) {
        this.listener = listener;
    }

    private class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {

        private final RecyclerView parent;

        public SingleTapDetector(RecyclerView parent) {
            this.parent = parent;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return findItemHolderUnder(e.getX(), e.getY()) != null;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            RecyclerView.ViewHolder holder = findItemHolderUnder(e.getX(), e.getY());
            if (holder != null) {
                View headerView = headerStore.getHeaderViewByItem(holder);
                long headerId = headerStore.getHeaderId(holder.getPosition());
                listener.onHeaderClick(headerView, headerId);
                return true;
            }

            return false;
        }

        private RecyclerView.ViewHolder findItemHolderUnder(float x, float y) {

            for (int i = parent.getChildCount() - 1; i > 0; i--) {
                View item = parent.getChildAt(i);
                RecyclerView.ViewHolder holder = parent.getChildViewHolder(item);

                if (holder != null && headerStore.isHeader(holder)) {
                    if (y < item.getTop() && item.getTop() - headerStore.getHeaderHeight(holder) < y) {
                        return holder;
                    }
                }
            }

            View firstItem = parent.getChildAt(0);

            if (firstItem != null) {
                RecyclerView.ViewHolder holder = parent.getChildViewHolder(firstItem);

                if (holder != null && y < headerStore.getHeaderHeight(holder)) {
                    if (holder.getPosition() == 0 || headerStore.isSticky()) {
                        return holder;
                    }
                }
            }

            return null;
        }
    }
}
