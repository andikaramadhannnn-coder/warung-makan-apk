package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.KasirScreen
import com.example.ui.screens.KelolaMenuScreen
import com.example.ui.screens.LaporanScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.WarungViewModel
import com.example.ui.viewmodel.WarungViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: WarungViewModel = viewModel(
                    factory = WarungViewModelFactory(application)
                )
                WarungNiswaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WarungNiswaApp(viewModel: WarungViewModel) {
    var currentTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val navItems = listOf(
        NavigationTab(
            title = "Kasir",
            selectedIcon = Icons.Filled.PointOfSale,
            unselectedIcon = Icons.Outlined.PointOfSale,
            testTag = "tab_kasir"
        ),
        NavigationTab(
            title = "Kelola Menu",
            selectedIcon = Icons.Filled.RestaurantMenu,
            unselectedIcon = Icons.Outlined.RestaurantMenu,
            testTag = "tab_kelola_menu"
        ),
        NavigationTab(
            title = "Laporan",
            selectedIcon = Icons.Filled.Assessment,
            unselectedIcon = Icons.Outlined.Assessment,
            testTag = "tab_laporan"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = currentTabIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTabIndex) {
                0 -> KasirScreen(viewModel = viewModel)
                1 -> KelolaMenuScreen(viewModel = viewModel)
                2 -> LaporanScreen(viewModel = viewModel)
            }
        }
    }
}

private data class NavigationTab(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Warung Niswa") }
}
