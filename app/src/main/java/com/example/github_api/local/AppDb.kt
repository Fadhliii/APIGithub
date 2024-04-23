package com.example.github_api.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github_api.API.MODEL.Item

@Database(entities = [Item::class], version =1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    abstract fun userDao(): UserDao
}