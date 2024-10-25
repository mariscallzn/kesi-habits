package com.kesicollection.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kesicollection.core.designsystem.R
import com.kesicollection.core.designsystem.icon.KesiIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    onBackPress: (() -> Unit)? = null,
    title: String? = null
) {
    TopAppBar(title = {
        title?.let {
            Text(it, style = MaterialTheme.typography.headlineMedium)
        }
    }, navigationIcon = {
        onBackPress?.let {
            IconButton(it) {
                Icon(
                    KesiIcons.ArrowBack,
                    stringResource(R.string.cd_go_back_arrow),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    })
}