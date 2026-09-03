package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Long): Order?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order): Long

    @Delete
    suspend fun delete(order: Order)

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun count(): Int

    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = 'Selesai'")
    fun getTotalRevenue(): Flow<Long?>

    @Query("DELETE FROM orders")
    suspend fun clearAll()
}
