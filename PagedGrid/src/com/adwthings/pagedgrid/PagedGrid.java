package com.adwthings.pagedgrid;
/*
 * Copyright (C) 2011 Gustavo Claramunt, AnderWeb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Custom ViewPager implementation creating an horizontal paginated grid of items
 * based on a regular android Adapter
 *
 * Author: Gustavo Claramunt a.k.a AnderWeb
 * Date: 10/01/12
 * Time: 12:57
 */
public class PagedGrid extends ViewPager {
    public PagedGrid(Context context) {
        super(context);
    }

    public PagedGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setAdapter(BaseAdapter adapter,int columns, int rows){
        super.setAdapter(new GridPagerAdapter(adapter,columns,rows));
    }

    /**
     * As we wrap an standard Adapter within our custom PagerAdapter, we do not want this class to be used otherwise
     * @param adapter
     */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        throw new IllegalStateException("This class cares itself about the PagerAdapter. Call setAdapter(BaseAdapter,columns,rows) instead");
    }

    private static class GridPagerAdapter extends PagerAdapter{
        private int PAGE_ROWS=4;
        private int PAGE_COLUMNS=4;
        private int PAGE_ITEMS=PAGE_COLUMNS*PAGE_ROWS;
        private BaseAdapter mInnerAdapter;
        public GridPagerAdapter(BaseAdapter innerAdapter,int columns, int rows){
            mInnerAdapter=innerAdapter;
            PAGE_ROWS=rows;
            PAGE_COLUMNS=columns;
            PAGE_ITEMS=rows*columns;
        }

        /**
         * This is the method that creates each page for the ViewPager
         * The current one uses a poor implementation with nested LinearLayout
         * As long as the GridLayout class is added to the android compatibility library
         * we should use it.
         * @param context Resources context to inflate views
         * @param position page position
         * @return a View representing the current page
         */
        private View createPage(Context context,int position){
            LinearLayout v= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.page,null,false);
            v.setWeightSum(PAGE_ROWS);
            int initialItem=Math.min(PAGE_ITEMS*position,mInnerAdapter.getCount());
            int finalItem=Math.min(initialItem+PAGE_ITEMS,mInnerAdapter.getCount());
            for(int i=0;i<PAGE_ROWS;i++){
                LinearLayout currentRow=new LinearLayout(context);
                currentRow.setWeightSum(PAGE_COLUMNS);
                currentRow.setBackgroundColor(0xFF00FF00);
                v.addView(currentRow,new LinearLayout.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        0,1));
            }
            int column=0;
            int row=0;
            for(int i=initialItem;i<finalItem;i++){
                final LinearLayout currentRow= (LinearLayout) v.getChildAt(row);
                currentRow.addView(mInnerAdapter.getView(i,null,currentRow),
                        new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT,1));
                column++;
                if(column>=PAGE_COLUMNS){
                    column=0;
                    row++;
                }
            }
            return v;
        }
        @Override
        public int getCount() {
            final int total=mInnerAdapter.getCount();
            final int perPage=PAGE_ITEMS;
            int count=total/perPage;
            if(total%perPage>0)
                count++;
            return count;
        }

        @Override
        public void startUpdate(View view) {

        }

        @Override
        public Object instantiateItem(View pager, int position) {
            View page=createPage(pager.getContext(),position);
            ((ViewPager)pager).addView(page);
            return page;
        }

        @Override
        public void destroyItem(View pager, int position, Object page) {
            ((ViewPager)pager).removeView((View) page);
        }

        @Override
        public void finishUpdate(View pager) {

        }

        @Override
        public boolean isViewFromObject(View view, Object page) {
            return view.equals(page);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

        }
    }
}
