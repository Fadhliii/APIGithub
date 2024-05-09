package com.example.github_api.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github_api.API.MODEL.Item
/**
 * Abstract class representing the application's database.
 * This class extends RoomDatabase and provides DAOs for the database.
 */
@Database(entities = [Item::class], version =1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    //call the UserDao interface to interact with the database.
    abstract fun userDao(): UserDao
}