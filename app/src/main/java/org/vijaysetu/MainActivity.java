package org.vijaysetu;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.vijaysetu.data.CategoryUtil;
import org.vijaysetu.databinding.ActivityMainBinding;
import org.vijaysetu.utils.CommonUtils;
import org.vijaysetu.utils.commonAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    //public static List<List<EducationHolder>> educationHolderArrayList;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //educationHolderArrayList = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //educationHolderArrayList = new ArrayList<>();
        CategoryViewAdapter adapter = new CategoryViewAdapter(CategoryUtil.catTitle);

        adapter.setOnRootItemClick((pos, view) -> startActivity(new Intent(MainActivity.this, SingleCatActivity.class).putExtra("index", pos)));
        //educationHolderArrayList.addAll(CommonUtils.addEducData(educationHolderArrayList));

        if (binding.recyclerCat.getAdapter() == null)
            binding.recyclerCat.setAdapter(adapter);
    }

    private static class CategoryViewAdapter extends commonAdapter {
        private final List<String> stringList;
        private onRootItemClick onRootItemClick;

        @Override
        public void setOnRootItemClick(onRootItemClick e) {
            this.onRootItemClick = e;
        }

        public CategoryViewAdapter(List<String> stringList) {
            this.stringList = stringList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView textView = holder.itemView.findViewById(android.R.id.text1);
            //holder.setOnRootItemClick(onRootItemClick);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setClickable(true);
            holder.itemView.setOnClickListener(view -> {
                onRootItemClick.position(position, view);
            });
            textView.setForeground(textView.getContext().getTheme().obtainStyledAttributes(R.style.Theme_Dummy, new int[]{android.R.attr.selectableItemBackground}).getDrawable(0));
            textView.setText(stringList.get(position));
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }
    }
}