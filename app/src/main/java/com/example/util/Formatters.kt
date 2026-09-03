package com.example.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    private val rupiahFormat: NumberFormat = NumberFormat.getIntegerInstance(Locale("id", "ID"))
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    private val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))

    fun formatRupiah(amount: Long): String {
        return "Rp " + rupiahFormat.format(amount)
    }

    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }
}
