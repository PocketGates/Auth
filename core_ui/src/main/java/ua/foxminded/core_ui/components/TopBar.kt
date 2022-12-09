package ua.foxminded.core_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.foxminded.core_ui.R

@Composable
fun TopBar(titleResource: Int, withExitIcon: Boolean = false, onExitClickAction: () -> Unit = {}) {
    TopAppBar(
        modifier = Modifier.clip(MaterialTheme.shapes.large),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        CompositionLocalProvider(LocalContentAlpha.provides(ContentAlpha.high)) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(titleResource),
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.align(Alignment.Center)
                )
                if (withExitIcon) {
                    IconButton(
                        onClick = onExitClickAction,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.exit),
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.width(22.75.dp),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}