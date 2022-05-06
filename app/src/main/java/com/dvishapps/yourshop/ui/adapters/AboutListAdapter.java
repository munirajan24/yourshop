package com.dvishapps.yourshop.ui.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AboutListAdapter extends RecyclerView.Adapter<AboutListAdapter.AboutHolder> {

    private List<About> list;

    public AboutListAdapter(List<About> list) {
        this.list = list;
    }

    public class AboutHolder extends RecyclerView.ViewHolder {

        public AboutHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public AboutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        view = inflater.inflate(R.layout.about_info, parent, false);
        AboutHolder viewHolder = new AboutHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AboutHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class About {
        private String title;
        private String content;

        public About(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
