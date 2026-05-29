package com.br.unifor.for_library.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

private fun capaUrlIsbn(isbn: String?): String =
    "https://covers.openlibrary.org/b/isbn/$isbn-M.jpg?default=false"

@Composable
fun CapaLivro(
    isbn: String?,
    tituloFallback: String,
    modifier: Modifier = Modifier,
    corFallback: Color = Color(0xFF1565C0),
    capaUrl: String? = null
) {
    val imageUrl = capaUrl?.takeIf { it.isNotBlank() } ?: capaUrlIsbn(isbn)
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Capa de $tituloFallback",
        contentScale = ContentScale.Crop,
        modifier = modifier,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE0E0E0))
            )
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(corFallback),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tituloFallback.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    )
}

