package com.dvishapps.yourshop.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvishapps.yourshop.databinding.DSelectItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.selectViewHolder> {

    private OnSelectListener onSelectListener;
    private DSelectItemBinding binding;
    private List<SelectObj> options;

    public SelectAdapter(List<SelectObj> options, OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        this.options = options;
    }

    @NonNull
    @Override
    public selectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = DSelectItemBinding.inflate(inflater, parent, false);
        return new selectViewHolder(binding, onSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull selectViewHolder holder, int position) {
        holder.bind(options.get(position));
    }

    @Override
    public int getItemCount() {
        return options != null ? options.size() : 0;
    }

    public class selectViewHolder extends RecyclerView.ViewHolder {
        private OnSelectListener onOptionSelect;
        private DSelectItemBinding binding;

        public selectViewHolder(@NonNull DSelectItemBinding binding, OnSelectListener onOptionSelect) {
            super(binding.getRoot());
            this.binding = binding;
            this.onOptionSelect = onOptionSelect;
        }

        public void bind(@NotNull SelectObj selectObj) {
            binding.setOption(selectObj.getTitle());
            this.binding.selectName.setOnClickListener(view -> {
                onOptionSelect.onSelect(selectObj);
            });
        }
    }

    public interface OnSelectListener {
        void onSelect(SelectObj selectObj);
    }

    public static class SelectObj {
        String title;
        String id;

        public SelectObj(String title, String id) {
            this.title = title;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
