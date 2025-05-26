package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String location;
    private String category;
    private String ageGroup;
    private String link;
    private boolean isFavorite;
    private boolean isInWalkthrough;

    public Course(int id, String title, String description, String location,
                  String category, String ageGroup, String link) {
        this(id, title, description, location, category, ageGroup, link, false, false);
    }

    public Course(int id, String title, String description, String location,
                  String category, String ageGroup, String link,
                  boolean isFavorite, boolean isInWalkthrough) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.ageGroup = ageGroup;
        this.link = link;
        this.isFavorite = isFavorite;
        this.isInWalkthrough = isInWalkthrough;
    }

    protected Course(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        category = in.readString();
        ageGroup = in.readString();
        link = in.readString();
        isFavorite = in.readByte() != 0;
        isInWalkthrough = in.readByte() != 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(category);
        dest.writeString(ageGroup);
        dest.writeString(link);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isInWalkthrough ? 1 : 0));
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getLink() {
        return link;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isInWalkthrough() {
        return isInWalkthrough;
    }

    public void setInWalkthrough(boolean inWalkthrough) {
        isInWalkthrough = inWalkthrough;
    }
}