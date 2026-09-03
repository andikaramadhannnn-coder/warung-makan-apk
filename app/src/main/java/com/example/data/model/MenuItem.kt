package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Makanan", "Minuman", "Paket", "Tambahan"
    val price: Long,
    val description: String = "",
    val isAvailable: Boolean = true,
    val iconType: String = "makanan"
)
