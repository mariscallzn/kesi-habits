package com.kesicollection.kesihabits

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kesicollection.core.designsystem.state.rememberScaffoldDefinition
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.core.designsystem.utils.TAG
import com.kesicollection.kesihabits.navigation.KhNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldDefinitionState = rememberScaffoldDefinition()
            Log.d(TAG, "Main Activity re render")
            KesiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = scaffoldDefinitionState.appBarComposable ?: {},
                    floatingActionButton = scaffoldDefinitionState.fabComposable ?: {},
                    bottomBar = scaffoldDefinitionState.bottomComposable ?: {}
                ) { innerPadding ->
                    KhNavHost(
                        scaffoldDefinitionState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}