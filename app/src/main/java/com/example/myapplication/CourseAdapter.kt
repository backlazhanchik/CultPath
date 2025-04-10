package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private var courses: List<Course>,
    private val onCourseClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_course_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_course_description)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_course_category)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_course_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        
        holder.tvTitle.text = course.title
        holder.tvDescription.text = course.description
        holder.tvCategory.text = course.category
        holder.tvLocation.text = course.location
        
        holder.itemView.setOnClickListener {
            onCourseClick(course)
        }
    }

    override fun getItemCount(): Int = courses.size

    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}
