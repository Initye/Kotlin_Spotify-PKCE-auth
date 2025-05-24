package com.example.spotdown

import android.content.Context
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.json.JSONObject

 val client = OkHttpClient()
fun getToken(context: Context, code: String) {
    val codeVerifier = getCodeVerifier(context)

    val postBody = FormBody.Builder()
        .add("client_id", clientId)
        .add("grant_type", "authorization_code")
        .add("code", code)
        .add("redirect_uri", redirectUri)
        .add("code_verifier", codeVerifier.toString())
        .build()


    val request = Request.Builder()
        .url("https://accounts.spotify.com/api/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .post(postBody)
        .build()

    client.newCall(request).execute().use() { response ->
        if(!response.isSuccessful) throw IOException("Unexpected code $response")

        val responseBody = response.body?.string()
        responseBody?.let {
            val jsonResponse = JSONObject(it)
            val accessToken = jsonResponse.getString("access_token")

            val sharedPrefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().putString("access_token", accessToken).apply()
        }
    }
}