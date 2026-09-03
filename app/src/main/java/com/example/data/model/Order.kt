package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

data class OrderItem(
    val menuItemId: Long,
    val name: String,
    val price: Long,
    val quantity: Int,
    val notes: String = ""
) {
    val subtotal: Long get() = price * quantity

    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("menuItemId", menuItemId)
            put("name", name)
            put("price", price)
            put("quantity", quantity)
            put("notes", notes)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): OrderItem {
            return OrderItem(
                menuItemId = json.optLong("menuItemId", 0L),
                name = json.optString("name", ""),
                price = json.optLong("price", 0L),
                quantity = json.optInt("quantity", 1),
                notes = json.optString("notes", "")
            )
        }
    }
}

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Long,
    val paidAmount: Long,
    val changeAmount: Long,
    val paymentMethod: String, // "Tunai", "QRIS", "Transfer"
    val orderType: String,      // "Makan di Tempat", "Bungkus"
    val customerInfo: String = "", // e.g. "Meja 3" or "Ibu Niswa"
    val itemsJson: String,
    val status: String = "Selesai"
) {
    fun parseItems(): List<OrderItem> {
        val list = mutableListOf<OrderItem>()
        if (itemsJson.isBlank()) return list
        try {
            val array = JSONArray(itemsJson)
            for (i in 0 until array.length()) {
                list.add(OrderItem.fromJson(array.getJSONObject(i)))
            }
        } catch (_: Exception) {}
        return list
    }

    companion object {
        fun serializeItems(items: List<OrderItem>): String {
            val array = JSONArray()
            items.forEach { array.put(it.toJson()) }
            return array.toString()
        }
    }
}

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int = 1,
    val notes: String = ""
) {
    val subtotal: Long get() = menuItem.price * quantity
}
