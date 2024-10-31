package com.kesicollection.core.designsystem.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kesicollection.core.designsystem.icon.KesiIcons
import com.kesicollection.core.designsystem.preview.DarkLightPreviews
import com.kesicollection.core.designsystem.theme.KesiTheme

@Composable
fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeHolder: String? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value,
        onValueChange,
        modifier.padding(
            top = WindowInsets.statusBars
                .add(WindowInsets.safeContent.only(WindowInsetsSides.Top))
                .asPaddingValues()
                .calculateTopPadding(),
            bottom = 0.dp,
            start = 16.dp,
            end = 16.dp
        ),
        placeholder = { placeHolder?.let { Text(it) } },
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(onCancel) {
                    Icon(
                        KesiIcons.Cancel,
                        contentDescription = stringResource(android.R.string.cancel)
                    )
                }
            }
        },
        shape = RoundedCornerShape(MaterialTheme.shapes.extraLarge.topStart),
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        singleLine = true
    )
}

@DarkLightPreviews
@Composable
private fun SearchTopBarNonLeadingIconPreview() {
//    val focusRequester = remember { FocusRequester() }
//
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    KesiTheme {
        SearchTopBar(
            value = "",
            onValueChange = {},
            onCancel = {},
            placeHolder = "Search",
//            modifier = Modifier.focusRequester(focusRequester)
        )
    }
}

@DarkLightPreviews
@Composable
private fun SearchTopBarLeadingIconPreview() {
    KesiTheme {
        SearchTopBar(
            value = "",
            onValueChange = {},
            onCancel = {},
            leadingIcon = {
                IconButton({}) {
                    Icon(KesiIcons.ArrowBack, "")
                }
            },
            placeHolder = "Search"
        )
    }
}