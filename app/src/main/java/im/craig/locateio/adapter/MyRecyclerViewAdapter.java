package im.craig.locateio.adapter;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import im.craig.locateio.LocationModel;
import im.craig.locateio.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LocationModel[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, LocationModel[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocationModel location = mData[position];

        holder.mTv_title.setText(location.mtitle);
        holder.mTv_description.setText(location.mdescription);
        holder.mTv_extraInfo.setText(location.mextraInfo);
        holder.mTv_username.setText(location.musername);
        holder.mTv_lat.setText(location.mlat);
        holder.mTv_lng.setText(location.mlng);
        holder.mTv_posted.setText(location.mposted);
        holder.mTv_rating.setText(location.mrating);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTv_title;
        TextView mTv_description;
        TextView mTv_extraInfo;
        TextView mTv_username;
        TextView mTv_lat;
        TextView mTv_lng;
        TextView mTv_posted;
        TextView mTv_rating;



        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_title);
            mTv_description = itemView.findViewById(R.id.tv_description);

            mTv_extraInfo = itemView.findViewById(R.id.tv_extraInfo);
            mTv_username= itemView.findViewById(R.id.tv_username);
            mTv_lat = itemView.findViewById(R.id.tv_lat);
            mTv_lng = itemView.findViewById(R.id.tv_lng);
            mTv_posted = itemView.findViewById(R.id.tv_posted);
            mTv_rating = itemView.findViewById(R.id.tv_rating);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id].mlocationID;

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}