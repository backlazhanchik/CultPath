package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private final int id;
    private final String title;
    private final String description;
    private final String location;
    private final String category;
    private final String ageGroup;
    private final String link;
    private final boolean isPopular;
    private boolean isFavorite;
    private boolean isInWalkthrough;

    public Movie(int id, String title, String description, String location,
                 String category, String ageGroup, String link, boolean b) {
        this(id, title, description, location, category, ageGroup, link, false, false, false);
    }

    public Movie(int id, String title, String description, String location,
                 String category, String ageGroup, String link,
                 boolean isPopular, boolean isFavorite, boolean isInWalkthrough) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.ageGroup = ageGroup;
        this.link = link;
        this.isPopular = isPopular;
        this.isFavorite = isFavorite;
        this.isInWalkthrough = isInWalkthrough;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        category = in.readString();
        ageGroup = in.readString();
        link = in.readString();
        isPopular = in.readByte() != 0;
        isFavorite = in.readByte() != 0;
        isInWalkthrough = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
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
        dest.writeByte((byte) (isPopular ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isInWalkthrough ? 1 : 0));
    }

    // Getters
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

    public boolean isPopular() {
        return isPopular;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isInWalkthrough() {
        return isInWalkthrough;
    }

    // Setters for mutable properties
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setInWalkthrough(boolean inWalkthrough) {
        isInWalkthrough = inWalkthrough;
    }
}