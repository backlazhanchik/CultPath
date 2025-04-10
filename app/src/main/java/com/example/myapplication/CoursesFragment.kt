package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CoursesFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var locationChipGroup: ChipGroup
    private lateinit var interestsChipGroup: ChipGroup
    private lateinit var ageChipGroup: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var toggleFiltersButton: MaterialButton
    private lateinit var filtersContainer: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView
    private var areFiltersVisible = true

    private val allCourses = listOf(
        Course(1, "Курс ораторского ремесла", "Эффективные\n" +
                "коммуникации\n" +
                "и публичные выступления", "Онлайн", "Искувсство", "19-25", "https://www.example.com/art-history"),
        Course(2, "Русский фольклор", "Изучение истории фольклора. Русские народные традиции. Фольклорные жанры: сказки, былины, загадки, пословицы и поговорки, анекдоты и др.", "Онлайн", "Фольклор", "13-18", "https://linguarus.education/courses/folklore/"),
        Course(3, "Онлайн-курс по практическим вопросам интеллектуальной собственности", "Со 2 октября 2025 года любой желающий может получить бесплатные видеолекции о том, как правильно защитить свою интеллектуальную собственность и получать доход от ее использования. Чтобы стать участником онлайн-курса, необходимо пройти регистрацию на сайте и получить доступ к видеолекциям от спикеров Фестиваля ВОИР: Наука и изобретения для жизни.", "Онлайн", "Научные изобретения", "19-25", "https://voirfest.ru/group/online/477b098d-d7bf-4dd7-b061-4591356e0f0f?tab=about"),
        Course(4, "Студия Центра Театрального Мастерства", " образовательная платформа и сообщество людей, неравнодушных к театральному искусству и культурной жизни города. Здесь мы поможем раскрыть вашу личность через обучение актерскому мастерству.", "Очно в Нижнем Новгороде", "Искувсство", "13-18", "https://ctm-nn.ru/repertuar/akterskaya-masterskaya/"),
    )

    private var filteredCourses = allCourses

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        nestedScrollView = view.findViewById(R.id.nested_scroll_view)

        searchView = view.findViewById(R.id.search_view)
        locationChipGroup = view.findViewById(R.id.location_chip_group)
        interestsChipGroup = view.findViewById(R.id.interests_chip_group)
        ageChipGroup = view.findViewById(R.id.age_chip_group)
        recyclerView = view.findViewById(R.id.recycler_view)
        toggleFiltersButton = view.findViewById(R.id.toggle_filters_button)
        filtersContainer = view.findViewById(R.id.filters_container)


        recyclerView.layoutManager = LinearLayoutManager(context)
        courseAdapter = CourseAdapter(filteredCourses) { course ->
            val fragment = CourseDetailFragment.newInstance(course)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = courseAdapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterCourses()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCourses()
                return true
            }
        })

        toggleFiltersButton.setOnClickListener {
            toggleFiltersVisibility()
        }

        setupChipGroupListeners()
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
        locationChipGroup.setOnCheckedStateChangeListener { _, _ -> filterCourses() }
        interestsChipGroup.setOnCheckedStateChangeListener { _, _ -> filterCourses() }
        ageChipGroup.setOnCheckedStateChangeListener { _, _ -> filterCourses() }
    }

    private fun filterCourses() {
        val searchQuery = searchView.query.toString().lowercase()
        val selectedFilters = mapOf(
            "location" to getSelectedChipTexts(locationChipGroup),
            "interest" to getSelectedChipTexts(interestsChipGroup),
            "age" to getSelectedChipTexts(ageChipGroup)
        )

        filteredCourses = allCourses.filter { course ->
            val matchesSearch = searchQuery.isEmpty() ||
                    course.title.contains(searchQuery, ignoreCase = true) ||
                    course.description.contains(searchQuery, ignoreCase = true)

            val matchesFilters = selectedFilters.all { (type, selectedValues) ->
                selectedValues.isEmpty() || when (type) {
                    "location" -> selectedValues.contains(course.location)
                    "interest" -> selectedValues.contains(course.category)
                    "age" -> selectedValues.contains(course.ageGroup)
                    else -> true
                }
            }

            matchesSearch && matchesFilters
        }

        courseAdapter.updateCourses(filteredCourses)
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