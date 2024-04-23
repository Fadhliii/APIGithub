package com.example.github_api.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_api.API.MODEL.Item
import retrofit2.http.DELETE

@Dao
interface UserDao {
    // If a user with the same primary key already exists, it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: Item)

    // use to fetch all users from the database.
    @Query("SELECT*FROM User")
    fun loadALl(): LiveData<MutableList<Item>>

    //used to find a user by their id in the database.
    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Item

    // used to delete a specific user from the database.
    @Delete
    fun delete(user: Item)
}