package com.umutsaydam.deardiary.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.enums.AuthStateEnum
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.presentation.Dimens.PaddingXSmall
import com.umutsaydam.deardiary.presentation.Dimens.PaddingMedium
import com.umutsaydam.deardiary.presentation.Dimens.SpacingMedium
import com.umutsaydam.deardiary.presentation.common.LoadingCircular
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.safeNavigateWithClearingBackStack

@Composable
fun AuthScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.authUiState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val uiMessageState by authViewModel.uiMessageState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val formWidth = when {
        screenWidth < 360.dp -> 0.85f
        screenWidth < 600.dp -> 0.6f
        else -> 0.4f
    }

    LaunchedEffect(uiMessageState) {
        when (val state = uiMessageState) {
            is UiMessage.Success -> {
                Toast.makeText(context, context.getString(state.message), Toast.LENGTH_SHORT).show()
                authViewModel.clearUiMessageState()
            }

            is UiMessage.Error -> {
                Toast.makeText(context, context.getString(state.message), Toast.LENGTH_SHORT).show()
                authViewModel.clearUiMessageState()
            }

            else -> {}
        }
    }

    LaunchedEffect(authUiState) {
        if (authUiState is UiState.Success) {
            navController.safeNavigateWithClearingBackStack(Route.Diaries.route)
        }
    }

    if (authUiState is UiState.Loading) {
        LoadingCircular()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo_medium),
            contentDescription = stringResource(R.string.app_logo),
            contentScale = ContentScale.Fit
        )

        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.displayMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(formWidth)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthOutlineText(
                modifier = Modifier.fillMaxWidth(),
                text = username,
                onValueChange = { value ->
                    username = value
                },
                textInfo = stringResource(R.string.username),
                leadIcon = R.drawable.ic_person_outline,
                contentDesc = stringResource(R.string.username_icon),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )

            AuthOutlineText(
                modifier = Modifier.fillMaxWidth(),
                text = password,
                onValueChange = { value ->
                    password = value
                },
                textInfo = stringResource(R.string.password),
                visualTransformation = PasswordVisualTransformation(),
                leadIcon = R.drawable.ic_password_outline,
                contentDesc = stringResource(R.string.password_icon),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (authState == AuthStateEnum.LOGIN) ImeAction.Done else ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )

            if (authState == AuthStateEnum.REGISTER) {
                AuthOutlineText(
                    modifier = Modifier.fillMaxWidth(),
                    text = passwordConfirm,
                    onValueChange = { value ->
                        passwordConfirm = value
                    },
                    textInfo = stringResource(R.string.confirm),
                    visualTransformation = PasswordVisualTransformation(),
                    leadIcon = R.drawable.ic_password_outline,
                    contentDesc = stringResource(R.string.confirm_icon),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    )
                )
            }

            Button(
                onClick = {
                    if (authState == AuthStateEnum.LOGIN) {
                        authViewModel.loginUser(
                            username.trim(),
                            password.trim()
                        )
                    } else {
                        authViewModel.createUser(
                            username.trim(),
                            password.trim(),
                            passwordConfirm.trim()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(if (authState == AuthStateEnum.LOGIN) R.string.sign_in else R.string.sign_up))
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = PaddingXSmall),
        contentAlignment = Alignment.BottomCenter
    ) {
        ColorfulText(
            normalText = stringResource(if (authState == AuthStateEnum.LOGIN) R.string.dont_have_account else R.string.do_have_account),
            normalTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            colorfulText = stringResource(if (authState == AuthStateEnum.LOGIN) R.string.sign_up_now else R.string.sign_in_now),
            colorfulTextColor = MaterialTheme.colorScheme.primary,
            onClick = {
                if (authState == AuthStateEnum.LOGIN) {
                    authViewModel.switchRegisterState()
                } else {
                    passwordConfirm = ""
                    authViewModel.switchLoginState()
                }
            },
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun AuthOutlineText(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    textInfo: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadIcon: Int,
    contentDesc: String,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        label = { Text(textInfo) },
        placeholder = { Text(textInfo) },
        singleLine = true,
        visualTransformation = visualTransformation,
        leadingIcon = {
            Icon(
                painter = painterResource(leadIcon),
                contentDescription = contentDesc,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun ColorfulText(
    modifier: Modifier = Modifier,
    normalText: String,
    normalTextColor: Color,
    colorfulText: String,
    colorfulTextColor: Color,
    onClick: () -> Unit,
    style: TextStyle
) {
    val annotatedText = buildAnnotatedString {
        append(normalText)
        withStyle(
            style = SpanStyle(
                color = colorfulTextColor,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(colorfulText)
        }
    }
    Text(
        modifier = modifier
            .padding(PaddingMedium)
            .clickable { onClick() },
        text = annotatedText,
        style = style,
        color = normalTextColor
    )
}