package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE id = :id LIMIT 1")
    suspend fun getMenuItemById(id: Long): MenuItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItem>)

    @Update
    suspend fun update(item: MenuItem)

    @Delete
    suspend fun delete(item: MenuItem)

    @Query("UPDATE menu_items SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateAvailability(id: Long, isAvailable: Boolean)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun count(): Int
}
