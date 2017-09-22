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

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.georgiosafo.recyclerview.headerdecoration.HeaderBuilder;
import ru.georgiosafo.recyclerview.headerdecoration.HeaderDecoration;

public class SampleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = view.findViewById(R.id.recycler_view);
        list.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        setAdapter(list);
    }

    protected void setAdapter(RecyclerView list) {
        final SampleAdapter adapter = new SampleAdapter(generateNewList());
        HeaderDecoration headerDecoration = new HeaderBuilder()
                .setAdapter(adapter)
                .setRecyclerView(list)
                .setStickyHeadersAdapter(adapter, false)
                .setSticky(true)
                .build();
        list.setAdapter(adapter);
        list.addItemDecoration(headerDecoration);
        list.invalidateItemDecorations();

    }

    private List<SampleItem> generateNewList() {
        List<SampleItem> list = new ArrayList<>();
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.SmallType, false));
        list.add(new SampleItem(SampleType.SmallType, false));
        list.add(new SampleItem(SampleType.SmallType, false));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        list.add(new SampleItem(SampleType.BigType, true));
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
