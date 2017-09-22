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
import android.view.ViewGroup;


/**
 * Interface which used by HeaderDecoration to bind ViewHolders
 *
 * @param <T> implementation of ViewHolder wich discrubed headers
 */
public interface IHeaderAdapter<T extends RecyclerView.ViewHolder> {

    /**
     * Get identifier of header in position
     *
     * @param position position which checked for add
     * @return id of header
     */
    long getHeaderId(int position);

    /**
     * Called when HeaderDecoration needs a new instance T of the given type to represent
     * an item.
     * This new instance ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     * @param parent parent view {@link ViewGroup}
     * @return instance of ViewHolder
     */
    T onCreateHeaderViewHolder(ViewGroup parent, int viewType);


    /**
     * Called by HeaderDecoration to display the header at the specified position.
     *
     * @param viewholder The instance of ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    void onBindHeaderViewHolder(T viewholder, int position);


    /**
     * Return type of Header View in current position
     *
     * @param position The position of the header item within the adapter's data set.
     * @return integer type of view
     */
    int getHeaderViewType(int position);
}
