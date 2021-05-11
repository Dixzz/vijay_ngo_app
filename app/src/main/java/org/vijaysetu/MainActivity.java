package org.vijaysetu;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.vijaysetu.data.CategoryUtil;
import org.vijaysetu.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerCat.setLayoutManager(new LinearLayoutManager(this));

        CategoryViewAdapter adapter = new CategoryViewAdapter(CategoryUtil.catTitle);

        if (binding.recyclerCat.getAdapter() == null)
            binding.recyclerCat.setAdapter(adapter);

        adapter.setOnItemClick(pos -> {
            startActivity(new Intent(MainActivity.this, SingleCatActivity.class).putExtra("index", pos));
        });
    }

    private static class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.ViewHolder> {
        private final List<String> stringList;
        private onItemClick onItemClick;

        public CategoryViewAdapter(List<String> stringList) {
            this.stringList = stringList;
        }

        public void setOnItemClick(CategoryViewAdapter.onItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            holder.textView.setClickable(true);
            holder.textView.setForeground(holder.textView.getContext().getTheme().obtainStyledAttributes(R.style.Theme_Dummy, new int[]{android.R.attr.selectableItemBackground}).getDrawable(0));
            holder.textView.setText(stringList.get(position));
            if (onItemClick != null)
                holder.textView.setOnClickListener(view -> onItemClick.item(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }

        interface onItemClick {
            void item(Integer pos);
        }
    }
}