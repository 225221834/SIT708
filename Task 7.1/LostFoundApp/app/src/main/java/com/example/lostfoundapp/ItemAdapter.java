package com.example.lostfoundapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


 //Adapter class for the RecyclerView in ShowItemsActivity.
 //Binds the list of 'Item' objects to the 'item_row' layout.
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Items> itemList;
    private OnItemClickListener listener;

    //Interface to handle click events on individual items in the list.
    public interface OnItemClickListener {
        void onItemClick(Items item);
    }

    public ItemAdapter(List<Items> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    //Creates a new ViewHolder by inflating the item_row layout.
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    //Binds data from an Item object to the UI components of the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Items item = itemList.get(position);
        holder.textView.setText(item.getType());
        holder.txtName.setText(item.getName());
        holder.txtCategory.setText(item.getCategory());
        
        // Loads image with basic downsampling to prevent out of memory issues
        if (item.getImage() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; 
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length, options);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageDrawable(null);
        }

        // Sets click listener for the entire row
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    //The total number of items in the list.
    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

     //Updates the adapter's data set and refreshes the UI.
     //Updated list of items.
    public void updateList(List<Items> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    //ViewHolder class to hold references to the views for each item row.
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, txtName, txtCategory;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_type);
            txtName = itemView.findViewById(R.id.item_name);
            txtCategory = itemView.findViewById(R.id.item_category);
        }
    }
}
