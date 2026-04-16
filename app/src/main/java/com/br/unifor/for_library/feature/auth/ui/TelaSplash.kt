package com.br.unifor.for_library.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.unifor.for_library.R
import kotlinx.coroutines.delay

private const val SPLASH_DELAY_MS = 3000L

@Composable
fun TelaSplashScreen(onSplashFinished: () -> Unit) {
    // Coloquei um tempo de espera 3 segundos
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MS)
        onSplashFinished()
    }
    //Colocar em res? nao sei
    val azulForLibrary = Color(0xFF1B65F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(azulForLibrary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Falta colocar uma logo oficial
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo ForLibrary",
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ForLibrary",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}