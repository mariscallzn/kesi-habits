package com.kesicollection.kesihabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.theme.KesiTheme
import com.kesicollection.kesihabits.navigation.KhNavHost

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (appBarTitle, setAppBarTitle) = remember { mutableStateOf<String?>(null) }
            val (fabOnClick, setFabOnClick) = remember { mutableStateOf<(() -> Unit)?>(null) }
            val (isFabVisible, setFabVisible) = remember { mutableStateOf(true) }
            KesiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        appBarTitle?.let {
                            TopAppBar(title = { Text(appBarTitle) })
                        }
                    },
                    floatingActionButton = {
                        if (isFabVisible) {
                            FloatingActionButton(onClick = {
                                fabOnClick?.invoke()
                            }) {
                                Icon(KesiIcons.Add, contentDescription = "Add")
                            }
                        }
                    }) { innerPadding ->
                    KhNavHost(
                        setAppBarTitle,
                        setFabOnClick,
                        setFabVisible,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}