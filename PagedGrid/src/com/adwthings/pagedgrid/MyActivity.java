package com.adwthings.pagedgrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Here's the "magic class" xD
        PagedGrid grid=(PagedGrid)findViewById(R.id.paginador);
        //And here we set up our usual adapter, specifying number of rows/columns
        grid.setAdapter(new GridAdapter(this),5,4);
    }

    /**
     * This is just a sample BaseAdapter implementation that uses the same image to show 87 items
     * But the PagedGrid should work with any Adpater implementation
     */
    private static class GridAdapter extends BaseAdapter{
        private Bitmap image;
        public GridAdapter(Context context){
            image= BitmapFactory.decodeResource(context.getResources(),R.drawable.adw);
        }
        @Override
        public int getCount() {
            return 87;
        }

        @Override
        public Object getItem(int i) {
            return image;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
                ImageView img= (ImageView) convertView.findViewById(R.id.thumb);
                img.setImageBitmap(image);
            }
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText("ITEM "+position);
            return convertView;
        }
    }
}
