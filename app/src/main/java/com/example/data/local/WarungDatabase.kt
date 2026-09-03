package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MenuItem
import com.example.data.model.Order

@Database(
    entities = [MenuItem::class, Order::class],
    version = 1,
    exportSchema = false
)
abstract class WarungDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: WarungDatabase? = null

        fun getDatabase(context: Context): WarungDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WarungDatabase::class.java,
                    "warung_niswa_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
