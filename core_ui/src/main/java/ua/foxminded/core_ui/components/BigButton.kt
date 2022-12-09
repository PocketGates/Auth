package ua.foxminded.core_ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ua.foxminded.core_ui.R

@Composable
fun BigButton(
    textResource: Int,
    isLoading: Boolean,
    isEnabled: Boolean,
    onSubmitAction: () -> Unit
) {
    Button(
        onClick = onSubmitAction,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.big_btn_height)),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryVariant
        ),
        elevation = null
    ) {
        Crossfade(
            targetState = isLoading,
            animationSpec = tween(400)
        ) {
            when (it) {
                true -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = textResource).uppercase()
                        )
                    }
                }
            }
        }
    }
}