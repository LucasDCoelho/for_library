package com.br.unifor.for_library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.br.unifor.for_library.core.designsystem.ForLibraryTheme
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

const val SUPABASE_URL = "https://aearixonwaztufxivlrk.supabase.co"
const val SUPABASE_KEY = "sb_publishable_x6K9DCFVnQCn5h0XT0iORQ_Lxh9x28T"

val supabase = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_KEY
) {
    install(Auth)
    install(Postgrest)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForLibraryTheme {
                ForLibraryApp()
            }
        }
    }
}