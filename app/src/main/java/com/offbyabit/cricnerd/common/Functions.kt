package com.offbyabit.cricnerd.common

import com.offbyabit.cricnerd.screens.match_detail_screen.client
import okhttp3.Request
import java.io.IOException

@Throws(InterruptedException::class, IOException::class)
fun isNdtvConnected(): Boolean {
    val command = "ping -c 1 sports.ndtv.com"
    return Runtime.getRuntime().exec(command).waitFor() == 0
}

@Throws(IOException::class)
fun fetchJSON(url: String): String {
    val request: Request = Request.Builder()
        .url(url)
        .build()
    val response = client.newCall(request).execute()
    return response.body?.string().toString()
}