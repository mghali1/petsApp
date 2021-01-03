package com.example.mohammadghali.petsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohammadghali on 11/20/18.
 *
 * This is the GridView custom adapter.
 */

public class GridViewAdapter extends BaseAdapter implements Filterable {

    private final LayoutInflater mInflater;
    List<Post>list;
    List<Post>orig;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View  view1 = mInflater.inflate(R.layout.row_data, null);

        //getting view in row_data
        TextView name = (TextView) view1.findViewById(R.id.pet_name_tv);
        ImageView image = (ImageView) view1.findViewById(R.id.pet_image_iv);

        name.setText(list.get(i).getPet_name());
        if(list.get(i).getPath() != null && !list.get(i).getPath().equals("null")){
            Glide.with(view1)
                    .load(list.get(i).getPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.defaultimage)
                            .circleCrop()
                            .fitCenter()
                            .override(100))
                    .into(image);
        }else
         image.setImageResource(R.drawable.defaultimage);
        return view1;



    }

    public GridViewAdapter(LayoutInflater mInflater, List<Post> list){
        this.mInflater = mInflater;
        this.list = list;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Post> results = new ArrayList<Post>();
                if (orig == null)
                    orig = list;
                if (charSequence != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Post g : orig) {
                            if (g.toString().toLowerCase()
                                    .contains(charSequence.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}