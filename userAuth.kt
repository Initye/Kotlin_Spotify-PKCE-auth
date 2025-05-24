package com.example.spotdown

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URL

val clientId = "" //Your client id
val redirectUri = "spotdown://callback" //remember to add this in your app, or make different redirect

val authUrl ="https://accounts.spotify.com/authorize"

data class Params (
    val response_type: String = "code",
    val client_id: String = clientId,
    val scope: String = "user-read-private user-read-email",
    val code_challenge_method: String = "S256",
    val code_challenge: String,
    val redirect_uri: String = redirectUri
)

fun buildAuthParams(context: Context): Params {
    val codeChallenge = generateCodeChallenge(context)
    return Params(
        code_challenge = codeChallenge
    )
}

fun launchSpotifyAuth(context: Context) {
    val params = buildAuthParams(context)

    val queryParams = Uri.Builder().apply {
        appendQueryParameter("response_type", params.response_type)
        appendQueryParameter("client_id", params.client_id)
        appendQueryParameter("scope", params.scope)
        appendQueryParameter("code_challenge_method", params.code_challenge_method)
        appendQueryParameter("code_challenge", params.code_challenge)
        appendQueryParameter("redirect_uri", params.redirect_uri)
    }.build().toString().substring(1)

    val fullAuthUrl = "$authUrl?$queryParams"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullAuthUrl))
    context.startActivity(intent)


}

fun handleRedirect(intent: Intent): String? {
    if(intent?.action == Intent.ACTION_VIEW) {
        val uri = intent.data
        return uri?.getQueryParameter("code")
    }
    return null
}