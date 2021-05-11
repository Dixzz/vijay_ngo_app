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
import org.vijaysetu.frags.BlankFragment;

import java.util.List;

public class SingleCatActivity extends AppCompatActivity {

    ActivitySingleCategBinding binding;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        //Init views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_categ);
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
            return fragmentPair.get(position).second;
        }

        @Override
        public int getCount() {
            return fragmentPair.size();
        }
    }
}