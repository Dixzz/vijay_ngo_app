package org.vijaysetu;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.flobiz.annotations_names.Annot;
import org.flobiz.annotations_names.AnnotMeth;
import org.flobiz.lib.MyClassNew;
import org.flobiz.lib.MyClassNew2;
import org.vijaysetu.databinding.ActivityMainBinding;
import org.vijaysetu.databinding.EducTextHolderBinding;
import org.vijaysetu.utils.CommonUtils;
import org.vijaysetu.utils.NameViewModel;
import org.vijaysetu.utils.commonAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static List<List<EducationHolder>> educationHolderArrayList;
    static List<String> list = new ArrayList<>();
    LiveData<String> stringLiveData;
    private static final String TAG = "MainActivity";

    static {
        for (int i = 0; i < 50; i++) {
            list.add((i + 1) + "");
        }
    }

    public NameViewModel model = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NameViewModel.class);
    ArrayList<EducationHolder> educationHolders = new ArrayList<>();

    @Annot
    @SuppressWarnings("unchecked")
    public String hello;

    Observer<String> observer = null;

    @AnnotMeth(permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 111)
    public void doSum() {
        Log.d(TAG, "doSum: ");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyClassNew.bind(this);


        CategoryViewAdapter adapter = new CategoryViewAdapter(educationHolders);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (educationHolders.size() == 0) {
            observer = newName -> {
                // Update the UI, in this case, a TextView.
                Log.d(TAG, "onChanged: " + newName);
                educationHolders.add(new EducationHolder(newName, "", "", ""));
                adapter.notifyItemInserted(adapter.getItemCount());
                binding.recyclerCat.scrollToPosition(adapter.getItemCount() - 1);
            };

        }
        if (!model.getCurrentName().hasActiveObservers()) {
            model.getCurrentName().observeForever(observer);
        }

        Fragment m = new Fragment() {
            MainActivity activity;

            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(R.layout.educ_text_holder, container, false);
            }

            @Override
            public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                activity = (MainActivity) getActivity();
                TextView textView = view.findViewById(R.id.text33);
                textView.append(activity.stringLiveData.getValue());
            }
        };

        educationHolderArrayList = new ArrayList<>();
        educationHolderArrayList.addAll(CommonUtils.addEducData(educationHolderArrayList));
        educationHolderArrayList.clear();
        List<EducationHolder> educationHolders = new ArrayList<>();

        adapter.setHasStableIds(true);
        //adapter.setOnRootItemClick((pos, view) -> startActivity(new Intent(MainActivity.this, SingleCatActivity.class).putExtra("index", pos)));
        adapter.setOnRootItemClick((pos, view) -> {
            Toast.makeText(this, "" + pos, Toast.LENGTH_SHORT).show();
        });
        // Create the observer which updates the UI.
        /*final Observer<String> nameObserver = newName -> {
            // Update the UI, in this case, a TextView.
            Log.d(TAG, "onChanged: " + newName);
            educationHolders.add(adapter.getItemCount(), new EducationHolder(newName, "Test", "", ""));
            //adapter.notifyItemInserted(adapter.getItemCount() - 1);
            //binding.recyclerCat.scrollToPosition(adapter.getItemCount() - 1);
        };*/

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        binding.fab.setOnClickListener(view -> {
            model.getCurrentName().postValue("Hello");
        });

        Thread t = new Thread() {
            int l = 0;

            @Override
            public void run() {
                Handler handler1 = new Handler();
                l += 1;
                model.getCurrentName().postValue("New " + l);
                //Log.e(TAG, "run: " + Thread.currentThread().getName());
                handler1.postDelayed(this, 1000);
            }
        };
        new Handler().post(t);
        /*Thread t = new Thread() {
            int l = 0;

            @Override
            public void run() {
                Handler handler1 = new Handler();
                l += 1;
                model.getCurrentName().postValue("New " + l);
                Log.e(TAG, "run: " + Thread.currentThread().getName());
                handler1.postDelayed(this, 1);
            }
        };
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            Looper.prepare();
            new Handler().post(t);
            Looper.loop();
        });*/

        if (binding.recyclerCat.getAdapter() == null)
            binding.recyclerCat.setAdapter(adapter);
        MyClassNew2.runMethod(this);
    }

    private static class CategoryViewAdapter extends commonAdapter {
        EducTextHolderBinding binding;
        private final ArrayList<EducationHolder> stringList;
        private onRootItemClick onRootItemClick;

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void setOnRootItemClick(onRootItemClick e) {
            this.onRootItemClick = e;
        }

        public CategoryViewAdapter(ArrayList<EducationHolder> stringList) {
            this.stringList = stringList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.educ_text_holder, parent, false);
            return new ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            binding.setData(stringList.get(position));
            //TextView textView = holder.itemView.findViewById(R.id.text33);
            /*if (position % 2 == 0)
                ((MaterialCardView) holder.itemView.findViewById(R.id.card)).setCardBackgroundColor(Color.BLUE);
            else
                ((MaterialCardView) holder.itemView.findViewById(R.id.card)).setCardBackgroundColor(Color.RED);*/
            //holder.setOnRootItemClick(onRootItemClick);
            /*textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setClickable(true);

            textView.setForeground(textView.getContext().getTheme().obtainStyledAttributes(R.style.Theme_Dummy, new int[]{android.R.attr.selectableItemBackground}).getDrawable(0));
            textView.setText(stringList.get(position));*/
            holder.itemView.setOnClickListener(view -> {
                onRootItemClick.position(position, view);
            });
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }
    }
}