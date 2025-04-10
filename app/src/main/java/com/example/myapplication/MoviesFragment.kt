package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MoviesFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var locationChipGroup: ChipGroup
    private lateinit var interestsChipGroup: ChipGroup
    private lateinit var ageChipGroup: ChipGroup
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var othersRecyclerView: RecyclerView
    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var otherMoviesAdapter: MovieAdapter
    private lateinit var toggleFiltersButton: MaterialButton
    private lateinit var filtersContainer: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView
    private var areFiltersVisible = true

    private val allMovies = listOf(
        Movie(1, "Древнейшее искусство России", "Ученые раскрывают тайны и смыслы наскальной живописи. Док об истории древнейшей культуры России", "Онлайн", "Исскувсство", "13-18", "https://www.kinopoisk.ru/series/5234123/?utm_referrer=www.google.comx", true),
        Movie(2, "Студия Нижний", "История жизни и любви талантливого фотографа Никиты Пирогова. Действие картины разворачивается в настоящем, прошлом и будущем Нижнего Новгорода. Фильм наполнен музыкой, творчеством, городскими достопримечательностями и нижегородскими историями.", "Очно в Нижнем Новгороде", "История", "19-25", "https://www.kinopoisk.ru/film/767272/", true),
        Movie(3, "Арт и Факт", "Какие секреты таят в себе полотна знаменитых художников? Новеллы о шедеврах из собрания Третьяковской галереи", "Онлайн", "Искувсство", "13-18", "https://www.kinopoisk.ru/series/5304281/", true),
        Movie(4, "Лесная симфония", "Жизнь фауны финских лесов — от насекомых до медведей. Впечатляющий док с отличной операторской работой", "Онлайн", "Фоклер", "13-18", "https://www.kinopoisk.ru/film/664849/", false),
        Movie(5, "Народные промыслы России", "Фильм о развитии и сохранении в России народных промыслов:\n" +
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
                "Покупатели в магазине.", "Очно в Нижнем Новгороде", "Ремесло", "19-25", "https://www.net-film.ru/film-9094/", false),
    )

    private var filteredPopularMovies = allMovies.filter { it.isPopular }
    private var filteredOtherMovies = allMovies.filter { !it.isPopular }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        nestedScrollView = view.findViewById(R.id.nested_scroll_view)

        searchView = view.findViewById(R.id.search_view)
        locationChipGroup = view.findViewById(R.id.location_chip_group)
        interestsChipGroup = view.findViewById(R.id.interests_chip_group)
        ageChipGroup = view.findViewById(R.id.age_chip_group)
        popularRecyclerView = view.findViewById(R.id.popular_recycler_view)
        othersRecyclerView = view.findViewById(R.id.others_recycler_view)
        toggleFiltersButton = view.findViewById(R.id.toggle_filters_button)
        filtersContainer = view.findViewById(R.id.filters_container)

        popularRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        othersRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        popularMoviesAdapter = MovieAdapter(filteredPopularMovies) { movie ->
            val fragment = MovieDetailFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        otherMoviesAdapter = MovieAdapter(filteredOtherMovies) { movie ->
            val fragment = MovieDetailFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        popularRecyclerView.adapter = popularMoviesAdapter
        othersRecyclerView.adapter = otherMoviesAdapter


        popularRecyclerView.isNestedScrollingEnabled = false
        othersRecyclerView.isNestedScrollingEnabled = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMovies()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies()
                return true
            }
        })

        toggleFiltersButton.setOnClickListener {
            toggleFiltersVisibility()
        }

        setupChipGroupListeners()
        updateSectionVisibility()
    }


    private fun toggleFiltersVisibility() {
        areFiltersVisible = !areFiltersVisible
        TransitionManager.beginDelayedTransition(filtersContainer.parent as ViewGroup)

        if (areFiltersVisible) {
            filtersContainer.visibility = View.VISIBLE
            toggleFiltersButton.text = "Скрыть"
            toggleFiltersButton.setIconResource(R.drawable.ic_arrow_up)
        } else {
            filtersContainer.visibility = View.GONE
            toggleFiltersButton.text = "Развернуть"
            toggleFiltersButton.setIconResource(R.drawable.ic_arrow_down)
        }
    }

    private fun setupChipGroupListeners() {
        locationChipGroup.setOnCheckedStateChangeListener { _, _ -> filterMovies() }
        interestsChipGroup.setOnCheckedStateChangeListener { _, _ -> filterMovies() }
        ageChipGroup.setOnCheckedStateChangeListener { _, _ -> filterMovies() }
    }

    private fun filterMovies() {
        val searchQuery = searchView.query.toString().lowercase()
        val selectedFilters = mapOf(
            "location" to getSelectedChipTexts(locationChipGroup),
            "interest" to getSelectedChipTexts(interestsChipGroup),
            "age" to getSelectedChipTexts(ageChipGroup)
        )

        val filteredMovies = allMovies.filter { movie ->
            val matchesSearch = searchQuery.isEmpty() ||
                    movie.title.contains(searchQuery, ignoreCase = true) ||
                    movie.description.contains(searchQuery, ignoreCase = true)

            val matchesFilters = selectedFilters.all { (type, selectedValues) ->
                selectedValues.isEmpty() || when (type) {
                    "location" -> selectedValues.contains(movie.location)
                    "interest" -> selectedValues.contains(movie.category)
                    "age" -> selectedValues.contains(movie.ageGroup)
                    else -> true
                }
            }

            matchesSearch && matchesFilters
        }

        filteredPopularMovies = filteredMovies.filter { it.isPopular }
        filteredOtherMovies = filteredMovies.filter { !it.isPopular }

        popularMoviesAdapter.updateMovies(filteredPopularMovies)
        otherMoviesAdapter.updateMovies(filteredOtherMovies)

        updateSectionVisibility()
    }

    private fun updateSectionVisibility() {
        fun setVisibility(viewId: Int, isVisible: Boolean) {
            view?.findViewById<View>(viewId)?.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        setVisibility(R.id.tv_popular_header, filteredPopularMovies.isNotEmpty())
        setVisibility(R.id.popular_recycler_view, filteredPopularMovies.isNotEmpty())
        setVisibility(R.id.tv_others_header, filteredOtherMovies.isNotEmpty())
        setVisibility(R.id.others_recycler_view, filteredOtherMovies.isNotEmpty())
    }

    private fun getSelectedChipTexts(chipGroup: ChipGroup): List<String> {
        val selectedChipTexts = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChipTexts.add(chip.text.toString())
            }
        }

        return selectedChipTexts
    }
}