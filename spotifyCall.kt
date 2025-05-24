package com.example.spotdown

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DisplayMode.Companion.Input
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.spotdown.ui.Input
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//What it does is that its getting your sharedprefs "spotify_prefs" and gets access token from it
//Then it sends call to api
//hack code ngl 

@Composable
fun linkPlaylist() {
    var playlistLink by remember { mutableStateOf("") }
    Column{ Input(linkPlaylist = playlistLink, onLinkChange = { playlistLink = it }) }
}

suspend fun getPlaylist(context: Context, playlistLink: String): JSONObject? = withContext(Dispatchers.IO) {
    val sharedPrefs  = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    val accessToken =  sharedPrefs.getString("access_token", null)

    val request = Request.Builder()
        .url("https://api.spotify.com/v1/playlists/$playlistLink") //or whatever call you want to make
        .header("Authorization", "Bearer $accessToken")
        .build()
    try {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string()
            if(responseBody != null) {
                JSONObject(responseBody)
            } else {
                null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}