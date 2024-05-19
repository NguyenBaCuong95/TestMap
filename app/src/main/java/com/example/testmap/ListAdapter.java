package com.example.testmap;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testmap.databinding.LayoutItemBinding;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolderItem> {
    private final List<SpannableString> list;
    private MainActivity.OnClickItem onClickItem;
    public ListAdapter(List<SpannableString> list, MainActivity.OnClickItem onClickItem) {
        this.list = list;
        this.onClickItem = onClickItem;
    }
    public void setList(List<SpannableString> list1){
        list.clear();
        list.addAll(list1);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolderItem(binding.getRoot(), binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position) {
        holder.onBind(list.get(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolderItem extends RecyclerView.ViewHolder {
        private LayoutItemBinding binding;

        public ViewHolderItem(@NonNull View itemView, LayoutItemBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public void onBind(SpannableString item) {
            binding.txtResult.setText(item);
            binding.imgDirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem.clickItem(item);
                }
            });
        }
    }
}
