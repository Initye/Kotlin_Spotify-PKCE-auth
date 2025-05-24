package com.example.spotdown


import android.content.Context
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


fun generateRandomString(length: Int): String {
    val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    return (1..length)
        .map { possible.random() }
        .joinToString("")
}

//Sha256 hash
fun sha256(plain: String): ByteArray {
    val bytes = plain.toByteArray(Charsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(bytes)
}

//Base64
@OptIn(ExperimentalEncodingApi::class)
fun base64encode(input: ByteArray): String {
    return Base64.UrlSafe.encode(input).trimEnd('=')
}

//Generate code challenge
fun generateCodeChallenge(context: Context): String {
    val codeVerifier = generateRandomString(64)
    val hashed = sha256(codeVerifier)
    val codeChallenge = base64encode(hashed)

    //Save code verifier to SharedPreferences
    val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    with(sharedPrefs.edit()) {
        putString("code_verifier", codeVerifier)
        apply()
    }

    return codeChallenge
}

//Retrieve code verifier
fun getCodeVerifier(context: Context): String? {
    val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return sharedPrefs.getString("code_verifier", null)
}

