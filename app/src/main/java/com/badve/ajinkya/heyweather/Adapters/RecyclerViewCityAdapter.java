package com.badve.ajinkya.heyweather.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.badve.ajinkya.heyweather.Models.City;
import com.badve.ajinkya.heyweather.R;

import java.util.ArrayList;

/**
 * Created by Ajinkya on 30-05-2015.
 */
public class RecyclerViewCityAdapter extends RecyclerView.Adapter<RecyclerViewCityAdapter.cityViewHolder> {

    private ArrayList<City> mCityArrayList;
    private Context context;
    private Activity activity;
    private LayoutInflater mInflater;


    public RecyclerViewCityAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        mInflater = LayoutInflater.from(context);
        this.mCityArrayList = new ArrayList<>();
    }

    public void addCity(City city){
        mCityArrayList.add(city);
        notifyDataSetChanged();
    }

    public City getCity(int position) {

        return mCityArrayList.get(position);
    }
    /**
     * Called when RecyclerView needs a new {@link RecyclerViewCityAdapter} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link # onBindViewHolder(RecyclerViewCityAdapter, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see # onBindViewHolder(RecyclerViewCityAdapter, int)
     */
    @Override
    public cityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO Auto-generated method stub
        View view;

        view = mInflater.inflate(R.layout.city_list_row, parent, false);
        cityViewHolder viewHolder = new cityViewHolder(view);
        return viewHolder;

    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link cityViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link cityViewHolder#getAdapterPosition()} which will have
     * the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(cityViewHolder holder, int position) {

        final City city = getCity(position);
        holder.mCityTextView.setText(city.getName());

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mCityArrayList.size();
    }

    public void setCities(ArrayList<City> recyclerViewCityAdapter) {
        this.mCityArrayList=recyclerViewCityAdapter;
        notifyDataSetChanged();
    }

    static class cityViewHolder extends RecyclerView.ViewHolder{

        TextView mCityTextView;

        public cityViewHolder(View itemView) {
            super(itemView);
            mCityTextView = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
