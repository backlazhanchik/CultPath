package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.example.culturalapp.database.DatabaseHelper

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1
    private var userEmail: String = ""
    private var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvName = view.findViewById(R.id.tv_name)
        tvEmail = view.findViewById(R.id.tv_email)
        tabLayout = view.findViewById(R.id.tab_layout)
        recyclerView = view.findViewById(R.id.recycler_view)


        databaseHelper = DatabaseHelper(requireContext())


        val sharedPref = requireActivity().getSharedPreferences("CulturalAppPrefs", 0)
        userId = sharedPref.getLong("USER_ID", -1)
        userEmail = sharedPref.getString("USER_EMAIL", "") ?: ""
        

        if (userId != -1L) {
            val user = databaseHelper.getUserById(userId)
            userName = "${user.firstName} ${user.lastName}"
        }


        tvName.text = userName
        tvEmail.text = userEmail


        recyclerView.layoutManager = LinearLayoutManager(context)
        courseAdapter = CourseAdapter(emptyList()) { course ->

            val fragment = CourseDetailFragment.newInstance(course)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = courseAdapter

        setupTabLayout()
    }

    private fun setupTabLayout() {

        tabLayout.addTab(tabLayout.newTab().setText("Прохождение"))
        tabLayout.addTab(tabLayout.newTab().setText("Избранное"))


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateCourseList(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        updateCourseList(0)
    }

    private fun updateCourseList(tabPosition: Int) {
        if (userId == -1L) return

        val courses = if (tabPosition == 0) {
            databaseHelper.getWalkthroughCourses(userId)
        } else {
            databaseHelper.getFavoriteCourses(userId)
        }

        courseAdapter.updateCourses(courses)
    }
}
