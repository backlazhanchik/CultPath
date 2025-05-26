package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Course;
import com.example.myapplication.Movie;
import com.example.myapplication.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CulturalApp.db";

    // User table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Interests table
    private static final String TABLE_INTERESTS = "interests";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_INTEREST = "interest";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_PURPOSE = "purpose";
    private static final String COLUMN_COMMENT = "comment";

    // Courses table
    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_COURSE_ID = "course_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AGE_GROUP = "age_group";
    private static final String COLUMN_LINK = "link";

    // Favorite courses table
    private static final String TABLE_FAVORITE_COURSES = "favorite_courses";

    // Walkthrough courses table
    private static final String TABLE_WALKTHROUGH_COURSES = "walkthrough_courses";

    // Movies table
    private static final String TABLE_MOVIES = "movies";
    private static final String COLUMN_MOVIE_ID = "movie_id";
    private static final String COLUMN_IS_POPULAR = "is_popular";

    // Favorite movies table
    private static final String TABLE_FAVORITE_MOVIES = "favorite_movies";

    // Walkthrough movies table
    private static final String TABLE_WALKTHROUGH_MOVIES = "walkthrough_movies";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(createUsersTable);

        // Create interests table
        String createInterestsTable = "CREATE TABLE " + TABLE_INTERESTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_INTEREST + " TEXT,"
                + COLUMN_LEVEL + " TEXT,"
                + COLUMN_PURPOSE + " TEXT,"
                + COLUMN_COMMENT + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createInterestsTable);

        // Create courses table
        String createCoursesTable = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_AGE_GROUP + " TEXT,"
                + COLUMN_LINK + " TEXT" + ")";
        db.execSQL(createCoursesTable);

        // Create favorite courses table
        String createFavoriteCoursesTable = "CREATE TABLE " + TABLE_FAVORITE_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COURSE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))";
        db.execSQL(createFavoriteCoursesTable);

        // Create walkthrough courses table
        String createWalkthroughCoursesTable = "CREATE TABLE " + TABLE_WALKTHROUGH_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COURSE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))";
        db.execSQL(createWalkthroughCoursesTable);

        // Create movies table
        String createMoviesTable = "CREATE TABLE " + TABLE_MOVIES + "("
                + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_AGE_GROUP + " TEXT,"
                + COLUMN_LINK + " TEXT,"
                + COLUMN_IS_POPULAR + " INTEGER" + ")";
        db.execSQL(createMoviesTable);

        // Create favorite movies table
        String createFavoriteMoviesTable = "CREATE TABLE " + TABLE_FAVORITE_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_MOVIE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "))";
        db.execSQL(createFavoriteMoviesTable);

        // Create walkthrough movies table
        String createWalkthroughMoviesTable = "CREATE TABLE " + TABLE_WALKTHROUGH_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_MOVIE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "))";
        db.execSQL(createWalkthroughMoviesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALKTHROUGH_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALKTHROUGH_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User methods

    public long addUser(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        // Insert row
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        long userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public User getUserById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        User user = new User("", "", "", "");
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            );
        }
        cursor.close();
        db.close();
        return user;
    }

    // Interest methods

    public void saveInterest(long userId, String interest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_INTEREST, interest);

        db.insert(TABLE_INTERESTS, null, values);
        db.close();
    }

    public void updateInterestLevel(long userId, String interest, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LEVEL, level);

        db.update(
                TABLE_INTERESTS,
                values,
                COLUMN_USER_ID + " = ? AND " + COLUMN_INTEREST + " = ?",
                new String[]{String.valueOf(userId), interest}
        );
        db.close();
    }

    public void saveLearningPurpose(long userId, String purpose) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PURPOSE, purpose);

        db.update(
                TABLE_INTERESTS,
                values,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );
        db.close();
    }

    public void saveComment(long userId, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment);

        db.update(
                TABLE_INTERESTS,
                values,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );
        db.close();
    }

    public List<String> getUserInterests(long userId) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_INTERESTS,
                new String[]{COLUMN_INTEREST},
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                interests.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INTEREST)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return interests;
    }

    // Course methods

    private long addCourseIfNotExists(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if course already exists
        Cursor cursor = db.query(
                TABLE_COURSES,
                new String[]{COLUMN_COURSE_ID},
                COLUMN_COURSE_ID + " = ?",
                new String[]{String.valueOf(course.getId())},
                null,
                null,
                null
        );

        boolean courseExists = cursor.getCount() > 0;
        cursor.close();

        if (!courseExists) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COURSE_ID, course.getId());
            values.put(COLUMN_TITLE, course.getTitle());
            values.put(COLUMN_DESCRIPTION, course.getDescription());
            values.put(COLUMN_LOCATION, course.getLocation());
            values.put(COLUMN_CATEGORY, course.getCategory());
            values.put(COLUMN_AGE_GROUP, course.getAgeGroup());
            values.put(COLUMN_LINK, course.getLink());

            db.insert(TABLE_COURSES, null, values);
        }

        db.close();
        return course.getId();
    }

    public void addCourseToFavorites(long userId, Course course) {
        long courseId = addCourseIfNotExists(course);

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if already in favorites
        Cursor cursor = db.query(
                TABLE_FAVORITE_COURSES,
                null,
                COLUMN_USER_ID + " = ? AND " + COLUMN_COURSE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(courseId)},
                null,
                null,
                null
        );

        boolean alreadyInFavorites = cursor.getCount() > 0;
        cursor.close();

        if (!alreadyInFavorites) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_COURSE_ID, courseId);

            db.insert(TABLE_FAVORITE_COURSES, null, values);
        }

        db.close();
    }

    public void addCourseToWalkthrough(long userId, Course course) {
        long courseId = addCourseIfNotExists(course);

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if already in walkthrough
        Cursor cursor = db.query(
                TABLE_WALKTHROUGH_COURSES,
                null,
                COLUMN_USER_ID + " = ? AND " + COLUMN_COURSE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(courseId)},
                null,
                null,
                null
        );

        boolean alreadyInWalkthrough = cursor.getCount() > 0;
        cursor.close();

        if (!alreadyInWalkthrough) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_COURSE_ID, courseId);

            db.insert(TABLE_WALKTHROUGH_COURSES, null, values);
        }

        db.close();
    }

    public List<Course> getFavoriteCourses(long userId) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.* FROM " + TABLE_COURSES + " c " +
                "INNER JOIN " + TABLE_FAVORITE_COURSES + " fc ON c." + COLUMN_COURSE_ID + " = fc." + COLUMN_COURSE_ID + " " +
                "WHERE fc." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK))
                );
                courses.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courses;
    }

    public List<Course> getWalkthroughCourses(long userId) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.* FROM " + TABLE_COURSES + " c " +
                "INNER JOIN " + TABLE_WALKTHROUGH_COURSES + " wc ON c." + COLUMN_COURSE_ID + " = wc." + COLUMN_COURSE_ID + " " +
                "WHERE wc." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK))
                );
                courses.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courses;
    }

    // Movie methods

    private long addMovieIfNotExists(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if movie already exists
        Cursor cursor = db.query(
                TABLE_MOVIES,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getId())},
                null,
                null,
                null
        );

        boolean movieExists = cursor.getCount() > 0;
        cursor.close();

        if (!movieExists) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MOVIE_ID, movie.getId());
            values.put(COLUMN_TITLE, movie.getTitle());
            values.put(COLUMN_DESCRIPTION, movie.getDescription());
            values.put(COLUMN_LOCATION, movie.getLocation());
            values.put(COLUMN_CATEGORY, movie.getCategory());
            values.put(COLUMN_AGE_GROUP, movie.getAgeGroup());
            values.put(COLUMN_LINK, movie.getLink());
            values.put(COLUMN_IS_POPULAR, movie.isPopular() ? 1 : 0);

            db.insert(TABLE_MOVIES, null, values);
        }

        db.close();
        return movie.getId();
    }

    public void addMovieToFavorites(long userId, Movie movie) {
        long movieId = addMovieIfNotExists(movie);

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if already in favorites
        Cursor cursor = db.query(
                TABLE_FAVORITE_MOVIES,
                null,
                COLUMN_USER_ID + " = ? AND " + COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(movieId)},
                null,
                null,
                null
        );

        boolean alreadyInFavorites = cursor.getCount() > 0;
        cursor.close();

        if (!alreadyInFavorites) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_MOVIE_ID, movieId);

            db.insert(TABLE_FAVORITE_MOVIES, null, values);
        }

        db.close();
    }

    public void addMovieToWalkthrough(long userId, Movie movie) {
        long movieId = addMovieIfNotExists(movie);

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if already in walkthrough
        Cursor cursor = db.query(
                TABLE_WALKTHROUGH_MOVIES,
                null,
                COLUMN_USER_ID + " = ? AND " + COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(movieId)},
                null,
                null,
                null
        );

        boolean alreadyInWalkthrough = cursor.getCount() > 0;
        cursor.close();

        if (!alreadyInWalkthrough) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_MOVIE_ID, movieId);

            db.insert(TABLE_WALKTHROUGH_MOVIES, null, values);
        }

        db.close();
    }

    public List<Movie> getFavoriteMovies(long userId) {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.* FROM " + TABLE_MOVIES + " m " +
                "INNER JOIN " + TABLE_FAVORITE_MOVIES + " fm ON m." + COLUMN_MOVIE_ID + " = fm." + COLUMN_MOVIE_ID + " " +
                "WHERE fm." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_POPULAR)) == 1
                );
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movies;
    }

    public List<Movie> getWalkthroughMovies(long userId) {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.* FROM " + TABLE_MOVIES + " m " +
                "INNER JOIN " + TABLE_WALKTHROUGH_MOVIES + " wm ON m." + COLUMN_MOVIE_ID + " = wm." + COLUMN_MOVIE_ID + " " +
                "WHERE wm." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_POPULAR)) == 1
                );
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movies;
    }

}