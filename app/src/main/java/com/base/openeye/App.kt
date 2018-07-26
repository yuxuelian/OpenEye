package com.base.openeye

import android.app.Application
import android.graphics.Typeface
import com.base.openeye.CustomSqliteActor
import zlc.season.rxdownload3.core.DownloadConfig
import zlc.season.rxdownload3.extension.ApkInstallExtension
import zlc.season.rxdownload3.extension.ApkOpenExtension

/**
 * @author Administrator
 * @date 2018/1/3 19:35
 * GitHub：
 * email：
 * description：
 */
class App : Application() {

    companion object {
        private lateinit var app: App

        fun instance(): App = app

        val fontLobster: Typeface by lazy {
            Typeface.createFromAsset(app.assets, "fonts/Lobster-1.4.otf")
        }

        val fontFZLanTingHeiSDb: Typeface by lazy {
            Typeface.createFromAsset(app.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        }

        val fontFZLanTingHeiSDb1: Typeface by lazy {
            Typeface.createFromAsset(app.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this@App

        val builder = DownloadConfig.Builder.create(this)
                .setDebug(true)
                .enableDb(true)
                .setDbActor(CustomSqliteActor(this))
//                .enableService(true)
                .enableNotification(true)
                .addExtension(ApkInstallExtension::class.java)
                .addExtension(ApkOpenExtension::class.java)

        DownloadConfig.init(builder)
    }
}