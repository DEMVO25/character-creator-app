package com.example.character_creator_app.character_creation.weapon_table

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    debounceTime: Long = 300L,
    filterRegex: Regex? = null,
    maxLength: Int = Int.MAX_VALUE
) {
    var localValue by remember(value) { mutableStateOf(value) }

    LaunchedEffect(localValue) {
        if (localValue != value) {
            delay(debounceTime)
            onValueChange(localValue)
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (localValue.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        BasicTextField(
            value = localValue,
            onValueChange = { input ->
                val filtered = filterRegex?.let { regex ->
                    input.filter { char -> char.toString().matches(regex) }
                } ?: input

                if (filtered.length <= maxLength) {
                    localValue = filtered
                }
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}