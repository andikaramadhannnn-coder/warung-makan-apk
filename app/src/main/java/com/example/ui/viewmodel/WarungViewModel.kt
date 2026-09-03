package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.WarungDatabase
import com.example.data.model.CartItem
import com.example.data.model.MenuItem
import com.example.data.model.Order
import com.example.data.model.OrderItem
import com.example.data.repository.WarungRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WarungViewModel(application: Application) : AndroidViewModel(application) {
    private val database = WarungDatabase.getDatabase(application)
    private val repository = WarungRepository(database.menuItemDao(), database.orderDao())

    val allMenuItems: StateFlow<List<MenuItem>> = repository.allMenuItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalRevenue: StateFlow<Long?> = repository.totalRevenue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    // Kasir state
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _orderType = MutableStateFlow("Makan di Tempat") // "Makan di Tempat" or "Bungkus"
    val orderType: StateFlow<String> = _orderType.asStateFlow()

    private val _customerInfo = MutableStateFlow("") // Meja X or Customer Name
    val customerInfo: StateFlow<String> = _customerInfo.asStateFlow()

    private val _isCartDrawerOpen = MutableStateFlow(false)
    val isCartDrawerOpen: StateFlow<Boolean> = _isCartDrawerOpen.asStateFlow()

    private val _isPaymentDialogOpen = MutableStateFlow(false)
    val isPaymentDialogOpen: StateFlow<Boolean> = _isPaymentDialogOpen.asStateFlow()

    private val _activeReceiptOrder = MutableStateFlow<Order?>(null)
    val activeReceiptOrder: StateFlow<Order?> = _activeReceiptOrder.asStateFlow()

    // Owner Menu Management State
    private val _isMenuFormOpen = MutableStateFlow(false)
    val isMenuFormOpen: StateFlow<Boolean> = _isMenuFormOpen.asStateFlow()

    private val _editingMenuItem = MutableStateFlow<MenuItem?>(null)
    val editingMenuItem: StateFlow<MenuItem?> = _editingMenuItem.asStateFlow()

    private val _menuSearchQuery = MutableStateFlow("")
    val menuSearchQuery: StateFlow<String> = _menuSearchQuery.asStateFlow()

    private val _menuFilterCategory = MutableStateFlow("Semua")
    val menuFilterCategory: StateFlow<String> = _menuFilterCategory.asStateFlow()

    private val _menuDeleteTarget = MutableStateFlow<MenuItem?>(null)
    val menuDeleteTarget: StateFlow<MenuItem?> = _menuDeleteTarget.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialMenu()
        }
    }

    // Filtered menu items for Kasir
    val filteredMenuItems: StateFlow<List<MenuItem>> = combine(
        allMenuItems,
        selectedCategory,
        searchQuery
    ) { items, category, query ->
        items.filter { item ->
            val matchesCategory = (category == "Semua" || item.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true) || item.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered menu items for Owner Menu Management
    val ownerFilteredMenuItems: StateFlow<List<MenuItem>> = combine(
        allMenuItems,
        menuFilterCategory,
        menuSearchQuery
    ) { items, category, query ->
        items.filter { item ->
            val matchesCategory = (category == "Semua" || item.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true) || item.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart calculations
    val cartSubtotal: StateFlow<Long> = combine(_cartItems) { itemsArray ->
        itemsArray[0].sumOf { it.subtotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val cartTotalCount: StateFlow<Int> = combine(_cartItems) { itemsArray ->
        itemsArray[0].sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Kasir Cart Actions
    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setOrderType(type: String) {
        _orderType.value = type
    }

    fun setCustomerInfo(info: String) {
        _customerInfo.value = info
    }

    fun setCartDrawerOpen(isOpen: Boolean) {
        _isCartDrawerOpen.value = isOpen
    }

    fun setPaymentDialogOpen(isOpen: Boolean) {
        _isPaymentDialogOpen.value = isOpen
    }

    fun showReceipt(order: Order?) {
        _activeReceiptOrder.value = order
    }

    fun addToCart(menuItem: MenuItem) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.menuItem.id == menuItem.id }
        if (index != -1) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            currentList.add(CartItem(menuItem = menuItem, quantity = 1))
        }
        _cartItems.value = currentList
    }

    fun removeFromCart(menuItemId: Long) {
        _cartItems.value = _cartItems.value.filterNot { it.menuItem.id == menuItemId }
    }

    fun increaseCartQuantity(menuItemId: Long) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.menuItem.id == menuItemId }
        if (index != -1) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + 1)
            _cartItems.value = currentList
        }
    }

    fun decreaseCartQuantity(menuItemId: Long) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.menuItem.id == menuItemId }
        if (index != -1) {
            val existing = currentList[index]
            if (existing.quantity > 1) {
                currentList[index] = existing.copy(quantity = existing.quantity - 1)
                _cartItems.value = currentList
            } else {
                currentList.removeAt(index)
                _cartItems.value = currentList
            }
        }
    }

    fun updateCartItemNotes(menuItemId: Long, notes: String) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.menuItem.id == menuItemId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(notes = notes)
            _cartItems.value = currentList
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _customerInfo.value = ""
        _orderType.value = "Makan di Tempat"
    }

    // Process Checkout
    fun processCheckout(paidAmount: Long, paymentMethod: String) {
        val currentItems = _cartItems.value
        if (currentItems.isEmpty()) return

        val total = currentItems.sumOf { it.subtotal }
        val change = if (paidAmount >= total) paidAmount - total else 0L

        val orderItems = currentItems.map {
            OrderItem(
                menuItemId = it.menuItem.id,
                name = it.menuItem.name,
                price = it.menuItem.price,
                quantity = it.quantity,
                notes = it.notes
            )
        }

        val orderNo = "NW-" + (System.currentTimeMillis() % 1000000)

        val newOrder = Order(
            orderNumber = orderNo,
            timestamp = System.currentTimeMillis(),
            totalAmount = total,
            paidAmount = paidAmount,
            changeAmount = change,
            paymentMethod = paymentMethod,
            orderType = _orderType.value,
            customerInfo = _customerInfo.value.trim().ifBlank {
                if (_orderType.value == "Makan di Tempat") "Meja 1" else "Bungkus"
            },
            itemsJson = Order.serializeItems(orderItems),
            status = "Selesai"
        )

        viewModelScope.launch {
            val insertedId = repository.insertOrder(newOrder)
            val completedOrder = newOrder.copy(id = insertedId)
            _activeReceiptOrder.value = completedOrder
            clearCart()
            _isPaymentDialogOpen.value = false
        }
    }

    // Owner Menu Management Actions
    fun setMenuSearchQuery(query: String) {
        _menuSearchQuery.value = query
    }

    fun setMenuFilterCategory(category: String) {
        _menuFilterCategory.value = category
    }

    fun openAddMenuDialog() {
        _editingMenuItem.value = null
        _isMenuFormOpen.value = true
    }

    fun openEditMenuDialog(item: MenuItem) {
        _editingMenuItem.value = item
        _isMenuFormOpen.value = true
    }

    fun closeMenuFormDialog() {
        _editingMenuItem.value = null
        _isMenuFormOpen.value = false
    }

    fun confirmDeleteMenu(item: MenuItem?) {
        _menuDeleteTarget.value = item
    }

    fun executeDeleteMenu(item: MenuItem) {
        viewModelScope.launch {
            repository.deleteMenuItem(item)
            _menuDeleteTarget.value = null
        }
    }

    fun toggleMenuAvailability(item: MenuItem) {
        viewModelScope.launch {
            repository.setMenuAvailability(item.id, !item.isAvailable)
        }
    }

    fun saveMenuItem(
        name: String,
        category: String,
        price: Long,
        description: String,
        isAvailable: Boolean,
        iconType: String
    ) {
        viewModelScope.launch {
            val currentEditing = _editingMenuItem.value
            if (currentEditing != null) {
                val updated = currentEditing.copy(
                    name = name.trim(),
                    category = category,
                    price = price,
                    description = description.trim(),
                    isAvailable = isAvailable,
                    iconType = iconType
                )
                repository.updateMenuItem(updated)
            } else {
                val newItem = MenuItem(
                    name = name.trim(),
                    category = category,
                    price = price,
                    description = description.trim(),
                    isAvailable = isAvailable,
                    iconType = iconType
                )
                repository.insertMenuItem(newItem)
            }
            closeMenuFormDialog()
        }
    }
}

class WarungViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WarungViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WarungViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
