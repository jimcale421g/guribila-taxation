package com.qarantech.guribila

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.qarantech.guribila.ui.theme.GuribilaTheme
import com.qarantech.guribila.ui.theme.gradientBrush
import com.qarantech.guribila.ui.theme.gradientBrushDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(darkMode) }
            
            GuribilaTheme(darkTheme = isDarkMode) {
                MainScreen(
                    onThemeUpdated = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onThemeUpdated: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                DrawerBody(
                    onThemeUpdated = onThemeUpdated,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Guribila Maps") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                MapScreen()
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = if (isSystemInDarkTheme()) gradientBrushDark else gradientBrush,
                shape = RectangleShape
            )
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "User",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.surface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Guest User",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    onThemeUpdated: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        NavigationDrawerItem(
            icon = { 
                Icon(
                    Icons.Default.DarkMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = { Text("Toggle Theme") },
            selected = false,
            onClick = {
                onThemeUpdated()
                scope.launch { drawerState.close() }
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        NavigationDrawerItem(
            icon = { 
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = { Text("Settings") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val hargeisa = LatLng(9.5582, 44.0604)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(hargeisa, 12f)
    }
    
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    var showMapTypeDialog by remember { mutableStateOf(false) }
    
    val isDark = isSystemInDarkTheme()
    val mapProperties by remember(isDark, mapType) {
        mutableStateOf(
            MapProperties(
                mapType = mapType,
                mapStyleOptions = if (isDark) {
                    MapStyleOptions(MapStyle.darkMapStyle)
                } else null,
                isMyLocationEnabled = hasLocationPermission(context)
            )
        )
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                mapToolbarEnabled = true,
                myLocationButtonEnabled = true
            )
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            Marker(
                state = MarkerState(position = hargeisa),
                title = "Hargeisa",
                snippet = "Capital City of Somaliland"
            )
        }

        // Quick Map Type Selector
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    MaterialTheme.shapes.medium
                )
        ) {
            MapType.values().filter { it != MapType.NONE }.forEach { type ->
                TextButton(
                    onClick = { mapType = type },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (mapType == type) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = when (type) {
                            MapType.NORMAL -> "Map"
                            MapType.SATELLITE -> "Satellite"
                            MapType.TERRAIN -> "Terrain"
                            MapType.HYBRID -> "Hybrid"
                            else -> ""
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // Control Buttons Column
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 72.dp) // Add padding to avoid overlap with zoom controls
        ) {
            // Plus FAB
            FloatingActionButton(
                onClick = { /* Add your plus button action here */ },
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Location FAB
            FloatingActionButton(
                onClick = {
                    if (!hasLocationPermission(context)) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = "My Location",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

            // Map Style FAB
            FloatingActionButton(
                onClick = { showMapTypeDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    Icons.Default.Layers,
                    contentDescription = "Change Map Type",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        if (showMapTypeDialog) {
            MapTypeDialog(
                onDismiss = { showMapTypeDialog = false },
                onMapTypeSelected = { 
                    mapType = it
                    showMapTypeDialog = false
                }
            )
        }
    }
}

@Composable
fun MapTypeDialog(
    onDismiss: () -> Unit,
    onMapTypeSelected: (MapType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Map Type") },
        text = {
            Column {
                MapType.values().forEach { type ->
                    if (type != MapType.NONE) {
                        TextButton(
                            onClick = { onMapTypeSelected(type) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type.name.lowercase().capitalize())
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}