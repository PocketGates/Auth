package ua.foxminded.core_ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import ua.foxminded.core_ui.R

@Composable
fun TrackerSnackbar(message: String?) {
    Snackbar(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(R.dimen.auth_screen_horizontal_padding),
                vertical = dimensionResource(R.dimen.snackbar_vertical_padding)
            )
    ) {
        Text(
            text = message ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption
        )
    }
}