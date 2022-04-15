package com.example.mealplanner

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

//creating the database logic, extending the SQLiteOpenHelper base class
class DataBaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MealsDatabase"

        private val TABLE_MEALS = "MealsTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_MEALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT" + ")")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_MEALS")
        onCreate(db)
    }
    /**
     * Function to insert data
     */
    fun addMeal(meal: MealDataClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, meal.name) // MealDataClass Name
        contentValues.put(KEY_DESCRIPTION, meal.description) // MealDataClass Email

        // Inserting meal details using insert query.
        val success = db.insert(TABLE_MEALS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }
    //Method to read the records from database in form of ArrayList
    @SuppressLint("Range")
    fun viewMeal(): ArrayList<MealDataClass> {

        val mealList: ArrayList<MealDataClass> = ArrayList<MealDataClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_MEALS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var description: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))

                val meal = MealDataClass(id = id, name = name, description = description)
                mealList.add(meal)

            } while (cursor.moveToNext())
        }
        return mealList
    }
    /**
     * Function to update record
     */
    fun updateMeal(meal: MealDataClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, meal.name) // MealDataClass Name
        contentValues.put(KEY_DESCRIPTION, meal.description) // MealDataClass Email

        // Updating Row
        val success = db.update(TABLE_MEALS, contentValues, KEY_ID + "=" + meal.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
    /**
     * Function to delete record
     */
    fun deleteMeal(meal: MealDataClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, meal.id) // MealDataClass id
        // Deleting Row
        val success = db.delete(TABLE_MEALS, KEY_ID + "=" + meal.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }
}