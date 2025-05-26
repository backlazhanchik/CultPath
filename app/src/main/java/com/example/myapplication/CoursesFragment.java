package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesFragment extends Fragment {

    private SearchView searchView;
    private ChipGroup locationChipGroup;
    private ChipGroup interestsChipGroup;
    private ChipGroup ageChipGroup;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private MaterialButton toggleFiltersButton;
    private LinearLayout filtersContainer;
    private NestedScrollView nestedScrollView;
    private boolean areFiltersVisible = true;

    private final List<Course> allCourses = new ArrayList<Course>() {{
        add(new Course(1, "Курс ораторского ремесла", "Эффективные\n" +
                "коммуникации\n" +
                "и публичные выступления", "Онлайн", "Искувсство", "19-25", "https://www.example.com/art-history"));
        add(new Course(2, "Русский фольклор", "Изучение истории фольклора. Русские народные традиции. Фольклорные жанры: сказки, былины, загадки, пословицы и поговорки, анекдоты и др.", "Онлайн", "Фольклор", "13-18", "https://linguarus.education/courses/folklore/"));
        add(new Course(3, "Онлайн-курс по практическим вопросам интеллектуальной собственности", "Со 2 октября 2025 года любой желающий может получить бесплатные видеолекции о том, как правильно защитить свою интеллектуальную собственность и получать доход от ее использования. Чтобы стать участником онлайн-курса, необходимо пройти регистрацию на сайте и получить доступ к видеолекциям от спикеров Фестиваля ВОИР: Наука и изобретения для жизни.", "Онлайн", "Научные изобретения", "19-25", "https://voirfest.ru/group/online/477b098d-d7bf-4dd7-b061-4591356e0f0f?tab=about"));
        add(new Course(4, "Студия Центра Театрального Мастерства", " образовательная платформа и сообщество людей, неравнодушных к театральному искусству и культурной жизни города. Здесь мы поможем раскрыть вашу личность через обучение актерскому мастерству.", "Очно в Нижнем Новгороде", "Искувсство", "13-18", "https://ctm-nn.ru/repertuar/akterskaya-masterskaya/"));
    }};

    private List<Course> filteredCourses = new ArrayList<>(allCourses);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        searchView = view.findViewById(R.id.search_view);
        locationChipGroup = view.findViewById(R.id.location_chip_group);
        interestsChipGroup = view.findViewById(R.id.interests_chip_group);
        ageChipGroup = view.findViewById(R.id.age_chip_group);
        recyclerView = view.findViewById(R.id.recycler_view);
        toggleFiltersButton = view.findViewById(R.id.toggle_filters_button);
        filtersContainer = view.findViewById(R.id.filters_container);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseAdapter = new CourseAdapter(filteredCourses, course -> {
            CourseDetailFragment fragment = CourseDetailFragment.newInstance(course);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(courseAdapter);

        // Setup search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCourses();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses();
                return true;
            }
        });

        // Setup toggle filters button
        toggleFiltersButton.setOnClickListener(v -> toggleFiltersVisibility());

        // Setup chip group listeners
        setupChipGroupListeners();
    }

    private void toggleFiltersVisibility() {
        areFiltersVisible = !areFiltersVisible;
        TransitionManager.beginDelayedTransition((ViewGroup) filtersContainer.getParent());

        if (areFiltersVisible) {
            filtersContainer.setVisibility(View.VISIBLE);
            toggleFiltersButton.setText("Скрыть");
            toggleFiltersButton.setIconResource(R.drawable.ic_arrow_up);
        } else {
            filtersContainer.setVisibility(View.GONE);
            toggleFiltersButton.setText("Развернуть");
            toggleFiltersButton.setIconResource(R.drawable.ic_arrow_down);
        }
    }

    private void setupChipGroupListeners() {
        locationChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterCourses());
        interestsChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterCourses());
        ageChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterCourses());
    }

    private void filterCourses() {
        String searchQuery = searchView.getQuery().toString().toLowerCase();
        Map<String, List<String>> selectedFilters = new HashMap<>();
        selectedFilters.put("location", getSelectedChipTexts(locationChipGroup));
        selectedFilters.put("interest", getSelectedChipTexts(interestsChipGroup));
        selectedFilters.put("age", getSelectedChipTexts(ageChipGroup));

        List<Course> filtered = new ArrayList<>();
        for (Course course : allCourses) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    course.getTitle().toLowerCase().contains(searchQuery) ||
                    course.getDescription().toLowerCase().contains(searchQuery);

            boolean matchesFilters = true;
            for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
                String type = entry.getKey();
                List<String> selectedValues = entry.getValue();

                if (!selectedValues.isEmpty()) {
                    if (type.equals("location") && !selectedValues.contains(course.getLocation())) {
                        matchesFilters = false;
                        break;
                    } else if (type.equals("interest") && !selectedValues.contains(course.getCategory())) {
                        matchesFilters = false;
                        break;
                    } else if (type.equals("age") && !selectedValues.contains(course.getAgeGroup())) {
                        matchesFilters = false;
                        break;
                    }
                }
            }

            if (matchesSearch && matchesFilters) {
                filtered.add(course);
            }
        }

        filteredCourses = filtered;
        courseAdapter.updateCourses(filteredCourses);
    }

    private List<String> getSelectedChipTexts(ChipGroup chipGroup) {
        List<String> selectedChipTexts = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedChipTexts.add(chip.getText().toString());
            }
        }
        return selectedChipTexts;
    }
}