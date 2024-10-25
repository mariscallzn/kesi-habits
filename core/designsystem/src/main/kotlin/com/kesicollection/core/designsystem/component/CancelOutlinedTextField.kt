package com.kesicollection.core.designsystem.component

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
import com.kesicollection.core.designsystem.icon.KesiIcons

@Composable
fun CancelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCancel: () -> Unit,
    placeHolder: String? = null,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value,
        onValueChange,
        modifier,
        placeholder = { placeHolder?.let { Text(it) } },
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
        shape = RoundedCornerShape(MaterialTheme.shapes.large.topStart),
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        singleLine = true
    )
}