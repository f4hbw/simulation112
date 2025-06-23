// File: app/src/main/java/com/f4hbw/simulation112/MainActivity.kt
package com.f4hbw.simulation112

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.f4hbw.simulation112.ui.NavGraph        // ← Assure-toi que ce package est correct
import com.f4hbw.simulation112.ui.theme.Simulation112Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simulation112Theme {
                NavGraph()
            }
        }
    }
}
