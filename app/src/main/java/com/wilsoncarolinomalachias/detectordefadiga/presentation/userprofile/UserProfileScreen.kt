package com.wilsoncarolinomalachias.detectordefadiga.presentation.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wilsoncarolinomalachias.detectordefadiga.R
import com.wilsoncarolinomalachias.detectordefadiga.presentation.AppAuthentication
import com.wilsoncarolinomalachias.detectordefadiga.presentation.ui.theme.primaryColor

@Composable
fun UserProfileScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("Perfil")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                backgroundColor = primaryColor,
                contentColor = Color.White
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Surface(
                modifier = Modifier.padding(padding),
                color = primaryColor
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.user_profile),
                        contentDescription = "",
                        modifier = Modifier
                            .size(94.dp),
                    )

                    Text(text = AppAuthentication.getUserName(), color = Color.White, fontSize = 24.sp)
                }
            }
        }
    )
}