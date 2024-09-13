package com.example.fetchdataappvaishnav;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine whether the current item is a header based on some logic
        if (itemList.get(position).isHeader()) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            // Inflate the header layout and return a HeaderViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
            return new HeaderViewHolder(view);
        } else {
            // Inflate the item layout and return an ItemViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = itemList.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            // Ensure the header title is set properly and not null
            headerHolder.headerTitle.setText(item.getName());
        } else {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.listIdTextView.setText(String.valueOf(item.getListId()));
            itemHolder.nameTextView.setText(item.getName());
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class for regular items
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView listIdTextView, nameTextView;

        public ItemViewHolder(View view) {
            super(view);
            listIdTextView = view.findViewById(R.id.listIdTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
        }
    }

    // ViewHolder class for headers
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = view.findViewById(R.id.headerTitleTextView);  // Assuming you have a TextView for header in `header_row.xml`
        }
    }
}
