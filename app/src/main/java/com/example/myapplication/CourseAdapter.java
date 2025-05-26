package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses;
    private final OnCourseClickListener onCourseClick;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CourseAdapter(List<Course> courses, OnCourseClickListener onCourseClick) {
        this.courses = courses;
        this.onCourseClick = onCourseClick;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvCategory;
        final TextView tvLocation;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_course_title);
            tvDescription = itemView.findViewById(R.id.tv_course_description);
            tvCategory = itemView.findViewById(R.id.tv_course_category);
            tvLocation = itemView.findViewById(R.id.tv_course_location);
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.tvTitle.setText(course.getTitle());
        holder.tvDescription.setText(course.getDescription());
        holder.tvCategory.setText(course.getCategory());
        holder.tvLocation.setText(course.getLocation());

        holder.itemView.setOnClickListener(v -> {
            onCourseClick.onCourseClick(course);
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateCourses(List<Course> newCourses) {
        this.courses = newCourses;
        notifyDataSetChanged();
    }
}