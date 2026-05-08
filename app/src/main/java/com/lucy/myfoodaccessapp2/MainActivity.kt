package com.lucy.myfoodaccessapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lucy.myfoodaccessapp2.ui.Navigation.AppNavigation
import com.lucy.myfoodaccessapp2.ui.theme.MyFoodAccessApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFoodAccessApp2Theme {
                AppNavigation()
            }
        }
    }
}
