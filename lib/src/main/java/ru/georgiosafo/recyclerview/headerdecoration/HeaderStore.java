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
import android.support.v7.widget.RecyclerViewHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class HeaderStore {

    private final RecyclerView parent;
    private final IHeaderAdapter adapter;
    private final HashMap<Long, View> headersViewByHeadersIds;
    private final HashMap<Long, Boolean> wasHeaderByItemId;
    private final ArrayList<Boolean> isHeaderByItemPosition;
    private final HashMap<Long, Integer> headersHeightsByItemsIds;
    private boolean isSticky;


    public HeaderStore(RecyclerView parent, IHeaderAdapter adapter, boolean isSticky) {
        this.parent = parent;
        this.adapter = adapter;
        this.isSticky = isSticky;
        this.headersViewByHeadersIds = new HashMap<Long, View>();
        this.wasHeaderByItemId = new HashMap<Long, Boolean>();
        this.isHeaderByItemPosition = new ArrayList<Boolean>();
        this.headersHeightsByItemsIds = new HashMap<Long, Integer>();
    }

    public View getHeaderViewByItem(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(parent, itemHolder.getPosition());

        if (itemPosition == -1) {
            return null;
        }

        long headerId = adapter.getHeaderId(itemPosition);

        if (!headersViewByHeadersIds.containsKey(headerId)) {
            RecyclerView.ViewHolder headerViewHolder = adapter.onCreateHeaderViewHolder(parent, adapter.getHeaderViewType(itemPosition));

            adapter.onBindHeaderViewHolder(headerViewHolder, itemPosition);
            layoutHeader(headerViewHolder.itemView);

            headersViewByHeadersIds.put(headerId, headerViewHolder.itemView);
        }

        return headersViewByHeadersIds.get(headerId);

    }

    public long getHeaderId(int itemPosition) {
        return adapter.getHeaderId(itemPosition);
    }

    public int getHeaderHeight(RecyclerView.ViewHolder itemHolder) {
        if (!headersHeightsByItemsIds.containsKey(itemHolder.getItemId())) {
            View header = getHeaderViewByItem(itemHolder);
            headersHeightsByItemsIds.put(itemHolder.getItemId(), header.getVisibility() == View.GONE ? 0 : header.getMeasuredHeight());
        }
        return headersHeightsByItemsIds.get(itemHolder.getItemId());
    }

    public boolean isHeader(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(parent, itemHolder.getLayoutPosition());
        if (isHeaderByItemPosition.size() <= itemPosition) {
            isHeaderByItemPosition.ensureCapacity(itemPosition + 1);
            for (int i = isHeaderByItemPosition.size(); i <= itemPosition; i++) {
                isHeaderByItemPosition.add(null);
            }
        }
        if (isHeaderByItemPosition.get(itemPosition) == null) {
            isHeaderByItemPosition.set(itemPosition, itemPosition == 0 || adapter.getHeaderId(itemPosition) != adapter.getHeaderId(itemPosition - 1));
        }

        return isHeaderByItemPosition.get(itemPosition);
    }

    public boolean wasHeader(RecyclerView.ViewHolder itemHolder) {
        if (!wasHeaderByItemId.containsKey(itemHolder.getItemId())) {
            int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(parent, itemHolder.getPosition());

            if (itemPosition == -1) {
                return false;
            }

            wasHeaderByItemId.put(itemHolder.getItemId(), itemPosition == 0 || adapter.getHeaderId(itemPosition) != adapter.getHeaderId(itemPosition - 1));
        }
        return wasHeaderByItemId.get(itemHolder.getItemId());
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void onItemRangeRemoved(int positionStart, int itemCount) {
        headersViewByHeadersIds.clear();

        if (isHeaderByItemPosition.size() > positionStart + itemCount) {

            for (int i = 0; i < itemCount; i++) {
                RecyclerView.ViewHolder holder = parent.findViewHolderForLayoutPosition(positionStart + i);
                if (holder != null) {
                    wasHeaderByItemId.put(holder.getItemId(), isHeaderByItemPosition.get(positionStart + i));
                }
            }

            isHeaderByItemPosition.set(positionStart + itemCount, null);

            for (int i = 0; i < itemCount; i++) {
                isHeaderByItemPosition.remove(positionStart);
            }
        }
    }

    public void onItemRangeInserted(int positionStart, int itemCount) {
        headersViewByHeadersIds.clear();
        if (isHeaderByItemPosition.size() > positionStart) {
            for (int i = 0; i < itemCount; i++) {
                isHeaderByItemPosition.add(positionStart, null);
            }
        }
        if (isHeaderByItemPosition.size() > positionStart + itemCount) {
            isHeaderByItemPosition.set(positionStart + itemCount, null);
        }
    }

    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        headersViewByHeadersIds.clear();

        int min = Math.min(fromPosition, toPosition);
        int max = Math.max(fromPosition, toPosition);
        for (int i = min; i <= max; i++) {
            if (i >= isHeaderByItemPosition.size()) {
                isHeaderByItemPosition.add(null);
            }
        }

        if (fromPosition < toPosition) {
            if (fromPosition == 0) {
                isHeaderByItemPosition.set(0, true);
            } else {
                long fromPositionId = adapter.getHeaderId(fromPosition);
                long beforeFromPositionId = adapter.getHeaderId(fromPosition - 1);
                long afterFromPositionId = adapter.getHeaderId(fromPosition + 1);
                isHeaderByItemPosition.set(fromPosition, fromPositionId != beforeFromPositionId);
                isHeaderByItemPosition.set(fromPosition + 1, fromPositionId != afterFromPositionId);
            }

            long toPositionId = adapter.getHeaderId(toPosition);
            long beforeToPositionId = adapter.getHeaderId(toPosition - 1);
            isHeaderByItemPosition.set(toPosition, toPositionId != beforeToPositionId);
            if (toPosition < isHeaderByItemPosition.size() - 1) {
                long afterToPositionId = adapter.getHeaderId(toPosition + 1);
                isHeaderByItemPosition.set(toPosition + 1, toPositionId != afterToPositionId);
            }
        } else if (fromPosition > toPosition) {
            if (toPosition == 0) {
                isHeaderByItemPosition.set(0, true);
            } else {
                long toPositionId = adapter.getHeaderId(toPosition);
                long beforeToPositionId = adapter.getHeaderId(toPosition - 1);
                long afterToPositionId = adapter.getHeaderId(toPosition + 1);
                isHeaderByItemPosition.set(toPosition, toPositionId != beforeToPositionId);
                isHeaderByItemPosition.set(toPosition + 1, toPositionId != afterToPositionId);
            }

            long fromPositionId = adapter.getHeaderId(fromPosition);
            long beforeFromPositionId = adapter.getHeaderId(fromPosition - 1);
            isHeaderByItemPosition.set(fromPosition, fromPositionId != beforeFromPositionId);

            if (fromPosition < isHeaderByItemPosition.size() - 1) {
                long afterFromPositionId = adapter.getHeaderId(fromPosition + 1);
                isHeaderByItemPosition.set(fromPosition + 1, fromPositionId != afterFromPositionId);
            }
        } else {
            if (fromPosition == 0) {
                isHeaderByItemPosition.set(0, true);
            } else {
                long fromPositionId = adapter.getHeaderId(fromPosition);
                long beforeFromPositionId = adapter.getHeaderId(fromPosition - 1);
                isHeaderByItemPosition.set(fromPosition, fromPositionId != beforeFromPositionId);

                if (fromPosition < isHeaderByItemPosition.size() - 1) {
                    long afterFromPositionId = adapter.getHeaderId(fromPosition + 1);
                    isHeaderByItemPosition.set(fromPosition + 1, fromPositionId != afterFromPositionId);
                }
            }
        }
    }

    public void onItemRangeChanged(int startPosition, int itemCount) {
        headersViewByHeadersIds.clear();

        if (startPosition >= isHeaderByItemPosition.size()) {
            return;
        }

        final int start = Math.min(startPosition, isHeaderByItemPosition.size());
        final int end = Math.min(startPosition + itemCount + 1, isHeaderByItemPosition.size());
        for (int i = start; i < end; ++i) {
            isHeaderByItemPosition.set(i, null);
        }
    }

    public void clear() {
        headersViewByHeadersIds.clear();
        isHeaderByItemPosition.clear();
        wasHeaderByItemId.clear();
    }

    private void layoutHeader(View header) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        header.measure(widthSpec, heightSpec);
        header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
    }
}
