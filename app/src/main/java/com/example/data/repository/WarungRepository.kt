package com.example.data.repository

import com.example.data.local.MenuItemDao
import com.example.data.local.OrderDao
import com.example.data.model.MenuItem
import com.example.data.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WarungRepository(
    private val menuItemDao: MenuItemDao,
    private val orderDao: OrderDao
) {
    val allMenuItems: Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()
    val totalRevenue: Flow<Long?> = orderDao.getTotalRevenue()

    suspend fun checkAndSeedInitialMenu() = withContext(Dispatchers.IO) {
        if (menuItemDao.count() == 0) {
            val initialMenu = listOf(
                MenuItem(
                    name = "Nasi Ayam Goreng Niswa",
                    category = "Makanan",
                    price = 18000,
                    description = "Ayam ungkep bumbu rempah kuning khas Niswa + sambal terasi matang & lalapan segar",
                    isAvailable = true,
                    iconType = "chicken"
                ),
                MenuItem(
                    name = "Nasi Rawon Daging Sapi",
                    category = "Makanan",
                    price = 22000,
                    description = "Kuah kluwek hitam gurih sedap dengan potongan daging sapi empuk, tauge pendek & sambal",
                    isAvailable = true,
                    iconType = "soup"
                ),
                MenuItem(
                    name = "Nasi Pecel Lele Kremes",
                    category = "Makanan",
                    price = 16000,
                    description = "Lele goreng garing gurih bertabur kremesan renyah disajikan dengan sambal terasi tomat",
                    isAvailable = true,
                    iconType = "fish"
                ),
                MenuItem(
                    name = "Nasi Soto Ayam Lamongan",
                    category = "Makanan",
                    price = 16000,
                    description = "Soto ayam berkuah kuning sedap dengan taburan koya gurih, suwiran ayam, soun & irisan telur",
                    isAvailable = true,
                    iconType = "soup"
                ),
                MenuItem(
                    name = "Sayur Asem Segar Niswa",
                    category = "Makanan",
                    price = 6000,
                    description = "Kuah asam segar kaya rempah dengan kacang panjang, labu siam, jagung manis & melinjo",
                    isAvailable = true,
                    iconType = "soup"
                ),
                MenuItem(
                    name = "Paket Komplit Ayam + Es Teh",
                    category = "Paket",
                    price = 21000,
                    description = "Nasi + Ayam Goreng Niswa + Tahu/Tempe + Lalap Sambal + Es Teh Manis Jumbo",
                    isAvailable = true,
                    iconType = "package"
                ),
                MenuItem(
                    name = "Paket Hemat Lele + Sayur Asem",
                    category = "Paket",
                    price = 19000,
                    description = "Nasi + Pecel Lele Kremes + Semangkuk Sayur Asem + Sambal Terasi",
                    isAvailable = true,
                    iconType = "package"
                ),
                MenuItem(
                    name = "Paket Soto Ayam + Es Jeruk",
                    category = "Paket",
                    price = 20000,
                    description = "Nasi Soto Ayam Lamongan kuah koya gurih + Es Jeruk Peras Dingin Segar",
                    isAvailable = true,
                    iconType = "package"
                ),
                MenuItem(
                    name = "Tempe Mendoan Hangat (Isi 3)",
                    category = "Tambahan",
                    price = 6000,
                    description = "Mendoan bumbu ketumbar daun bawang digoreng hangat dengan cocolan sambal kecap rawit",
                    isAvailable = true,
                    iconType = "snack"
                ),
                MenuItem(
                    name = "Tahu Goreng Krispi (Isi 3)",
                    category = "Tambahan",
                    price = 5000,
                    description = "Tahu goreng gurih renyah di luar lembut di dalam",
                    isAvailable = true,
                    iconType = "snack"
                ),
                MenuItem(
                    name = "Telur Balado Khas Warung",
                    category = "Tambahan",
                    price = 5000,
                    description = "Telur ayam rebus dibalut sambal balado merah pedas manis gurih",
                    isAvailable = true,
                    iconType = "egg"
                ),
                MenuItem(
                    name = "Kerupuk Kaleng Putih",
                    category = "Tambahan",
                    price = 2000,
                    description = "Kerupuk blek putih renyah pelengkap makan",
                    isAvailable = true,
                    iconType = "snack"
                ),
                MenuItem(
                    name = "Es Teh Manis Segar",
                    category = "Minuman",
                    price = 4000,
                    description = "Teh melati wangi khas warung diseduh harum dengan gula pasir asli",
                    isAvailable = true,
                    iconType = "drink"
                ),
                MenuItem(
                    name = "Es Jeruk Peras Asli",
                    category = "Minuman",
                    price = 6000,
                    description = "Jeruk peras murni segar kaya vitamin C disajikan dingin",
                    isAvailable = true,
                    iconType = "drink"
                ),
                MenuItem(
                    name = "Kopi Hitam Tubruk Khas Warung",
                    category = "Minuman",
                    price = 5000,
                    description = "Kopi hitam kental beraroma khas diseduh air mendidih",
                    isAvailable = true,
                    iconType = "coffee"
                ),
                MenuItem(
                    name = "Air Mineral Botol Dingin",
                    category = "Minuman",
                    price = 4000,
                    description = "Air mineral botol 600ml sejuk",
                    isAvailable = true,
                    iconType = "drink"
                )
            )
            menuItemDao.insertAll(initialMenu)
        }
    }

    suspend fun insertMenuItem(item: MenuItem): Long = withContext(Dispatchers.IO) {
        menuItemDao.insert(item)
    }

    suspend fun updateMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
        menuItemDao.update(item)
    }

    suspend fun deleteMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
        menuItemDao.delete(item)
    }

    suspend fun setMenuAvailability(id: Long, isAvailable: Boolean) = withContext(Dispatchers.IO) {
        menuItemDao.updateAvailability(id, isAvailable)
    }

    suspend fun insertOrder(order: Order): Long = withContext(Dispatchers.IO) {
        orderDao.insert(order)
    }

    suspend fun deleteOrder(order: Order) = withContext(Dispatchers.IO) {
        orderDao.delete(order)
    }
}
