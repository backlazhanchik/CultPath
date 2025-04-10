package com.example.culturalapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.Course
import com.example.myapplication.Movie
import com.example.myapplication.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CulturalApp.db"

        // User table
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        // Interests table
        private const val TABLE_INTERESTS = "interests"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_INTEREST = "interest"
        private const val COLUMN_LEVEL = "level"
        private const val COLUMN_PURPOSE = "purpose"
        private const val COLUMN_COMMENT = "comment"

        // Courses table
        private const val TABLE_COURSES = "courses"
        private const val COLUMN_COURSE_ID = "course_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_AGE_GROUP = "age_group"
        private const val COLUMN_LINK = "link"

        // Favorite courses table
        private const val TABLE_FAVORITE_COURSES = "favorite_courses"

        // Walkthrough courses table
        private const val TABLE_WALKTHROUGH_COURSES = "walkthrough_courses"
        
        // Movies table
        private const val TABLE_MOVIES = "movies"
        private const val COLUMN_MOVIE_ID = "movie_id"
        private const val COLUMN_IS_POPULAR = "is_popular"
        
        // Favorite movies table
        private const val TABLE_FAVORITE_MOVIES = "favorite_movies"
        
        // Walkthrough movies table
        private const val TABLE_WALKTHROUGH_MOVIES = "walkthrough_movies"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create users table
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")")
        db.execSQL(createUsersTable)

        // Create interests table
        val createInterestsTable = ("CREATE TABLE " + TABLE_INTERESTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_INTEREST + " TEXT,"
                + COLUMN_LEVEL + " TEXT,"
                + COLUMN_PURPOSE + " TEXT,"
                + COLUMN_COMMENT + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))")
        db.execSQL(createInterestsTable)

        // Create courses table
        val createCoursesTable = ("CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_AGE_GROUP + " TEXT,"
                + COLUMN_LINK + " TEXT" + ")")
        db.execSQL(createCoursesTable)

        // Create favorite courses table
        val createFavoriteCoursesTable = ("CREATE TABLE " + TABLE_FAVORITE_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COURSE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))")
        db.execSQL(createFavoriteCoursesTable)

        // Create walkthrough courses table
        val createWalkthroughCoursesTable = ("CREATE TABLE " + TABLE_WALKTHROUGH_COURSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_COURSE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))")
        db.execSQL(createWalkthroughCoursesTable)
        
        // Create movies table
        val createMoviesTable = ("CREATE TABLE " + TABLE_MOVIES + "("
                + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_AGE_GROUP + " TEXT,"
                + COLUMN_LINK + " TEXT,"
                + COLUMN_IS_POPULAR + " INTEGER" + ")")
        db.execSQL(createMoviesTable)
        
        // Create favorite movies table
        val createFavoriteMoviesTable = ("CREATE TABLE " + TABLE_FAVORITE_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_MOVIE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "))")
        db.execSQL(createFavoriteMoviesTable)
        
        // Create walkthrough movies table
        val createWalkthroughMoviesTable = ("CREATE TABLE " + TABLE_WALKTHROUGH_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_MOVIE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "))")
        db.execSQL(createWalkthroughMoviesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WALKTHROUGH_MOVIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_MOVIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WALKTHROUGH_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INTERESTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User methods
    
    fun addUser(firstName: String, lastName: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_FIRST_NAME, firstName)
        values.put(COLUMN_LAST_NAME, lastName)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        
        // Insert row
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        
        val cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        
        return cursorCount > 0
    }

    fun getUserIdByEmail(email: String): Long {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        
        var userId: Long = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun getUserById(userId: Long): User {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        
        var user = User("", "", "", "")
        if (cursor.moveToFirst()) {
            user = User(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    // Interest methods
    
    fun saveInterest(userId: Long, interest: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_ID, userId)
        values.put(COLUMN_INTEREST, interest)
        
        db.insert(TABLE_INTERESTS, null, values)
        db.close()
    }

    fun updateInterestLevel(userId: Long, interest: String, level: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_LEVEL, level)
        
        db.update(
            TABLE_INTERESTS,
            values,
            "$COLUMN_USER_ID = ? AND $COLUMN_INTEREST = ?",
            arrayOf(userId.toString(), interest)
        )
        db.close()
    }

    fun saveLearningPurpose(userId: Long, purpose: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PURPOSE, purpose)
        
        db.update(
            TABLE_INTERESTS,
            values,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
    }

    fun saveComment(userId: Long, comment: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_COMMENT, comment)
        
        db.update(
            TABLE_INTERESTS,
            values,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
    }

    fun getUserInterests(userId: Long): List<String> {
        val interests = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_INTERESTS,
            arrayOf(COLUMN_INTEREST),
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        
        if (cursor.moveToFirst()) {
            do {
                interests.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INTEREST)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return interests
    }

    // Course methods
    
    private fun addCourseIfNotExists(course: Course): Long {
        val db = this.writableDatabase
        
        // Check if course already exists
        val cursor = db.query(
            TABLE_COURSES,
            arrayOf(COLUMN_COURSE_ID),
            "$COLUMN_COURSE_ID = ?",
            arrayOf(course.id.toString()),
            null,
            null,
            null
        )
        
        val courseExists = cursor.count > 0
        cursor.close()
        
        if (!courseExists) {
            val values = ContentValues()
            values.put(COLUMN_COURSE_ID, course.id)
            values.put(COLUMN_TITLE, course.title)
            values.put(COLUMN_DESCRIPTION, course.description)
            values.put(COLUMN_LOCATION, course.location)
            values.put(COLUMN_CATEGORY, course.category)
            values.put(COLUMN_AGE_GROUP, course.ageGroup)
            values.put(COLUMN_LINK, course.link)
            
            db.insert(TABLE_COURSES, null, values)
        }
        
        db.close()
        return course.id.toLong()
    }

    fun addCourseToFavorites(userId: Long, course: Course) {
        val courseId = addCourseIfNotExists(course)
        
        val db = this.writableDatabase
        
        // Check if already in favorites
        val cursor = db.query(
            TABLE_FAVORITE_COURSES,
            null,
            "$COLUMN_USER_ID = ? AND $COLUMN_COURSE_ID = ?",
            arrayOf(userId.toString(), courseId.toString()),
            null,
            null,
            null
        )
        
        val alreadyInFavorites = cursor.count > 0
        cursor.close()
        
        if (!alreadyInFavorites) {
            val values = ContentValues()
            values.put(COLUMN_USER_ID, userId)
            values.put(COLUMN_COURSE_ID, courseId)
            
            db.insert(TABLE_FAVORITE_COURSES, null, values)
        }
        
        db.close()
    }

    fun addCourseToWalkthrough(userId: Long, course: Course) {
        val courseId = addCourseIfNotExists(course)
        
        val db = this.writableDatabase
        
        // Check if already in walkthrough
        val cursor = db.query(
            TABLE_WALKTHROUGH_COURSES,
            null,
            "$COLUMN_USER_ID = ? AND $COLUMN_COURSE_ID = ?",
            arrayOf(userId.toString(), courseId.toString()),
            null,
            null,
            null
        )
        
        val alreadyInWalkthrough = cursor.count > 0
        cursor.close()
        
        if (!alreadyInWalkthrough) {
            val values = ContentValues()
            values.put(COLUMN_USER_ID, userId)
            values.put(COLUMN_COURSE_ID, courseId)
            
            db.insert(TABLE_WALKTHROUGH_COURSES, null, values)
        }
        
        db.close()
    }

    fun getFavoriteCourses(userId: Long): List<Course> {
        val courses = mutableListOf<Course>()
        val db = this.readableDatabase
        
        val query = """
            SELECT c.* FROM $TABLE_COURSES c
            INNER JOIN $TABLE_FAVORITE_COURSES fc ON c.$COLUMN_COURSE_ID = fc.$COLUMN_COURSE_ID
            WHERE fc.$COLUMN_USER_ID = ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        
        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                    true,
                    false
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        db.close()
        return courses
    }

    fun getWalkthroughCourses(userId: Long): List<Course> {
        val courses = mutableListOf<Course>()
        val db = this.readableDatabase
        
        val query = """
            SELECT c.* FROM $TABLE_COURSES c
            INNER JOIN $TABLE_WALKTHROUGH_COURSES wc ON c.$COLUMN_COURSE_ID = wc.$COLUMN_COURSE_ID
            WHERE wc.$COLUMN_USER_ID = ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        
        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                    false,
                    true
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        db.close()
        return courses
    }
    
    // Movie methods
    
    private fun addMovieIfNotExists(movie: Movie): Long {
        val db = this.writableDatabase
        
        // Check if movie already exists
        val cursor = db.query(
            TABLE_MOVIES,
            arrayOf(COLUMN_MOVIE_ID),
            "$COLUMN_MOVIE_ID = ?",
            arrayOf(movie.id.toString()),
            null,
            null,
            null
        )
        
        val movieExists = cursor.count > 0
        cursor.close()
        
        if (!movieExists) {
            val values = ContentValues()
            values.put(COLUMN_MOVIE_ID, movie.id)
            values.put(COLUMN_TITLE, movie.title)
            values.put(COLUMN_DESCRIPTION, movie.description)
            values.put(COLUMN_LOCATION, movie.location)
            values.put(COLUMN_CATEGORY, movie.category)
            values.put(COLUMN_AGE_GROUP, movie.ageGroup)
            values.put(COLUMN_LINK, movie.link)
            values.put(COLUMN_IS_POPULAR, if (movie.isPopular) 1 else 0)
            
            db.insert(TABLE_MOVIES, null, values)
        }
        
        db.close()
        return movie.id.toLong()
    }
    
    fun addMovieToFavorites(userId: Long, movie: Movie) {
        val movieId = addMovieIfNotExists(movie)
        
        val db = this.writableDatabase
        
        // Check if already in favorites
        val cursor = db.query(
            TABLE_FAVORITE_MOVIES,
            null,
            "$COLUMN_USER_ID = ? AND $COLUMN_MOVIE_ID = ?",
            arrayOf(userId.toString(), movieId.toString()),
            null,
            null,
            null
        )
        
        val alreadyInFavorites = cursor.count > 0
        cursor.close()
        
        if (!alreadyInFavorites) {
            val values = ContentValues()
            values.put(COLUMN_USER_ID, userId)
            values.put(COLUMN_MOVIE_ID, movieId)
            
            db.insert(TABLE_FAVORITE_MOVIES, null, values)
        }
        
        db.close()
    }
    
    fun addMovieToWalkthrough(userId: Long, movie: Movie) {
        val movieId = addMovieIfNotExists(movie)
        
        val db = this.writableDatabase
        
        // Check if already in walkthrough
        val cursor = db.query(
            TABLE_WALKTHROUGH_MOVIES,
            null,
            "$COLUMN_USER_ID = ? AND $COLUMN_MOVIE_ID = ?",
            arrayOf(userId.toString(), movieId.toString()),
            null,
            null,
            null
        )
        
        val alreadyInWalkthrough = cursor.count > 0
        cursor.close()
        
        if (!alreadyInWalkthrough) {
            val values = ContentValues()
            values.put(COLUMN_USER_ID, userId)
            values.put(COLUMN_MOVIE_ID, movieId)
            
            db.insert(TABLE_WALKTHROUGH_MOVIES, null, values)
        }
        
        db.close()
    }
    
    fun getFavoriteMovies(userId: Long): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = this.readableDatabase
        
        val query = """
            SELECT m.* FROM $TABLE_MOVIES m
            INNER JOIN $TABLE_FAVORITE_MOVIES fm ON m.$COLUMN_MOVIE_ID = fm.$COLUMN_MOVIE_ID
            WHERE fm.$COLUMN_USER_ID = ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        
        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_POPULAR)) == 1,
                    true,
                    false
                )
                movies.add(movie)
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        db.close()
        return movies
    }
    
    fun getWalkthroughMovies(userId: Long): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = this.readableDatabase
        
        val query = """
            SELECT m.* FROM $TABLE_MOVIES m
            INNER JOIN $TABLE_WALKTHROUGH_MOVIES wm ON m.$COLUMN_MOVIE_ID = wm.$COLUMN_MOVIE_ID
            WHERE wm.$COLUMN_USER_ID = ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        
        if (cursor.moveToFirst()) {
            do {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE_GROUP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_POPULAR)) == 1,
                    false,
                    true
                )
                movies.add(movie)
            } while (cursor.moveToNext())
        }
        
        cursor.close()
        db.close()
        return movies
    }
}
