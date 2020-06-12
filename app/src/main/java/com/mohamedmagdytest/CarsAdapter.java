package com.mohamedmagdytest;

import android.app.Activity;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder>  {

    private final Activity activity;
    private int resource;
    private ArrayList<Car> items;




    public CarsAdapter(Activity activity, int resource, ArrayList<Car> items) {
        this.items = items;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public CarsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        ViewHolder vh;

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(resource, parent, false);
            vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.tt1.setText(items.get(position).getBrand());
            if (items.get(position).getIs_Used().equals("true"))
            {
                holder.tt2.setTextColor(activity.getResources().getColor(R.color.red));
                holder.tt2.setText("Used");

            }else
            {
                holder.tt2.setText("New");

            }
            holder.tt3.setText(items.get(position).getConstruction_year());
            String image=items.get(position).getImage();
            Uri uri = Uri.parse(image);
            holder.simpleDraweeView.setImageURI(uri);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tt1;
        public TextView tt2;
        public TextView tt3;

        private SimpleDraweeView simpleDraweeView;

        public ViewHolder(View v) {
            super(v);
            tt1 = (TextView) v.findViewById(R.id.name);
            tt2 = (TextView) v.findViewById(R.id.used);
            tt3 = (TextView) v.findViewById(R.id.cons);
            simpleDraweeView = (SimpleDraweeView) v.findViewById(R.id.image_view);
        }
    }
    public static class ViewHolderLoading extends CarsAdapter.ViewHolder {
        public ViewHolderLoading(View v) {
            super(v);

        }
    }
}
