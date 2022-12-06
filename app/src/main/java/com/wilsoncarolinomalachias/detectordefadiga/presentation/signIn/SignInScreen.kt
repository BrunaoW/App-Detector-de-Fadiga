package com.wilsoncarolinomalachias.detectordefadiga.presentation.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.TransparentTextField
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.DetectorDeFadigaTheme
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.wilsoncarolinomalachias.detectordefadiga.presentation.components.RoundedButton


@Composable
fun SignInScreen() {

    val emailValue = rememberSaveable{ mutableStateOf("") }
    val passwordValue = rememberSaveable{ mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.detec),
            contentDescription = "Login",
            contentScale = ContentScale.Inside
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ConstraintLayout {
            val (surface, fab) = createRefs()

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .constrainAs(surface) {
                        bottom.linkTo(parent.bottom)
                    },
                color = Color.White,
                shape = RoundedCornerShape(
                    topStartPercent = 8,
                    topEndPercent = 8
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Detector de Fadiga ",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Login to your Account",
                        style = MaterialTheme.typography.h5.copy(
                            color = MaterialTheme.colors.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TransparentTextField(
                            textFieldValue = emailValue,
                            textLabel = "Email",
                            keyboardType = KeyboardType.Email,
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            imeAction = ImeAction.Next
                        )

                        TransparentTextField(
                            textFieldValue = passwordValue,
                            textLabel = "Password",
                            keyboardType = KeyboardType.Password,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            imeAction = ImeAction.Done,
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        passwordVisibility = !passwordVisibility
                                    }
                                ) {
                                    Icon(
                                        imageVector = if(passwordVisibility) {
                                            Icons.Default.Visibility
                                        } else {
                                            Icons.Default.VisibilityOff
                                        },
                                        contentDescription = "Toggle Password Icon"
                                    )
                                }
                            },
                            visualTransformation = if(passwordVisibility) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            }
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.End
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RoundedButton(
                            text = "Login",
                            displayProgressBar = false,
                            onClick = {
                                // TODO("LOGIN")
                            }
                        )

                        ClickableText(
                            text = buildAnnotatedString {
                                append("Do not have an Account?")

                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ){
                                    append("Sign up")
                                }
                            }
                        ){
                            // TODO("NAVIGATE TO REGISTER SCREEN")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SignInPreview() {
    DetectorDeFadigaTheme {
        SignInScreen()
    }
}