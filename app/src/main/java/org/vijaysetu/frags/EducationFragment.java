package org.vijaysetu.frags;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.vijaysetu.EducationHolder;
import org.vijaysetu.R;
import org.vijaysetu.SingleCatActivity;
import org.vijaysetu.databinding.EducTextHolderBinding;
import org.vijaysetu.utils.PaginationScrollListener;
import org.vijaysetu.utils.commonAdapter;

import java.util.ArrayList;
import java.util.List;

public class EducationFragment extends Fragment {
    SingleCatActivity activity;
    ConstraintLayout root;
    educAdapter adapter;
    RecyclerView recyclerView;

    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;

    // total no. of pages to load.
    private int TOTAL_PAGES = 0;

    // indicates the current page which Pagination is fetching.
    private int currentPage = 0;
    List<EducationHolder> totalList = new ArrayList<>();
    private static final String TAG = "EducationFragment";

    public EducationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView: ");
        activity = (SingleCatActivity) getActivity();
        return inflater.inflate(R.layout.fragment_educ, container, false);
    }

    // Reference views from here
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.root);
        Log.e(TAG, "onViewCreated: ");

        int pos = getArguments().getInt("index");
        totalList = activity.educationHolderArrayList.get(pos);

        TOTAL_PAGES = totalList.size();
        isLastPage = false;
        Log.d("AAA", "max pages: " + TOTAL_PAGES);
        recyclerView = view.findViewById(R.id.educRec);
        adapter = new educAdapter(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        loadFirstPage();
        currentPage = adapter.getItemCount();
        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            protected void loadMoreItems() {
                //Increment page index to load the next one
                if (currentPage <= TOTAL_PAGES) {
                    currentPage += 5;
                    recyclerView.postDelayed(() -> {
                        loadNextPage();
                    }, 500);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }


    private void loadFirstPage() {
        for (int i = 0; i < 10; i++) {
            if (i < totalList.size())
                adapter.add(totalList.get(i));
        }
        currentPage = adapter.getItemCount();
        Log.e(TAG, "loadFirstPage: " + currentPage);
    }

    private void toggleLinProg() {
        /*boolean isVis = activity.binding.indic.getVisibility() == View.VISIBLE;
        activity.binding.indic.setIndeterminate(!isVis);
        activity.binding.indic.setVisibility(isVis ? View.INVISIBLE : View.VISIBLE);*/
    }

    boolean play = false;
    private void loadNextPage() {
        //int itemsAdded = 0;
        if (currentPage <= TOTAL_PAGES) {
            for (int i = adapter.getItemCount(); i < currentPage; i++) {
                if (i < TOTAL_PAGES) {
                    adapter.add(totalList.get(i));
                    //itemsAdded+=i;
                }
            }
            /*if (!play) {
                recyclerView.smoothScrollBy(0, (activity.cardHeightEduc * itemsAdded) / 2, new AccelerateDecelerateInterpolator());
                play = true;
            }*/
        } else if (adapter.getItemCount() < TOTAL_PAGES) {
            Log.d(TAG, "loadNextPage: adding leftover list total - currentPageIncrement rate");
            Log.d("AAA", "else " + currentPage);
            for (int i = adapter.getItemCount(); i < TOTAL_PAGES; i++) {
                adapter.add(totalList.get(i));
                //itemsAdded+=i;
            }
            isLastPage = true;
        }

        //recyclerView.smoothScrollToPosition(adapter.getItemCount());
        Log.d("AAAA", adapter.getItemCount() + "added: " + totalList.size());
    }

    private static class educAdapter extends commonAdapter implements commonAdapter.onRootItemClick {
        EducTextHolderBinding binding;
        List<EducationHolder> educationHolderArrayList = new ArrayList<>();
        ConstraintLayout root;

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void add(EducationHolder e) {
            educationHolderArrayList.add(e);
            notifyItemInserted(educationHolderArrayList.size() - 1);
        }

        public void addAll(List<EducationHolder> e) {
            int i = getItemCount();
            educationHolderArrayList.addAll(e);
            notifyItemRangeInserted(i, e.size());
        }

        public educAdapter(ConstraintLayout root) {
            this.root = root;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.educ_text_holder, parent, false);
            return new ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.setOnRootItemClick(this);
            holder.itemView.findViewById(R.id.phoneCon).setOnClickListener(view -> {
                root.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.getData().getPhone())));
            });
            binding.setData(educationHolderArrayList.get(position));
            //Log.d("AAA", "size: " + getItemCount());
        }

        @Override
        public int getItemCount() {
            return educationHolderArrayList.size();
        }

        @Override
        public void position(Integer pos, View view) {
            LinearLayout collapse = ((LinearLayout) view.findViewById(R.id.collapCon));
            if (collapse.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(root);
                collapse.setVisibility(View.GONE);
            } else {
                TransitionManager.beginDelayedTransition(root);
                collapse.setVisibility(View.VISIBLE);
            }
        }
    }
}