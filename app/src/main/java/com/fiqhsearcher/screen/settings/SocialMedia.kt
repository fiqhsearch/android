package com.fiqhsearcher.screen.settings

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri

private const val TELEGRAM_GROUP = "droosonline"
private const val TWITTER_USERNAME = "droos_online"
private const val TWITTER_USER_ID = "droos_online"
private const val FACEBOOK_PAGE_URL = "droosonline"

fun Activity.openFacebook() {
    val pageURL = "https://www.facebook.com/n/?$FACEBOOK_PAGE_URL"
    val browserIntent = Intent(ACTION_VIEW, Uri.parse(pageURL))
    startActivity(browserIntent)
}

fun Activity.openTwitter() {
    val intent = try {
        packageManager.getPackageInfo("com.twitter.android", 0)
        Intent(
            ACTION_VIEW,
            Uri.parse("twitter://user?user_id=$TWITTER_USER_ID")
        ).also { it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
    } catch (ignored: Exception) {
        Intent(
            ACTION_VIEW,
            Uri.parse("https://twitter.com/$TWITTER_USERNAME")
        )
    }
    startActivity(intent)
}

fun Activity.openTelegram() {
    val url = Uri.parse("https://t.me/$TELEGRAM_GROUP")
    try {
        val telegram = Intent(ACTION_VIEW, url)
        telegram.setPackage("org.telegram.messenger")
        startActivity(telegram)
    } catch (t: Throwable) {
        val telegram = Intent(ACTION_VIEW, url)
        startActivity(telegram)
    }
}