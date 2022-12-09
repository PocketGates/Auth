package ua.foxminded.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ua.foxminded.auth.R
import ua.foxminded.core_ui.components.BigButton
import ua.foxminded.core_ui.components.TopBar
import ua.foxminded.core_ui.components.TrackerSnackbar
import ua.foxminded.core_ui.theme.TrackerTheme

@Composable
fun AuthScreen(
    event: AuthEvent,
    onAuthAction: (Boolean) -> Unit,
    onAuthVariantClick: () -> Unit,
    authModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        authModel.onCreate()
    }

    val screenState by authModel.authUiState.collectAsState()
    val resourcesFromContext = LocalContext.current.resources
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(screenState.isLogged) {
        onAuthAction(screenState.isLogged)
    }

    LaunchedEffect(screenState.passwordResetStatus) {
        if (screenState.passwordResetStatus && !screenState.passwordResetError) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = resourcesFromContext.getString(R.string.password_reset_message),
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    var userName by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        onDispose {
            userName = ""
            password = ""
            authModel.resetUiState()
        }
    }

    val currentAction = if (event is SignUpEvent) R.string.sign_up else R.string.sign_in

    val onSubmitAction: () -> Unit = {
        authModel.send(event.apply { handle(userName = userName, password = password) })
    }

    val onPasswordResetAction: () -> Unit = {
        scope.launch {
            focusManager.clearFocus()
            authModel.send(ResetPasswordEvent.apply { handle(userName = userName) })
        }
    }

    Scaffold(
        topBar = { TopBar(currentAction) },
        backgroundColor = MaterialTheme.colors.primary,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                TrackerSnackbar(snackbarHostState.currentSnackbarData?.message)
            }
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = scaffoldPadding)
                .padding(horizontal = dimensionResource(id = R.dimen.auth_screen_horizontal_padding))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthBox(
                currentAction = currentAction,
                userNameVal = userName,
                onUserNameChange = { userName = it.trim() },
                passwordVal = password,
                onPasswordChange = { password = it },
                onSubmitAction = onSubmitAction,
                isLoading = screenState.isLoading,
                errorMessage = screenState.errorMessage,
                userError = screenState.userNameError,
                passwordError = screenState.passwordError,
                onPasswordReset = onPasswordResetAction,
                focusManager = focusManager
            )
            if (event is SignUpEvent) {
                HelperText(
                    hasAccountResource = R.string.has_account,
                    stringResource = R.string.sign_in,
                    onClickAction = { onAuthVariantClick() }
                )
            } else {
                HelperText(
                    hasAccountResource = R.string.no_account,
                    stringResource = R.string.sign_up,
                    onClickAction = { onAuthVariantClick() }
                )
            }
        }
    }

}

@Composable
private fun AuthBox(
    currentAction: Int,
    userNameVal: String,
    passwordVal: String,
    isLoading: Boolean,
    errorMessage: Int? = null,
    userError: Boolean,
    passwordError: Boolean,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordReset: () -> Unit,
    onSubmitAction: () -> Unit,
    focusManager: FocusManager
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.auth_card_vertical_padding))
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.auth_card_inner_horizontal_padding),
                    vertical = dimensionResource(id = R.dimen.auth_card_inner_vertical_padding)
                )
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginInput(
                label = R.string.user_name,
                value = userNameVal,
                onValueChange = onUserNameChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = userError
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.auth_inputs_spacing)))
            LoginInput(
                label = R.string.password,
                isPassword = true,
                showPassword = isPasswordVisible,
                onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible },
                value = passwordVal,
                onValueChange = onPasswordChange,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                isError = passwordError
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.auth_err_message_vertical_padding)))
            AnimatedVisibility(visible = errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text(
                        text = stringResource(id = errorMessage ?: R.string.no_error),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.auth_err_message_padding)),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.auth_err_message_vertical_padding)))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextWithInteraction(
                    textResource = R.string.password_forgot,
                    onClickAction = onPasswordReset,
                    style = TextStyle(color = MaterialTheme.colors.onSurface)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.auth_forgot_pass_spacing)))
            BigButton(
                textResource = currentAction,
                isEnabled = userNameVal.isNotBlank() && passwordVal.isNotEmpty(),
                onSubmitAction = onSubmitAction,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun HelperText(hasAccountResource: Int, stringResource: Int, onClickAction: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(text = stringResource(hasAccountResource))
        TextWithInteraction(
            textResource = stringResource,
            onClickAction = onClickAction,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onPrimary
            )
        )
    }
}

@Composable
private fun TextWithInteraction(textResource: Int, onClickAction: () -> Unit, style: TextStyle) {
    Text(
        text = stringResource(textResource),
        style = MaterialTheme.typography.h3 + style,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClickAction,
                indication = rememberRipple(bounded = true)
            )
    )
}

@Composable
private fun LoginInput(
    label: Int,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {},
    value: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val borderColor by animateColorAsState(
        targetValue = if (isError) Color.Red else Color.Transparent
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.body2,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colors.onSurface,
            unfocusedLabelColor = MaterialTheme.colors.onSecondary,
            errorTrailingIconColor = Color.Red
        ),
        visualTransformation = if (isPassword && !showPassword) {
            PasswordVisualTransformation('*')
        } else {
            VisualTransformation.None
        },
        shape = MaterialTheme.shapes.small,
        label = {
            Text(
                text = stringResource(label)
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onPasswordVisibilityChange) {
                    Crossfade(
                        targetState = showPassword,
                        animationSpec = tween(400)
                    ) {
                        when (it) {
                            true -> PasswordIcon(R.drawable.ic_visibility_off)
                            else -> PasswordIcon(R.drawable.ic_visibility)
                        }
                    }
                }
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        isError = isError,
        modifier = Modifier
            .border(
                BorderStroke(
                    width = dimensionResource(id = R.dimen.border_err_stroke_width),
                    color = borderColor
                ),
                shape = MaterialTheme.shapes.small
            )
            .fillMaxWidth()
    )
}

@Composable
private fun PasswordIcon(iconResource: Int) {
    Icon(
        painter = painterResource(iconResource),
        contentDescription = null,
        tint = MaterialTheme.colors.onSecondary,
        modifier = Modifier.width(dimensionResource(id = R.dimen.pass_icon_width))
    )
}

@Preview(showBackground = true)
@Composable
fun TrackerSnackbarPrev() {
    TrackerTheme {
        TrackerSnackbar("Something message test on f message test")
    }
}
/*
@Preview(showBackground = true)
@Composable
fun LoginLightPreview() {
    TrackerTheme {
        AuthScreen(event = SignInEvent, onAuthAction = {}, onAuthVariantClick = {})
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginDarkPreview() {
    TrackerTheme {
        AuthScreen(event = SignUpEvent, onAuthAction = {}, onAuthVariantClick = {})
    }

}*/
