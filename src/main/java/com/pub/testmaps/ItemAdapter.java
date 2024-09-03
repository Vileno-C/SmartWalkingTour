package com.pub.testmaps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {

        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.checkBox.setChecked(item.isSelected());
        holder.imageView.setImageResource(item.getImageResource());
        holder.textViewName.setText(item.getTextViewNameText());
        holder.textViewRegion.setText(item.getTextViewText());

        holder.checkBox.setOnClickListener(v -> {
            item.setSelected(((CheckBox) v).isChecked());
            holder.itemView.setBackgroundColor(item.isSelected() ? 0xFFEEEEEE : 0xFFFFFFFF);

        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView imageView;
        TextView textViewName;
        TextView textViewRegion;

        ItemViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
        }
    }
}




