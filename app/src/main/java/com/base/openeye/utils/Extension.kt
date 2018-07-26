package com.base.openeye.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.base.openeye.R
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.Status
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by lvruheng on 2017/7/2.
 */
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}

inline fun <reified T : Activity> Activity.newIntent() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

val RxDownload.statusMap: MutableMap<String, Status>
    get() = ConcurrentHashMap()

fun RxDownload.download(url: String): Flowable<Status> {
    this.statusMap[url] = Status()
    return this
            .create(url)
            .observeOn(AndroidSchedulers.mainThread())
            .map { status ->
                this.statusMap[url] = status
                status
            }

}
