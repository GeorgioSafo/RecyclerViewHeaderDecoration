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

package recyclerview.georgiosafo.ru.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.georgiosafo.recyclerview.headerdecoration.IHeaderAdapter;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> implements
        IHeaderAdapter<RecyclerView.ViewHolder> {


    private static final int BIG_HEADER = 0;
    private static final int SMALL_HEADER = 1;
    private List<SampleItem> mSampleItemList;

    public SampleAdapter(List<SampleItem> sampleItemList) {
        mSampleItemList = sampleItemList;
        setHasStableIds(true);
    }

    //region RecyclerView.Adapter

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sample_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.item.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mSampleItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return mSampleItemList.get(position).hashCode();
    }

    //endregion RecyclerView.Adapter


    //region IHeaderAdapter

    @Override
    public long getHeaderId(int position) {
        return mSampleItemList.get(position).getType().hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BIG_HEADER) {
            return new BigHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.big_header_item, parent, false));
        } else {
            return new SmallHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.small_header_item, parent, false));
        }
    }

    @Override
    public int getHeaderViewType(int position) {
        return mSampleItemList.get(position).getType() == SampleType.BigType ? BIG_HEADER : SMALL_HEADER;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        if (viewholder instanceof BigHeaderHolder) {
            ((BigHeaderHolder) viewholder).header.setText(String.valueOf(position));
        } else {
            ((SmallHeaderHolder) viewholder).header.setText(String.valueOf(position));
        }
    }

    //endregion


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView;
        }
    }

    static class BigHeaderHolder extends RecyclerView.ViewHolder {
        TextView header;

        BigHeaderHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.textView);
        }
    }

    static class SmallHeaderHolder extends RecyclerView.ViewHolder {
        TextView header;

        SmallHeaderHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.textView);
        }
    }
}

