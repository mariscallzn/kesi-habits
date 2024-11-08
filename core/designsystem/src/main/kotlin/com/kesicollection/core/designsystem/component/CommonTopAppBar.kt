package com.kesicollection.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
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
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.theme.KesiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    onBackPress: (() -> Unit)? = null,
    title: String? = null,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(title = {
        title?.let {
            Text(it, style = MaterialTheme.typography.titleLarge)
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
    },
        actions = actions,
        modifier = modifier
    )
}

@DarkLightPreviews
@Composable
private fun CommonTopAppBarPreview() {
    KesiTheme {
        CommonTopAppBar({}, "7:38 AM • Nov 5")
    }
}