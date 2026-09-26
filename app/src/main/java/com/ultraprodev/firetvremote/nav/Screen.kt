package com.ultraprodev.firetvremote.nav

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object DeviceScan : Screen("device_scan")
    data object Pairing : Screen("pairing/{ip}/{name}") {
        fun route(ip: String, name: String) = "pairing/$ip/${java.net.URLEncoder.encode(name, "UTF-8")}"
    }
    data object Remote : Screen("remote")
    data object Apps : Screen("apps")
    data object QuickActions : Screen("quick_actions")
    data object Touchpad : Screen("touchpad")
    data object Settings : Screen("settings")
}

/** The bottom-nav destinations shown once a device is connected. */
enum class BottomTab(val screen: Screen, val label: String, val shortLabel: String = label) {
    REMOTE(Screen.Remote, "Remote"),
    APPS(Screen.Apps, "Apps"),
    QUICK_ACTIONS(Screen.QuickActions, "Quick Actions", "Actions"),
    TOUCHPAD(Screen.Touchpad, "Touchpad", "Touch"),
    SETTINGS(Screen.Settings, "Settings")
}
