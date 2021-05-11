package org.vijaysetu.data;

import android.util.Pair;

import androidx.fragment.app.Fragment;

import org.vijaysetu.frags.BlankFragment;
import org.vijaysetu.frags.EducationFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtil {
    //Each Category Title
    public static final List<String> catTitle = new ArrayList<>();

    static {
        catTitle.add("Services");
        catTitle.add("Education");
    }

    private static final List<Pair<String, Fragment>> catOne = new ArrayList<>();
    private static final List<Pair<String, Fragment>> catTwo = new ArrayList<>();

    public static final List<List<Pair<String, Fragment>>> items = new ArrayList<>(catTitle.size());

    //Add Title, Fragment for Category
    static {
        //Services
        catOne.add(new Pair<>("Offices", new BlankFragment()));
        catOne.add(new Pair<>("Orphanages", new BlankFragment()));
        catOne.add(new Pair<>("Old Age", new BlankFragment()));

        //Education
        catTwo.add(new Pair<>("Schools", new EducationFragment()));
        catTwo.add(new Pair<>("College", new BlankFragment()));
        catTwo.add(new Pair<>("Classes", new BlankFragment()));

        items.add(catOne);
        items.add(catTwo);
    }
}
