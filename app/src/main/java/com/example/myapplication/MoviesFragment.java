package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

public class MoviesFragment extends Fragment {

    private SearchView searchView;
    private ChipGroup locationChipGroup;
    private ChipGroup interestsChipGroup;
    private ChipGroup ageChipGroup;
    private RecyclerView popularRecyclerView;
    private RecyclerView othersRecyclerView;
    private MovieAdapter popularMoviesAdapter;
    private MovieAdapter otherMoviesAdapter;
    private MaterialButton toggleFiltersButton;
    private LinearLayout filtersContainer;
    private NestedScrollView nestedScrollView;
    private boolean areFiltersVisible = true;

    private final List<Movie> allMovies = new ArrayList<Movie>() {{
        add(new Movie(1, "Древнейшее искусство России", "Ученые раскрывают тайны и смыслы наскальной живописи. Док об истории древнейшей культуры России", "Онлайн", "Искусство", "13-18", "https://www.kinopoisk.ru/series/5234123/?utm_referrer=www.google.comx", true));
        add(new Movie(2, "Студия Нижний", "История жизни и любви талантливого фотографа Никиты Пирогова. Действие картины разворачивается в настоящем, прошлом и будущем Нижнего Новгорода. Фильм наполнен музыкой, творчеством, городскими достопримечательностями и нижегородскими историями.", "Очно в Нижнем Новгороде", "История", "19-25", "https://www.kinopoisk.ru/film/767272/", true));
        add(new Movie(3, "Арт и Факт", "Какие секреты таят в себе полотна знаменитых художников? Новеллы о шедеврах из собрания Третьяковской галереи", "Онлайн", "Искусство", "13-18", "https://www.kinopoisk.ru/series/5304281/", true));
        add(new Movie(4, "Лесная симфония", "Жизнь фауны финских лесов — от насекомых до медведей. Впечатляющий док с отличной операторской работой", "Онлайн", "Фоклор", "13-18", "https://www.kinopoisk.ru/film/664849/", false));
        add(new Movie(5, "Народные промыслы России", "Фильм о развитии и сохранении в России народных промыслов:\n" +
                "гжельской керамики, вологодских кружев, федоскинской миниатюры, хохломской росписи.\n" +
                "Цветущие акации, яблони, березы, дуб.\n" +
                "Поют девушки из ансамбля \"Русская песня\".\n" +
                "Кружевницы плетут кружева коклюшками.\n" +
                "Изделия федоскинской миниатюры.\n" +
                "Шкатулка \"Конек Горбунок\". Жостовские подносы.\n" +
                "Изделия из гжели: чайный сервиз, игрушки.\n" +
                "Работают художники в цехе объединения \"Гжель\".\n" +
                "Изделия из хохломы. Женщины в цехе за росписью изделий.\n" +
                "Витрины магазина \"Русский сувенир\".\n" +
                "Покупатели в магазине.", "Очно в Нижнем Новгороде", "Рукоделие", "19-25", "https://www.net-film.ru/film-9094/", false));
    }};

    private List<Movie> filteredPopularMovies = new ArrayList<>();
    private List<Movie> filteredOtherMovies = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nestedScrollView = view.findViewById(R.id.nested_scroll_view);

        searchView = view.findViewById(R.id.search_view);
        locationChipGroup = view.findViewById(R.id.location_chip_group);
        interestsChipGroup = view.findViewById(R.id.interests_chip_group);
        ageChipGroup = view.findViewById(R.id.age_chip_group);
        popularRecyclerView = view.findViewById(R.id.popular_recycler_view);
        othersRecyclerView = view.findViewById(R.id.others_recycler_view);
        toggleFiltersButton = view.findViewById(R.id.toggle_filters_button);
        filtersContainer = view.findViewById(R.id.filters_container);

        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        othersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Initialize filtered lists
        for (Movie movie : allMovies) {
            if (movie.isPopular()) {
                filteredPopularMovies.add(movie);
            } else {
                filteredOtherMovies.add(movie);
            }
        }

        popularMoviesAdapter = new MovieAdapter(filteredPopularMovies, movie -> {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        otherMoviesAdapter = new MovieAdapter(filteredOtherMovies, movie -> {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        popularRecyclerView.setAdapter(popularMoviesAdapter);
        othersRecyclerView.setAdapter(otherMoviesAdapter);

        popularRecyclerView.setNestedScrollingEnabled(false);
        othersRecyclerView.setNestedScrollingEnabled(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMovies();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMovies();
                return true;
            }
        });

        toggleFiltersButton.setOnClickListener(v -> toggleFiltersVisibility());

        setupChipGroupListeners();
        updateSectionVisibility();
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
        locationChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterMovies());
        interestsChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterMovies());
        ageChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterMovies());
    }

    private void filterMovies() {
        String searchQuery = searchView.getQuery().toString().toLowerCase();
        Map<String, List<String>> selectedFilters = new HashMap<>();
        selectedFilters.put("location", getSelectedChipTexts(locationChipGroup));
        selectedFilters.put("interest", getSelectedChipTexts(interestsChipGroup));
        selectedFilters.put("age", getSelectedChipTexts(ageChipGroup));

        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : allMovies) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    movie.getTitle().toLowerCase().contains(searchQuery) ||
                    movie.getDescription().toLowerCase().contains(searchQuery);

            boolean matchesFilters = true;
            for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
                String type = entry.getKey();
                List<String> selectedValues = entry.getValue();

                if (!selectedValues.isEmpty()) {
                    if (type.equals("location")) {
                        matchesFilters = selectedValues.contains(movie.getLocation());
                    } else if (type.equals("interest")) {
                        matchesFilters = selectedValues.contains(movie.getCategory());
                    } else if (type.equals("age")) {
                        matchesFilters = selectedValues.contains(movie.getAgeGroup());
                    }
                    if (!matchesFilters) break;
                }
            }

            if (matchesSearch && matchesFilters) {
                filteredMovies.add(movie);
            }
        }

        filteredPopularMovies.clear();
        filteredOtherMovies.clear();
        for (Movie movie : filteredMovies) {
            if (movie.isPopular()) {
                filteredPopularMovies.add(movie);
            } else {
                filteredOtherMovies.add(movie);
            }
        }

        popularMoviesAdapter.updateMovies(filteredPopularMovies);
        otherMoviesAdapter.updateMovies(filteredOtherMovies);

        updateSectionVisibility();
    }

    private void updateSectionVisibility() {
        View view = getView();
        if (view == null) return;

        setVisibility(view, R.id.tv_popular_header, !filteredPopularMovies.isEmpty());
        setVisibility(view, R.id.popular_recycler_view, !filteredPopularMovies.isEmpty());
        setVisibility(view, R.id.tv_others_header, !filteredOtherMovies.isEmpty());
        setVisibility(view, R.id.others_recycler_view, !filteredOtherMovies.isEmpty());
    }

    private void setVisibility(View parentView, int viewId, boolean isVisible) {
        View view = parentView.findViewById(viewId);
        if (view != null) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
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