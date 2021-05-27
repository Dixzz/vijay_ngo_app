package org.vijaysetu;

import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.vijaysetu.data.CategoryUtil;
import org.vijaysetu.databinding.ActivitySingleCategBinding;
import org.vijaysetu.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleCatActivity extends AppCompatActivity {

    public ActivitySingleCategBinding binding;
    int index;
    private static final String TAG = "SingleCatActivity";
    public List<List<EducationHolder>> educationHolderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        educationHolderArrayList = new ArrayList<>();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_categ);

        ExecutorService executorService = null;

        try {
            executorService = Executors.newCachedThreadPool();
            executorService.execute(() -> educationHolderArrayList.addAll(CommonUtils.addEducData(educationHolderArrayList)));
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

        /*if (MainActivity.educationHolderArrayList != null && MainActivity.educationHolderArrayList.size() > 0) {
            educationHolderArrayList = MainActivity.educationHolderArrayList;
        } else {
            new Thread() {
                @Override
                public void run() {
                    educationHolderArrayList.addAll(CommonUtils.addEducData(educationHolderArrayList));
                }
            }.start();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //educationHolderArrayList.clear();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Init Params
        index = getIntent().getIntExtra("index", 0);

        binding.tabLayout.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), CategoryUtil.items.get(index)));
    }

    public static class FragmentAdapter extends FragmentPagerAdapter {
        List<Pair<String, Fragment>> fragmentPair;

        public FragmentAdapter(@NonNull FragmentManager fm, List<Pair<String, Fragment>> f) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragmentPair = f;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentPair.get(position).first;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            Fragment f = fragmentPair.get(position).second;
            bundle.putInt("index", position);
            f.setArguments(bundle);
            return f;
        }

        @Override
        public int getCount() {
            return fragmentPair.size();
        }
    }
}